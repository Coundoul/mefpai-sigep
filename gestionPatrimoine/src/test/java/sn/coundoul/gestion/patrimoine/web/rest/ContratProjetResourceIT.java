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
import sn.coundoul.gestion.patrimoine.domain.ContratProjet;
import sn.coundoul.gestion.patrimoine.repository.ContratProjetRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link ContratProjetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ContratProjetResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contrat-projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContratProjetRepository contratProjetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ContratProjet contratProjet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContratProjet createEntity(EntityManager em) {
        ContratProjet contratProjet = new ContratProjet().nom(DEFAULT_NOM);
        return contratProjet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContratProjet createUpdatedEntity(EntityManager em) {
        ContratProjet contratProjet = new ContratProjet().nom(UPDATED_NOM);
        return contratProjet;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ContratProjet.class).block();
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
        contratProjet = createEntity(em);
    }

    @Test
    void createContratProjet() throws Exception {
        int databaseSizeBeforeCreate = contratProjetRepository.findAll().collectList().block().size();
        // Create the ContratProjet
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contratProjet))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeCreate + 1);
        ContratProjet testContratProjet = contratProjetList.get(contratProjetList.size() - 1);
        assertThat(testContratProjet.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    void createContratProjetWithExistingId() throws Exception {
        // Create the ContratProjet with an existing ID
        contratProjet.setId(1L);

        int databaseSizeBeforeCreate = contratProjetRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contratProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratProjetRepository.findAll().collectList().block().size();
        // set the field null
        contratProjet.setNom(null);

        // Create the ContratProjet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contratProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllContratProjets() {
        // Initialize the database
        contratProjetRepository.save(contratProjet).block();

        // Get all the contratProjetList
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
            .value(hasItem(contratProjet.getId().intValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM));
    }

    @Test
    void getContratProjet() {
        // Initialize the database
        contratProjetRepository.save(contratProjet).block();

        // Get the contratProjet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, contratProjet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(contratProjet.getId().intValue()))
            .jsonPath("$.nom")
            .value(is(DEFAULT_NOM));
    }

    @Test
    void getNonExistingContratProjet() {
        // Get the contratProjet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewContratProjet() throws Exception {
        // Initialize the database
        contratProjetRepository.save(contratProjet).block();

        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().collectList().block().size();

        // Update the contratProjet
        ContratProjet updatedContratProjet = contratProjetRepository.findById(contratProjet.getId()).block();
        updatedContratProjet.nom(UPDATED_NOM);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedContratProjet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedContratProjet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
        ContratProjet testContratProjet = contratProjetList.get(contratProjetList.size() - 1);
        assertThat(testContratProjet.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    void putNonExistingContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().collectList().block().size();
        contratProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, contratProjet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contratProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().collectList().block().size();
        contratProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contratProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().collectList().block().size();
        contratProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contratProjet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateContratProjetWithPatch() throws Exception {
        // Initialize the database
        contratProjetRepository.save(contratProjet).block();

        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().collectList().block().size();

        // Update the contratProjet using partial update
        ContratProjet partialUpdatedContratProjet = new ContratProjet();
        partialUpdatedContratProjet.setId(contratProjet.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedContratProjet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedContratProjet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
        ContratProjet testContratProjet = contratProjetList.get(contratProjetList.size() - 1);
        assertThat(testContratProjet.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    void fullUpdateContratProjetWithPatch() throws Exception {
        // Initialize the database
        contratProjetRepository.save(contratProjet).block();

        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().collectList().block().size();

        // Update the contratProjet using partial update
        ContratProjet partialUpdatedContratProjet = new ContratProjet();
        partialUpdatedContratProjet.setId(contratProjet.getId());

        partialUpdatedContratProjet.nom(UPDATED_NOM);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedContratProjet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedContratProjet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
        ContratProjet testContratProjet = contratProjetList.get(contratProjetList.size() - 1);
        assertThat(testContratProjet.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    void patchNonExistingContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().collectList().block().size();
        contratProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, contratProjet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contratProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().collectList().block().size();
        contratProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contratProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().collectList().block().size();
        contratProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contratProjet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteContratProjet() {
        // Initialize the database
        contratProjetRepository.save(contratProjet).block();

        int databaseSizeBeforeDelete = contratProjetRepository.findAll().collectList().block().size();

        // Delete the contratProjet
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, contratProjet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll().collectList().block();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
