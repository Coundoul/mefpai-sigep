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
import sn.coundoul.gestion.patrimoine.domain.NatureFoncier;
import sn.coundoul.gestion.patrimoine.repository.NatureFoncierRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link NatureFoncierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class NatureFoncierResourceIT {

    private static final String DEFAULT_TYPE_FONCIER = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_FONCIER = "BBBBBBBBBB";

    private static final String DEFAULT_PIECE_JOINTE = "AAAAAAAAAA";
    private static final String UPDATED_PIECE_JOINTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nature-fonciers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NatureFoncierRepository natureFoncierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private NatureFoncier natureFoncier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NatureFoncier createEntity(EntityManager em) {
        NatureFoncier natureFoncier = new NatureFoncier().typeFoncier(DEFAULT_TYPE_FONCIER).pieceJointe(DEFAULT_PIECE_JOINTE);
        return natureFoncier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NatureFoncier createUpdatedEntity(EntityManager em) {
        NatureFoncier natureFoncier = new NatureFoncier().typeFoncier(UPDATED_TYPE_FONCIER).pieceJointe(UPDATED_PIECE_JOINTE);
        return natureFoncier;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(NatureFoncier.class).block();
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
        natureFoncier = createEntity(em);
    }

    @Test
    void createNatureFoncier() throws Exception {
        int databaseSizeBeforeCreate = natureFoncierRepository.findAll().collectList().block().size();
        // Create the NatureFoncier
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(natureFoncier))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeCreate + 1);
        NatureFoncier testNatureFoncier = natureFoncierList.get(natureFoncierList.size() - 1);
        assertThat(testNatureFoncier.getTypeFoncier()).isEqualTo(DEFAULT_TYPE_FONCIER);
        assertThat(testNatureFoncier.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
    }

    @Test
    void createNatureFoncierWithExistingId() throws Exception {
        // Create the NatureFoncier with an existing ID
        natureFoncier.setId(1L);

        int databaseSizeBeforeCreate = natureFoncierRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(natureFoncier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeFoncierIsRequired() throws Exception {
        int databaseSizeBeforeTest = natureFoncierRepository.findAll().collectList().block().size();
        // set the field null
        natureFoncier.setTypeFoncier(null);

        // Create the NatureFoncier, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(natureFoncier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPieceJointeIsRequired() throws Exception {
        int databaseSizeBeforeTest = natureFoncierRepository.findAll().collectList().block().size();
        // set the field null
        natureFoncier.setPieceJointe(null);

        // Create the NatureFoncier, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(natureFoncier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllNatureFonciers() {
        // Initialize the database
        natureFoncierRepository.save(natureFoncier).block();

        // Get all the natureFoncierList
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
            .value(hasItem(natureFoncier.getId().intValue()))
            .jsonPath("$.[*].typeFoncier")
            .value(hasItem(DEFAULT_TYPE_FONCIER))
            .jsonPath("$.[*].pieceJointe")
            .value(hasItem(DEFAULT_PIECE_JOINTE));
    }

    @Test
    void getNatureFoncier() {
        // Initialize the database
        natureFoncierRepository.save(natureFoncier).block();

        // Get the natureFoncier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, natureFoncier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(natureFoncier.getId().intValue()))
            .jsonPath("$.typeFoncier")
            .value(is(DEFAULT_TYPE_FONCIER))
            .jsonPath("$.pieceJointe")
            .value(is(DEFAULT_PIECE_JOINTE));
    }

    @Test
    void getNonExistingNatureFoncier() {
        // Get the natureFoncier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewNatureFoncier() throws Exception {
        // Initialize the database
        natureFoncierRepository.save(natureFoncier).block();

        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().collectList().block().size();

        // Update the natureFoncier
        NatureFoncier updatedNatureFoncier = natureFoncierRepository.findById(natureFoncier.getId()).block();
        updatedNatureFoncier.typeFoncier(UPDATED_TYPE_FONCIER).pieceJointe(UPDATED_PIECE_JOINTE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedNatureFoncier.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedNatureFoncier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
        NatureFoncier testNatureFoncier = natureFoncierList.get(natureFoncierList.size() - 1);
        assertThat(testNatureFoncier.getTypeFoncier()).isEqualTo(UPDATED_TYPE_FONCIER);
        assertThat(testNatureFoncier.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
    }

    @Test
    void putNonExistingNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().collectList().block().size();
        natureFoncier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, natureFoncier.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(natureFoncier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().collectList().block().size();
        natureFoncier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(natureFoncier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().collectList().block().size();
        natureFoncier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(natureFoncier))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNatureFoncierWithPatch() throws Exception {
        // Initialize the database
        natureFoncierRepository.save(natureFoncier).block();

        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().collectList().block().size();

        // Update the natureFoncier using partial update
        NatureFoncier partialUpdatedNatureFoncier = new NatureFoncier();
        partialUpdatedNatureFoncier.setId(natureFoncier.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNatureFoncier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNatureFoncier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
        NatureFoncier testNatureFoncier = natureFoncierList.get(natureFoncierList.size() - 1);
        assertThat(testNatureFoncier.getTypeFoncier()).isEqualTo(DEFAULT_TYPE_FONCIER);
        assertThat(testNatureFoncier.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
    }

    @Test
    void fullUpdateNatureFoncierWithPatch() throws Exception {
        // Initialize the database
        natureFoncierRepository.save(natureFoncier).block();

        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().collectList().block().size();

        // Update the natureFoncier using partial update
        NatureFoncier partialUpdatedNatureFoncier = new NatureFoncier();
        partialUpdatedNatureFoncier.setId(natureFoncier.getId());

        partialUpdatedNatureFoncier.typeFoncier(UPDATED_TYPE_FONCIER).pieceJointe(UPDATED_PIECE_JOINTE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNatureFoncier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNatureFoncier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
        NatureFoncier testNatureFoncier = natureFoncierList.get(natureFoncierList.size() - 1);
        assertThat(testNatureFoncier.getTypeFoncier()).isEqualTo(UPDATED_TYPE_FONCIER);
        assertThat(testNatureFoncier.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
    }

    @Test
    void patchNonExistingNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().collectList().block().size();
        natureFoncier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, natureFoncier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(natureFoncier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().collectList().block().size();
        natureFoncier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(natureFoncier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().collectList().block().size();
        natureFoncier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(natureFoncier))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNatureFoncier() {
        // Initialize the database
        natureFoncierRepository.save(natureFoncier).block();

        int databaseSizeBeforeDelete = natureFoncierRepository.findAll().collectList().block().size();

        // Delete the natureFoncier
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, natureFoncier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll().collectList().block();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
