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
import sn.coundoul.gestion.patrimoine.domain.Directeur;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.DirecteurRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link DirecteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class DirecteurResourceIT {

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

    private static final String ENTITY_API_URL = "/api/directeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DirecteurRepository directeurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Directeur directeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directeur createEntity(EntityManager em) {
        Directeur directeur = new Directeur()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return directeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directeur createUpdatedEntity(EntityManager em) {
        Directeur directeur = new Directeur()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return directeur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Directeur.class).block();
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
        directeur = createEntity(em);
    }

    @Test
    void createDirecteur() throws Exception {
        int databaseSizeBeforeCreate = directeurRepository.findAll().collectList().block().size();
        // Create the Directeur
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeCreate + 1);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testDirecteur.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testDirecteur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDirecteur.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testDirecteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDirecteur.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void createDirecteurWithExistingId() throws Exception {
        // Create the Directeur with an existing ID
        directeur.setId(1L);

        int databaseSizeBeforeCreate = directeurRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().collectList().block().size();
        // set the field null
        directeur.setNomPers(null);

        // Create the Directeur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().collectList().block().size();
        // set the field null
        directeur.setPrenomPers(null);

        // Create the Directeur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().collectList().block().size();
        // set the field null
        directeur.setSexe(null);

        // Create the Directeur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().collectList().block().size();
        // set the field null
        directeur.setMobile(null);

        // Create the Directeur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().collectList().block().size();
        // set the field null
        directeur.setDirection(null);

        // Create the Directeur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDirecteurs() {
        // Initialize the database
        directeurRepository.save(directeur).block();

        // Get all the directeurList
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
            .value(hasItem(directeur.getId().intValue()))
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
    void getDirecteur() {
        // Initialize the database
        directeurRepository.save(directeur).block();

        // Get the directeur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, directeur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(directeur.getId().intValue()))
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
    void getNonExistingDirecteur() {
        // Get the directeur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDirecteur() throws Exception {
        // Initialize the database
        directeurRepository.save(directeur).block();

        int databaseSizeBeforeUpdate = directeurRepository.findAll().collectList().block().size();

        // Update the directeur
        Directeur updatedDirecteur = directeurRepository.findById(directeur.getId()).block();
        updatedDirecteur
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDirecteur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDirecteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testDirecteur.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testDirecteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDirecteur.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testDirecteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDirecteur.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void putNonExistingDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().collectList().block().size();
        directeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, directeur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().collectList().block().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().collectList().block().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDirecteurWithPatch() throws Exception {
        // Initialize the database
        directeurRepository.save(directeur).block();

        int databaseSizeBeforeUpdate = directeurRepository.findAll().collectList().block().size();

        // Update the directeur using partial update
        Directeur partialUpdatedDirecteur = new Directeur();
        partialUpdatedDirecteur.setId(directeur.getId());

        partialUpdatedDirecteur.nomPers(UPDATED_NOM_PERS).prenomPers(UPDATED_PRENOM_PERS).direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDirecteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDirecteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testDirecteur.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testDirecteur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDirecteur.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testDirecteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDirecteur.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void fullUpdateDirecteurWithPatch() throws Exception {
        // Initialize the database
        directeurRepository.save(directeur).block();

        int databaseSizeBeforeUpdate = directeurRepository.findAll().collectList().block().size();

        // Update the directeur using partial update
        Directeur partialUpdatedDirecteur = new Directeur();
        partialUpdatedDirecteur.setId(directeur.getId());

        partialUpdatedDirecteur
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDirecteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDirecteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testDirecteur.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testDirecteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDirecteur.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testDirecteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDirecteur.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void patchNonExistingDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().collectList().block().size();
        directeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, directeur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().collectList().block().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().collectList().block().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(directeur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDirecteur() {
        // Initialize the database
        directeurRepository.save(directeur).block();

        int databaseSizeBeforeDelete = directeurRepository.findAll().collectList().block().size();

        // Delete the directeur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, directeur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Directeur> directeurList = directeurRepository.findAll().collectList().block();
        assertThat(directeurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
