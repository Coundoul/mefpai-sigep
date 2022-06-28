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
import sn.coundoul.gestion.patrimoine.domain.CorpsEtat;
import sn.coundoul.gestion.patrimoine.repository.CorpsEtatRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link CorpsEtatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class CorpsEtatResourceIT {

    private static final String DEFAULT_NOM_CORPS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CORPS = "BBBBBBBBBB";

    private static final String DEFAULT_GROS_OEUVRE = "AAAAAAAAAA";
    private static final String UPDATED_GROS_OEUVRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_GROS_OEUVRE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_GROS_OEUVRE = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_OEUVRE = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_OEUVRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_SECOND_OEUVRE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_SECOND_OEUVRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OSERVATION = false;
    private static final Boolean UPDATED_OSERVATION = true;

    private static final Boolean DEFAULT_ETAT_CORPS = false;
    private static final Boolean UPDATED_ETAT_CORPS = true;

    private static final String ENTITY_API_URL = "/api/corps-etats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CorpsEtatRepository corpsEtatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CorpsEtat corpsEtat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CorpsEtat createEntity(EntityManager em) {
        CorpsEtat corpsEtat = new CorpsEtat()
            .nomCorps(DEFAULT_NOM_CORPS)
            .grosOeuvre(DEFAULT_GROS_OEUVRE)
            .descriptionGrosOeuvre(DEFAULT_DESCRIPTION_GROS_OEUVRE)
            .secondOeuvre(DEFAULT_SECOND_OEUVRE)
            .descriptionSecondOeuvre(DEFAULT_DESCRIPTION_SECOND_OEUVRE)
            .oservation(DEFAULT_OSERVATION)
            .etatCorps(DEFAULT_ETAT_CORPS);
        return corpsEtat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CorpsEtat createUpdatedEntity(EntityManager em) {
        CorpsEtat corpsEtat = new CorpsEtat()
            .nomCorps(UPDATED_NOM_CORPS)
            .grosOeuvre(UPDATED_GROS_OEUVRE)
            .descriptionGrosOeuvre(UPDATED_DESCRIPTION_GROS_OEUVRE)
            .secondOeuvre(UPDATED_SECOND_OEUVRE)
            .descriptionSecondOeuvre(UPDATED_DESCRIPTION_SECOND_OEUVRE)
            .oservation(UPDATED_OSERVATION)
            .etatCorps(UPDATED_ETAT_CORPS);
        return corpsEtat;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CorpsEtat.class).block();
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
        corpsEtat = createEntity(em);
    }

    @Test
    void createCorpsEtat() throws Exception {
        int databaseSizeBeforeCreate = corpsEtatRepository.findAll().collectList().block().size();
        // Create the CorpsEtat
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeCreate + 1);
        CorpsEtat testCorpsEtat = corpsEtatList.get(corpsEtatList.size() - 1);
        assertThat(testCorpsEtat.getNomCorps()).isEqualTo(DEFAULT_NOM_CORPS);
        assertThat(testCorpsEtat.getGrosOeuvre()).isEqualTo(DEFAULT_GROS_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionGrosOeuvre()).isEqualTo(DEFAULT_DESCRIPTION_GROS_OEUVRE);
        assertThat(testCorpsEtat.getSecondOeuvre()).isEqualTo(DEFAULT_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionSecondOeuvre()).isEqualTo(DEFAULT_DESCRIPTION_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getOservation()).isEqualTo(DEFAULT_OSERVATION);
        assertThat(testCorpsEtat.getEtatCorps()).isEqualTo(DEFAULT_ETAT_CORPS);
    }

    @Test
    void createCorpsEtatWithExistingId() throws Exception {
        // Create the CorpsEtat with an existing ID
        corpsEtat.setId(1L);

        int databaseSizeBeforeCreate = corpsEtatRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomCorpsIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().collectList().block().size();
        // set the field null
        corpsEtat.setNomCorps(null);

        // Create the CorpsEtat, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkGrosOeuvreIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().collectList().block().size();
        // set the field null
        corpsEtat.setGrosOeuvre(null);

        // Create the CorpsEtat, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionGrosOeuvreIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().collectList().block().size();
        // set the field null
        corpsEtat.setDescriptionGrosOeuvre(null);

        // Create the CorpsEtat, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSecondOeuvreIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().collectList().block().size();
        // set the field null
        corpsEtat.setSecondOeuvre(null);

        // Create the CorpsEtat, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionSecondOeuvreIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().collectList().block().size();
        // set the field null
        corpsEtat.setDescriptionSecondOeuvre(null);

        // Create the CorpsEtat, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOservationIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().collectList().block().size();
        // set the field null
        corpsEtat.setOservation(null);

        // Create the CorpsEtat, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCorpsEtats() {
        // Initialize the database
        corpsEtatRepository.save(corpsEtat).block();

        // Get all the corpsEtatList
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
            .value(hasItem(corpsEtat.getId().intValue()))
            .jsonPath("$.[*].nomCorps")
            .value(hasItem(DEFAULT_NOM_CORPS))
            .jsonPath("$.[*].grosOeuvre")
            .value(hasItem(DEFAULT_GROS_OEUVRE))
            .jsonPath("$.[*].descriptionGrosOeuvre")
            .value(hasItem(DEFAULT_DESCRIPTION_GROS_OEUVRE))
            .jsonPath("$.[*].secondOeuvre")
            .value(hasItem(DEFAULT_SECOND_OEUVRE))
            .jsonPath("$.[*].descriptionSecondOeuvre")
            .value(hasItem(DEFAULT_DESCRIPTION_SECOND_OEUVRE))
            .jsonPath("$.[*].oservation")
            .value(hasItem(DEFAULT_OSERVATION.booleanValue()))
            .jsonPath("$.[*].etatCorps")
            .value(hasItem(DEFAULT_ETAT_CORPS.booleanValue()));
    }

    @Test
    void getCorpsEtat() {
        // Initialize the database
        corpsEtatRepository.save(corpsEtat).block();

        // Get the corpsEtat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, corpsEtat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(corpsEtat.getId().intValue()))
            .jsonPath("$.nomCorps")
            .value(is(DEFAULT_NOM_CORPS))
            .jsonPath("$.grosOeuvre")
            .value(is(DEFAULT_GROS_OEUVRE))
            .jsonPath("$.descriptionGrosOeuvre")
            .value(is(DEFAULT_DESCRIPTION_GROS_OEUVRE))
            .jsonPath("$.secondOeuvre")
            .value(is(DEFAULT_SECOND_OEUVRE))
            .jsonPath("$.descriptionSecondOeuvre")
            .value(is(DEFAULT_DESCRIPTION_SECOND_OEUVRE))
            .jsonPath("$.oservation")
            .value(is(DEFAULT_OSERVATION.booleanValue()))
            .jsonPath("$.etatCorps")
            .value(is(DEFAULT_ETAT_CORPS.booleanValue()));
    }

    @Test
    void getNonExistingCorpsEtat() {
        // Get the corpsEtat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCorpsEtat() throws Exception {
        // Initialize the database
        corpsEtatRepository.save(corpsEtat).block();

        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().collectList().block().size();

        // Update the corpsEtat
        CorpsEtat updatedCorpsEtat = corpsEtatRepository.findById(corpsEtat.getId()).block();
        updatedCorpsEtat
            .nomCorps(UPDATED_NOM_CORPS)
            .grosOeuvre(UPDATED_GROS_OEUVRE)
            .descriptionGrosOeuvre(UPDATED_DESCRIPTION_GROS_OEUVRE)
            .secondOeuvre(UPDATED_SECOND_OEUVRE)
            .descriptionSecondOeuvre(UPDATED_DESCRIPTION_SECOND_OEUVRE)
            .oservation(UPDATED_OSERVATION)
            .etatCorps(UPDATED_ETAT_CORPS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCorpsEtat.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCorpsEtat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
        CorpsEtat testCorpsEtat = corpsEtatList.get(corpsEtatList.size() - 1);
        assertThat(testCorpsEtat.getNomCorps()).isEqualTo(UPDATED_NOM_CORPS);
        assertThat(testCorpsEtat.getGrosOeuvre()).isEqualTo(UPDATED_GROS_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionGrosOeuvre()).isEqualTo(UPDATED_DESCRIPTION_GROS_OEUVRE);
        assertThat(testCorpsEtat.getSecondOeuvre()).isEqualTo(UPDATED_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionSecondOeuvre()).isEqualTo(UPDATED_DESCRIPTION_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getOservation()).isEqualTo(UPDATED_OSERVATION);
        assertThat(testCorpsEtat.getEtatCorps()).isEqualTo(UPDATED_ETAT_CORPS);
    }

    @Test
    void putNonExistingCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().collectList().block().size();
        corpsEtat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, corpsEtat.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().collectList().block().size();
        corpsEtat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().collectList().block().size();
        corpsEtat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCorpsEtatWithPatch() throws Exception {
        // Initialize the database
        corpsEtatRepository.save(corpsEtat).block();

        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().collectList().block().size();

        // Update the corpsEtat using partial update
        CorpsEtat partialUpdatedCorpsEtat = new CorpsEtat();
        partialUpdatedCorpsEtat.setId(corpsEtat.getId());

        partialUpdatedCorpsEtat.nomCorps(UPDATED_NOM_CORPS).oservation(UPDATED_OSERVATION).etatCorps(UPDATED_ETAT_CORPS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCorpsEtat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCorpsEtat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
        CorpsEtat testCorpsEtat = corpsEtatList.get(corpsEtatList.size() - 1);
        assertThat(testCorpsEtat.getNomCorps()).isEqualTo(UPDATED_NOM_CORPS);
        assertThat(testCorpsEtat.getGrosOeuvre()).isEqualTo(DEFAULT_GROS_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionGrosOeuvre()).isEqualTo(DEFAULT_DESCRIPTION_GROS_OEUVRE);
        assertThat(testCorpsEtat.getSecondOeuvre()).isEqualTo(DEFAULT_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionSecondOeuvre()).isEqualTo(DEFAULT_DESCRIPTION_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getOservation()).isEqualTo(UPDATED_OSERVATION);
        assertThat(testCorpsEtat.getEtatCorps()).isEqualTo(UPDATED_ETAT_CORPS);
    }

    @Test
    void fullUpdateCorpsEtatWithPatch() throws Exception {
        // Initialize the database
        corpsEtatRepository.save(corpsEtat).block();

        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().collectList().block().size();

        // Update the corpsEtat using partial update
        CorpsEtat partialUpdatedCorpsEtat = new CorpsEtat();
        partialUpdatedCorpsEtat.setId(corpsEtat.getId());

        partialUpdatedCorpsEtat
            .nomCorps(UPDATED_NOM_CORPS)
            .grosOeuvre(UPDATED_GROS_OEUVRE)
            .descriptionGrosOeuvre(UPDATED_DESCRIPTION_GROS_OEUVRE)
            .secondOeuvre(UPDATED_SECOND_OEUVRE)
            .descriptionSecondOeuvre(UPDATED_DESCRIPTION_SECOND_OEUVRE)
            .oservation(UPDATED_OSERVATION)
            .etatCorps(UPDATED_ETAT_CORPS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCorpsEtat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCorpsEtat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
        CorpsEtat testCorpsEtat = corpsEtatList.get(corpsEtatList.size() - 1);
        assertThat(testCorpsEtat.getNomCorps()).isEqualTo(UPDATED_NOM_CORPS);
        assertThat(testCorpsEtat.getGrosOeuvre()).isEqualTo(UPDATED_GROS_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionGrosOeuvre()).isEqualTo(UPDATED_DESCRIPTION_GROS_OEUVRE);
        assertThat(testCorpsEtat.getSecondOeuvre()).isEqualTo(UPDATED_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionSecondOeuvre()).isEqualTo(UPDATED_DESCRIPTION_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getOservation()).isEqualTo(UPDATED_OSERVATION);
        assertThat(testCorpsEtat.getEtatCorps()).isEqualTo(UPDATED_ETAT_CORPS);
    }

    @Test
    void patchNonExistingCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().collectList().block().size();
        corpsEtat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, corpsEtat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().collectList().block().size();
        corpsEtat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().collectList().block().size();
        corpsEtat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(corpsEtat))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCorpsEtat() {
        // Initialize the database
        corpsEtatRepository.save(corpsEtat).block();

        int databaseSizeBeforeDelete = corpsEtatRepository.findAll().collectList().block().size();

        // Delete the corpsEtat
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, corpsEtat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll().collectList().block();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
