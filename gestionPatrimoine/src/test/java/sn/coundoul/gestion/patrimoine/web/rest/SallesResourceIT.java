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
import sn.coundoul.gestion.patrimoine.domain.Salles;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Classe;
import sn.coundoul.gestion.patrimoine.repository.SallesRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link SallesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class SallesResourceIT {

    private static final String DEFAULT_NOM_SALLE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SALLE = "BBBBBBBBBB";

    private static final Classe DEFAULT_CLASSE = Classe.ClassePhysique;
    private static final Classe UPDATED_CLASSE = Classe.ClassePedagogique;

    private static final String ENTITY_API_URL = "/api/salles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SallesRepository sallesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Salles salles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salles createEntity(EntityManager em) {
        Salles salles = new Salles().nomSalle(DEFAULT_NOM_SALLE).classe(DEFAULT_CLASSE);
        return salles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salles createUpdatedEntity(EntityManager em) {
        Salles salles = new Salles().nomSalle(UPDATED_NOM_SALLE).classe(UPDATED_CLASSE);
        return salles;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Salles.class).block();
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
        salles = createEntity(em);
    }

    @Test
    void createSalles() throws Exception {
        int databaseSizeBeforeCreate = sallesRepository.findAll().collectList().block().size();
        // Create the Salles
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salles))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeCreate + 1);
        Salles testSalles = sallesList.get(sallesList.size() - 1);
        assertThat(testSalles.getNomSalle()).isEqualTo(DEFAULT_NOM_SALLE);
        assertThat(testSalles.getClasse()).isEqualTo(DEFAULT_CLASSE);
    }

    @Test
    void createSallesWithExistingId() throws Exception {
        // Create the Salles with an existing ID
        salles.setId(1L);

        int databaseSizeBeforeCreate = sallesRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salles))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomSalleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sallesRepository.findAll().collectList().block().size();
        // set the field null
        salles.setNomSalle(null);

        // Create the Salles, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salles))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkClasseIsRequired() throws Exception {
        int databaseSizeBeforeTest = sallesRepository.findAll().collectList().block().size();
        // set the field null
        salles.setClasse(null);

        // Create the Salles, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salles))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSalles() {
        // Initialize the database
        sallesRepository.save(salles).block();

        // Get all the sallesList
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
            .value(hasItem(salles.getId().intValue()))
            .jsonPath("$.[*].nomSalle")
            .value(hasItem(DEFAULT_NOM_SALLE))
            .jsonPath("$.[*].classe")
            .value(hasItem(DEFAULT_CLASSE.toString()));
    }

    @Test
    void getSalles() {
        // Initialize the database
        sallesRepository.save(salles).block();

        // Get the salles
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, salles.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(salles.getId().intValue()))
            .jsonPath("$.nomSalle")
            .value(is(DEFAULT_NOM_SALLE))
            .jsonPath("$.classe")
            .value(is(DEFAULT_CLASSE.toString()));
    }

    @Test
    void getNonExistingSalles() {
        // Get the salles
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSalles() throws Exception {
        // Initialize the database
        sallesRepository.save(salles).block();

        int databaseSizeBeforeUpdate = sallesRepository.findAll().collectList().block().size();

        // Update the salles
        Salles updatedSalles = sallesRepository.findById(salles.getId()).block();
        updatedSalles.nomSalle(UPDATED_NOM_SALLE).classe(UPDATED_CLASSE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSalles.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSalles))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
        Salles testSalles = sallesList.get(sallesList.size() - 1);
        assertThat(testSalles.getNomSalle()).isEqualTo(UPDATED_NOM_SALLE);
        assertThat(testSalles.getClasse()).isEqualTo(UPDATED_CLASSE);
    }

    @Test
    void putNonExistingSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().collectList().block().size();
        salles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, salles.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salles))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().collectList().block().size();
        salles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salles))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().collectList().block().size();
        salles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salles))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSallesWithPatch() throws Exception {
        // Initialize the database
        sallesRepository.save(salles).block();

        int databaseSizeBeforeUpdate = sallesRepository.findAll().collectList().block().size();

        // Update the salles using partial update
        Salles partialUpdatedSalles = new Salles();
        partialUpdatedSalles.setId(salles.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSalles.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSalles))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
        Salles testSalles = sallesList.get(sallesList.size() - 1);
        assertThat(testSalles.getNomSalle()).isEqualTo(DEFAULT_NOM_SALLE);
        assertThat(testSalles.getClasse()).isEqualTo(DEFAULT_CLASSE);
    }

    @Test
    void fullUpdateSallesWithPatch() throws Exception {
        // Initialize the database
        sallesRepository.save(salles).block();

        int databaseSizeBeforeUpdate = sallesRepository.findAll().collectList().block().size();

        // Update the salles using partial update
        Salles partialUpdatedSalles = new Salles();
        partialUpdatedSalles.setId(salles.getId());

        partialUpdatedSalles.nomSalle(UPDATED_NOM_SALLE).classe(UPDATED_CLASSE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSalles.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSalles))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
        Salles testSalles = sallesList.get(sallesList.size() - 1);
        assertThat(testSalles.getNomSalle()).isEqualTo(UPDATED_NOM_SALLE);
        assertThat(testSalles.getClasse()).isEqualTo(UPDATED_CLASSE);
    }

    @Test
    void patchNonExistingSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().collectList().block().size();
        salles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, salles.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salles))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().collectList().block().size();
        salles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salles))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().collectList().block().size();
        salles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salles))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSalles() {
        // Initialize the database
        sallesRepository.save(salles).block();

        int databaseSizeBeforeDelete = sallesRepository.findAll().collectList().block().size();

        // Delete the salles
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, salles.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Salles> sallesList = sallesRepository.findAll().collectList().block();
        assertThat(sallesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
