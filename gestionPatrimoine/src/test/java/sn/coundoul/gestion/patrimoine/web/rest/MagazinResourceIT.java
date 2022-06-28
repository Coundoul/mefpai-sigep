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
import sn.coundoul.gestion.patrimoine.domain.Magazin;
import sn.coundoul.gestion.patrimoine.repository.MagazinRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link MagazinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class MagazinResourceIT {

    private static final String DEFAULT_NOM_MAGAZIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_MAGAZIN = "BBBBBBBBBB";

    private static final Double DEFAULT_SURFACE_BATIE = 1D;
    private static final Double UPDATED_SURFACE_BATIE = 2D;

    private static final Double DEFAULT_SUPERFICIE = 1D;
    private static final Double UPDATED_SUPERFICIE = 2D;

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/magazins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MagazinRepository magazinRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Magazin magazin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Magazin createEntity(EntityManager em) {
        Magazin magazin = new Magazin()
            .nomMagazin(DEFAULT_NOM_MAGAZIN)
            .surfaceBatie(DEFAULT_SURFACE_BATIE)
            .superficie(DEFAULT_SUPERFICIE)
            .idPers(DEFAULT_ID_PERS);
        return magazin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Magazin createUpdatedEntity(EntityManager em) {
        Magazin magazin = new Magazin()
            .nomMagazin(UPDATED_NOM_MAGAZIN)
            .surfaceBatie(UPDATED_SURFACE_BATIE)
            .superficie(UPDATED_SUPERFICIE)
            .idPers(UPDATED_ID_PERS);
        return magazin;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Magazin.class).block();
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
        magazin = createEntity(em);
    }

    @Test
    void createMagazin() throws Exception {
        int databaseSizeBeforeCreate = magazinRepository.findAll().collectList().block().size();
        // Create the Magazin
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeCreate + 1);
        Magazin testMagazin = magazinList.get(magazinList.size() - 1);
        assertThat(testMagazin.getNomMagazin()).isEqualTo(DEFAULT_NOM_MAGAZIN);
        assertThat(testMagazin.getSurfaceBatie()).isEqualTo(DEFAULT_SURFACE_BATIE);
        assertThat(testMagazin.getSuperficie()).isEqualTo(DEFAULT_SUPERFICIE);
        assertThat(testMagazin.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    void createMagazinWithExistingId() throws Exception {
        // Create the Magazin with an existing ID
        magazin.setId(1L);

        int databaseSizeBeforeCreate = magazinRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomMagazinIsRequired() throws Exception {
        int databaseSizeBeforeTest = magazinRepository.findAll().collectList().block().size();
        // set the field null
        magazin.setNomMagazin(null);

        // Create the Magazin, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSurfaceBatieIsRequired() throws Exception {
        int databaseSizeBeforeTest = magazinRepository.findAll().collectList().block().size();
        // set the field null
        magazin.setSurfaceBatie(null);

        // Create the Magazin, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSuperficieIsRequired() throws Exception {
        int databaseSizeBeforeTest = magazinRepository.findAll().collectList().block().size();
        // set the field null
        magazin.setSuperficie(null);

        // Create the Magazin, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = magazinRepository.findAll().collectList().block().size();
        // set the field null
        magazin.setIdPers(null);

        // Create the Magazin, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMagazins() {
        // Initialize the database
        magazinRepository.save(magazin).block();

        // Get all the magazinList
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
            .value(hasItem(magazin.getId().intValue()))
            .jsonPath("$.[*].nomMagazin")
            .value(hasItem(DEFAULT_NOM_MAGAZIN))
            .jsonPath("$.[*].surfaceBatie")
            .value(hasItem(DEFAULT_SURFACE_BATIE.doubleValue()))
            .jsonPath("$.[*].superficie")
            .value(hasItem(DEFAULT_SUPERFICIE.doubleValue()))
            .jsonPath("$.[*].idPers")
            .value(hasItem(DEFAULT_ID_PERS));
    }

    @Test
    void getMagazin() {
        // Initialize the database
        magazinRepository.save(magazin).block();

        // Get the magazin
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, magazin.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(magazin.getId().intValue()))
            .jsonPath("$.nomMagazin")
            .value(is(DEFAULT_NOM_MAGAZIN))
            .jsonPath("$.surfaceBatie")
            .value(is(DEFAULT_SURFACE_BATIE.doubleValue()))
            .jsonPath("$.superficie")
            .value(is(DEFAULT_SUPERFICIE.doubleValue()))
            .jsonPath("$.idPers")
            .value(is(DEFAULT_ID_PERS));
    }

    @Test
    void getNonExistingMagazin() {
        // Get the magazin
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewMagazin() throws Exception {
        // Initialize the database
        magazinRepository.save(magazin).block();

        int databaseSizeBeforeUpdate = magazinRepository.findAll().collectList().block().size();

        // Update the magazin
        Magazin updatedMagazin = magazinRepository.findById(magazin.getId()).block();
        updatedMagazin
            .nomMagazin(UPDATED_NOM_MAGAZIN)
            .surfaceBatie(UPDATED_SURFACE_BATIE)
            .superficie(UPDATED_SUPERFICIE)
            .idPers(UPDATED_ID_PERS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMagazin.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedMagazin))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
        Magazin testMagazin = magazinList.get(magazinList.size() - 1);
        assertThat(testMagazin.getNomMagazin()).isEqualTo(UPDATED_NOM_MAGAZIN);
        assertThat(testMagazin.getSurfaceBatie()).isEqualTo(UPDATED_SURFACE_BATIE);
        assertThat(testMagazin.getSuperficie()).isEqualTo(UPDATED_SUPERFICIE);
        assertThat(testMagazin.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void putNonExistingMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().collectList().block().size();
        magazin.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, magazin.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().collectList().block().size();
        magazin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().collectList().block().size();
        magazin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMagazinWithPatch() throws Exception {
        // Initialize the database
        magazinRepository.save(magazin).block();

        int databaseSizeBeforeUpdate = magazinRepository.findAll().collectList().block().size();

        // Update the magazin using partial update
        Magazin partialUpdatedMagazin = new Magazin();
        partialUpdatedMagazin.setId(magazin.getId());

        partialUpdatedMagazin.surfaceBatie(UPDATED_SURFACE_BATIE).superficie(UPDATED_SUPERFICIE).idPers(UPDATED_ID_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMagazin.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMagazin))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
        Magazin testMagazin = magazinList.get(magazinList.size() - 1);
        assertThat(testMagazin.getNomMagazin()).isEqualTo(DEFAULT_NOM_MAGAZIN);
        assertThat(testMagazin.getSurfaceBatie()).isEqualTo(UPDATED_SURFACE_BATIE);
        assertThat(testMagazin.getSuperficie()).isEqualTo(UPDATED_SUPERFICIE);
        assertThat(testMagazin.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void fullUpdateMagazinWithPatch() throws Exception {
        // Initialize the database
        magazinRepository.save(magazin).block();

        int databaseSizeBeforeUpdate = magazinRepository.findAll().collectList().block().size();

        // Update the magazin using partial update
        Magazin partialUpdatedMagazin = new Magazin();
        partialUpdatedMagazin.setId(magazin.getId());

        partialUpdatedMagazin
            .nomMagazin(UPDATED_NOM_MAGAZIN)
            .surfaceBatie(UPDATED_SURFACE_BATIE)
            .superficie(UPDATED_SUPERFICIE)
            .idPers(UPDATED_ID_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMagazin.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMagazin))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
        Magazin testMagazin = magazinList.get(magazinList.size() - 1);
        assertThat(testMagazin.getNomMagazin()).isEqualTo(UPDATED_NOM_MAGAZIN);
        assertThat(testMagazin.getSurfaceBatie()).isEqualTo(UPDATED_SURFACE_BATIE);
        assertThat(testMagazin.getSuperficie()).isEqualTo(UPDATED_SUPERFICIE);
        assertThat(testMagazin.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void patchNonExistingMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().collectList().block().size();
        magazin.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, magazin.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().collectList().block().size();
        magazin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().collectList().block().size();
        magazin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(magazin))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMagazin() {
        // Initialize the database
        magazinRepository.save(magazin).block();

        int databaseSizeBeforeDelete = magazinRepository.findAll().collectList().block().size();

        // Delete the magazin
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, magazin.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Magazin> magazinList = magazinRepository.findAll().collectList().block();
        assertThat(magazinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
