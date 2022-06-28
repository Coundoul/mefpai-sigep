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
import sn.coundoul.gestion.patrimoine.domain.ChefMaintenance;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.ChefMaintenanceRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link ChefMaintenanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ChefMaintenanceResourceIT {

    private static final String DEFAULT_NOM_PERS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PERS = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_PERS = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_PERS = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.Masculin;
    private static final Sexe UPDATED_SEXE = Sexe.Feminin;

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Direction DEFAULT_DIRECTION = Direction.DAGE;
    private static final Direction UPDATED_DIRECTION = Direction.DFPT;

    private static final String ENTITY_API_URL = "/api/chef-maintenances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChefMaintenanceRepository chefMaintenanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ChefMaintenance chefMaintenance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefMaintenance createEntity(EntityManager em) {
        ChefMaintenance chefMaintenance = new ChefMaintenance()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return chefMaintenance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefMaintenance createUpdatedEntity(EntityManager em) {
        ChefMaintenance chefMaintenance = new ChefMaintenance()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return chefMaintenance;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ChefMaintenance.class).block();
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
        chefMaintenance = createEntity(em);
    }

    @Test
    void createChefMaintenance() throws Exception {
        int databaseSizeBeforeCreate = chefMaintenanceRepository.findAll().collectList().block().size();
        // Create the ChefMaintenance
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeCreate + 1);
        ChefMaintenance testChefMaintenance = chefMaintenanceList.get(chefMaintenanceList.size() - 1);
        assertThat(testChefMaintenance.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testChefMaintenance.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testChefMaintenance.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefMaintenance.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testChefMaintenance.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testChefMaintenance.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void createChefMaintenanceWithExistingId() throws Exception {
        // Create the ChefMaintenance with an existing ID
        chefMaintenance.setId(1L);

        int databaseSizeBeforeCreate = chefMaintenanceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefMaintenanceRepository.findAll().collectList().block().size();
        // set the field null
        chefMaintenance.setNomPers(null);

        // Create the ChefMaintenance, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefMaintenanceRepository.findAll().collectList().block().size();
        // set the field null
        chefMaintenance.setPrenomPers(null);

        // Create the ChefMaintenance, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefMaintenanceRepository.findAll().collectList().block().size();
        // set the field null
        chefMaintenance.setSexe(null);

        // Create the ChefMaintenance, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefMaintenanceRepository.findAll().collectList().block().size();
        // set the field null
        chefMaintenance.setMobile(null);

        // Create the ChefMaintenance, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllChefMaintenances() {
        // Initialize the database
        chefMaintenanceRepository.save(chefMaintenance).block();

        // Get all the chefMaintenanceList
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
            .value(hasItem(chefMaintenance.getId().intValue()))
            .jsonPath("$.[*].nomPers")
            .value(hasItem(DEFAULT_NOM_PERS))
            .jsonPath("$.[*].prenomPers")
            .value(hasItem(DEFAULT_PRENOM_PERS))
            .jsonPath("$.[*].sexe")
            .value(hasItem(DEFAULT_SEXE.toString()))
            .jsonPath("$.[*].mobile")
            .value(hasItem(DEFAULT_MOBILE))
            .jsonPath("$.[*].adresse")
            .value(hasItem(DEFAULT_ADRESSE))
            .jsonPath("$.[*].direction")
            .value(hasItem(DEFAULT_DIRECTION.toString()));
    }

    @Test
    void getChefMaintenance() {
        // Initialize the database
        chefMaintenanceRepository.save(chefMaintenance).block();

        // Get the chefMaintenance
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, chefMaintenance.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(chefMaintenance.getId().intValue()))
            .jsonPath("$.nomPers")
            .value(is(DEFAULT_NOM_PERS))
            .jsonPath("$.prenomPers")
            .value(is(DEFAULT_PRENOM_PERS))
            .jsonPath("$.sexe")
            .value(is(DEFAULT_SEXE.toString()))
            .jsonPath("$.mobile")
            .value(is(DEFAULT_MOBILE))
            .jsonPath("$.adresse")
            .value(is(DEFAULT_ADRESSE))
            .jsonPath("$.direction")
            .value(is(DEFAULT_DIRECTION.toString()));
    }

    @Test
    void getNonExistingChefMaintenance() {
        // Get the chefMaintenance
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewChefMaintenance() throws Exception {
        // Initialize the database
        chefMaintenanceRepository.save(chefMaintenance).block();

        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().collectList().block().size();

        // Update the chefMaintenance
        ChefMaintenance updatedChefMaintenance = chefMaintenanceRepository.findById(chefMaintenance.getId()).block();
        updatedChefMaintenance
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedChefMaintenance.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedChefMaintenance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        ChefMaintenance testChefMaintenance = chefMaintenanceList.get(chefMaintenanceList.size() - 1);
        assertThat(testChefMaintenance.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefMaintenance.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefMaintenance.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefMaintenance.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefMaintenance.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChefMaintenance.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void putNonExistingChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().collectList().block().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chefMaintenance.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().collectList().block().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().collectList().block().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateChefMaintenanceWithPatch() throws Exception {
        // Initialize the database
        chefMaintenanceRepository.save(chefMaintenance).block();

        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().collectList().block().size();

        // Update the chefMaintenance using partial update
        ChefMaintenance partialUpdatedChefMaintenance = new ChefMaintenance();
        partialUpdatedChefMaintenance.setId(chefMaintenance.getId());

        partialUpdatedChefMaintenance.nomPers(UPDATED_NOM_PERS).mobile(UPDATED_MOBILE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChefMaintenance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChefMaintenance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        ChefMaintenance testChefMaintenance = chefMaintenanceList.get(chefMaintenanceList.size() - 1);
        assertThat(testChefMaintenance.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefMaintenance.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testChefMaintenance.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefMaintenance.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefMaintenance.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testChefMaintenance.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void fullUpdateChefMaintenanceWithPatch() throws Exception {
        // Initialize the database
        chefMaintenanceRepository.save(chefMaintenance).block();

        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().collectList().block().size();

        // Update the chefMaintenance using partial update
        ChefMaintenance partialUpdatedChefMaintenance = new ChefMaintenance();
        partialUpdatedChefMaintenance.setId(chefMaintenance.getId());

        partialUpdatedChefMaintenance
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChefMaintenance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChefMaintenance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        ChefMaintenance testChefMaintenance = chefMaintenanceList.get(chefMaintenanceList.size() - 1);
        assertThat(testChefMaintenance.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefMaintenance.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefMaintenance.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefMaintenance.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefMaintenance.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChefMaintenance.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void patchNonExistingChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().collectList().block().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, chefMaintenance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().collectList().block().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().collectList().block().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteChefMaintenance() {
        // Initialize the database
        chefMaintenanceRepository.save(chefMaintenance).block();

        int databaseSizeBeforeDelete = chefMaintenanceRepository.findAll().collectList().block().size();

        // Delete the chefMaintenance
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, chefMaintenance.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll().collectList().block();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
