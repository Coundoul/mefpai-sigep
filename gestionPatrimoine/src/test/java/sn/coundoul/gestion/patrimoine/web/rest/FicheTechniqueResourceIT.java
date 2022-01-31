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
import sn.coundoul.gestion.patrimoine.domain.FicheTechnique;
import sn.coundoul.gestion.patrimoine.repository.FicheTechniqueRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link FicheTechniqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class FicheTechniqueResourceIT {

    private static final String DEFAULT_PIECE_JOINTE = "AAAAAAAAAA";
    private static final String UPDATED_PIECE_JOINTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DEPOT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEPOT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/fiche-techniques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FicheTechniqueRepository ficheTechniqueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FicheTechnique ficheTechnique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FicheTechnique createEntity(EntityManager em) {
        FicheTechnique ficheTechnique = new FicheTechnique().pieceJointe(DEFAULT_PIECE_JOINTE).dateDepot(DEFAULT_DATE_DEPOT);
        return ficheTechnique;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FicheTechnique createUpdatedEntity(EntityManager em) {
        FicheTechnique ficheTechnique = new FicheTechnique().pieceJointe(UPDATED_PIECE_JOINTE).dateDepot(UPDATED_DATE_DEPOT);
        return ficheTechnique;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FicheTechnique.class).block();
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
        ficheTechnique = createEntity(em);
    }

    @Test
    void createFicheTechnique() throws Exception {
        int databaseSizeBeforeCreate = ficheTechniqueRepository.findAll().collectList().block().size();
        // Create the FicheTechnique
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechnique))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeCreate + 1);
        FicheTechnique testFicheTechnique = ficheTechniqueList.get(ficheTechniqueList.size() - 1);
        assertThat(testFicheTechnique.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testFicheTechnique.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
    }

    @Test
    void createFicheTechniqueWithExistingId() throws Exception {
        // Create the FicheTechnique with an existing ID
        ficheTechnique.setId(1L);

        int databaseSizeBeforeCreate = ficheTechniqueRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechnique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPieceJointeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheTechniqueRepository.findAll().collectList().block().size();
        // set the field null
        ficheTechnique.setPieceJointe(null);

        // Create the FicheTechnique, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechnique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFicheTechniques() {
        // Initialize the database
        ficheTechniqueRepository.save(ficheTechnique).block();

        // Get all the ficheTechniqueList
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
            .value(hasItem(ficheTechnique.getId().intValue()))
            .jsonPath("$.[*].pieceJointe")
            .value(hasItem(DEFAULT_PIECE_JOINTE))
            .jsonPath("$.[*].dateDepot")
            .value(hasItem(DEFAULT_DATE_DEPOT.toString()));
    }

    @Test
    void getFicheTechnique() {
        // Initialize the database
        ficheTechniqueRepository.save(ficheTechnique).block();

        // Get the ficheTechnique
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ficheTechnique.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ficheTechnique.getId().intValue()))
            .jsonPath("$.pieceJointe")
            .value(is(DEFAULT_PIECE_JOINTE))
            .jsonPath("$.dateDepot")
            .value(is(DEFAULT_DATE_DEPOT.toString()));
    }

    @Test
    void getNonExistingFicheTechnique() {
        // Get the ficheTechnique
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFicheTechnique() throws Exception {
        // Initialize the database
        ficheTechniqueRepository.save(ficheTechnique).block();

        int databaseSizeBeforeUpdate = ficheTechniqueRepository.findAll().collectList().block().size();

        // Update the ficheTechnique
        FicheTechnique updatedFicheTechnique = ficheTechniqueRepository.findById(ficheTechnique.getId()).block();
        updatedFicheTechnique.pieceJointe(UPDATED_PIECE_JOINTE).dateDepot(UPDATED_DATE_DEPOT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFicheTechnique.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFicheTechnique))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeUpdate);
        FicheTechnique testFicheTechnique = ficheTechniqueList.get(ficheTechniqueList.size() - 1);
        assertThat(testFicheTechnique.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testFicheTechnique.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
    }

    @Test
    void putNonExistingFicheTechnique() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueRepository.findAll().collectList().block().size();
        ficheTechnique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ficheTechnique.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechnique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFicheTechnique() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueRepository.findAll().collectList().block().size();
        ficheTechnique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechnique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFicheTechnique() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueRepository.findAll().collectList().block().size();
        ficheTechnique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechnique))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFicheTechniqueWithPatch() throws Exception {
        // Initialize the database
        ficheTechniqueRepository.save(ficheTechnique).block();

        int databaseSizeBeforeUpdate = ficheTechniqueRepository.findAll().collectList().block().size();

        // Update the ficheTechnique using partial update
        FicheTechnique partialUpdatedFicheTechnique = new FicheTechnique();
        partialUpdatedFicheTechnique.setId(ficheTechnique.getId());

        partialUpdatedFicheTechnique.pieceJointe(UPDATED_PIECE_JOINTE).dateDepot(UPDATED_DATE_DEPOT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFicheTechnique.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFicheTechnique))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeUpdate);
        FicheTechnique testFicheTechnique = ficheTechniqueList.get(ficheTechniqueList.size() - 1);
        assertThat(testFicheTechnique.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testFicheTechnique.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
    }

    @Test
    void fullUpdateFicheTechniqueWithPatch() throws Exception {
        // Initialize the database
        ficheTechniqueRepository.save(ficheTechnique).block();

        int databaseSizeBeforeUpdate = ficheTechniqueRepository.findAll().collectList().block().size();

        // Update the ficheTechnique using partial update
        FicheTechnique partialUpdatedFicheTechnique = new FicheTechnique();
        partialUpdatedFicheTechnique.setId(ficheTechnique.getId());

        partialUpdatedFicheTechnique.pieceJointe(UPDATED_PIECE_JOINTE).dateDepot(UPDATED_DATE_DEPOT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFicheTechnique.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFicheTechnique))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeUpdate);
        FicheTechnique testFicheTechnique = ficheTechniqueList.get(ficheTechniqueList.size() - 1);
        assertThat(testFicheTechnique.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testFicheTechnique.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
    }

    @Test
    void patchNonExistingFicheTechnique() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueRepository.findAll().collectList().block().size();
        ficheTechnique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ficheTechnique.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechnique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFicheTechnique() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueRepository.findAll().collectList().block().size();
        ficheTechnique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechnique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFicheTechnique() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueRepository.findAll().collectList().block().size();
        ficheTechnique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ficheTechnique))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FicheTechnique in the database
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFicheTechnique() {
        // Initialize the database
        ficheTechniqueRepository.save(ficheTechnique).block();

        int databaseSizeBeforeDelete = ficheTechniqueRepository.findAll().collectList().block().size();

        // Delete the ficheTechnique
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ficheTechnique.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FicheTechnique> ficheTechniqueList = ficheTechniqueRepository.findAll().collectList().block();
        assertThat(ficheTechniqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
