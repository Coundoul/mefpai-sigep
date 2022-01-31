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
import sn.coundoul.gestion.patrimoine.domain.FiliereStabilise;
import sn.coundoul.gestion.patrimoine.repository.FiliereStabiliseRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link FiliereStabiliseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class FiliereStabiliseResourceIT {

    private static final String DEFAULT_NOM_FILIERE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FILIERE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/filiere-stabilises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiliereStabiliseRepository filiereStabiliseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FiliereStabilise filiereStabilise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiliereStabilise createEntity(EntityManager em) {
        FiliereStabilise filiereStabilise = new FiliereStabilise().nomFiliere(DEFAULT_NOM_FILIERE);
        return filiereStabilise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiliereStabilise createUpdatedEntity(EntityManager em) {
        FiliereStabilise filiereStabilise = new FiliereStabilise().nomFiliere(UPDATED_NOM_FILIERE);
        return filiereStabilise;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FiliereStabilise.class).block();
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
        filiereStabilise = createEntity(em);
    }

    @Test
    void createFiliereStabilise() throws Exception {
        int databaseSizeBeforeCreate = filiereStabiliseRepository.findAll().collectList().block().size();
        // Create the FiliereStabilise
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeCreate + 1);
        FiliereStabilise testFiliereStabilise = filiereStabiliseList.get(filiereStabiliseList.size() - 1);
        assertThat(testFiliereStabilise.getNomFiliere()).isEqualTo(DEFAULT_NOM_FILIERE);
    }

    @Test
    void createFiliereStabiliseWithExistingId() throws Exception {
        // Create the FiliereStabilise with an existing ID
        filiereStabilise.setId(1L);

        int databaseSizeBeforeCreate = filiereStabiliseRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomFiliereIsRequired() throws Exception {
        int databaseSizeBeforeTest = filiereStabiliseRepository.findAll().collectList().block().size();
        // set the field null
        filiereStabilise.setNomFiliere(null);

        // Create the FiliereStabilise, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFiliereStabilises() {
        // Initialize the database
        filiereStabiliseRepository.save(filiereStabilise).block();

        // Get all the filiereStabiliseList
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
            .value(hasItem(filiereStabilise.getId().intValue()))
            .jsonPath("$.[*].nomFiliere")
            .value(hasItem(DEFAULT_NOM_FILIERE));
    }

    @Test
    void getFiliereStabilise() {
        // Initialize the database
        filiereStabiliseRepository.save(filiereStabilise).block();

        // Get the filiereStabilise
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, filiereStabilise.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(filiereStabilise.getId().intValue()))
            .jsonPath("$.nomFiliere")
            .value(is(DEFAULT_NOM_FILIERE));
    }

    @Test
    void getNonExistingFiliereStabilise() {
        // Get the filiereStabilise
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFiliereStabilise() throws Exception {
        // Initialize the database
        filiereStabiliseRepository.save(filiereStabilise).block();

        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().collectList().block().size();

        // Update the filiereStabilise
        FiliereStabilise updatedFiliereStabilise = filiereStabiliseRepository.findById(filiereStabilise.getId()).block();
        updatedFiliereStabilise.nomFiliere(UPDATED_NOM_FILIERE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFiliereStabilise.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFiliereStabilise))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
        FiliereStabilise testFiliereStabilise = filiereStabiliseList.get(filiereStabiliseList.size() - 1);
        assertThat(testFiliereStabilise.getNomFiliere()).isEqualTo(UPDATED_NOM_FILIERE);
    }

    @Test
    void putNonExistingFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().collectList().block().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, filiereStabilise.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().collectList().block().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().collectList().block().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFiliereStabiliseWithPatch() throws Exception {
        // Initialize the database
        filiereStabiliseRepository.save(filiereStabilise).block();

        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().collectList().block().size();

        // Update the filiereStabilise using partial update
        FiliereStabilise partialUpdatedFiliereStabilise = new FiliereStabilise();
        partialUpdatedFiliereStabilise.setId(filiereStabilise.getId());

        partialUpdatedFiliereStabilise.nomFiliere(UPDATED_NOM_FILIERE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFiliereStabilise.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFiliereStabilise))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
        FiliereStabilise testFiliereStabilise = filiereStabiliseList.get(filiereStabiliseList.size() - 1);
        assertThat(testFiliereStabilise.getNomFiliere()).isEqualTo(UPDATED_NOM_FILIERE);
    }

    @Test
    void fullUpdateFiliereStabiliseWithPatch() throws Exception {
        // Initialize the database
        filiereStabiliseRepository.save(filiereStabilise).block();

        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().collectList().block().size();

        // Update the filiereStabilise using partial update
        FiliereStabilise partialUpdatedFiliereStabilise = new FiliereStabilise();
        partialUpdatedFiliereStabilise.setId(filiereStabilise.getId());

        partialUpdatedFiliereStabilise.nomFiliere(UPDATED_NOM_FILIERE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFiliereStabilise.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFiliereStabilise))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
        FiliereStabilise testFiliereStabilise = filiereStabiliseList.get(filiereStabiliseList.size() - 1);
        assertThat(testFiliereStabilise.getNomFiliere()).isEqualTo(UPDATED_NOM_FILIERE);
    }

    @Test
    void patchNonExistingFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().collectList().block().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, filiereStabilise.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().collectList().block().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().collectList().block().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFiliereStabilise() {
        // Initialize the database
        filiereStabiliseRepository.save(filiereStabilise).block();

        int databaseSizeBeforeDelete = filiereStabiliseRepository.findAll().collectList().block().size();

        // Delete the filiereStabilise
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, filiereStabilise.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll().collectList().block();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
