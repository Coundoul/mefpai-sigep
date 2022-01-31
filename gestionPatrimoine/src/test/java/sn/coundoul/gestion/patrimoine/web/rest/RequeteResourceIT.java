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
import sn.coundoul.gestion.patrimoine.domain.Requete;
import sn.coundoul.gestion.patrimoine.repository.RequeteRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link RequeteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class RequeteResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_TYPE_PANNE = 1D;
    private static final Double UPDATED_TYPE_PANNE = 2D;

    private static final Double DEFAULT_DATE_POST = 1D;
    private static final Double UPDATED_DATE_POST = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ETAT_TRAITE = false;
    private static final Boolean UPDATED_ETAT_TRAITE = true;

    private static final Instant DEFAULT_DATE_LANCEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_LANCEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/requetes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RequeteRepository requeteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Requete requete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requete createEntity(EntityManager em) {
        Requete requete = new Requete()
            .type(DEFAULT_TYPE)
            .typePanne(DEFAULT_TYPE_PANNE)
            .datePost(DEFAULT_DATE_POST)
            .description(DEFAULT_DESCRIPTION)
            .etatTraite(DEFAULT_ETAT_TRAITE)
            .dateLancement(DEFAULT_DATE_LANCEMENT)
            .idPers(DEFAULT_ID_PERS);
        return requete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requete createUpdatedEntity(EntityManager em) {
        Requete requete = new Requete()
            .type(UPDATED_TYPE)
            .typePanne(UPDATED_TYPE_PANNE)
            .datePost(UPDATED_DATE_POST)
            .description(UPDATED_DESCRIPTION)
            .etatTraite(UPDATED_ETAT_TRAITE)
            .dateLancement(UPDATED_DATE_LANCEMENT)
            .idPers(UPDATED_ID_PERS);
        return requete;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Requete.class).block();
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
        requete = createEntity(em);
    }

    @Test
    void createRequete() throws Exception {
        int databaseSizeBeforeCreate = requeteRepository.findAll().collectList().block().size();
        // Create the Requete
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeCreate + 1);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRequete.getTypePanne()).isEqualTo(DEFAULT_TYPE_PANNE);
        assertThat(testRequete.getDatePost()).isEqualTo(DEFAULT_DATE_POST);
        assertThat(testRequete.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRequete.getEtatTraite()).isEqualTo(DEFAULT_ETAT_TRAITE);
        assertThat(testRequete.getDateLancement()).isEqualTo(DEFAULT_DATE_LANCEMENT);
        assertThat(testRequete.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    void createRequeteWithExistingId() throws Exception {
        // Create the Requete with an existing ID
        requete.setId(1L);

        int databaseSizeBeforeCreate = requeteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().collectList().block().size();
        // set the field null
        requete.setType(null);

        // Create the Requete, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypePanneIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().collectList().block().size();
        // set the field null
        requete.setTypePanne(null);

        // Create the Requete, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDatePostIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().collectList().block().size();
        // set the field null
        requete.setDatePost(null);

        // Create the Requete, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().collectList().block().size();
        // set the field null
        requete.setDescription(null);

        // Create the Requete, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().collectList().block().size();
        // set the field null
        requete.setIdPers(null);

        // Create the Requete, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRequetes() {
        // Initialize the database
        requeteRepository.save(requete).block();

        // Get all the requeteList
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
            .value(hasItem(requete.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].typePanne")
            .value(hasItem(DEFAULT_TYPE_PANNE.doubleValue()))
            .jsonPath("$.[*].datePost")
            .value(hasItem(DEFAULT_DATE_POST.doubleValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].etatTraite")
            .value(hasItem(DEFAULT_ETAT_TRAITE.booleanValue()))
            .jsonPath("$.[*].dateLancement")
            .value(hasItem(DEFAULT_DATE_LANCEMENT.toString()))
            .jsonPath("$.[*].idPers")
            .value(hasItem(DEFAULT_ID_PERS));
    }

    @Test
    void getRequete() {
        // Initialize the database
        requeteRepository.save(requete).block();

        // Get the requete
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, requete.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(requete.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.typePanne")
            .value(is(DEFAULT_TYPE_PANNE.doubleValue()))
            .jsonPath("$.datePost")
            .value(is(DEFAULT_DATE_POST.doubleValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.etatTraite")
            .value(is(DEFAULT_ETAT_TRAITE.booleanValue()))
            .jsonPath("$.dateLancement")
            .value(is(DEFAULT_DATE_LANCEMENT.toString()))
            .jsonPath("$.idPers")
            .value(is(DEFAULT_ID_PERS));
    }

    @Test
    void getNonExistingRequete() {
        // Get the requete
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewRequete() throws Exception {
        // Initialize the database
        requeteRepository.save(requete).block();

        int databaseSizeBeforeUpdate = requeteRepository.findAll().collectList().block().size();

        // Update the requete
        Requete updatedRequete = requeteRepository.findById(requete.getId()).block();
        updatedRequete
            .type(UPDATED_TYPE)
            .typePanne(UPDATED_TYPE_PANNE)
            .datePost(UPDATED_DATE_POST)
            .description(UPDATED_DESCRIPTION)
            .etatTraite(UPDATED_ETAT_TRAITE)
            .dateLancement(UPDATED_DATE_LANCEMENT)
            .idPers(UPDATED_ID_PERS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRequete.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRequete))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequete.getTypePanne()).isEqualTo(UPDATED_TYPE_PANNE);
        assertThat(testRequete.getDatePost()).isEqualTo(UPDATED_DATE_POST);
        assertThat(testRequete.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequete.getEtatTraite()).isEqualTo(UPDATED_ETAT_TRAITE);
        assertThat(testRequete.getDateLancement()).isEqualTo(UPDATED_DATE_LANCEMENT);
        assertThat(testRequete.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void putNonExistingRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().collectList().block().size();
        requete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requete.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().collectList().block().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().collectList().block().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRequeteWithPatch() throws Exception {
        // Initialize the database
        requeteRepository.save(requete).block();

        int databaseSizeBeforeUpdate = requeteRepository.findAll().collectList().block().size();

        // Update the requete using partial update
        Requete partialUpdatedRequete = new Requete();
        partialUpdatedRequete.setId(requete.getId());

        partialUpdatedRequete.datePost(UPDATED_DATE_POST).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequete.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequete))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRequete.getTypePanne()).isEqualTo(DEFAULT_TYPE_PANNE);
        assertThat(testRequete.getDatePost()).isEqualTo(UPDATED_DATE_POST);
        assertThat(testRequete.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequete.getEtatTraite()).isEqualTo(DEFAULT_ETAT_TRAITE);
        assertThat(testRequete.getDateLancement()).isEqualTo(DEFAULT_DATE_LANCEMENT);
        assertThat(testRequete.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    void fullUpdateRequeteWithPatch() throws Exception {
        // Initialize the database
        requeteRepository.save(requete).block();

        int databaseSizeBeforeUpdate = requeteRepository.findAll().collectList().block().size();

        // Update the requete using partial update
        Requete partialUpdatedRequete = new Requete();
        partialUpdatedRequete.setId(requete.getId());

        partialUpdatedRequete
            .type(UPDATED_TYPE)
            .typePanne(UPDATED_TYPE_PANNE)
            .datePost(UPDATED_DATE_POST)
            .description(UPDATED_DESCRIPTION)
            .etatTraite(UPDATED_ETAT_TRAITE)
            .dateLancement(UPDATED_DATE_LANCEMENT)
            .idPers(UPDATED_ID_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequete.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequete))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequete.getTypePanne()).isEqualTo(UPDATED_TYPE_PANNE);
        assertThat(testRequete.getDatePost()).isEqualTo(UPDATED_DATE_POST);
        assertThat(testRequete.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequete.getEtatTraite()).isEqualTo(UPDATED_ETAT_TRAITE);
        assertThat(testRequete.getDateLancement()).isEqualTo(UPDATED_DATE_LANCEMENT);
        assertThat(testRequete.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void patchNonExistingRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().collectList().block().size();
        requete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, requete.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().collectList().block().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().collectList().block().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requete))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRequete() {
        // Initialize the database
        requeteRepository.save(requete).block();

        int databaseSizeBeforeDelete = requeteRepository.findAll().collectList().block().size();

        // Delete the requete
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, requete.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Requete> requeteList = requeteRepository.findAll().collectList().block();
        assertThat(requeteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
