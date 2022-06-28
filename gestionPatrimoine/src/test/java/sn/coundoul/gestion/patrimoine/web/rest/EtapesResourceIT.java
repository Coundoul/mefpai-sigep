package sn.coundoul.gestion.patrimoine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static sn.coundoul.gestion.patrimoine.web.rest.TestUtil.sameInstant;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import sn.coundoul.gestion.patrimoine.domain.Etapes;
import sn.coundoul.gestion.patrimoine.repository.EtapesRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link EtapesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class EtapesResourceIT {

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NOM_TACHE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_TACHE = "BBBBBBBBBB";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/etapes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtapesRepository etapesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Etapes etapes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etapes createEntity(EntityManager em) {
        Etapes etapes = new Etapes()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .nomTache(DEFAULT_NOM_TACHE)
            .duration(DEFAULT_DURATION);
        return etapes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etapes createUpdatedEntity(EntityManager em) {
        Etapes etapes = new Etapes()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .nomTache(UPDATED_NOM_TACHE)
            .duration(UPDATED_DURATION);
        return etapes;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Etapes.class).block();
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
        etapes = createEntity(em);
    }

    @Test
    void createEtapes() throws Exception {
        int databaseSizeBeforeCreate = etapesRepository.findAll().collectList().block().size();
        // Create the Etapes
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeCreate + 1);
        Etapes testEtapes = etapesList.get(etapesList.size() - 1);
        assertThat(testEtapes.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testEtapes.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testEtapes.getNomTache()).isEqualTo(DEFAULT_NOM_TACHE);
        assertThat(testEtapes.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    void createEtapesWithExistingId() throws Exception {
        // Create the Etapes with an existing ID
        etapes.setId(1L);

        int databaseSizeBeforeCreate = etapesRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = etapesRepository.findAll().collectList().block().size();
        // set the field null
        etapes.setDateDebut(null);

        // Create the Etapes, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = etapesRepository.findAll().collectList().block().size();
        // set the field null
        etapes.setDateFin(null);

        // Create the Etapes, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNomTacheIsRequired() throws Exception {
        int databaseSizeBeforeTest = etapesRepository.findAll().collectList().block().size();
        // set the field null
        etapes.setNomTache(null);

        // Create the Etapes, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEtapes() {
        // Initialize the database
        etapesRepository.save(etapes).block();

        // Get all the etapesList
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
            .value(hasItem(etapes.getId().intValue()))
            .jsonPath("$.[*].dateDebut")
            .value(hasItem(sameInstant(DEFAULT_DATE_DEBUT)))
            .jsonPath("$.[*].dateFin")
            .value(hasItem(sameInstant(DEFAULT_DATE_FIN)))
            .jsonPath("$.[*].nomTache")
            .value(hasItem(DEFAULT_NOM_TACHE))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION.toString()));
    }

    @Test
    void getEtapes() {
        // Initialize the database
        etapesRepository.save(etapes).block();

        // Get the etapes
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, etapes.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(etapes.getId().intValue()))
            .jsonPath("$.dateDebut")
            .value(is(sameInstant(DEFAULT_DATE_DEBUT)))
            .jsonPath("$.dateFin")
            .value(is(sameInstant(DEFAULT_DATE_FIN)))
            .jsonPath("$.nomTache")
            .value(is(DEFAULT_NOM_TACHE))
            .jsonPath("$.duration")
            .value(is(DEFAULT_DURATION.toString()));
    }

    @Test
    void getNonExistingEtapes() {
        // Get the etapes
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewEtapes() throws Exception {
        // Initialize the database
        etapesRepository.save(etapes).block();

        int databaseSizeBeforeUpdate = etapesRepository.findAll().collectList().block().size();

        // Update the etapes
        Etapes updatedEtapes = etapesRepository.findById(etapes.getId()).block();
        updatedEtapes.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).nomTache(UPDATED_NOM_TACHE).duration(UPDATED_DURATION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEtapes.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedEtapes))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
        Etapes testEtapes = etapesList.get(etapesList.size() - 1);
        assertThat(testEtapes.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testEtapes.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testEtapes.getNomTache()).isEqualTo(UPDATED_NOM_TACHE);
        assertThat(testEtapes.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    void putNonExistingEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().collectList().block().size();
        etapes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, etapes.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().collectList().block().size();
        etapes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().collectList().block().size();
        etapes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEtapesWithPatch() throws Exception {
        // Initialize the database
        etapesRepository.save(etapes).block();

        int databaseSizeBeforeUpdate = etapesRepository.findAll().collectList().block().size();

        // Update the etapes using partial update
        Etapes partialUpdatedEtapes = new Etapes();
        partialUpdatedEtapes.setId(etapes.getId());

        partialUpdatedEtapes.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).duration(UPDATED_DURATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEtapes.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEtapes))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
        Etapes testEtapes = etapesList.get(etapesList.size() - 1);
        assertThat(testEtapes.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testEtapes.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testEtapes.getNomTache()).isEqualTo(DEFAULT_NOM_TACHE);
        assertThat(testEtapes.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    void fullUpdateEtapesWithPatch() throws Exception {
        // Initialize the database
        etapesRepository.save(etapes).block();

        int databaseSizeBeforeUpdate = etapesRepository.findAll().collectList().block().size();

        // Update the etapes using partial update
        Etapes partialUpdatedEtapes = new Etapes();
        partialUpdatedEtapes.setId(etapes.getId());

        partialUpdatedEtapes.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).nomTache(UPDATED_NOM_TACHE).duration(UPDATED_DURATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEtapes.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEtapes))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
        Etapes testEtapes = etapesList.get(etapesList.size() - 1);
        assertThat(testEtapes.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testEtapes.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testEtapes.getNomTache()).isEqualTo(UPDATED_NOM_TACHE);
        assertThat(testEtapes.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    void patchNonExistingEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().collectList().block().size();
        etapes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, etapes.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().collectList().block().size();
        etapes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().collectList().block().size();
        etapes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(etapes))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEtapes() {
        // Initialize the database
        etapesRepository.save(etapes).block();

        int databaseSizeBeforeDelete = etapesRepository.findAll().collectList().block().size();

        // Delete the etapes
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, etapes.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Etapes> etapesList = etapesRepository.findAll().collectList().block();
        assertThat(etapesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
