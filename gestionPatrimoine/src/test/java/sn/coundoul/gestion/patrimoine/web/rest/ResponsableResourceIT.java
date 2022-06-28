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
import sn.coundoul.gestion.patrimoine.domain.Responsable;
import sn.coundoul.gestion.patrimoine.repository.ResponsableRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link ResponsableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ResponsableResourceIT {

    private static final String DEFAULT_NOM_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALITE = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALITE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMB_1 = "AAAAAAAAAA";
    private static final String UPDATED_NUMB_1 = "BBBBBBBBBB";

    private static final String DEFAULT_NUMB_2 = "AAAAAAAAAA";
    private static final String UPDATED_NUMB_2 = "BBBBBBBBBB";

    private static final String DEFAULT_RAISON_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/responsables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResponsableRepository responsableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Responsable responsable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Responsable createEntity(EntityManager em) {
        Responsable responsable = new Responsable()
            .nomResponsable(DEFAULT_NOM_RESPONSABLE)
            .prenomResponsable(DEFAULT_PRENOM_RESPONSABLE)
            .email(DEFAULT_EMAIL)
            .specialite(DEFAULT_SPECIALITE)
            .numb1(DEFAULT_NUMB_1)
            .numb2(DEFAULT_NUMB_2)
            .raisonSocial(DEFAULT_RAISON_SOCIAL);
        return responsable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Responsable createUpdatedEntity(EntityManager em) {
        Responsable responsable = new Responsable()
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .prenomResponsable(UPDATED_PRENOM_RESPONSABLE)
            .email(UPDATED_EMAIL)
            .specialite(UPDATED_SPECIALITE)
            .numb1(UPDATED_NUMB_1)
            .numb2(UPDATED_NUMB_2)
            .raisonSocial(UPDATED_RAISON_SOCIAL);
        return responsable;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Responsable.class).block();
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
        responsable = createEntity(em);
    }

    @Test
    void createResponsable() throws Exception {
        int databaseSizeBeforeCreate = responsableRepository.findAll().collectList().block().size();
        // Create the Responsable
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeCreate + 1);
        Responsable testResponsable = responsableList.get(responsableList.size() - 1);
        assertThat(testResponsable.getNomResponsable()).isEqualTo(DEFAULT_NOM_RESPONSABLE);
        assertThat(testResponsable.getPrenomResponsable()).isEqualTo(DEFAULT_PRENOM_RESPONSABLE);
        assertThat(testResponsable.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testResponsable.getSpecialite()).isEqualTo(DEFAULT_SPECIALITE);
        assertThat(testResponsable.getNumb1()).isEqualTo(DEFAULT_NUMB_1);
        assertThat(testResponsable.getNumb2()).isEqualTo(DEFAULT_NUMB_2);
        assertThat(testResponsable.getRaisonSocial()).isEqualTo(DEFAULT_RAISON_SOCIAL);
    }

    @Test
    void createResponsableWithExistingId() throws Exception {
        // Create the Responsable with an existing ID
        responsable.setId(1L);

        int databaseSizeBeforeCreate = responsableRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsableRepository.findAll().collectList().block().size();
        // set the field null
        responsable.setNomResponsable(null);

        // Create the Responsable, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsableRepository.findAll().collectList().block().size();
        // set the field null
        responsable.setPrenomResponsable(null);

        // Create the Responsable, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsableRepository.findAll().collectList().block().size();
        // set the field null
        responsable.setEmail(null);

        // Create the Responsable, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSpecialiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsableRepository.findAll().collectList().block().size();
        // set the field null
        responsable.setSpecialite(null);

        // Create the Responsable, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNumb1IsRequired() throws Exception {
        int databaseSizeBeforeTest = responsableRepository.findAll().collectList().block().size();
        // set the field null
        responsable.setNumb1(null);

        // Create the Responsable, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRaisonSocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsableRepository.findAll().collectList().block().size();
        // set the field null
        responsable.setRaisonSocial(null);

        // Create the Responsable, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllResponsables() {
        // Initialize the database
        responsableRepository.save(responsable).block();

        // Get all the responsableList
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
            .value(hasItem(responsable.getId().intValue()))
            .jsonPath("$.[*].nomResponsable")
            .value(hasItem(DEFAULT_NOM_RESPONSABLE))
            .jsonPath("$.[*].prenomResponsable")
            .value(hasItem(DEFAULT_PRENOM_RESPONSABLE))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].specialite")
            .value(hasItem(DEFAULT_SPECIALITE))
            .jsonPath("$.[*].numb1")
            .value(hasItem(DEFAULT_NUMB_1))
            .jsonPath("$.[*].numb2")
            .value(hasItem(DEFAULT_NUMB_2))
            .jsonPath("$.[*].raisonSocial")
            .value(hasItem(DEFAULT_RAISON_SOCIAL));
    }

    @Test
    void getResponsable() {
        // Initialize the database
        responsableRepository.save(responsable).block();

        // Get the responsable
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, responsable.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(responsable.getId().intValue()))
            .jsonPath("$.nomResponsable")
            .value(is(DEFAULT_NOM_RESPONSABLE))
            .jsonPath("$.prenomResponsable")
            .value(is(DEFAULT_PRENOM_RESPONSABLE))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.specialite")
            .value(is(DEFAULT_SPECIALITE))
            .jsonPath("$.numb1")
            .value(is(DEFAULT_NUMB_1))
            .jsonPath("$.numb2")
            .value(is(DEFAULT_NUMB_2))
            .jsonPath("$.raisonSocial")
            .value(is(DEFAULT_RAISON_SOCIAL));
    }

    @Test
    void getNonExistingResponsable() {
        // Get the responsable
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewResponsable() throws Exception {
        // Initialize the database
        responsableRepository.save(responsable).block();

        int databaseSizeBeforeUpdate = responsableRepository.findAll().collectList().block().size();

        // Update the responsable
        Responsable updatedResponsable = responsableRepository.findById(responsable.getId()).block();
        updatedResponsable
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .prenomResponsable(UPDATED_PRENOM_RESPONSABLE)
            .email(UPDATED_EMAIL)
            .specialite(UPDATED_SPECIALITE)
            .numb1(UPDATED_NUMB_1)
            .numb2(UPDATED_NUMB_2)
            .raisonSocial(UPDATED_RAISON_SOCIAL);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedResponsable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedResponsable))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
        Responsable testResponsable = responsableList.get(responsableList.size() - 1);
        assertThat(testResponsable.getNomResponsable()).isEqualTo(UPDATED_NOM_RESPONSABLE);
        assertThat(testResponsable.getPrenomResponsable()).isEqualTo(UPDATED_PRENOM_RESPONSABLE);
        assertThat(testResponsable.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testResponsable.getSpecialite()).isEqualTo(UPDATED_SPECIALITE);
        assertThat(testResponsable.getNumb1()).isEqualTo(UPDATED_NUMB_1);
        assertThat(testResponsable.getNumb2()).isEqualTo(UPDATED_NUMB_2);
        assertThat(testResponsable.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
    }

    @Test
    void putNonExistingResponsable() throws Exception {
        int databaseSizeBeforeUpdate = responsableRepository.findAll().collectList().block().size();
        responsable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, responsable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchResponsable() throws Exception {
        int databaseSizeBeforeUpdate = responsableRepository.findAll().collectList().block().size();
        responsable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamResponsable() throws Exception {
        int databaseSizeBeforeUpdate = responsableRepository.findAll().collectList().block().size();
        responsable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateResponsableWithPatch() throws Exception {
        // Initialize the database
        responsableRepository.save(responsable).block();

        int databaseSizeBeforeUpdate = responsableRepository.findAll().collectList().block().size();

        // Update the responsable using partial update
        Responsable partialUpdatedResponsable = new Responsable();
        partialUpdatedResponsable.setId(responsable.getId());

        partialUpdatedResponsable
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .prenomResponsable(UPDATED_PRENOM_RESPONSABLE)
            .numb1(UPDATED_NUMB_1)
            .raisonSocial(UPDATED_RAISON_SOCIAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResponsable.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedResponsable))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
        Responsable testResponsable = responsableList.get(responsableList.size() - 1);
        assertThat(testResponsable.getNomResponsable()).isEqualTo(UPDATED_NOM_RESPONSABLE);
        assertThat(testResponsable.getPrenomResponsable()).isEqualTo(UPDATED_PRENOM_RESPONSABLE);
        assertThat(testResponsable.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testResponsable.getSpecialite()).isEqualTo(DEFAULT_SPECIALITE);
        assertThat(testResponsable.getNumb1()).isEqualTo(UPDATED_NUMB_1);
        assertThat(testResponsable.getNumb2()).isEqualTo(DEFAULT_NUMB_2);
        assertThat(testResponsable.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
    }

    @Test
    void fullUpdateResponsableWithPatch() throws Exception {
        // Initialize the database
        responsableRepository.save(responsable).block();

        int databaseSizeBeforeUpdate = responsableRepository.findAll().collectList().block().size();

        // Update the responsable using partial update
        Responsable partialUpdatedResponsable = new Responsable();
        partialUpdatedResponsable.setId(responsable.getId());

        partialUpdatedResponsable
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .prenomResponsable(UPDATED_PRENOM_RESPONSABLE)
            .email(UPDATED_EMAIL)
            .specialite(UPDATED_SPECIALITE)
            .numb1(UPDATED_NUMB_1)
            .numb2(UPDATED_NUMB_2)
            .raisonSocial(UPDATED_RAISON_SOCIAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResponsable.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedResponsable))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
        Responsable testResponsable = responsableList.get(responsableList.size() - 1);
        assertThat(testResponsable.getNomResponsable()).isEqualTo(UPDATED_NOM_RESPONSABLE);
        assertThat(testResponsable.getPrenomResponsable()).isEqualTo(UPDATED_PRENOM_RESPONSABLE);
        assertThat(testResponsable.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testResponsable.getSpecialite()).isEqualTo(UPDATED_SPECIALITE);
        assertThat(testResponsable.getNumb1()).isEqualTo(UPDATED_NUMB_1);
        assertThat(testResponsable.getNumb2()).isEqualTo(UPDATED_NUMB_2);
        assertThat(testResponsable.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
    }

    @Test
    void patchNonExistingResponsable() throws Exception {
        int databaseSizeBeforeUpdate = responsableRepository.findAll().collectList().block().size();
        responsable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, responsable.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchResponsable() throws Exception {
        int databaseSizeBeforeUpdate = responsableRepository.findAll().collectList().block().size();
        responsable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamResponsable() throws Exception {
        int databaseSizeBeforeUpdate = responsableRepository.findAll().collectList().block().size();
        responsable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsable))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteResponsable() {
        // Initialize the database
        responsableRepository.save(responsable).block();

        int databaseSizeBeforeDelete = responsableRepository.findAll().collectList().block().size();

        // Delete the responsable
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, responsable.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Responsable> responsableList = responsableRepository.findAll().collectList().block();
        assertThat(responsableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
