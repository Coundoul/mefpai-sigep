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
import sn.coundoul.gestion.patrimoine.domain.Batiment;
import sn.coundoul.gestion.patrimoine.repository.BatimentRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link BatimentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class BatimentResourceIT {

    private static final String DEFAULT_NOM_BATIMENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_BATIMENT = "BBBBBBBBBB";

    private static final String DEFAULT_NBR_PIECE = "AAAAAAAAAA";
    private static final String UPDATED_NBR_PIECE = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Double DEFAULT_SURFACE = 1D;
    private static final Double UPDATED_SURFACE = 2D;

    private static final Boolean DEFAULT_ETAT_GENERAL = false;
    private static final Boolean UPDATED_ETAT_GENERAL = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NOMBRE_SALLE = 1;
    private static final Integer UPDATED_NOMBRE_SALLE = 2;

    private static final String ENTITY_API_URL = "/api/batiments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BatimentRepository batimentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Batiment batiment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batiment createEntity(EntityManager em) {
        Batiment batiment = new Batiment()
            .nomBatiment(DEFAULT_NOM_BATIMENT)
            .nbrPiece(DEFAULT_NBR_PIECE)
            .designation(DEFAULT_DESIGNATION)
            .surface(DEFAULT_SURFACE)
            .etatGeneral(DEFAULT_ETAT_GENERAL)
            .description(DEFAULT_DESCRIPTION)
            .nombreSalle(DEFAULT_NOMBRE_SALLE);
        return batiment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batiment createUpdatedEntity(EntityManager em) {
        Batiment batiment = new Batiment()
            .nomBatiment(UPDATED_NOM_BATIMENT)
            .nbrPiece(UPDATED_NBR_PIECE)
            .designation(UPDATED_DESIGNATION)
            .surface(UPDATED_SURFACE)
            .etatGeneral(UPDATED_ETAT_GENERAL)
            .description(UPDATED_DESCRIPTION)
            .nombreSalle(UPDATED_NOMBRE_SALLE);
        return batiment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Batiment.class).block();
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
        batiment = createEntity(em);
    }

    @Test
    void createBatiment() throws Exception {
        int databaseSizeBeforeCreate = batimentRepository.findAll().collectList().block().size();
        // Create the Batiment
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeCreate + 1);
        Batiment testBatiment = batimentList.get(batimentList.size() - 1);
        assertThat(testBatiment.getNomBatiment()).isEqualTo(DEFAULT_NOM_BATIMENT);
        assertThat(testBatiment.getNbrPiece()).isEqualTo(DEFAULT_NBR_PIECE);
        assertThat(testBatiment.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testBatiment.getSurface()).isEqualTo(DEFAULT_SURFACE);
        assertThat(testBatiment.getEtatGeneral()).isEqualTo(DEFAULT_ETAT_GENERAL);
        assertThat(testBatiment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBatiment.getNombreSalle()).isEqualTo(DEFAULT_NOMBRE_SALLE);
    }

    @Test
    void createBatimentWithExistingId() throws Exception {
        // Create the Batiment with an existing ID
        batiment.setId(1L);

        int databaseSizeBeforeCreate = batimentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomBatimentIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().collectList().block().size();
        // set the field null
        batiment.setNomBatiment(null);

        // Create the Batiment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNbrPieceIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().collectList().block().size();
        // set the field null
        batiment.setNbrPiece(null);

        // Create the Batiment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().collectList().block().size();
        // set the field null
        batiment.setDesignation(null);

        // Create the Batiment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSurfaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().collectList().block().size();
        // set the field null
        batiment.setSurface(null);

        // Create the Batiment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEtatGeneralIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().collectList().block().size();
        // set the field null
        batiment.setEtatGeneral(null);

        // Create the Batiment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreSalleIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().collectList().block().size();
        // set the field null
        batiment.setNombreSalle(null);

        // Create the Batiment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBatiments() {
        // Initialize the database
        batimentRepository.save(batiment).block();

        // Get all the batimentList
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
            .value(hasItem(batiment.getId().intValue()))
            .jsonPath("$.[*].nomBatiment")
            .value(hasItem(DEFAULT_NOM_BATIMENT))
            .jsonPath("$.[*].nbrPiece")
            .value(hasItem(DEFAULT_NBR_PIECE))
            .jsonPath("$.[*].designation")
            .value(hasItem(DEFAULT_DESIGNATION))
            .jsonPath("$.[*].surface")
            .value(hasItem(DEFAULT_SURFACE.doubleValue()))
            .jsonPath("$.[*].etatGeneral")
            .value(hasItem(DEFAULT_ETAT_GENERAL.booleanValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].nombreSalle")
            .value(hasItem(DEFAULT_NOMBRE_SALLE));
    }

    @Test
    void getBatiment() {
        // Initialize the database
        batimentRepository.save(batiment).block();

        // Get the batiment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, batiment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(batiment.getId().intValue()))
            .jsonPath("$.nomBatiment")
            .value(is(DEFAULT_NOM_BATIMENT))
            .jsonPath("$.nbrPiece")
            .value(is(DEFAULT_NBR_PIECE))
            .jsonPath("$.designation")
            .value(is(DEFAULT_DESIGNATION))
            .jsonPath("$.surface")
            .value(is(DEFAULT_SURFACE.doubleValue()))
            .jsonPath("$.etatGeneral")
            .value(is(DEFAULT_ETAT_GENERAL.booleanValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.nombreSalle")
            .value(is(DEFAULT_NOMBRE_SALLE));
    }

    @Test
    void getNonExistingBatiment() {
        // Get the batiment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBatiment() throws Exception {
        // Initialize the database
        batimentRepository.save(batiment).block();

        int databaseSizeBeforeUpdate = batimentRepository.findAll().collectList().block().size();

        // Update the batiment
        Batiment updatedBatiment = batimentRepository.findById(batiment.getId()).block();
        updatedBatiment
            .nomBatiment(UPDATED_NOM_BATIMENT)
            .nbrPiece(UPDATED_NBR_PIECE)
            .designation(UPDATED_DESIGNATION)
            .surface(UPDATED_SURFACE)
            .etatGeneral(UPDATED_ETAT_GENERAL)
            .description(UPDATED_DESCRIPTION)
            .nombreSalle(UPDATED_NOMBRE_SALLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBatiment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBatiment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
        Batiment testBatiment = batimentList.get(batimentList.size() - 1);
        assertThat(testBatiment.getNomBatiment()).isEqualTo(UPDATED_NOM_BATIMENT);
        assertThat(testBatiment.getNbrPiece()).isEqualTo(UPDATED_NBR_PIECE);
        assertThat(testBatiment.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testBatiment.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testBatiment.getEtatGeneral()).isEqualTo(UPDATED_ETAT_GENERAL);
        assertThat(testBatiment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBatiment.getNombreSalle()).isEqualTo(UPDATED_NOMBRE_SALLE);
    }

    @Test
    void putNonExistingBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().collectList().block().size();
        batiment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, batiment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().collectList().block().size();
        batiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().collectList().block().size();
        batiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBatimentWithPatch() throws Exception {
        // Initialize the database
        batimentRepository.save(batiment).block();

        int databaseSizeBeforeUpdate = batimentRepository.findAll().collectList().block().size();

        // Update the batiment using partial update
        Batiment partialUpdatedBatiment = new Batiment();
        partialUpdatedBatiment.setId(batiment.getId());

        partialUpdatedBatiment
            .nomBatiment(UPDATED_NOM_BATIMENT)
            .surface(UPDATED_SURFACE)
            .description(UPDATED_DESCRIPTION)
            .nombreSalle(UPDATED_NOMBRE_SALLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBatiment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBatiment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
        Batiment testBatiment = batimentList.get(batimentList.size() - 1);
        assertThat(testBatiment.getNomBatiment()).isEqualTo(UPDATED_NOM_BATIMENT);
        assertThat(testBatiment.getNbrPiece()).isEqualTo(DEFAULT_NBR_PIECE);
        assertThat(testBatiment.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testBatiment.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testBatiment.getEtatGeneral()).isEqualTo(DEFAULT_ETAT_GENERAL);
        assertThat(testBatiment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBatiment.getNombreSalle()).isEqualTo(UPDATED_NOMBRE_SALLE);
    }

    @Test
    void fullUpdateBatimentWithPatch() throws Exception {
        // Initialize the database
        batimentRepository.save(batiment).block();

        int databaseSizeBeforeUpdate = batimentRepository.findAll().collectList().block().size();

        // Update the batiment using partial update
        Batiment partialUpdatedBatiment = new Batiment();
        partialUpdatedBatiment.setId(batiment.getId());

        partialUpdatedBatiment
            .nomBatiment(UPDATED_NOM_BATIMENT)
            .nbrPiece(UPDATED_NBR_PIECE)
            .designation(UPDATED_DESIGNATION)
            .surface(UPDATED_SURFACE)
            .etatGeneral(UPDATED_ETAT_GENERAL)
            .description(UPDATED_DESCRIPTION)
            .nombreSalle(UPDATED_NOMBRE_SALLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBatiment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBatiment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
        Batiment testBatiment = batimentList.get(batimentList.size() - 1);
        assertThat(testBatiment.getNomBatiment()).isEqualTo(UPDATED_NOM_BATIMENT);
        assertThat(testBatiment.getNbrPiece()).isEqualTo(UPDATED_NBR_PIECE);
        assertThat(testBatiment.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testBatiment.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testBatiment.getEtatGeneral()).isEqualTo(UPDATED_ETAT_GENERAL);
        assertThat(testBatiment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBatiment.getNombreSalle()).isEqualTo(UPDATED_NOMBRE_SALLE);
    }

    @Test
    void patchNonExistingBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().collectList().block().size();
        batiment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, batiment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().collectList().block().size();
        batiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().collectList().block().size();
        batiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(batiment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBatiment() {
        // Initialize the database
        batimentRepository.save(batiment).block();

        int databaseSizeBeforeDelete = batimentRepository.findAll().collectList().block().size();

        // Delete the batiment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, batiment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Batiment> batimentList = batimentRepository.findAll().collectList().block();
        assertThat(batimentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
