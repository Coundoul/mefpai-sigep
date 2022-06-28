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
import sn.coundoul.gestion.patrimoine.domain.Atelier;
import sn.coundoul.gestion.patrimoine.repository.AtelierRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link AtelierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class AtelierResourceIT {

    private static final String DEFAULT_NOM_ATELIER = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ATELIER = "BBBBBBBBBB";

    private static final Double DEFAULT_SURFACE = 1D;
    private static final Double UPDATED_SURFACE = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ateliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AtelierRepository atelierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Atelier atelier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atelier createEntity(EntityManager em) {
        Atelier atelier = new Atelier().nomAtelier(DEFAULT_NOM_ATELIER).surface(DEFAULT_SURFACE).description(DEFAULT_DESCRIPTION);
        return atelier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atelier createUpdatedEntity(EntityManager em) {
        Atelier atelier = new Atelier().nomAtelier(UPDATED_NOM_ATELIER).surface(UPDATED_SURFACE).description(UPDATED_DESCRIPTION);
        return atelier;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Atelier.class).block();
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
        atelier = createEntity(em);
    }

    @Test
    void createAtelier() throws Exception {
        int databaseSizeBeforeCreate = atelierRepository.findAll().collectList().block().size();
        // Create the Atelier
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeCreate + 1);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getNomAtelier()).isEqualTo(DEFAULT_NOM_ATELIER);
        assertThat(testAtelier.getSurface()).isEqualTo(DEFAULT_SURFACE);
        assertThat(testAtelier.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createAtelierWithExistingId() throws Exception {
        // Create the Atelier with an existing ID
        atelier.setId(1L);

        int databaseSizeBeforeCreate = atelierRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomAtelierIsRequired() throws Exception {
        int databaseSizeBeforeTest = atelierRepository.findAll().collectList().block().size();
        // set the field null
        atelier.setNomAtelier(null);

        // Create the Atelier, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSurfaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = atelierRepository.findAll().collectList().block().size();
        // set the field null
        atelier.setSurface(null);

        // Create the Atelier, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = atelierRepository.findAll().collectList().block().size();
        // set the field null
        atelier.setDescription(null);

        // Create the Atelier, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAteliers() {
        // Initialize the database
        atelierRepository.save(atelier).block();

        // Get all the atelierList
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
            .value(hasItem(atelier.getId().intValue()))
            .jsonPath("$.[*].nomAtelier")
            .value(hasItem(DEFAULT_NOM_ATELIER))
            .jsonPath("$.[*].surface")
            .value(hasItem(DEFAULT_SURFACE.doubleValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getAtelier() {
        // Initialize the database
        atelierRepository.save(atelier).block();

        // Get the atelier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, atelier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(atelier.getId().intValue()))
            .jsonPath("$.nomAtelier")
            .value(is(DEFAULT_NOM_ATELIER))
            .jsonPath("$.surface")
            .value(is(DEFAULT_SURFACE.doubleValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingAtelier() {
        // Get the atelier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAtelier() throws Exception {
        // Initialize the database
        atelierRepository.save(atelier).block();

        int databaseSizeBeforeUpdate = atelierRepository.findAll().collectList().block().size();

        // Update the atelier
        Atelier updatedAtelier = atelierRepository.findById(atelier.getId()).block();
        updatedAtelier.nomAtelier(UPDATED_NOM_ATELIER).surface(UPDATED_SURFACE).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAtelier.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAtelier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getNomAtelier()).isEqualTo(UPDATED_NOM_ATELIER);
        assertThat(testAtelier.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testAtelier.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().collectList().block().size();
        atelier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, atelier.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().collectList().block().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().collectList().block().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAtelierWithPatch() throws Exception {
        // Initialize the database
        atelierRepository.save(atelier).block();

        int databaseSizeBeforeUpdate = atelierRepository.findAll().collectList().block().size();

        // Update the atelier using partial update
        Atelier partialUpdatedAtelier = new Atelier();
        partialUpdatedAtelier.setId(atelier.getId());

        partialUpdatedAtelier.nomAtelier(UPDATED_NOM_ATELIER).surface(UPDATED_SURFACE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAtelier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAtelier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getNomAtelier()).isEqualTo(UPDATED_NOM_ATELIER);
        assertThat(testAtelier.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testAtelier.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateAtelierWithPatch() throws Exception {
        // Initialize the database
        atelierRepository.save(atelier).block();

        int databaseSizeBeforeUpdate = atelierRepository.findAll().collectList().block().size();

        // Update the atelier using partial update
        Atelier partialUpdatedAtelier = new Atelier();
        partialUpdatedAtelier.setId(atelier.getId());

        partialUpdatedAtelier.nomAtelier(UPDATED_NOM_ATELIER).surface(UPDATED_SURFACE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAtelier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAtelier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getNomAtelier()).isEqualTo(UPDATED_NOM_ATELIER);
        assertThat(testAtelier.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testAtelier.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().collectList().block().size();
        atelier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, atelier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().collectList().block().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().collectList().block().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(atelier))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAtelier() {
        // Initialize the database
        atelierRepository.save(atelier).block();

        int databaseSizeBeforeDelete = atelierRepository.findAll().collectList().block().size();

        // Delete the atelier
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, atelier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Atelier> atelierList = atelierRepository.findAll().collectList().block();
        assertThat(atelierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
