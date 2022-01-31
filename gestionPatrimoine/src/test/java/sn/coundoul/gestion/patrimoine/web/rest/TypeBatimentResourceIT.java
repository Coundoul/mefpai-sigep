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
import sn.coundoul.gestion.patrimoine.domain.TypeBatiment;
import sn.coundoul.gestion.patrimoine.repository.TypeBatimentRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link TypeBatimentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class TypeBatimentResourceIT {

    private static final String DEFAULT_TYPE_BA = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_BA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-batiments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeBatimentRepository typeBatimentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TypeBatiment typeBatiment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeBatiment createEntity(EntityManager em) {
        TypeBatiment typeBatiment = new TypeBatiment().typeBa(DEFAULT_TYPE_BA);
        return typeBatiment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeBatiment createUpdatedEntity(EntityManager em) {
        TypeBatiment typeBatiment = new TypeBatiment().typeBa(UPDATED_TYPE_BA);
        return typeBatiment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TypeBatiment.class).block();
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
        typeBatiment = createEntity(em);
    }

    @Test
    void createTypeBatiment() throws Exception {
        int databaseSizeBeforeCreate = typeBatimentRepository.findAll().collectList().block().size();
        // Create the TypeBatiment
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeBatiment))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeCreate + 1);
        TypeBatiment testTypeBatiment = typeBatimentList.get(typeBatimentList.size() - 1);
        assertThat(testTypeBatiment.getTypeBa()).isEqualTo(DEFAULT_TYPE_BA);
    }

    @Test
    void createTypeBatimentWithExistingId() throws Exception {
        // Create the TypeBatiment with an existing ID
        typeBatiment.setId(1L);

        int databaseSizeBeforeCreate = typeBatimentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeBatiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeBaIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeBatimentRepository.findAll().collectList().block().size();
        // set the field null
        typeBatiment.setTypeBa(null);

        // Create the TypeBatiment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeBatiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTypeBatiments() {
        // Initialize the database
        typeBatimentRepository.save(typeBatiment).block();

        // Get all the typeBatimentList
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
            .value(hasItem(typeBatiment.getId().intValue()))
            .jsonPath("$.[*].typeBa")
            .value(hasItem(DEFAULT_TYPE_BA));
    }

    @Test
    void getTypeBatiment() {
        // Initialize the database
        typeBatimentRepository.save(typeBatiment).block();

        // Get the typeBatiment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, typeBatiment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(typeBatiment.getId().intValue()))
            .jsonPath("$.typeBa")
            .value(is(DEFAULT_TYPE_BA));
    }

    @Test
    void getNonExistingTypeBatiment() {
        // Get the typeBatiment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTypeBatiment() throws Exception {
        // Initialize the database
        typeBatimentRepository.save(typeBatiment).block();

        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().collectList().block().size();

        // Update the typeBatiment
        TypeBatiment updatedTypeBatiment = typeBatimentRepository.findById(typeBatiment.getId()).block();
        updatedTypeBatiment.typeBa(UPDATED_TYPE_BA);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTypeBatiment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTypeBatiment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
        TypeBatiment testTypeBatiment = typeBatimentList.get(typeBatimentList.size() - 1);
        assertThat(testTypeBatiment.getTypeBa()).isEqualTo(UPDATED_TYPE_BA);
    }

    @Test
    void putNonExistingTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().collectList().block().size();
        typeBatiment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, typeBatiment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeBatiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().collectList().block().size();
        typeBatiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeBatiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().collectList().block().size();
        typeBatiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeBatiment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTypeBatimentWithPatch() throws Exception {
        // Initialize the database
        typeBatimentRepository.save(typeBatiment).block();

        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().collectList().block().size();

        // Update the typeBatiment using partial update
        TypeBatiment partialUpdatedTypeBatiment = new TypeBatiment();
        partialUpdatedTypeBatiment.setId(typeBatiment.getId());

        partialUpdatedTypeBatiment.typeBa(UPDATED_TYPE_BA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTypeBatiment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeBatiment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
        TypeBatiment testTypeBatiment = typeBatimentList.get(typeBatimentList.size() - 1);
        assertThat(testTypeBatiment.getTypeBa()).isEqualTo(UPDATED_TYPE_BA);
    }

    @Test
    void fullUpdateTypeBatimentWithPatch() throws Exception {
        // Initialize the database
        typeBatimentRepository.save(typeBatiment).block();

        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().collectList().block().size();

        // Update the typeBatiment using partial update
        TypeBatiment partialUpdatedTypeBatiment = new TypeBatiment();
        partialUpdatedTypeBatiment.setId(typeBatiment.getId());

        partialUpdatedTypeBatiment.typeBa(UPDATED_TYPE_BA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTypeBatiment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeBatiment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
        TypeBatiment testTypeBatiment = typeBatimentList.get(typeBatimentList.size() - 1);
        assertThat(testTypeBatiment.getTypeBa()).isEqualTo(UPDATED_TYPE_BA);
    }

    @Test
    void patchNonExistingTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().collectList().block().size();
        typeBatiment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, typeBatiment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeBatiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().collectList().block().size();
        typeBatiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeBatiment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().collectList().block().size();
        typeBatiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeBatiment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTypeBatiment() {
        // Initialize the database
        typeBatimentRepository.save(typeBatiment).block();

        int databaseSizeBeforeDelete = typeBatimentRepository.findAll().collectList().block().size();

        // Delete the typeBatiment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, typeBatiment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll().collectList().block();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
