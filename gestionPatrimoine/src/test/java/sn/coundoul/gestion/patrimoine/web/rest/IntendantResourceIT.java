package sn.coundoul.gestion.patrimoine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import sn.coundoul.gestion.patrimoine.IntegrationTest;
import sn.coundoul.gestion.patrimoine.domain.Intendant;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.IntendantRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link IntendantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class IntendantResourceIT {

    private static final String DEFAULT_NOM_PERS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PERS = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_PERS = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_PERS = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.Masculin;
    private static final Sexe UPDATED_SEXE = Sexe.Feminin;

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/intendants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IntendantRepository intendantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Intendant intendant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intendant createEntity(EntityManager em) {
        Intendant intendant = new Intendant()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE);
        return intendant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intendant createUpdatedEntity(EntityManager em) {
        Intendant intendant = new Intendant()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);
        return intendant;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Intendant.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        intendant = createEntity(em);
    }

    @Test
    void createIntendant() throws Exception {
        int databaseSizeBeforeCreate = intendantRepository.findAll().collectList().block().size();
        // Create the Intendant
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeCreate + 1);
        Intendant testIntendant = intendantList.get(intendantList.size() - 1);
        assertThat(testIntendant.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testIntendant.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testIntendant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testIntendant.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testIntendant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    void createIntendantWithExistingId() throws Exception {
        // Create the Intendant with an existing ID
        intendant.setId(1L);

        int databaseSizeBeforeCreate = intendantRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = intendantRepository.findAll().collectList().block().size();
        // set the field null
        intendant.setNomPers(null);

        // Create the Intendant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = intendantRepository.findAll().collectList().block().size();
        // set the field null
        intendant.setPrenomPers(null);

        // Create the Intendant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = intendantRepository.findAll().collectList().block().size();
        // set the field null
        intendant.setSexe(null);

        // Create the Intendant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = intendantRepository.findAll().collectList().block().size();
        // set the field null
        intendant.setMobile(null);

        // Create the Intendant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllIntendants() {
        // Initialize the database
        intendantRepository.save(intendant).block();

        // Get all the intendantList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(intendant.getId().intValue()))
            .jsonPath("$.[*].nomPers")
            .value(hasItem(DEFAULT_NOM_PERS))
            .jsonPath("$.[*].prenomPers")
            .value(hasItem(DEFAULT_PRENOM_PERS))
            .jsonPath("$.[*].sexe")
            .value(hasItem(DEFAULT_SEXE.toString()))
            .jsonPath("$.[*].mobile")
            .value(hasItem(DEFAULT_MOBILE))
            .jsonPath("$.[*].adresse")
            .value(hasItem(DEFAULT_ADRESSE));
    }

    @Test
    void getIntendant() {
        // Initialize the database
        intendantRepository.save(intendant).block();

        // Get the intendant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, intendant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(intendant.getId().intValue()))
            .jsonPath("$.nomPers")
            .value(is(DEFAULT_NOM_PERS))
            .jsonPath("$.prenomPers")
            .value(is(DEFAULT_PRENOM_PERS))
            .jsonPath("$.sexe")
            .value(is(DEFAULT_SEXE.toString()))
            .jsonPath("$.mobile")
            .value(is(DEFAULT_MOBILE))
            .jsonPath("$.adresse")
            .value(is(DEFAULT_ADRESSE));
    }

    @Test
    void getNonExistingIntendant() {
        // Get the intendant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIntendant() throws Exception {
        // Initialize the database
        intendantRepository.save(intendant).block();

        int databaseSizeBeforeUpdate = intendantRepository.findAll().collectList().block().size();

        // Update the intendant
        Intendant updatedIntendant = intendantRepository.findById(intendant.getId()).block();
        updatedIntendant
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIntendant.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIntendant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
        Intendant testIntendant = intendantList.get(intendantList.size() - 1);
        assertThat(testIntendant.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testIntendant.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testIntendant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testIntendant.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testIntendant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    void putNonExistingIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().collectList().block().size();
        intendant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, intendant.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().collectList().block().size();
        intendant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().collectList().block().size();
        intendant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIntendantWithPatch() throws Exception {
        // Initialize the database
        intendantRepository.save(intendant).block();

        int databaseSizeBeforeUpdate = intendantRepository.findAll().collectList().block().size();

        // Update the intendant using partial update
        Intendant partialUpdatedIntendant = new Intendant();
        partialUpdatedIntendant.setId(intendant.getId());

        partialUpdatedIntendant.prenomPers(UPDATED_PRENOM_PERS).sexe(UPDATED_SEXE).mobile(UPDATED_MOBILE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntendant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIntendant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
        Intendant testIntendant = intendantList.get(intendantList.size() - 1);
        assertThat(testIntendant.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testIntendant.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testIntendant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testIntendant.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testIntendant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    void fullUpdateIntendantWithPatch() throws Exception {
        // Initialize the database
        intendantRepository.save(intendant).block();

        int databaseSizeBeforeUpdate = intendantRepository.findAll().collectList().block().size();

        // Update the intendant using partial update
        Intendant partialUpdatedIntendant = new Intendant();
        partialUpdatedIntendant.setId(intendant.getId());

        partialUpdatedIntendant
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntendant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIntendant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
        Intendant testIntendant = intendantList.get(intendantList.size() - 1);
        assertThat(testIntendant.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testIntendant.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testIntendant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testIntendant.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testIntendant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    void patchNonExistingIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().collectList().block().size();
        intendant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, intendant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().collectList().block().size();
        intendant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().collectList().block().size();
        intendant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(intendant))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIntendant() {
        // Initialize the database
        intendantRepository.save(intendant).block();

        int databaseSizeBeforeDelete = intendantRepository.findAll().collectList().block().size();

        // Delete the intendant
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, intendant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Intendant> intendantList = intendantRepository.findAll().collectList().block();
        assertThat(intendantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
