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
import sn.coundoul.gestion.patrimoine.domain.ChefProjet;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.ChefProjetRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link ChefProjetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ChefProjetResourceIT {

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

    private static final String ENTITY_API_URL = "/api/chef-projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChefProjetRepository chefProjetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ChefProjet chefProjet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefProjet createEntity(EntityManager em) {
        ChefProjet chefProjet = new ChefProjet()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return chefProjet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefProjet createUpdatedEntity(EntityManager em) {
        ChefProjet chefProjet = new ChefProjet()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return chefProjet;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ChefProjet.class).block();
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
        chefProjet = createEntity(em);
    }

    @Test
    void createChefProjet() throws Exception {
        int databaseSizeBeforeCreate = chefProjetRepository.findAll().collectList().block().size();
        // Create the ChefProjet
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeCreate + 1);
        ChefProjet testChefProjet = chefProjetList.get(chefProjetList.size() - 1);
        assertThat(testChefProjet.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testChefProjet.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testChefProjet.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefProjet.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testChefProjet.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testChefProjet.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void createChefProjetWithExistingId() throws Exception {
        // Create the ChefProjet with an existing ID
        chefProjet.setId(1L);

        int databaseSizeBeforeCreate = chefProjetRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProjetRepository.findAll().collectList().block().size();
        // set the field null
        chefProjet.setNomPers(null);

        // Create the ChefProjet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProjetRepository.findAll().collectList().block().size();
        // set the field null
        chefProjet.setPrenomPers(null);

        // Create the ChefProjet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProjetRepository.findAll().collectList().block().size();
        // set the field null
        chefProjet.setSexe(null);

        // Create the ChefProjet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProjetRepository.findAll().collectList().block().size();
        // set the field null
        chefProjet.setMobile(null);

        // Create the ChefProjet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllChefProjets() {
        // Initialize the database
        chefProjetRepository.save(chefProjet).block();

        // Get all the chefProjetList
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
            .value(hasItem(chefProjet.getId().intValue()))
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
    void getChefProjet() {
        // Initialize the database
        chefProjetRepository.save(chefProjet).block();

        // Get the chefProjet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, chefProjet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(chefProjet.getId().intValue()))
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
    void getNonExistingChefProjet() {
        // Get the chefProjet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewChefProjet() throws Exception {
        // Initialize the database
        chefProjetRepository.save(chefProjet).block();

        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().collectList().block().size();

        // Update the chefProjet
        ChefProjet updatedChefProjet = chefProjetRepository.findById(chefProjet.getId()).block();
        updatedChefProjet
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedChefProjet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedChefProjet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
        ChefProjet testChefProjet = chefProjetList.get(chefProjetList.size() - 1);
        assertThat(testChefProjet.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefProjet.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefProjet.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefProjet.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefProjet.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChefProjet.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void putNonExistingChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().collectList().block().size();
        chefProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chefProjet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().collectList().block().size();
        chefProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().collectList().block().size();
        chefProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateChefProjetWithPatch() throws Exception {
        // Initialize the database
        chefProjetRepository.save(chefProjet).block();

        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().collectList().block().size();

        // Update the chefProjet using partial update
        ChefProjet partialUpdatedChefProjet = new ChefProjet();
        partialUpdatedChefProjet.setId(chefProjet.getId());

        partialUpdatedChefProjet.prenomPers(UPDATED_PRENOM_PERS).mobile(UPDATED_MOBILE).adresse(UPDATED_ADRESSE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChefProjet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChefProjet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
        ChefProjet testChefProjet = chefProjetList.get(chefProjetList.size() - 1);
        assertThat(testChefProjet.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testChefProjet.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefProjet.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefProjet.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefProjet.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChefProjet.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void fullUpdateChefProjetWithPatch() throws Exception {
        // Initialize the database
        chefProjetRepository.save(chefProjet).block();

        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().collectList().block().size();

        // Update the chefProjet using partial update
        ChefProjet partialUpdatedChefProjet = new ChefProjet();
        partialUpdatedChefProjet.setId(chefProjet.getId());

        partialUpdatedChefProjet
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChefProjet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChefProjet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
        ChefProjet testChefProjet = chefProjetList.get(chefProjetList.size() - 1);
        assertThat(testChefProjet.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefProjet.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefProjet.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefProjet.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefProjet.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChefProjet.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void patchNonExistingChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().collectList().block().size();
        chefProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, chefProjet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().collectList().block().size();
        chefProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().collectList().block().size();
        chefProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefProjet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteChefProjet() {
        // Initialize the database
        chefProjetRepository.save(chefProjet).block();

        int databaseSizeBeforeDelete = chefProjetRepository.findAll().collectList().block().size();

        // Delete the chefProjet
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, chefProjet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll().collectList().block();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
