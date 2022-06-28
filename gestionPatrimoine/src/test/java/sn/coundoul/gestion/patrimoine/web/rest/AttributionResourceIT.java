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
import sn.coundoul.gestion.patrimoine.domain.Attribution;
import sn.coundoul.gestion.patrimoine.repository.AttributionRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link AttributionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class AttributionResourceIT {

    private static final Integer DEFAULT_QUANTITE_AFFECTER = 1;
    private static final Integer UPDATED_QUANTITE_AFFECTER = 2;

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final Instant DEFAULT_DATE_AFFECTATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_AFFECTATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/attributions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttributionRepository attributionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Attribution attribution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attribution createEntity(EntityManager em) {
        Attribution attribution = new Attribution()
            .quantiteAffecter(DEFAULT_QUANTITE_AFFECTER)
            .idPers(DEFAULT_ID_PERS)
            .dateAffectation(DEFAULT_DATE_AFFECTATION);
        return attribution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attribution createUpdatedEntity(EntityManager em) {
        Attribution attribution = new Attribution()
            .quantiteAffecter(UPDATED_QUANTITE_AFFECTER)
            .idPers(UPDATED_ID_PERS)
            .dateAffectation(UPDATED_DATE_AFFECTATION);
        return attribution;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Attribution.class).block();
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
        attribution = createEntity(em);
    }

    @Test
    void createAttribution() throws Exception {
        int databaseSizeBeforeCreate = attributionRepository.findAll().collectList().block().size();
        // Create the Attribution
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attribution))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeCreate + 1);
        Attribution testAttribution = attributionList.get(attributionList.size() - 1);
        assertThat(testAttribution.getQuantiteAffecter()).isEqualTo(DEFAULT_QUANTITE_AFFECTER);
        assertThat(testAttribution.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
        assertThat(testAttribution.getDateAffectation()).isEqualTo(DEFAULT_DATE_AFFECTATION);
    }

    @Test
    void createAttributionWithExistingId() throws Exception {
        // Create the Attribution with an existing ID
        attribution.setId(1L);

        int databaseSizeBeforeCreate = attributionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkQuantiteAffecterIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributionRepository.findAll().collectList().block().size();
        // set the field null
        attribution.setQuantiteAffecter(null);

        // Create the Attribution, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributionRepository.findAll().collectList().block().size();
        // set the field null
        attribution.setIdPers(null);

        // Create the Attribution, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAttributions() {
        // Initialize the database
        attributionRepository.save(attribution).block();

        // Get all the attributionList
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
            .value(hasItem(attribution.getId().intValue()))
            .jsonPath("$.[*].quantiteAffecter")
            .value(hasItem(DEFAULT_QUANTITE_AFFECTER))
            .jsonPath("$.[*].idPers")
            .value(hasItem(DEFAULT_ID_PERS))
            .jsonPath("$.[*].dateAffectation")
            .value(hasItem(DEFAULT_DATE_AFFECTATION.toString()));
    }

    @Test
    void getAttribution() {
        // Initialize the database
        attributionRepository.save(attribution).block();

        // Get the attribution
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, attribution.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(attribution.getId().intValue()))
            .jsonPath("$.quantiteAffecter")
            .value(is(DEFAULT_QUANTITE_AFFECTER))
            .jsonPath("$.idPers")
            .value(is(DEFAULT_ID_PERS))
            .jsonPath("$.dateAffectation")
            .value(is(DEFAULT_DATE_AFFECTATION.toString()));
    }

    @Test
    void getNonExistingAttribution() {
        // Get the attribution
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAttribution() throws Exception {
        // Initialize the database
        attributionRepository.save(attribution).block();

        int databaseSizeBeforeUpdate = attributionRepository.findAll().collectList().block().size();

        // Update the attribution
        Attribution updatedAttribution = attributionRepository.findById(attribution.getId()).block();
        updatedAttribution.quantiteAffecter(UPDATED_QUANTITE_AFFECTER).idPers(UPDATED_ID_PERS).dateAffectation(UPDATED_DATE_AFFECTATION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAttribution.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAttribution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeUpdate);
        Attribution testAttribution = attributionList.get(attributionList.size() - 1);
        assertThat(testAttribution.getQuantiteAffecter()).isEqualTo(UPDATED_QUANTITE_AFFECTER);
        assertThat(testAttribution.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testAttribution.getDateAffectation()).isEqualTo(UPDATED_DATE_AFFECTATION);
    }

    @Test
    void putNonExistingAttribution() throws Exception {
        int databaseSizeBeforeUpdate = attributionRepository.findAll().collectList().block().size();
        attribution.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attribution.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAttribution() throws Exception {
        int databaseSizeBeforeUpdate = attributionRepository.findAll().collectList().block().size();
        attribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAttribution() throws Exception {
        int databaseSizeBeforeUpdate = attributionRepository.findAll().collectList().block().size();
        attribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attribution))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAttributionWithPatch() throws Exception {
        // Initialize the database
        attributionRepository.save(attribution).block();

        int databaseSizeBeforeUpdate = attributionRepository.findAll().collectList().block().size();

        // Update the attribution using partial update
        Attribution partialUpdatedAttribution = new Attribution();
        partialUpdatedAttribution.setId(attribution.getId());

        partialUpdatedAttribution
            .quantiteAffecter(UPDATED_QUANTITE_AFFECTER)
            .idPers(UPDATED_ID_PERS)
            .dateAffectation(UPDATED_DATE_AFFECTATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttribution.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAttribution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeUpdate);
        Attribution testAttribution = attributionList.get(attributionList.size() - 1);
        assertThat(testAttribution.getQuantiteAffecter()).isEqualTo(UPDATED_QUANTITE_AFFECTER);
        assertThat(testAttribution.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testAttribution.getDateAffectation()).isEqualTo(UPDATED_DATE_AFFECTATION);
    }

    @Test
    void fullUpdateAttributionWithPatch() throws Exception {
        // Initialize the database
        attributionRepository.save(attribution).block();

        int databaseSizeBeforeUpdate = attributionRepository.findAll().collectList().block().size();

        // Update the attribution using partial update
        Attribution partialUpdatedAttribution = new Attribution();
        partialUpdatedAttribution.setId(attribution.getId());

        partialUpdatedAttribution
            .quantiteAffecter(UPDATED_QUANTITE_AFFECTER)
            .idPers(UPDATED_ID_PERS)
            .dateAffectation(UPDATED_DATE_AFFECTATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttribution.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAttribution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeUpdate);
        Attribution testAttribution = attributionList.get(attributionList.size() - 1);
        assertThat(testAttribution.getQuantiteAffecter()).isEqualTo(UPDATED_QUANTITE_AFFECTER);
        assertThat(testAttribution.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testAttribution.getDateAffectation()).isEqualTo(UPDATED_DATE_AFFECTATION);
    }

    @Test
    void patchNonExistingAttribution() throws Exception {
        int databaseSizeBeforeUpdate = attributionRepository.findAll().collectList().block().size();
        attribution.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, attribution.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAttribution() throws Exception {
        int databaseSizeBeforeUpdate = attributionRepository.findAll().collectList().block().size();
        attribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAttribution() throws Exception {
        int databaseSizeBeforeUpdate = attributionRepository.findAll().collectList().block().size();
        attribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attribution))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Attribution in the database
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAttribution() {
        // Initialize the database
        attributionRepository.save(attribution).block();

        int databaseSizeBeforeDelete = attributionRepository.findAll().collectList().block().size();

        // Delete the attribution
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, attribution.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Attribution> attributionList = attributionRepository.findAll().collectList().block();
        assertThat(attributionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
