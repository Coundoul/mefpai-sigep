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
import sn.coundoul.gestion.patrimoine.domain.ChefEtablissement;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.ChefEtablissementRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link ChefEtablissementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ChefEtablissementResourceIT {

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

    private static final String ENTITY_API_URL = "/api/chef-etablissements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChefEtablissementRepository chefEtablissementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ChefEtablissement chefEtablissement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefEtablissement createEntity(EntityManager em) {
        ChefEtablissement chefEtablissement = new ChefEtablissement()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE);
        return chefEtablissement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefEtablissement createUpdatedEntity(EntityManager em) {
        ChefEtablissement chefEtablissement = new ChefEtablissement()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);
        return chefEtablissement;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ChefEtablissement.class).block();
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
        chefEtablissement = createEntity(em);
    }

    @Test
    void createChefEtablissement() throws Exception {
        int databaseSizeBeforeCreate = chefEtablissementRepository.findAll().collectList().block().size();
        // Create the ChefEtablissement
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeCreate + 1);
        ChefEtablissement testChefEtablissement = chefEtablissementList.get(chefEtablissementList.size() - 1);
        assertThat(testChefEtablissement.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testChefEtablissement.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testChefEtablissement.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefEtablissement.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testChefEtablissement.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    void createChefEtablissementWithExistingId() throws Exception {
        // Create the ChefEtablissement with an existing ID
        chefEtablissement.setId(1L);

        int databaseSizeBeforeCreate = chefEtablissementRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefEtablissementRepository.findAll().collectList().block().size();
        // set the field null
        chefEtablissement.setNomPers(null);

        // Create the ChefEtablissement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefEtablissementRepository.findAll().collectList().block().size();
        // set the field null
        chefEtablissement.setPrenomPers(null);

        // Create the ChefEtablissement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefEtablissementRepository.findAll().collectList().block().size();
        // set the field null
        chefEtablissement.setSexe(null);

        // Create the ChefEtablissement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefEtablissementRepository.findAll().collectList().block().size();
        // set the field null
        chefEtablissement.setMobile(null);

        // Create the ChefEtablissement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllChefEtablissements() {
        // Initialize the database
        chefEtablissementRepository.save(chefEtablissement).block();

        // Get all the chefEtablissementList
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
            .value(hasItem(chefEtablissement.getId().intValue()))
            .jsonPath("$.[*].nomPers")
            .value(hasItem(DEFAULT_NOM_PERS))
            .jsonPath("$.[*].prenomPers")
            .value(hasItem(DEFAULT_PRENOM_PERS))
            .jsonPath("$.[*].sexe")
            .value(hasItem(DEFAULT_SEXE.toString()))
            .jsonPath("$.[*].mobile")
            .value(hasItem(DEFAULT_MOBILE))
            .jsonPath("$.[*].adresse")
            .value(hasItem(DEFAULT_ADRESSE));
    }

    @Test
    void getChefEtablissement() {
        // Initialize the database
        chefEtablissementRepository.save(chefEtablissement).block();

        // Get the chefEtablissement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, chefEtablissement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(chefEtablissement.getId().intValue()))
            .jsonPath("$.nomPers")
            .value(is(DEFAULT_NOM_PERS))
            .jsonPath("$.prenomPers")
            .value(is(DEFAULT_PRENOM_PERS))
            .jsonPath("$.sexe")
            .value(is(DEFAULT_SEXE.toString()))
            .jsonPath("$.mobile")
            .value(is(DEFAULT_MOBILE))
            .jsonPath("$.adresse")
            .value(is(DEFAULT_ADRESSE));
    }

    @Test
    void getNonExistingChefEtablissement() {
        // Get the chefEtablissement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewChefEtablissement() throws Exception {
        // Initialize the database
        chefEtablissementRepository.save(chefEtablissement).block();

        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().collectList().block().size();

        // Update the chefEtablissement
        ChefEtablissement updatedChefEtablissement = chefEtablissementRepository.findById(chefEtablissement.getId()).block();
        updatedChefEtablissement
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedChefEtablissement.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedChefEtablissement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
        ChefEtablissement testChefEtablissement = chefEtablissementList.get(chefEtablissementList.size() - 1);
        assertThat(testChefEtablissement.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefEtablissement.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefEtablissement.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefEtablissement.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefEtablissement.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    void putNonExistingChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().collectList().block().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chefEtablissement.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().collectList().block().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().collectList().block().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateChefEtablissementWithPatch() throws Exception {
        // Initialize the database
        chefEtablissementRepository.save(chefEtablissement).block();

        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().collectList().block().size();

        // Update the chefEtablissement using partial update
        ChefEtablissement partialUpdatedChefEtablissement = new ChefEtablissement();
        partialUpdatedChefEtablissement.setId(chefEtablissement.getId());

        partialUpdatedChefEtablissement.nomPers(UPDATED_NOM_PERS).prenomPers(UPDATED_PRENOM_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChefEtablissement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChefEtablissement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
        ChefEtablissement testChefEtablissement = chefEtablissementList.get(chefEtablissementList.size() - 1);
        assertThat(testChefEtablissement.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefEtablissement.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefEtablissement.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefEtablissement.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testChefEtablissement.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    void fullUpdateChefEtablissementWithPatch() throws Exception {
        // Initialize the database
        chefEtablissementRepository.save(chefEtablissement).block();

        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().collectList().block().size();

        // Update the chefEtablissement using partial update
        ChefEtablissement partialUpdatedChefEtablissement = new ChefEtablissement();
        partialUpdatedChefEtablissement.setId(chefEtablissement.getId());

        partialUpdatedChefEtablissement
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChefEtablissement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChefEtablissement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
        ChefEtablissement testChefEtablissement = chefEtablissementList.get(chefEtablissementList.size() - 1);
        assertThat(testChefEtablissement.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefEtablissement.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefEtablissement.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefEtablissement.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefEtablissement.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    void patchNonExistingChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().collectList().block().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, chefEtablissement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().collectList().block().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().collectList().block().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteChefEtablissement() {
        // Initialize the database
        chefEtablissementRepository.save(chefEtablissement).block();

        int databaseSizeBeforeDelete = chefEtablissementRepository.findAll().collectList().block().size();

        // Delete the chefEtablissement
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, chefEtablissement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll().collectList().block();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
