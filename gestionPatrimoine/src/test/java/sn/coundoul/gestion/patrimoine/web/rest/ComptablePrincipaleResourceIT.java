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
import sn.coundoul.gestion.patrimoine.domain.ComptablePrincipale;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.ComptablePrincipaleRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link ComptablePrincipaleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ComptablePrincipaleResourceIT {

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

    private static final String ENTITY_API_URL = "/api/comptable-principales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComptablePrincipaleRepository comptablePrincipaleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ComptablePrincipale comptablePrincipale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComptablePrincipale createEntity(EntityManager em) {
        ComptablePrincipale comptablePrincipale = new ComptablePrincipale()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return comptablePrincipale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComptablePrincipale createUpdatedEntity(EntityManager em) {
        ComptablePrincipale comptablePrincipale = new ComptablePrincipale()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return comptablePrincipale;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ComptablePrincipale.class).block();
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
        comptablePrincipale = createEntity(em);
    }

    @Test
    void createComptablePrincipale() throws Exception {
        int databaseSizeBeforeCreate = comptablePrincipaleRepository.findAll().collectList().block().size();
        // Create the ComptablePrincipale
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeCreate + 1);
        ComptablePrincipale testComptablePrincipale = comptablePrincipaleList.get(comptablePrincipaleList.size() - 1);
        assertThat(testComptablePrincipale.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testComptablePrincipale.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testComptablePrincipale.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testComptablePrincipale.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testComptablePrincipale.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testComptablePrincipale.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void createComptablePrincipaleWithExistingId() throws Exception {
        // Create the ComptablePrincipale with an existing ID
        comptablePrincipale.setId(1L);

        int databaseSizeBeforeCreate = comptablePrincipaleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptablePrincipaleRepository.findAll().collectList().block().size();
        // set the field null
        comptablePrincipale.setNomPers(null);

        // Create the ComptablePrincipale, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptablePrincipaleRepository.findAll().collectList().block().size();
        // set the field null
        comptablePrincipale.setPrenomPers(null);

        // Create the ComptablePrincipale, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptablePrincipaleRepository.findAll().collectList().block().size();
        // set the field null
        comptablePrincipale.setSexe(null);

        // Create the ComptablePrincipale, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptablePrincipaleRepository.findAll().collectList().block().size();
        // set the field null
        comptablePrincipale.setMobile(null);

        // Create the ComptablePrincipale, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptablePrincipaleRepository.findAll().collectList().block().size();
        // set the field null
        comptablePrincipale.setDirection(null);

        // Create the ComptablePrincipale, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllComptablePrincipales() {
        // Initialize the database
        comptablePrincipaleRepository.save(comptablePrincipale).block();

        // Get all the comptablePrincipaleList
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
            .value(hasItem(comptablePrincipale.getId().intValue()))
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
    void getComptablePrincipale() {
        // Initialize the database
        comptablePrincipaleRepository.save(comptablePrincipale).block();

        // Get the comptablePrincipale
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, comptablePrincipale.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(comptablePrincipale.getId().intValue()))
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
    void getNonExistingComptablePrincipale() {
        // Get the comptablePrincipale
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewComptablePrincipale() throws Exception {
        // Initialize the database
        comptablePrincipaleRepository.save(comptablePrincipale).block();

        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().collectList().block().size();

        // Update the comptablePrincipale
        ComptablePrincipale updatedComptablePrincipale = comptablePrincipaleRepository.findById(comptablePrincipale.getId()).block();
        updatedComptablePrincipale
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedComptablePrincipale.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedComptablePrincipale))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
        ComptablePrincipale testComptablePrincipale = comptablePrincipaleList.get(comptablePrincipaleList.size() - 1);
        assertThat(testComptablePrincipale.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testComptablePrincipale.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testComptablePrincipale.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testComptablePrincipale.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testComptablePrincipale.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testComptablePrincipale.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void putNonExistingComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().collectList().block().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, comptablePrincipale.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().collectList().block().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().collectList().block().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateComptablePrincipaleWithPatch() throws Exception {
        // Initialize the database
        comptablePrincipaleRepository.save(comptablePrincipale).block();

        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().collectList().block().size();

        // Update the comptablePrincipale using partial update
        ComptablePrincipale partialUpdatedComptablePrincipale = new ComptablePrincipale();
        partialUpdatedComptablePrincipale.setId(comptablePrincipale.getId());

        partialUpdatedComptablePrincipale.direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedComptablePrincipale.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedComptablePrincipale))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
        ComptablePrincipale testComptablePrincipale = comptablePrincipaleList.get(comptablePrincipaleList.size() - 1);
        assertThat(testComptablePrincipale.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testComptablePrincipale.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testComptablePrincipale.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testComptablePrincipale.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testComptablePrincipale.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testComptablePrincipale.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void fullUpdateComptablePrincipaleWithPatch() throws Exception {
        // Initialize the database
        comptablePrincipaleRepository.save(comptablePrincipale).block();

        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().collectList().block().size();

        // Update the comptablePrincipale using partial update
        ComptablePrincipale partialUpdatedComptablePrincipale = new ComptablePrincipale();
        partialUpdatedComptablePrincipale.setId(comptablePrincipale.getId());

        partialUpdatedComptablePrincipale
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedComptablePrincipale.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedComptablePrincipale))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
        ComptablePrincipale testComptablePrincipale = comptablePrincipaleList.get(comptablePrincipaleList.size() - 1);
        assertThat(testComptablePrincipale.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testComptablePrincipale.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testComptablePrincipale.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testComptablePrincipale.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testComptablePrincipale.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testComptablePrincipale.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void patchNonExistingComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().collectList().block().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, comptablePrincipale.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().collectList().block().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().collectList().block().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteComptablePrincipale() {
        // Initialize the database
        comptablePrincipaleRepository.save(comptablePrincipale).block();

        int databaseSizeBeforeDelete = comptablePrincipaleRepository.findAll().collectList().block().size();

        // Delete the comptablePrincipale
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, comptablePrincipale.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll().collectList().block();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
