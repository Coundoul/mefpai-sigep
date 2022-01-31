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
import sn.coundoul.gestion.patrimoine.domain.Departement;
import sn.coundoul.gestion.patrimoine.repository.DepartementRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link DepartementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class DepartementResourceIT {

    private static final String DEFAULT_NOM_DEPARTEMENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_DEPARTEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Departement departement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createEntity(EntityManager em) {
        Departement departement = new Departement().nomDepartement(DEFAULT_NOM_DEPARTEMENT);
        return departement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createUpdatedEntity(EntityManager em) {
        Departement departement = new Departement().nomDepartement(UPDATED_NOM_DEPARTEMENT);
        return departement;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Departement.class).block();
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
        departement = createEntity(em);
    }

    @Test
    void createDepartement() throws Exception {
        int databaseSizeBeforeCreate = departementRepository.findAll().collectList().block().size();
        // Create the Departement
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(departement))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate + 1);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNomDepartement()).isEqualTo(DEFAULT_NOM_DEPARTEMENT);
    }

    @Test
    void createDepartementWithExistingId() throws Exception {
        // Create the Departement with an existing ID
        departement.setId(1L);

        int databaseSizeBeforeCreate = departementRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(departement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomDepartementIsRequired() throws Exception {
        int databaseSizeBeforeTest = departementRepository.findAll().collectList().block().size();
        // set the field null
        departement.setNomDepartement(null);

        // Create the Departement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(departement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDepartements() {
        // Initialize the database
        departementRepository.save(departement).block();

        // Get all the departementList
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
            .value(hasItem(departement.getId().intValue()))
            .jsonPath("$.[*].nomDepartement")
            .value(hasItem(DEFAULT_NOM_DEPARTEMENT));
    }

    @Test
    void getDepartement() {
        // Initialize the database
        departementRepository.save(departement).block();

        // Get the departement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, departement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(departement.getId().intValue()))
            .jsonPath("$.nomDepartement")
            .value(is(DEFAULT_NOM_DEPARTEMENT));
    }

    @Test
    void getNonExistingDepartement() {
        // Get the departement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDepartement() throws Exception {
        // Initialize the database
        departementRepository.save(departement).block();

        int databaseSizeBeforeUpdate = departementRepository.findAll().collectList().block().size();

        // Update the departement
        Departement updatedDepartement = departementRepository.findById(departement.getId()).block();
        updatedDepartement.nomDepartement(UPDATED_NOM_DEPARTEMENT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDepartement.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDepartement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNomDepartement()).isEqualTo(UPDATED_NOM_DEPARTEMENT);
    }

    @Test
    void putNonExistingDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().collectList().block().size();
        departement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, departement.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(departement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().collectList().block().size();
        departement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(departement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().collectList().block().size();
        departement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(departement))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        departementRepository.save(departement).block();

        int databaseSizeBeforeUpdate = departementRepository.findAll().collectList().block().size();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement.nomDepartement(UPDATED_NOM_DEPARTEMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNomDepartement()).isEqualTo(UPDATED_NOM_DEPARTEMENT);
    }

    @Test
    void fullUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        departementRepository.save(departement).block();

        int databaseSizeBeforeUpdate = departementRepository.findAll().collectList().block().size();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement.nomDepartement(UPDATED_NOM_DEPARTEMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNomDepartement()).isEqualTo(UPDATED_NOM_DEPARTEMENT);
    }

    @Test
    void patchNonExistingDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().collectList().block().size();
        departement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, departement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(departement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().collectList().block().size();
        departement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(departement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().collectList().block().size();
        departement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(departement))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDepartement() {
        // Initialize the database
        departementRepository.save(departement).block();

        int databaseSizeBeforeDelete = departementRepository.findAll().collectList().block().size();

        // Delete the departement
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, departement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Departement> departementList = departementRepository.findAll().collectList().block();
        assertThat(departementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
