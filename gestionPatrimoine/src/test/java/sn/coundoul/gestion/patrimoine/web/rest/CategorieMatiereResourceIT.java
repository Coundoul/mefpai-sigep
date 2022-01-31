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
import sn.coundoul.gestion.patrimoine.domain.CategorieMatiere;
import sn.coundoul.gestion.patrimoine.repository.CategorieMatiereRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link CategorieMatiereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class CategorieMatiereResourceIT {

    private static final String DEFAULT_CATEGORIE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categorie-matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategorieMatiereRepository categorieMatiereRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CategorieMatiere categorieMatiere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieMatiere createEntity(EntityManager em) {
        CategorieMatiere categorieMatiere = new CategorieMatiere().categorie(DEFAULT_CATEGORIE);
        return categorieMatiere;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieMatiere createUpdatedEntity(EntityManager em) {
        CategorieMatiere categorieMatiere = new CategorieMatiere().categorie(UPDATED_CATEGORIE);
        return categorieMatiere;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CategorieMatiere.class).block();
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
        categorieMatiere = createEntity(em);
    }

    @Test
    void createCategorieMatiere() throws Exception {
        int databaseSizeBeforeCreate = categorieMatiereRepository.findAll().collectList().block().size();
        // Create the CategorieMatiere
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeCreate + 1);
        CategorieMatiere testCategorieMatiere = categorieMatiereList.get(categorieMatiereList.size() - 1);
        assertThat(testCategorieMatiere.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
    }

    @Test
    void createCategorieMatiereWithExistingId() throws Exception {
        // Create the CategorieMatiere with an existing ID
        categorieMatiere.setId(1L);

        int databaseSizeBeforeCreate = categorieMatiereRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCategorieIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieMatiereRepository.findAll().collectList().block().size();
        // set the field null
        categorieMatiere.setCategorie(null);

        // Create the CategorieMatiere, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCategorieMatieres() {
        // Initialize the database
        categorieMatiereRepository.save(categorieMatiere).block();

        // Get all the categorieMatiereList
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
            .value(hasItem(categorieMatiere.getId().intValue()))
            .jsonPath("$.[*].categorie")
            .value(hasItem(DEFAULT_CATEGORIE));
    }

    @Test
    void getCategorieMatiere() {
        // Initialize the database
        categorieMatiereRepository.save(categorieMatiere).block();

        // Get the categorieMatiere
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, categorieMatiere.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(categorieMatiere.getId().intValue()))
            .jsonPath("$.categorie")
            .value(is(DEFAULT_CATEGORIE));
    }

    @Test
    void getNonExistingCategorieMatiere() {
        // Get the categorieMatiere
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCategorieMatiere() throws Exception {
        // Initialize the database
        categorieMatiereRepository.save(categorieMatiere).block();

        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().collectList().block().size();

        // Update the categorieMatiere
        CategorieMatiere updatedCategorieMatiere = categorieMatiereRepository.findById(categorieMatiere.getId()).block();
        updatedCategorieMatiere.categorie(UPDATED_CATEGORIE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCategorieMatiere.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCategorieMatiere))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
        CategorieMatiere testCategorieMatiere = categorieMatiereList.get(categorieMatiereList.size() - 1);
        assertThat(testCategorieMatiere.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
    }

    @Test
    void putNonExistingCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().collectList().block().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, categorieMatiere.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().collectList().block().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().collectList().block().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCategorieMatiereWithPatch() throws Exception {
        // Initialize the database
        categorieMatiereRepository.save(categorieMatiere).block();

        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().collectList().block().size();

        // Update the categorieMatiere using partial update
        CategorieMatiere partialUpdatedCategorieMatiere = new CategorieMatiere();
        partialUpdatedCategorieMatiere.setId(categorieMatiere.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategorieMatiere.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorieMatiere))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
        CategorieMatiere testCategorieMatiere = categorieMatiereList.get(categorieMatiereList.size() - 1);
        assertThat(testCategorieMatiere.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
    }

    @Test
    void fullUpdateCategorieMatiereWithPatch() throws Exception {
        // Initialize the database
        categorieMatiereRepository.save(categorieMatiere).block();

        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().collectList().block().size();

        // Update the categorieMatiere using partial update
        CategorieMatiere partialUpdatedCategorieMatiere = new CategorieMatiere();
        partialUpdatedCategorieMatiere.setId(categorieMatiere.getId());

        partialUpdatedCategorieMatiere.categorie(UPDATED_CATEGORIE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategorieMatiere.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorieMatiere))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
        CategorieMatiere testCategorieMatiere = categorieMatiereList.get(categorieMatiereList.size() - 1);
        assertThat(testCategorieMatiere.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
    }

    @Test
    void patchNonExistingCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().collectList().block().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, categorieMatiere.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().collectList().block().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().collectList().block().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCategorieMatiere() {
        // Initialize the database
        categorieMatiereRepository.save(categorieMatiere).block();

        int databaseSizeBeforeDelete = categorieMatiereRepository.findAll().collectList().block().size();

        // Delete the categorieMatiere
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, categorieMatiere.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll().collectList().block();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
