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
import sn.coundoul.gestion.patrimoine.domain.Detenteur;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.DetenteurRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link DetenteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class DetenteurResourceIT {

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

    private static final Direction DEFAULT_DIRECTION = Direction.DAGE;
    private static final Direction UPDATED_DIRECTION = Direction.DFPT;

    private static final String ENTITY_API_URL = "/api/detenteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetenteurRepository detenteurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Detenteur detenteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Detenteur createEntity(EntityManager em) {
        Detenteur detenteur = new Detenteur()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return detenteur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Detenteur createUpdatedEntity(EntityManager em) {
        Detenteur detenteur = new Detenteur()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return detenteur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Detenteur.class).block();
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
        detenteur = createEntity(em);
    }

    @Test
    void createDetenteur() throws Exception {
        int databaseSizeBeforeCreate = detenteurRepository.findAll().collectList().block().size();
        // Create the Detenteur
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeCreate + 1);
        Detenteur testDetenteur = detenteurList.get(detenteurList.size() - 1);
        assertThat(testDetenteur.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testDetenteur.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testDetenteur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDetenteur.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testDetenteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDetenteur.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void createDetenteurWithExistingId() throws Exception {
        // Create the Detenteur with an existing ID
        detenteur.setId(1L);

        int databaseSizeBeforeCreate = detenteurRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = detenteurRepository.findAll().collectList().block().size();
        // set the field null
        detenteur.setNomPers(null);

        // Create the Detenteur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = detenteurRepository.findAll().collectList().block().size();
        // set the field null
        detenteur.setPrenomPers(null);

        // Create the Detenteur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = detenteurRepository.findAll().collectList().block().size();
        // set the field null
        detenteur.setSexe(null);

        // Create the Detenteur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = detenteurRepository.findAll().collectList().block().size();
        // set the field null
        detenteur.setMobile(null);

        // Create the Detenteur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = detenteurRepository.findAll().collectList().block().size();
        // set the field null
        detenteur.setDirection(null);

        // Create the Detenteur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDetenteurs() {
        // Initialize the database
        detenteurRepository.save(detenteur).block();

        // Get all the detenteurList
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
            .value(hasItem(detenteur.getId().intValue()))
            .jsonPath("$.[*].nomPers")
            .value(hasItem(DEFAULT_NOM_PERS))
            .jsonPath("$.[*].prenomPers")
            .value(hasItem(DEFAULT_PRENOM_PERS))
            .jsonPath("$.[*].sexe")
            .value(hasItem(DEFAULT_SEXE.toString()))
            .jsonPath("$.[*].mobile")
            .value(hasItem(DEFAULT_MOBILE))
            .jsonPath("$.[*].adresse")
            .value(hasItem(DEFAULT_ADRESSE))
            .jsonPath("$.[*].direction")
            .value(hasItem(DEFAULT_DIRECTION.toString()));
    }

    @Test
    void getDetenteur() {
        // Initialize the database
        detenteurRepository.save(detenteur).block();

        // Get the detenteur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, detenteur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(detenteur.getId().intValue()))
            .jsonPath("$.nomPers")
            .value(is(DEFAULT_NOM_PERS))
            .jsonPath("$.prenomPers")
            .value(is(DEFAULT_PRENOM_PERS))
            .jsonPath("$.sexe")
            .value(is(DEFAULT_SEXE.toString()))
            .jsonPath("$.mobile")
            .value(is(DEFAULT_MOBILE))
            .jsonPath("$.adresse")
            .value(is(DEFAULT_ADRESSE))
            .jsonPath("$.direction")
            .value(is(DEFAULT_DIRECTION.toString()));
    }

    @Test
    void getNonExistingDetenteur() {
        // Get the detenteur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDetenteur() throws Exception {
        // Initialize the database
        detenteurRepository.save(detenteur).block();

        int databaseSizeBeforeUpdate = detenteurRepository.findAll().collectList().block().size();

        // Update the detenteur
        Detenteur updatedDetenteur = detenteurRepository.findById(detenteur.getId()).block();
        updatedDetenteur
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDetenteur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDetenteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
        Detenteur testDetenteur = detenteurList.get(detenteurList.size() - 1);
        assertThat(testDetenteur.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testDetenteur.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testDetenteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDetenteur.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testDetenteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDetenteur.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void putNonExistingDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().collectList().block().size();
        detenteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, detenteur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().collectList().block().size();
        detenteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().collectList().block().size();
        detenteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDetenteurWithPatch() throws Exception {
        // Initialize the database
        detenteurRepository.save(detenteur).block();

        int databaseSizeBeforeUpdate = detenteurRepository.findAll().collectList().block().size();

        // Update the detenteur using partial update
        Detenteur partialUpdatedDetenteur = new Detenteur();
        partialUpdatedDetenteur.setId(detenteur.getId());

        partialUpdatedDetenteur.mobile(UPDATED_MOBILE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDetenteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDetenteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
        Detenteur testDetenteur = detenteurList.get(detenteurList.size() - 1);
        assertThat(testDetenteur.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testDetenteur.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testDetenteur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDetenteur.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testDetenteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDetenteur.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void fullUpdateDetenteurWithPatch() throws Exception {
        // Initialize the database
        detenteurRepository.save(detenteur).block();

        int databaseSizeBeforeUpdate = detenteurRepository.findAll().collectList().block().size();

        // Update the detenteur using partial update
        Detenteur partialUpdatedDetenteur = new Detenteur();
        partialUpdatedDetenteur.setId(detenteur.getId());

        partialUpdatedDetenteur
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDetenteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDetenteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
        Detenteur testDetenteur = detenteurList.get(detenteurList.size() - 1);
        assertThat(testDetenteur.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testDetenteur.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testDetenteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDetenteur.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testDetenteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDetenteur.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void patchNonExistingDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().collectList().block().size();
        detenteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, detenteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().collectList().block().size();
        detenteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().collectList().block().size();
        detenteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detenteur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDetenteur() {
        // Initialize the database
        detenteurRepository.save(detenteur).block();

        int databaseSizeBeforeDelete = detenteurRepository.findAll().collectList().block().size();

        // Delete the detenteur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, detenteur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Detenteur> detenteurList = detenteurRepository.findAll().collectList().block();
        assertThat(detenteurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
