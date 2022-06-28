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
import sn.coundoul.gestion.patrimoine.domain.Projets;
import sn.coundoul.gestion.patrimoine.domain.enumeration.TypeProjet;
import sn.coundoul.gestion.patrimoine.repository.ProjetsRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link ProjetsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ProjetsResourceIT {

    private static final TypeProjet DEFAULT_TYPE_PROJET = TypeProjet.Construction;
    private static final TypeProjet UPDATED_TYPE_PROJET = TypeProjet.Rehabilitation;

    private static final String DEFAULT_NOM_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PROJET = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EXTENSION = false;
    private static final Boolean UPDATED_EXTENSION = true;

    private static final String ENTITY_API_URL = "/api/projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjetsRepository projetsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Projets projets;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projets createEntity(EntityManager em) {
        Projets projets = new Projets()
            .typeProjet(DEFAULT_TYPE_PROJET)
            .nomProjet(DEFAULT_NOM_PROJET)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .description(DEFAULT_DESCRIPTION)
            .extension(DEFAULT_EXTENSION);
        return projets;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projets createUpdatedEntity(EntityManager em) {
        Projets projets = new Projets()
            .typeProjet(UPDATED_TYPE_PROJET)
            .nomProjet(UPDATED_NOM_PROJET)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION)
            .extension(UPDATED_EXTENSION);
        return projets;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Projets.class).block();
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
        projets = createEntity(em);
    }

    @Test
    void createProjets() throws Exception {
        int databaseSizeBeforeCreate = projetsRepository.findAll().collectList().block().size();
        // Create the Projets
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeCreate + 1);
        Projets testProjets = projetsList.get(projetsList.size() - 1);
        assertThat(testProjets.getTypeProjet()).isEqualTo(DEFAULT_TYPE_PROJET);
        assertThat(testProjets.getNomProjet()).isEqualTo(DEFAULT_NOM_PROJET);
        assertThat(testProjets.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testProjets.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testProjets.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjets.getExtension()).isEqualTo(DEFAULT_EXTENSION);
    }

    @Test
    void createProjetsWithExistingId() throws Exception {
        // Create the Projets with an existing ID
        projets.setId(1L);

        int databaseSizeBeforeCreate = projetsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().collectList().block().size();
        // set the field null
        projets.setTypeProjet(null);

        // Create the Projets, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNomProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().collectList().block().size();
        // set the field null
        projets.setNomProjet(null);

        // Create the Projets, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().collectList().block().size();
        // set the field null
        projets.setDateDebut(null);

        // Create the Projets, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().collectList().block().size();
        // set the field null
        projets.setDateFin(null);

        // Create the Projets, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().collectList().block().size();
        // set the field null
        projets.setDescription(null);

        // Create the Projets, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkExtensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().collectList().block().size();
        // set the field null
        projets.setExtension(null);

        // Create the Projets, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProjets() {
        // Initialize the database
        projetsRepository.save(projets).block();

        // Get all the projetsList
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
            .value(hasItem(projets.getId().intValue()))
            .jsonPath("$.[*].typeProjet")
            .value(hasItem(DEFAULT_TYPE_PROJET.toString()))
            .jsonPath("$.[*].nomProjet")
            .value(hasItem(DEFAULT_NOM_PROJET))
            .jsonPath("$.[*].dateDebut")
            .value(hasItem(sameInstant(DEFAULT_DATE_DEBUT)))
            .jsonPath("$.[*].dateFin")
            .value(hasItem(sameInstant(DEFAULT_DATE_FIN)))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].extension")
            .value(hasItem(DEFAULT_EXTENSION.booleanValue()));
    }

    @Test
    void getProjets() {
        // Initialize the database
        projetsRepository.save(projets).block();

        // Get the projets
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, projets.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(projets.getId().intValue()))
            .jsonPath("$.typeProjet")
            .value(is(DEFAULT_TYPE_PROJET.toString()))
            .jsonPath("$.nomProjet")
            .value(is(DEFAULT_NOM_PROJET))
            .jsonPath("$.dateDebut")
            .value(is(sameInstant(DEFAULT_DATE_DEBUT)))
            .jsonPath("$.dateFin")
            .value(is(sameInstant(DEFAULT_DATE_FIN)))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.extension")
            .value(is(DEFAULT_EXTENSION.booleanValue()));
    }

    @Test
    void getNonExistingProjets() {
        // Get the projets
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProjets() throws Exception {
        // Initialize the database
        projetsRepository.save(projets).block();

        int databaseSizeBeforeUpdate = projetsRepository.findAll().collectList().block().size();

        // Update the projets
        Projets updatedProjets = projetsRepository.findById(projets.getId()).block();
        updatedProjets
            .typeProjet(UPDATED_TYPE_PROJET)
            .nomProjet(UPDATED_NOM_PROJET)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION)
            .extension(UPDATED_EXTENSION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedProjets.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedProjets))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
        Projets testProjets = projetsList.get(projetsList.size() - 1);
        assertThat(testProjets.getTypeProjet()).isEqualTo(UPDATED_TYPE_PROJET);
        assertThat(testProjets.getNomProjet()).isEqualTo(UPDATED_NOM_PROJET);
        assertThat(testProjets.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjets.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProjets.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjets.getExtension()).isEqualTo(UPDATED_EXTENSION);
    }

    @Test
    void putNonExistingProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().collectList().block().size();
        projets.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, projets.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().collectList().block().size();
        projets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().collectList().block().size();
        projets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProjetsWithPatch() throws Exception {
        // Initialize the database
        projetsRepository.save(projets).block();

        int databaseSizeBeforeUpdate = projetsRepository.findAll().collectList().block().size();

        // Update the projets using partial update
        Projets partialUpdatedProjets = new Projets();
        partialUpdatedProjets.setId(projets.getId());

        partialUpdatedProjets.typeProjet(UPDATED_TYPE_PROJET).dateDebut(UPDATED_DATE_DEBUT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProjets.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProjets))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
        Projets testProjets = projetsList.get(projetsList.size() - 1);
        assertThat(testProjets.getTypeProjet()).isEqualTo(UPDATED_TYPE_PROJET);
        assertThat(testProjets.getNomProjet()).isEqualTo(DEFAULT_NOM_PROJET);
        assertThat(testProjets.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjets.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testProjets.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjets.getExtension()).isEqualTo(DEFAULT_EXTENSION);
    }

    @Test
    void fullUpdateProjetsWithPatch() throws Exception {
        // Initialize the database
        projetsRepository.save(projets).block();

        int databaseSizeBeforeUpdate = projetsRepository.findAll().collectList().block().size();

        // Update the projets using partial update
        Projets partialUpdatedProjets = new Projets();
        partialUpdatedProjets.setId(projets.getId());

        partialUpdatedProjets
            .typeProjet(UPDATED_TYPE_PROJET)
            .nomProjet(UPDATED_NOM_PROJET)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION)
            .extension(UPDATED_EXTENSION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProjets.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProjets))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
        Projets testProjets = projetsList.get(projetsList.size() - 1);
        assertThat(testProjets.getTypeProjet()).isEqualTo(UPDATED_TYPE_PROJET);
        assertThat(testProjets.getNomProjet()).isEqualTo(UPDATED_NOM_PROJET);
        assertThat(testProjets.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjets.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProjets.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjets.getExtension()).isEqualTo(UPDATED_EXTENSION);
    }

    @Test
    void patchNonExistingProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().collectList().block().size();
        projets.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, projets.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().collectList().block().size();
        projets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().collectList().block().size();
        projets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(projets))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProjets() {
        // Initialize the database
        projetsRepository.save(projets).block();

        int databaseSizeBeforeDelete = projetsRepository.findAll().collectList().block().size();

        // Delete the projets
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, projets.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Projets> projetsList = projetsRepository.findAll().collectList().block();
        assertThat(projetsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
