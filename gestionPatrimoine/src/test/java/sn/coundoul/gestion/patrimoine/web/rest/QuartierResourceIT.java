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
import sn.coundoul.gestion.patrimoine.domain.Quartier;
import sn.coundoul.gestion.patrimoine.repository.QuartierRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link QuartierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class QuartierResourceIT {

    private static final String DEFAULT_NOM_QUARTIER = "AAAAAAAAAA";
    private static final String UPDATED_NOM_QUARTIER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quartiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuartierRepository quartierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Quartier quartier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quartier createEntity(EntityManager em) {
        Quartier quartier = new Quartier().nomQuartier(DEFAULT_NOM_QUARTIER);
        return quartier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quartier createUpdatedEntity(EntityManager em) {
        Quartier quartier = new Quartier().nomQuartier(UPDATED_NOM_QUARTIER);
        return quartier;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Quartier.class).block();
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
        quartier = createEntity(em);
    }

    @Test
    void createQuartier() throws Exception {
        int databaseSizeBeforeCreate = quartierRepository.findAll().collectList().block().size();
        // Create the Quartier
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quartier))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeCreate + 1);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNomQuartier()).isEqualTo(DEFAULT_NOM_QUARTIER);
    }

    @Test
    void createQuartierWithExistingId() throws Exception {
        // Create the Quartier with an existing ID
        quartier.setId(1L);

        int databaseSizeBeforeCreate = quartierRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quartier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomQuartierIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartierRepository.findAll().collectList().block().size();
        // set the field null
        quartier.setNomQuartier(null);

        // Create the Quartier, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quartier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllQuartiers() {
        // Initialize the database
        quartierRepository.save(quartier).block();

        // Get all the quartierList
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
            .value(hasItem(quartier.getId().intValue()))
            .jsonPath("$.[*].nomQuartier")
            .value(hasItem(DEFAULT_NOM_QUARTIER));
    }

    @Test
    void getQuartier() {
        // Initialize the database
        quartierRepository.save(quartier).block();

        // Get the quartier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, quartier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(quartier.getId().intValue()))
            .jsonPath("$.nomQuartier")
            .value(is(DEFAULT_NOM_QUARTIER));
    }

    @Test
    void getNonExistingQuartier() {
        // Get the quartier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewQuartier() throws Exception {
        // Initialize the database
        quartierRepository.save(quartier).block();

        int databaseSizeBeforeUpdate = quartierRepository.findAll().collectList().block().size();

        // Update the quartier
        Quartier updatedQuartier = quartierRepository.findById(quartier.getId()).block();
        updatedQuartier.nomQuartier(UPDATED_NOM_QUARTIER);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedQuartier.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedQuartier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNomQuartier()).isEqualTo(UPDATED_NOM_QUARTIER);
    }

    @Test
    void putNonExistingQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().collectList().block().size();
        quartier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, quartier.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quartier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().collectList().block().size();
        quartier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quartier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().collectList().block().size();
        quartier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quartier))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateQuartierWithPatch() throws Exception {
        // Initialize the database
        quartierRepository.save(quartier).block();

        int databaseSizeBeforeUpdate = quartierRepository.findAll().collectList().block().size();

        // Update the quartier using partial update
        Quartier partialUpdatedQuartier = new Quartier();
        partialUpdatedQuartier.setId(quartier.getId());

        partialUpdatedQuartier.nomQuartier(UPDATED_NOM_QUARTIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuartier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedQuartier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNomQuartier()).isEqualTo(UPDATED_NOM_QUARTIER);
    }

    @Test
    void fullUpdateQuartierWithPatch() throws Exception {
        // Initialize the database
        quartierRepository.save(quartier).block();

        int databaseSizeBeforeUpdate = quartierRepository.findAll().collectList().block().size();

        // Update the quartier using partial update
        Quartier partialUpdatedQuartier = new Quartier();
        partialUpdatedQuartier.setId(quartier.getId());

        partialUpdatedQuartier.nomQuartier(UPDATED_NOM_QUARTIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuartier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedQuartier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNomQuartier()).isEqualTo(UPDATED_NOM_QUARTIER);
    }

    @Test
    void patchNonExistingQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().collectList().block().size();
        quartier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, quartier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(quartier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().collectList().block().size();
        quartier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(quartier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().collectList().block().size();
        quartier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(quartier))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteQuartier() {
        // Initialize the database
        quartierRepository.save(quartier).block();

        int databaseSizeBeforeDelete = quartierRepository.findAll().collectList().block().size();

        // Delete the quartier
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, quartier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Quartier> quartierList = quartierRepository.findAll().collectList().block();
        assertThat(quartierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
