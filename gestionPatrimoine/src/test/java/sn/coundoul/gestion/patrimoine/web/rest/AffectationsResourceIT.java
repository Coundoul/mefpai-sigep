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
import sn.coundoul.gestion.patrimoine.domain.Affectations;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Type;
import sn.coundoul.gestion.patrimoine.repository.AffectationsRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link AffectationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class AffectationsResourceIT {

    private static final Integer DEFAULT_QUANTITE_AFFECTER = 1;
    private static final Integer UPDATED_QUANTITE_AFFECTER = 2;

    private static final Type DEFAULT_TYPE_ATTRIBUTION = Type.ReAffectation;
    private static final Type UPDATED_TYPE_ATTRIBUTION = Type.Affectation;

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final Instant DEFAULT_DATE_ATTRIBUTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ATTRIBUTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/affectations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AffectationsRepository affectationsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Affectations affectations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affectations createEntity(EntityManager em) {
        Affectations affectations = new Affectations()
            .quantiteAffecter(DEFAULT_QUANTITE_AFFECTER)
            .typeAttribution(DEFAULT_TYPE_ATTRIBUTION)
            .idPers(DEFAULT_ID_PERS)
            .dateAttribution(DEFAULT_DATE_ATTRIBUTION);
        return affectations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affectations createUpdatedEntity(EntityManager em) {
        Affectations affectations = new Affectations()
            .quantiteAffecter(UPDATED_QUANTITE_AFFECTER)
            .typeAttribution(UPDATED_TYPE_ATTRIBUTION)
            .idPers(UPDATED_ID_PERS)
            .dateAttribution(UPDATED_DATE_ATTRIBUTION);
        return affectations;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Affectations.class).block();
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
        affectations = createEntity(em);
    }

    @Test
    void createAffectations() throws Exception {
        int databaseSizeBeforeCreate = affectationsRepository.findAll().collectList().block().size();
        // Create the Affectations
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeCreate + 1);
        Affectations testAffectations = affectationsList.get(affectationsList.size() - 1);
        assertThat(testAffectations.getQuantiteAffecter()).isEqualTo(DEFAULT_QUANTITE_AFFECTER);
        assertThat(testAffectations.getTypeAttribution()).isEqualTo(DEFAULT_TYPE_ATTRIBUTION);
        assertThat(testAffectations.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
        assertThat(testAffectations.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
    }

    @Test
    void createAffectationsWithExistingId() throws Exception {
        // Create the Affectations with an existing ID
        affectations.setId(1L);

        int databaseSizeBeforeCreate = affectationsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkQuantiteAffecterIsRequired() throws Exception {
        int databaseSizeBeforeTest = affectationsRepository.findAll().collectList().block().size();
        // set the field null
        affectations.setQuantiteAffecter(null);

        // Create the Affectations, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeAttributionIsRequired() throws Exception {
        int databaseSizeBeforeTest = affectationsRepository.findAll().collectList().block().size();
        // set the field null
        affectations.setTypeAttribution(null);

        // Create the Affectations, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = affectationsRepository.findAll().collectList().block().size();
        // set the field null
        affectations.setIdPers(null);

        // Create the Affectations, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAffectations() {
        // Initialize the database
        affectationsRepository.save(affectations).block();

        // Get all the affectationsList
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
            .value(hasItem(affectations.getId().intValue()))
            .jsonPath("$.[*].quantiteAffecter")
            .value(hasItem(DEFAULT_QUANTITE_AFFECTER))
            .jsonPath("$.[*].typeAttribution")
            .value(hasItem(DEFAULT_TYPE_ATTRIBUTION.toString()))
            .jsonPath("$.[*].idPers")
            .value(hasItem(DEFAULT_ID_PERS))
            .jsonPath("$.[*].dateAttribution")
            .value(hasItem(DEFAULT_DATE_ATTRIBUTION.toString()));
    }

    @Test
    void getAffectations() {
        // Initialize the database
        affectationsRepository.save(affectations).block();

        // Get the affectations
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, affectations.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(affectations.getId().intValue()))
            .jsonPath("$.quantiteAffecter")
            .value(is(DEFAULT_QUANTITE_AFFECTER))
            .jsonPath("$.typeAttribution")
            .value(is(DEFAULT_TYPE_ATTRIBUTION.toString()))
            .jsonPath("$.idPers")
            .value(is(DEFAULT_ID_PERS))
            .jsonPath("$.dateAttribution")
            .value(is(DEFAULT_DATE_ATTRIBUTION.toString()));
    }

    @Test
    void getNonExistingAffectations() {
        // Get the affectations
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAffectations() throws Exception {
        // Initialize the database
        affectationsRepository.save(affectations).block();

        int databaseSizeBeforeUpdate = affectationsRepository.findAll().collectList().block().size();

        // Update the affectations
        Affectations updatedAffectations = affectationsRepository.findById(affectations.getId()).block();
        updatedAffectations
            .quantiteAffecter(UPDATED_QUANTITE_AFFECTER)
            .typeAttribution(UPDATED_TYPE_ATTRIBUTION)
            .idPers(UPDATED_ID_PERS)
            .dateAttribution(UPDATED_DATE_ATTRIBUTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAffectations.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAffectations))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
        Affectations testAffectations = affectationsList.get(affectationsList.size() - 1);
        assertThat(testAffectations.getQuantiteAffecter()).isEqualTo(UPDATED_QUANTITE_AFFECTER);
        assertThat(testAffectations.getTypeAttribution()).isEqualTo(UPDATED_TYPE_ATTRIBUTION);
        assertThat(testAffectations.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testAffectations.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
    }

    @Test
    void putNonExistingAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().collectList().block().size();
        affectations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, affectations.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().collectList().block().size();
        affectations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().collectList().block().size();
        affectations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAffectationsWithPatch() throws Exception {
        // Initialize the database
        affectationsRepository.save(affectations).block();

        int databaseSizeBeforeUpdate = affectationsRepository.findAll().collectList().block().size();

        // Update the affectations using partial update
        Affectations partialUpdatedAffectations = new Affectations();
        partialUpdatedAffectations.setId(affectations.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAffectations.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAffectations))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
        Affectations testAffectations = affectationsList.get(affectationsList.size() - 1);
        assertThat(testAffectations.getQuantiteAffecter()).isEqualTo(DEFAULT_QUANTITE_AFFECTER);
        assertThat(testAffectations.getTypeAttribution()).isEqualTo(DEFAULT_TYPE_ATTRIBUTION);
        assertThat(testAffectations.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
        assertThat(testAffectations.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
    }

    @Test
    void fullUpdateAffectationsWithPatch() throws Exception {
        // Initialize the database
        affectationsRepository.save(affectations).block();

        int databaseSizeBeforeUpdate = affectationsRepository.findAll().collectList().block().size();

        // Update the affectations using partial update
        Affectations partialUpdatedAffectations = new Affectations();
        partialUpdatedAffectations.setId(affectations.getId());

        partialUpdatedAffectations
            .quantiteAffecter(UPDATED_QUANTITE_AFFECTER)
            .typeAttribution(UPDATED_TYPE_ATTRIBUTION)
            .idPers(UPDATED_ID_PERS)
            .dateAttribution(UPDATED_DATE_ATTRIBUTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAffectations.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAffectations))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
        Affectations testAffectations = affectationsList.get(affectationsList.size() - 1);
        assertThat(testAffectations.getQuantiteAffecter()).isEqualTo(UPDATED_QUANTITE_AFFECTER);
        assertThat(testAffectations.getTypeAttribution()).isEqualTo(UPDATED_TYPE_ATTRIBUTION);
        assertThat(testAffectations.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testAffectations.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
    }

    @Test
    void patchNonExistingAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().collectList().block().size();
        affectations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, affectations.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().collectList().block().size();
        affectations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().collectList().block().size();
        affectations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectations))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAffectations() {
        // Initialize the database
        affectationsRepository.save(affectations).block();

        int databaseSizeBeforeDelete = affectationsRepository.findAll().collectList().block().size();

        // Delete the affectations
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, affectations.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Affectations> affectationsList = affectationsRepository.findAll().collectList().block();
        assertThat(affectationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
