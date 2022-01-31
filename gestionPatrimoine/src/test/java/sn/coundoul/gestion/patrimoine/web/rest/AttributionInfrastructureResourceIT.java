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
import sn.coundoul.gestion.patrimoine.domain.AttributionInfrastructure;
import sn.coundoul.gestion.patrimoine.repository.AttributionInfrastructureRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link AttributionInfrastructureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class AttributionInfrastructureResourceIT {

    private static final Instant DEFAULT_DATE_ATTRIBUTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ATTRIBUTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Integer DEFAULT_ID_EQUIPEMENT = 1;
    private static final Integer UPDATED_ID_EQUIPEMENT = 2;

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/attribution-infrastructures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttributionInfrastructureRepository attributionInfrastructureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AttributionInfrastructure attributionInfrastructure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributionInfrastructure createEntity(EntityManager em) {
        AttributionInfrastructure attributionInfrastructure = new AttributionInfrastructure()
            .dateAttribution(DEFAULT_DATE_ATTRIBUTION)
            .quantite(DEFAULT_QUANTITE)
            .idEquipement(DEFAULT_ID_EQUIPEMENT)
            .idPers(DEFAULT_ID_PERS);
        return attributionInfrastructure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributionInfrastructure createUpdatedEntity(EntityManager em) {
        AttributionInfrastructure attributionInfrastructure = new AttributionInfrastructure()
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);
        return attributionInfrastructure;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AttributionInfrastructure.class).block();
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
        attributionInfrastructure = createEntity(em);
    }

    @Test
    void createAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeCreate = attributionInfrastructureRepository.findAll().collectList().block().size();
        // Create the AttributionInfrastructure
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeCreate + 1);
        AttributionInfrastructure testAttributionInfrastructure = attributionInfrastructureList.get(
            attributionInfrastructureList.size() - 1
        );
        assertThat(testAttributionInfrastructure.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
        assertThat(testAttributionInfrastructure.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testAttributionInfrastructure.getIdEquipement()).isEqualTo(DEFAULT_ID_EQUIPEMENT);
        assertThat(testAttributionInfrastructure.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    void createAttributionInfrastructureWithExistingId() throws Exception {
        // Create the AttributionInfrastructure with an existing ID
        attributionInfrastructure.setId(1L);

        int databaseSizeBeforeCreate = attributionInfrastructureRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributionInfrastructureRepository.findAll().collectList().block().size();
        // set the field null
        attributionInfrastructure.setQuantite(null);

        // Create the AttributionInfrastructure, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdEquipementIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributionInfrastructureRepository.findAll().collectList().block().size();
        // set the field null
        attributionInfrastructure.setIdEquipement(null);

        // Create the AttributionInfrastructure, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributionInfrastructureRepository.findAll().collectList().block().size();
        // set the field null
        attributionInfrastructure.setIdPers(null);

        // Create the AttributionInfrastructure, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAttributionInfrastructures() {
        // Initialize the database
        attributionInfrastructureRepository.save(attributionInfrastructure).block();

        // Get all the attributionInfrastructureList
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
            .value(hasItem(attributionInfrastructure.getId().intValue()))
            .jsonPath("$.[*].dateAttribution")
            .value(hasItem(DEFAULT_DATE_ATTRIBUTION.toString()))
            .jsonPath("$.[*].quantite")
            .value(hasItem(DEFAULT_QUANTITE))
            .jsonPath("$.[*].idEquipement")
            .value(hasItem(DEFAULT_ID_EQUIPEMENT))
            .jsonPath("$.[*].idPers")
            .value(hasItem(DEFAULT_ID_PERS));
    }

    @Test
    void getAttributionInfrastructure() {
        // Initialize the database
        attributionInfrastructureRepository.save(attributionInfrastructure).block();

        // Get the attributionInfrastructure
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, attributionInfrastructure.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(attributionInfrastructure.getId().intValue()))
            .jsonPath("$.dateAttribution")
            .value(is(DEFAULT_DATE_ATTRIBUTION.toString()))
            .jsonPath("$.quantite")
            .value(is(DEFAULT_QUANTITE))
            .jsonPath("$.idEquipement")
            .value(is(DEFAULT_ID_EQUIPEMENT))
            .jsonPath("$.idPers")
            .value(is(DEFAULT_ID_PERS));
    }

    @Test
    void getNonExistingAttributionInfrastructure() {
        // Get the attributionInfrastructure
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAttributionInfrastructure() throws Exception {
        // Initialize the database
        attributionInfrastructureRepository.save(attributionInfrastructure).block();

        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().collectList().block().size();

        // Update the attributionInfrastructure
        AttributionInfrastructure updatedAttributionInfrastructure = attributionInfrastructureRepository
            .findById(attributionInfrastructure.getId())
            .block();
        updatedAttributionInfrastructure
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAttributionInfrastructure.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAttributionInfrastructure))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
        AttributionInfrastructure testAttributionInfrastructure = attributionInfrastructureList.get(
            attributionInfrastructureList.size() - 1
        );
        assertThat(testAttributionInfrastructure.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
        assertThat(testAttributionInfrastructure.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testAttributionInfrastructure.getIdEquipement()).isEqualTo(UPDATED_ID_EQUIPEMENT);
        assertThat(testAttributionInfrastructure.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void putNonExistingAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().collectList().block().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attributionInfrastructure.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().collectList().block().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().collectList().block().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAttributionInfrastructureWithPatch() throws Exception {
        // Initialize the database
        attributionInfrastructureRepository.save(attributionInfrastructure).block();

        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().collectList().block().size();

        // Update the attributionInfrastructure using partial update
        AttributionInfrastructure partialUpdatedAttributionInfrastructure = new AttributionInfrastructure();
        partialUpdatedAttributionInfrastructure.setId(attributionInfrastructure.getId());

        partialUpdatedAttributionInfrastructure.quantite(UPDATED_QUANTITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttributionInfrastructure.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributionInfrastructure))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
        AttributionInfrastructure testAttributionInfrastructure = attributionInfrastructureList.get(
            attributionInfrastructureList.size() - 1
        );
        assertThat(testAttributionInfrastructure.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
        assertThat(testAttributionInfrastructure.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testAttributionInfrastructure.getIdEquipement()).isEqualTo(DEFAULT_ID_EQUIPEMENT);
        assertThat(testAttributionInfrastructure.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    void fullUpdateAttributionInfrastructureWithPatch() throws Exception {
        // Initialize the database
        attributionInfrastructureRepository.save(attributionInfrastructure).block();

        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().collectList().block().size();

        // Update the attributionInfrastructure using partial update
        AttributionInfrastructure partialUpdatedAttributionInfrastructure = new AttributionInfrastructure();
        partialUpdatedAttributionInfrastructure.setId(attributionInfrastructure.getId());

        partialUpdatedAttributionInfrastructure
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttributionInfrastructure.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributionInfrastructure))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
        AttributionInfrastructure testAttributionInfrastructure = attributionInfrastructureList.get(
            attributionInfrastructureList.size() - 1
        );
        assertThat(testAttributionInfrastructure.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
        assertThat(testAttributionInfrastructure.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testAttributionInfrastructure.getIdEquipement()).isEqualTo(UPDATED_ID_EQUIPEMENT);
        assertThat(testAttributionInfrastructure.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void patchNonExistingAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().collectList().block().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, attributionInfrastructure.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().collectList().block().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().collectList().block().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAttributionInfrastructure() {
        // Initialize the database
        attributionInfrastructureRepository.save(attributionInfrastructure).block();

        int databaseSizeBeforeDelete = attributionInfrastructureRepository.findAll().collectList().block().size();

        // Delete the attributionInfrastructure
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, attributionInfrastructure.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll().collectList().block();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
