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
import sn.coundoul.gestion.patrimoine.domain.ProjetAttribution;
import sn.coundoul.gestion.patrimoine.repository.ProjetAttributionRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link ProjetAttributionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ProjetAttributionResourceIT {

    private static final Instant DEFAULT_DATE_ATTRIBUTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ATTRIBUTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Integer DEFAULT_ID_EQUIPEMENT = 1;
    private static final Integer UPDATED_ID_EQUIPEMENT = 2;

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/projet-attributions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjetAttributionRepository projetAttributionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ProjetAttribution projetAttribution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjetAttribution createEntity(EntityManager em) {
        ProjetAttribution projetAttribution = new ProjetAttribution()
            .dateAttribution(DEFAULT_DATE_ATTRIBUTION)
            .quantite(DEFAULT_QUANTITE)
            .idEquipement(DEFAULT_ID_EQUIPEMENT)
            .idPers(DEFAULT_ID_PERS);
        return projetAttribution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjetAttribution createUpdatedEntity(EntityManager em) {
        ProjetAttribution projetAttribution = new ProjetAttribution()
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);
        return projetAttribution;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ProjetAttribution.class).block();
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
        projetAttribution = createEntity(em);
    }

    @Test
    void createProjetAttribution() throws Exception {
        int databaseSizeBeforeCreate = projetAttributionRepository.findAll().collectList().block().size();
        // Create the ProjetAttribution
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeCreate + 1);
        ProjetAttribution testProjetAttribution = projetAttributionList.get(projetAttributionList.size() - 1);
        assertThat(testProjetAttribution.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
        assertThat(testProjetAttribution.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testProjetAttribution.getIdEquipement()).isEqualTo(DEFAULT_ID_EQUIPEMENT);
        assertThat(testProjetAttribution.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    void createProjetAttributionWithExistingId() throws Exception {
        // Create the ProjetAttribution with an existing ID
        projetAttribution.setId(1L);

        int databaseSizeBeforeCreate = projetAttributionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetAttributionRepository.findAll().collectList().block().size();
        // set the field null
        projetAttribution.setQuantite(null);

        // Create the ProjetAttribution, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdEquipementIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetAttributionRepository.findAll().collectList().block().size();
        // set the field null
        projetAttribution.setIdEquipement(null);

        // Create the ProjetAttribution, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetAttributionRepository.findAll().collectList().block().size();
        // set the field null
        projetAttribution.setIdPers(null);

        // Create the ProjetAttribution, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProjetAttributions() {
        // Initialize the database
        projetAttributionRepository.save(projetAttribution).block();

        // Get all the projetAttributionList
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
            .value(hasItem(projetAttribution.getId().intValue()))
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
    void getProjetAttribution() {
        // Initialize the database
        projetAttributionRepository.save(projetAttribution).block();

        // Get the projetAttribution
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, projetAttribution.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(projetAttribution.getId().intValue()))
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
    void getNonExistingProjetAttribution() {
        // Get the projetAttribution
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProjetAttribution() throws Exception {
        // Initialize the database
        projetAttributionRepository.save(projetAttribution).block();

        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().collectList().block().size();

        // Update the projetAttribution
        ProjetAttribution updatedProjetAttribution = projetAttributionRepository.findById(projetAttribution.getId()).block();
        updatedProjetAttribution
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedProjetAttribution.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedProjetAttribution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
        ProjetAttribution testProjetAttribution = projetAttributionList.get(projetAttributionList.size() - 1);
        assertThat(testProjetAttribution.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
        assertThat(testProjetAttribution.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testProjetAttribution.getIdEquipement()).isEqualTo(UPDATED_ID_EQUIPEMENT);
        assertThat(testProjetAttribution.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void putNonExistingProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().collectList().block().size();
        projetAttribution.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, projetAttribution.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().collectList().block().size();
        projetAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().collectList().block().size();
        projetAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProjetAttributionWithPatch() throws Exception {
        // Initialize the database
        projetAttributionRepository.save(projetAttribution).block();

        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().collectList().block().size();

        // Update the projetAttribution using partial update
        ProjetAttribution partialUpdatedProjetAttribution = new ProjetAttribution();
        partialUpdatedProjetAttribution.setId(projetAttribution.getId());

        partialUpdatedProjetAttribution.quantite(UPDATED_QUANTITE).idEquipement(UPDATED_ID_EQUIPEMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProjetAttribution.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProjetAttribution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
        ProjetAttribution testProjetAttribution = projetAttributionList.get(projetAttributionList.size() - 1);
        assertThat(testProjetAttribution.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
        assertThat(testProjetAttribution.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testProjetAttribution.getIdEquipement()).isEqualTo(UPDATED_ID_EQUIPEMENT);
        assertThat(testProjetAttribution.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    void fullUpdateProjetAttributionWithPatch() throws Exception {
        // Initialize the database
        projetAttributionRepository.save(projetAttribution).block();

        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().collectList().block().size();

        // Update the projetAttribution using partial update
        ProjetAttribution partialUpdatedProjetAttribution = new ProjetAttribution();
        partialUpdatedProjetAttribution.setId(projetAttribution.getId());

        partialUpdatedProjetAttribution
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProjetAttribution.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProjetAttribution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
        ProjetAttribution testProjetAttribution = projetAttributionList.get(projetAttributionList.size() - 1);
        assertThat(testProjetAttribution.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
        assertThat(testProjetAttribution.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testProjetAttribution.getIdEquipement()).isEqualTo(UPDATED_ID_EQUIPEMENT);
        assertThat(testProjetAttribution.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void patchNonExistingProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().collectList().block().size();
        projetAttribution.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, projetAttribution.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().collectList().block().size();
        projetAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().collectList().block().size();
        projetAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(projetAttribution))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProjetAttribution() {
        // Initialize the database
        projetAttributionRepository.save(projetAttribution).block();

        int databaseSizeBeforeDelete = projetAttributionRepository.findAll().collectList().block().size();

        // Delete the projetAttribution
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, projetAttribution.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll().collectList().block();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
