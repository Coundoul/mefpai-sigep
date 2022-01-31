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
import sn.coundoul.gestion.patrimoine.domain.OrdonnaceurMatiere;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.OrdonnaceurMatiereRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link OrdonnaceurMatiereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class OrdonnaceurMatiereResourceIT {

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

    private static final String ENTITY_API_URL = "/api/ordonnaceur-matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdonnaceurMatiereRepository ordonnaceurMatiereRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private OrdonnaceurMatiere ordonnaceurMatiere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdonnaceurMatiere createEntity(EntityManager em) {
        OrdonnaceurMatiere ordonnaceurMatiere = new OrdonnaceurMatiere()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return ordonnaceurMatiere;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdonnaceurMatiere createUpdatedEntity(EntityManager em) {
        OrdonnaceurMatiere ordonnaceurMatiere = new OrdonnaceurMatiere()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return ordonnaceurMatiere;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(OrdonnaceurMatiere.class).block();
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
        ordonnaceurMatiere = createEntity(em);
    }

    @Test
    void createOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeCreate = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        // Create the OrdonnaceurMatiere
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeCreate + 1);
        OrdonnaceurMatiere testOrdonnaceurMatiere = ordonnaceurMatiereList.get(ordonnaceurMatiereList.size() - 1);
        assertThat(testOrdonnaceurMatiere.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testOrdonnaceurMatiere.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testOrdonnaceurMatiere.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testOrdonnaceurMatiere.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testOrdonnaceurMatiere.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testOrdonnaceurMatiere.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void createOrdonnaceurMatiereWithExistingId() throws Exception {
        // Create the OrdonnaceurMatiere with an existing ID
        ordonnaceurMatiere.setId(1L);

        int databaseSizeBeforeCreate = ordonnaceurMatiereRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        // set the field null
        ordonnaceurMatiere.setNomPers(null);

        // Create the OrdonnaceurMatiere, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        // set the field null
        ordonnaceurMatiere.setPrenomPers(null);

        // Create the OrdonnaceurMatiere, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        // set the field null
        ordonnaceurMatiere.setSexe(null);

        // Create the OrdonnaceurMatiere, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        // set the field null
        ordonnaceurMatiere.setMobile(null);

        // Create the OrdonnaceurMatiere, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllOrdonnaceurMatieres() {
        // Initialize the database
        ordonnaceurMatiereRepository.save(ordonnaceurMatiere).block();

        // Get all the ordonnaceurMatiereList
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
            .value(hasItem(ordonnaceurMatiere.getId().intValue()))
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
    void getOrdonnaceurMatiere() {
        // Initialize the database
        ordonnaceurMatiereRepository.save(ordonnaceurMatiere).block();

        // Get the ordonnaceurMatiere
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ordonnaceurMatiere.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ordonnaceurMatiere.getId().intValue()))
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
    void getNonExistingOrdonnaceurMatiere() {
        // Get the ordonnaceurMatiere
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewOrdonnaceurMatiere() throws Exception {
        // Initialize the database
        ordonnaceurMatiereRepository.save(ordonnaceurMatiere).block();

        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().collectList().block().size();

        // Update the ordonnaceurMatiere
        OrdonnaceurMatiere updatedOrdonnaceurMatiere = ordonnaceurMatiereRepository.findById(ordonnaceurMatiere.getId()).block();
        updatedOrdonnaceurMatiere
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedOrdonnaceurMatiere.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedOrdonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
        OrdonnaceurMatiere testOrdonnaceurMatiere = ordonnaceurMatiereList.get(ordonnaceurMatiereList.size() - 1);
        assertThat(testOrdonnaceurMatiere.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testOrdonnaceurMatiere.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testOrdonnaceurMatiere.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testOrdonnaceurMatiere.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testOrdonnaceurMatiere.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testOrdonnaceurMatiere.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void putNonExistingOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ordonnaceurMatiere.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrdonnaceurMatiereWithPatch() throws Exception {
        // Initialize the database
        ordonnaceurMatiereRepository.save(ordonnaceurMatiere).block();

        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().collectList().block().size();

        // Update the ordonnaceurMatiere using partial update
        OrdonnaceurMatiere partialUpdatedOrdonnaceurMatiere = new OrdonnaceurMatiere();
        partialUpdatedOrdonnaceurMatiere.setId(ordonnaceurMatiere.getId());

        partialUpdatedOrdonnaceurMatiere.mobile(UPDATED_MOBILE).adresse(UPDATED_ADRESSE).direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrdonnaceurMatiere.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
        OrdonnaceurMatiere testOrdonnaceurMatiere = ordonnaceurMatiereList.get(ordonnaceurMatiereList.size() - 1);
        assertThat(testOrdonnaceurMatiere.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testOrdonnaceurMatiere.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testOrdonnaceurMatiere.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testOrdonnaceurMatiere.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testOrdonnaceurMatiere.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testOrdonnaceurMatiere.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void fullUpdateOrdonnaceurMatiereWithPatch() throws Exception {
        // Initialize the database
        ordonnaceurMatiereRepository.save(ordonnaceurMatiere).block();

        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().collectList().block().size();

        // Update the ordonnaceurMatiere using partial update
        OrdonnaceurMatiere partialUpdatedOrdonnaceurMatiere = new OrdonnaceurMatiere();
        partialUpdatedOrdonnaceurMatiere.setId(ordonnaceurMatiere.getId());

        partialUpdatedOrdonnaceurMatiere
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrdonnaceurMatiere.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
        OrdonnaceurMatiere testOrdonnaceurMatiere = ordonnaceurMatiereList.get(ordonnaceurMatiereList.size() - 1);
        assertThat(testOrdonnaceurMatiere.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testOrdonnaceurMatiere.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testOrdonnaceurMatiere.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testOrdonnaceurMatiere.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testOrdonnaceurMatiere.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testOrdonnaceurMatiere.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void patchNonExistingOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ordonnaceurMatiere.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().collectList().block().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrdonnaceurMatiere() {
        // Initialize the database
        ordonnaceurMatiereRepository.save(ordonnaceurMatiere).block();

        int databaseSizeBeforeDelete = ordonnaceurMatiereRepository.findAll().collectList().block().size();

        // Delete the ordonnaceurMatiere
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ordonnaceurMatiere.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll().collectList().block();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
