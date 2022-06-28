package sn.coundoul.gestion.patrimoine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.coundoul.gestion.patrimoine.domain.FicheTechniqueMaintenance;
import sn.coundoul.gestion.patrimoine.repository.FicheTechniqueMaintenanceRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link FicheTechniqueMaintenanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class FicheTechniqueMaintenanceResourceIT {

    private static final String DEFAULT_PIECE_JOINTE = "AAAAAAAAAA";
    private static final String UPDATED_PIECE_JOINTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final Instant DEFAULT_DATE_DEPOT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEPOT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/fiche-technique-maintenances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FicheTechniqueMaintenanceRepository ficheTechniqueMaintenanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FicheTechniqueMaintenance ficheTechniqueMaintenance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FicheTechniqueMaintenance createEntity(EntityManager em) {
        FicheTechniqueMaintenance ficheTechniqueMaintenance = new FicheTechniqueMaintenance()
            .pieceJointe(DEFAULT_PIECE_JOINTE)
            .idPers(DEFAULT_ID_PERS)
            .dateDepot(DEFAULT_DATE_DEPOT);
        return ficheTechniqueMaintenance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FicheTechniqueMaintenance createUpdatedEntity(EntityManager em) {
        FicheTechniqueMaintenance ficheTechniqueMaintenance = new FicheTechniqueMaintenance()
            .pieceJointe(UPDATED_PIECE_JOINTE)
            .idPers(UPDATED_ID_PERS)
            .dateDepot(UPDATED_DATE_DEPOT);
        return ficheTechniqueMaintenance;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FicheTechniqueMaintenance.class).block();
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
        ficheTechniqueMaintenance = createEntity(em);
    }

    @Test
    void createFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeCreate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();
        // Create the FicheTechniqueMaintenance
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeCreate + 1);
        FicheTechniqueMaintenance testFicheTechniqueMaintenance = ficheTechniqueMaintenanceList.get(
            ficheTechniqueMaintenanceList.size() - 1
        );
        assertThat(testFicheTechniqueMaintenance.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testFicheTechniqueMaintenance.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
        assertThat(testFicheTechniqueMaintenance.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
    }

    @Test
    void createFicheTechniqueMaintenanceWithExistingId() throws Exception {
        // Create the FicheTechniqueMaintenance with an existing ID
        ficheTechniqueMaintenance.setId(1L);

        int databaseSizeBeforeCreate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPieceJointeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();
        // set the field null
        ficheTechniqueMaintenance.setPieceJointe(null);

        // Create the FicheTechniqueMaintenance, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();
        // set the field null
        ficheTechniqueMaintenance.setIdPers(null);

        // Create the FicheTechniqueMaintenance, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFicheTechniqueMaintenances() {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.save(ficheTechniqueMaintenance).block();

        // Get all the ficheTechniqueMaintenanceList
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
            .value(hasItem(ficheTechniqueMaintenance.getId().intValue()))
            .jsonPath("$.[*].pieceJointe")
            .value(hasItem(DEFAULT_PIECE_JOINTE))
            .jsonPath("$.[*].idPers")
            .value(hasItem(DEFAULT_ID_PERS))
            .jsonPath("$.[*].dateDepot")
            .value(hasItem(DEFAULT_DATE_DEPOT.toString()));
    }

    @Test
    void getFicheTechniqueMaintenance() {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.save(ficheTechniqueMaintenance).block();

        // Get the ficheTechniqueMaintenance
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ficheTechniqueMaintenance.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ficheTechniqueMaintenance.getId().intValue()))
            .jsonPath("$.pieceJointe")
            .value(is(DEFAULT_PIECE_JOINTE))
            .jsonPath("$.idPers")
            .value(is(DEFAULT_ID_PERS))
            .jsonPath("$.dateDepot")
            .value(is(DEFAULT_DATE_DEPOT.toString()));
    }

    @Test
    void getNonExistingFicheTechniqueMaintenance() {
        // Get the ficheTechniqueMaintenance
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFicheTechniqueMaintenance() throws Exception {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.save(ficheTechniqueMaintenance).block();

        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();

        // Update the ficheTechniqueMaintenance
        FicheTechniqueMaintenance updatedFicheTechniqueMaintenance = ficheTechniqueMaintenanceRepository
            .findById(ficheTechniqueMaintenance.getId())
            .block();
        updatedFicheTechniqueMaintenance.pieceJointe(UPDATED_PIECE_JOINTE).idPers(UPDATED_ID_PERS).dateDepot(UPDATED_DATE_DEPOT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFicheTechniqueMaintenance.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFicheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        FicheTechniqueMaintenance testFicheTechniqueMaintenance = ficheTechniqueMaintenanceList.get(
            ficheTechniqueMaintenanceList.size() - 1
        );
        assertThat(testFicheTechniqueMaintenance.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testFicheTechniqueMaintenance.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testFicheTechniqueMaintenance.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
    }

    @Test
    void putNonExistingFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ficheTechniqueMaintenance.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFicheTechniqueMaintenanceWithPatch() throws Exception {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.save(ficheTechniqueMaintenance).block();

        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();

        // Update the ficheTechniqueMaintenance using partial update
        FicheTechniqueMaintenance partialUpdatedFicheTechniqueMaintenance = new FicheTechniqueMaintenance();
        partialUpdatedFicheTechniqueMaintenance.setId(ficheTechniqueMaintenance.getId());

        partialUpdatedFicheTechniqueMaintenance.idPers(UPDATED_ID_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFicheTechniqueMaintenance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFicheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        FicheTechniqueMaintenance testFicheTechniqueMaintenance = ficheTechniqueMaintenanceList.get(
            ficheTechniqueMaintenanceList.size() - 1
        );
        assertThat(testFicheTechniqueMaintenance.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testFicheTechniqueMaintenance.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testFicheTechniqueMaintenance.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
    }

    @Test
    void fullUpdateFicheTechniqueMaintenanceWithPatch() throws Exception {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.save(ficheTechniqueMaintenance).block();

        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();

        // Update the ficheTechniqueMaintenance using partial update
        FicheTechniqueMaintenance partialUpdatedFicheTechniqueMaintenance = new FicheTechniqueMaintenance();
        partialUpdatedFicheTechniqueMaintenance.setId(ficheTechniqueMaintenance.getId());

        partialUpdatedFicheTechniqueMaintenance.pieceJointe(UPDATED_PIECE_JOINTE).idPers(UPDATED_ID_PERS).dateDepot(UPDATED_DATE_DEPOT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFicheTechniqueMaintenance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFicheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        FicheTechniqueMaintenance testFicheTechniqueMaintenance = ficheTechniqueMaintenanceList.get(
            ficheTechniqueMaintenanceList.size() - 1
        );
        assertThat(testFicheTechniqueMaintenance.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testFicheTechniqueMaintenance.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testFicheTechniqueMaintenance.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
    }

    @Test
    void patchNonExistingFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ficheTechniqueMaintenance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFicheTechniqueMaintenance() {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.save(ficheTechniqueMaintenance).block();

        int databaseSizeBeforeDelete = ficheTechniqueMaintenanceRepository.findAll().collectList().block().size();

        // Delete the ficheTechniqueMaintenance
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ficheTechniqueMaintenance.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll().collectList().block();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
