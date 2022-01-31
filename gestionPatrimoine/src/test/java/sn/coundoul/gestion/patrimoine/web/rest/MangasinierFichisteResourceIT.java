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
import sn.coundoul.gestion.patrimoine.domain.MangasinierFichiste;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.MangasinierFichisteRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link MangasinierFichisteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class MangasinierFichisteResourceIT {

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

    private static final String ENTITY_API_URL = "/api/mangasinier-fichistes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MangasinierFichisteRepository mangasinierFichisteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MangasinierFichiste mangasinierFichiste;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MangasinierFichiste createEntity(EntityManager em) {
        MangasinierFichiste mangasinierFichiste = new MangasinierFichiste()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return mangasinierFichiste;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MangasinierFichiste createUpdatedEntity(EntityManager em) {
        MangasinierFichiste mangasinierFichiste = new MangasinierFichiste()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return mangasinierFichiste;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MangasinierFichiste.class).block();
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
        mangasinierFichiste = createEntity(em);
    }

    @Test
    void createMangasinierFichiste() throws Exception {
        int databaseSizeBeforeCreate = mangasinierFichisteRepository.findAll().collectList().block().size();
        // Create the MangasinierFichiste
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeCreate + 1);
        MangasinierFichiste testMangasinierFichiste = mangasinierFichisteList.get(mangasinierFichisteList.size() - 1);
        assertThat(testMangasinierFichiste.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testMangasinierFichiste.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testMangasinierFichiste.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testMangasinierFichiste.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testMangasinierFichiste.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testMangasinierFichiste.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void createMangasinierFichisteWithExistingId() throws Exception {
        // Create the MangasinierFichiste with an existing ID
        mangasinierFichiste.setId(1L);

        int databaseSizeBeforeCreate = mangasinierFichisteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = mangasinierFichisteRepository.findAll().collectList().block().size();
        // set the field null
        mangasinierFichiste.setNomPers(null);

        // Create the MangasinierFichiste, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = mangasinierFichisteRepository.findAll().collectList().block().size();
        // set the field null
        mangasinierFichiste.setPrenomPers(null);

        // Create the MangasinierFichiste, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = mangasinierFichisteRepository.findAll().collectList().block().size();
        // set the field null
        mangasinierFichiste.setSexe(null);

        // Create the MangasinierFichiste, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = mangasinierFichisteRepository.findAll().collectList().block().size();
        // set the field null
        mangasinierFichiste.setMobile(null);

        // Create the MangasinierFichiste, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = mangasinierFichisteRepository.findAll().collectList().block().size();
        // set the field null
        mangasinierFichiste.setDirection(null);

        // Create the MangasinierFichiste, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMangasinierFichistes() {
        // Initialize the database
        mangasinierFichisteRepository.save(mangasinierFichiste).block();

        // Get all the mangasinierFichisteList
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
            .value(hasItem(mangasinierFichiste.getId().intValue()))
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
    void getMangasinierFichiste() {
        // Initialize the database
        mangasinierFichisteRepository.save(mangasinierFichiste).block();

        // Get the mangasinierFichiste
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, mangasinierFichiste.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(mangasinierFichiste.getId().intValue()))
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
    void getNonExistingMangasinierFichiste() {
        // Get the mangasinierFichiste
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewMangasinierFichiste() throws Exception {
        // Initialize the database
        mangasinierFichisteRepository.save(mangasinierFichiste).block();

        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().collectList().block().size();

        // Update the mangasinierFichiste
        MangasinierFichiste updatedMangasinierFichiste = mangasinierFichisteRepository.findById(mangasinierFichiste.getId()).block();
        updatedMangasinierFichiste
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMangasinierFichiste.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedMangasinierFichiste))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
        MangasinierFichiste testMangasinierFichiste = mangasinierFichisteList.get(mangasinierFichisteList.size() - 1);
        assertThat(testMangasinierFichiste.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testMangasinierFichiste.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testMangasinierFichiste.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testMangasinierFichiste.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testMangasinierFichiste.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testMangasinierFichiste.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void putNonExistingMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().collectList().block().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, mangasinierFichiste.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().collectList().block().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().collectList().block().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMangasinierFichisteWithPatch() throws Exception {
        // Initialize the database
        mangasinierFichisteRepository.save(mangasinierFichiste).block();

        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().collectList().block().size();

        // Update the mangasinierFichiste using partial update
        MangasinierFichiste partialUpdatedMangasinierFichiste = new MangasinierFichiste();
        partialUpdatedMangasinierFichiste.setId(mangasinierFichiste.getId());

        partialUpdatedMangasinierFichiste
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .mobile(UPDATED_MOBILE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMangasinierFichiste.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMangasinierFichiste))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
        MangasinierFichiste testMangasinierFichiste = mangasinierFichisteList.get(mangasinierFichisteList.size() - 1);
        assertThat(testMangasinierFichiste.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testMangasinierFichiste.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testMangasinierFichiste.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testMangasinierFichiste.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testMangasinierFichiste.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testMangasinierFichiste.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void fullUpdateMangasinierFichisteWithPatch() throws Exception {
        // Initialize the database
        mangasinierFichisteRepository.save(mangasinierFichiste).block();

        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().collectList().block().size();

        // Update the mangasinierFichiste using partial update
        MangasinierFichiste partialUpdatedMangasinierFichiste = new MangasinierFichiste();
        partialUpdatedMangasinierFichiste.setId(mangasinierFichiste.getId());

        partialUpdatedMangasinierFichiste
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMangasinierFichiste.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMangasinierFichiste))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
        MangasinierFichiste testMangasinierFichiste = mangasinierFichisteList.get(mangasinierFichisteList.size() - 1);
        assertThat(testMangasinierFichiste.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testMangasinierFichiste.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testMangasinierFichiste.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testMangasinierFichiste.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testMangasinierFichiste.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testMangasinierFichiste.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void patchNonExistingMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().collectList().block().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, mangasinierFichiste.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().collectList().block().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().collectList().block().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMangasinierFichiste() {
        // Initialize the database
        mangasinierFichisteRepository.save(mangasinierFichiste).block();

        int databaseSizeBeforeDelete = mangasinierFichisteRepository.findAll().collectList().block().size();

        // Delete the mangasinierFichiste
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, mangasinierFichiste.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll().collectList().block();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
