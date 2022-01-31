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
import sn.coundoul.gestion.patrimoine.domain.Etablissement;
import sn.coundoul.gestion.patrimoine.repository.EtablissementRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link EtablissementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class EtablissementResourceIT {

    private static final String DEFAULT_NOM_ETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ETABLISSEMENT = "BBBBBBBBBB";

    private static final Double DEFAULT_SURFACE_BATIE = 1D;
    private static final Double UPDATED_SURFACE_BATIE = 2D;

    private static final Double DEFAULT_SUPERFICIE = 1D;
    private static final Double UPDATED_SUPERFICIE = 2D;

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/etablissements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtablissementRepository etablissementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Etablissement etablissement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etablissement createEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement()
            .nomEtablissement(DEFAULT_NOM_ETABLISSEMENT)
            .surfaceBatie(DEFAULT_SURFACE_BATIE)
            .superficie(DEFAULT_SUPERFICIE)
            .idPers(DEFAULT_ID_PERS);
        return etablissement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etablissement createUpdatedEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement()
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .surfaceBatie(UPDATED_SURFACE_BATIE)
            .superficie(UPDATED_SUPERFICIE)
            .idPers(UPDATED_ID_PERS);
        return etablissement;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Etablissement.class).block();
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
        etablissement = createEntity(em);
    }

    @Test
    void createEtablissement() throws Exception {
        int databaseSizeBeforeCreate = etablissementRepository.findAll().collectList().block().size();
        // Create the Etablissement
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate + 1);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNomEtablissement()).isEqualTo(DEFAULT_NOM_ETABLISSEMENT);
        assertThat(testEtablissement.getSurfaceBatie()).isEqualTo(DEFAULT_SURFACE_BATIE);
        assertThat(testEtablissement.getSuperficie()).isEqualTo(DEFAULT_SUPERFICIE);
        assertThat(testEtablissement.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    void createEtablissementWithExistingId() throws Exception {
        // Create the Etablissement with an existing ID
        etablissement.setId(1L);

        int databaseSizeBeforeCreate = etablissementRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomEtablissementIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().collectList().block().size();
        // set the field null
        etablissement.setNomEtablissement(null);

        // Create the Etablissement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSurfaceBatieIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().collectList().block().size();
        // set the field null
        etablissement.setSurfaceBatie(null);

        // Create the Etablissement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSuperficieIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().collectList().block().size();
        // set the field null
        etablissement.setSuperficie(null);

        // Create the Etablissement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().collectList().block().size();
        // set the field null
        etablissement.setIdPers(null);

        // Create the Etablissement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEtablissements() {
        // Initialize the database
        etablissementRepository.save(etablissement).block();

        // Get all the etablissementList
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
            .value(hasItem(etablissement.getId().intValue()))
            .jsonPath("$.[*].nomEtablissement")
            .value(hasItem(DEFAULT_NOM_ETABLISSEMENT))
            .jsonPath("$.[*].surfaceBatie")
            .value(hasItem(DEFAULT_SURFACE_BATIE.doubleValue()))
            .jsonPath("$.[*].superficie")
            .value(hasItem(DEFAULT_SUPERFICIE.doubleValue()))
            .jsonPath("$.[*].idPers")
            .value(hasItem(DEFAULT_ID_PERS));
    }

    @Test
    void getEtablissement() {
        // Initialize the database
        etablissementRepository.save(etablissement).block();

        // Get the etablissement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, etablissement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(etablissement.getId().intValue()))
            .jsonPath("$.nomEtablissement")
            .value(is(DEFAULT_NOM_ETABLISSEMENT))
            .jsonPath("$.surfaceBatie")
            .value(is(DEFAULT_SURFACE_BATIE.doubleValue()))
            .jsonPath("$.superficie")
            .value(is(DEFAULT_SUPERFICIE.doubleValue()))
            .jsonPath("$.idPers")
            .value(is(DEFAULT_ID_PERS));
    }

    @Test
    void getNonExistingEtablissement() {
        // Get the etablissement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.save(etablissement).block();

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().collectList().block().size();

        // Update the etablissement
        Etablissement updatedEtablissement = etablissementRepository.findById(etablissement.getId()).block();
        updatedEtablissement
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .surfaceBatie(UPDATED_SURFACE_BATIE)
            .superficie(UPDATED_SUPERFICIE)
            .idPers(UPDATED_ID_PERS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEtablissement.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedEtablissement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testEtablissement.getSurfaceBatie()).isEqualTo(UPDATED_SURFACE_BATIE);
        assertThat(testEtablissement.getSuperficie()).isEqualTo(UPDATED_SUPERFICIE);
        assertThat(testEtablissement.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void putNonExistingEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().collectList().block().size();
        etablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, etablissement.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().collectList().block().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().collectList().block().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEtablissementWithPatch() throws Exception {
        // Initialize the database
        etablissementRepository.save(etablissement).block();

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().collectList().block().size();

        // Update the etablissement using partial update
        Etablissement partialUpdatedEtablissement = new Etablissement();
        partialUpdatedEtablissement.setId(etablissement.getId());

        partialUpdatedEtablissement.nomEtablissement(UPDATED_NOM_ETABLISSEMENT).superficie(UPDATED_SUPERFICIE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEtablissement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEtablissement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testEtablissement.getSurfaceBatie()).isEqualTo(DEFAULT_SURFACE_BATIE);
        assertThat(testEtablissement.getSuperficie()).isEqualTo(UPDATED_SUPERFICIE);
        assertThat(testEtablissement.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    void fullUpdateEtablissementWithPatch() throws Exception {
        // Initialize the database
        etablissementRepository.save(etablissement).block();

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().collectList().block().size();

        // Update the etablissement using partial update
        Etablissement partialUpdatedEtablissement = new Etablissement();
        partialUpdatedEtablissement.setId(etablissement.getId());

        partialUpdatedEtablissement
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .surfaceBatie(UPDATED_SURFACE_BATIE)
            .superficie(UPDATED_SUPERFICIE)
            .idPers(UPDATED_ID_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEtablissement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEtablissement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testEtablissement.getSurfaceBatie()).isEqualTo(UPDATED_SURFACE_BATIE);
        assertThat(testEtablissement.getSuperficie()).isEqualTo(UPDATED_SUPERFICIE);
        assertThat(testEtablissement.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void patchNonExistingEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().collectList().block().size();
        etablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, etablissement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().collectList().block().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().collectList().block().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(etablissement))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEtablissement() {
        // Initialize the database
        etablissementRepository.save(etablissement).block();

        int databaseSizeBeforeDelete = etablissementRepository.findAll().collectList().block().size();

        // Delete the etablissement
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, etablissement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Etablissement> etablissementList = etablissementRepository.findAll().collectList().block();
        assertThat(etablissementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
