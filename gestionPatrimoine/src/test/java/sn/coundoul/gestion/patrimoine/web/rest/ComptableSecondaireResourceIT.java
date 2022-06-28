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
import sn.coundoul.gestion.patrimoine.domain.ComptableSecondaire;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.ComptableSecondaireRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link ComptableSecondaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ComptableSecondaireResourceIT {

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

    private static final String ENTITY_API_URL = "/api/comptable-secondaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComptableSecondaireRepository comptableSecondaireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ComptableSecondaire comptableSecondaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComptableSecondaire createEntity(EntityManager em) {
        ComptableSecondaire comptableSecondaire = new ComptableSecondaire()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return comptableSecondaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComptableSecondaire createUpdatedEntity(EntityManager em) {
        ComptableSecondaire comptableSecondaire = new ComptableSecondaire()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return comptableSecondaire;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ComptableSecondaire.class).block();
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
        comptableSecondaire = createEntity(em);
    }

    @Test
    void createComptableSecondaire() throws Exception {
        int databaseSizeBeforeCreate = comptableSecondaireRepository.findAll().collectList().block().size();
        // Create the ComptableSecondaire
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeCreate + 1);
        ComptableSecondaire testComptableSecondaire = comptableSecondaireList.get(comptableSecondaireList.size() - 1);
        assertThat(testComptableSecondaire.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testComptableSecondaire.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testComptableSecondaire.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testComptableSecondaire.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testComptableSecondaire.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testComptableSecondaire.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void createComptableSecondaireWithExistingId() throws Exception {
        // Create the ComptableSecondaire with an existing ID
        comptableSecondaire.setId(1L);

        int databaseSizeBeforeCreate = comptableSecondaireRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptableSecondaireRepository.findAll().collectList().block().size();
        // set the field null
        comptableSecondaire.setNomPers(null);

        // Create the ComptableSecondaire, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptableSecondaireRepository.findAll().collectList().block().size();
        // set the field null
        comptableSecondaire.setPrenomPers(null);

        // Create the ComptableSecondaire, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptableSecondaireRepository.findAll().collectList().block().size();
        // set the field null
        comptableSecondaire.setSexe(null);

        // Create the ComptableSecondaire, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptableSecondaireRepository.findAll().collectList().block().size();
        // set the field null
        comptableSecondaire.setMobile(null);

        // Create the ComptableSecondaire, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptableSecondaireRepository.findAll().collectList().block().size();
        // set the field null
        comptableSecondaire.setDirection(null);

        // Create the ComptableSecondaire, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllComptableSecondaires() {
        // Initialize the database
        comptableSecondaireRepository.save(comptableSecondaire).block();

        // Get all the comptableSecondaireList
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
            .value(hasItem(comptableSecondaire.getId().intValue()))
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
    void getComptableSecondaire() {
        // Initialize the database
        comptableSecondaireRepository.save(comptableSecondaire).block();

        // Get the comptableSecondaire
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, comptableSecondaire.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(comptableSecondaire.getId().intValue()))
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
    void getNonExistingComptableSecondaire() {
        // Get the comptableSecondaire
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewComptableSecondaire() throws Exception {
        // Initialize the database
        comptableSecondaireRepository.save(comptableSecondaire).block();

        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().collectList().block().size();

        // Update the comptableSecondaire
        ComptableSecondaire updatedComptableSecondaire = comptableSecondaireRepository.findById(comptableSecondaire.getId()).block();
        updatedComptableSecondaire
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedComptableSecondaire.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedComptableSecondaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
        ComptableSecondaire testComptableSecondaire = comptableSecondaireList.get(comptableSecondaireList.size() - 1);
        assertThat(testComptableSecondaire.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testComptableSecondaire.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testComptableSecondaire.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testComptableSecondaire.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testComptableSecondaire.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testComptableSecondaire.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void putNonExistingComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().collectList().block().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, comptableSecondaire.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().collectList().block().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().collectList().block().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateComptableSecondaireWithPatch() throws Exception {
        // Initialize the database
        comptableSecondaireRepository.save(comptableSecondaire).block();

        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().collectList().block().size();

        // Update the comptableSecondaire using partial update
        ComptableSecondaire partialUpdatedComptableSecondaire = new ComptableSecondaire();
        partialUpdatedComptableSecondaire.setId(comptableSecondaire.getId());

        partialUpdatedComptableSecondaire.prenomPers(UPDATED_PRENOM_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedComptableSecondaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedComptableSecondaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
        ComptableSecondaire testComptableSecondaire = comptableSecondaireList.get(comptableSecondaireList.size() - 1);
        assertThat(testComptableSecondaire.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testComptableSecondaire.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testComptableSecondaire.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testComptableSecondaire.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testComptableSecondaire.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testComptableSecondaire.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void fullUpdateComptableSecondaireWithPatch() throws Exception {
        // Initialize the database
        comptableSecondaireRepository.save(comptableSecondaire).block();

        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().collectList().block().size();

        // Update the comptableSecondaire using partial update
        ComptableSecondaire partialUpdatedComptableSecondaire = new ComptableSecondaire();
        partialUpdatedComptableSecondaire.setId(comptableSecondaire.getId());

        partialUpdatedComptableSecondaire
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedComptableSecondaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedComptableSecondaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
        ComptableSecondaire testComptableSecondaire = comptableSecondaireList.get(comptableSecondaireList.size() - 1);
        assertThat(testComptableSecondaire.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testComptableSecondaire.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testComptableSecondaire.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testComptableSecondaire.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testComptableSecondaire.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testComptableSecondaire.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void patchNonExistingComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().collectList().block().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, comptableSecondaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().collectList().block().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().collectList().block().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteComptableSecondaire() {
        // Initialize the database
        comptableSecondaireRepository.save(comptableSecondaire).block();

        int databaseSizeBeforeDelete = comptableSecondaireRepository.findAll().collectList().block().size();

        // Delete the comptableSecondaire
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, comptableSecondaire.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll().collectList().block();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
