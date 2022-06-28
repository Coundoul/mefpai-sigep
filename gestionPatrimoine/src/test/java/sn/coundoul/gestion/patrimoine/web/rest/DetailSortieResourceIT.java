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
import sn.coundoul.gestion.patrimoine.domain.DetailSortie;
import sn.coundoul.gestion.patrimoine.repository.DetailSortieRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link DetailSortieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class DetailSortieResourceIT {

    private static final String DEFAULT_PIECE_JOINTE = "AAAAAAAAAA";
    private static final String UPDATED_PIECE_JOINTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/detail-sorties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetailSortieRepository detailSortieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DetailSortie detailSortie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailSortie createEntity(EntityManager em) {
        DetailSortie detailSortie = new DetailSortie().pieceJointe(DEFAULT_PIECE_JOINTE).idPers(DEFAULT_ID_PERS);
        return detailSortie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailSortie createUpdatedEntity(EntityManager em) {
        DetailSortie detailSortie = new DetailSortie().pieceJointe(UPDATED_PIECE_JOINTE).idPers(UPDATED_ID_PERS);
        return detailSortie;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DetailSortie.class).block();
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
        detailSortie = createEntity(em);
    }

    @Test
    void createDetailSortie() throws Exception {
        int databaseSizeBeforeCreate = detailSortieRepository.findAll().collectList().block().size();
        // Create the DetailSortie
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detailSortie))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeCreate + 1);
        DetailSortie testDetailSortie = detailSortieList.get(detailSortieList.size() - 1);
        assertThat(testDetailSortie.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testDetailSortie.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    void createDetailSortieWithExistingId() throws Exception {
        // Create the DetailSortie with an existing ID
        detailSortie.setId(1L);

        int databaseSizeBeforeCreate = detailSortieRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detailSortie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPieceJointeIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailSortieRepository.findAll().collectList().block().size();
        // set the field null
        detailSortie.setPieceJointe(null);

        // Create the DetailSortie, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detailSortie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailSortieRepository.findAll().collectList().block().size();
        // set the field null
        detailSortie.setIdPers(null);

        // Create the DetailSortie, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detailSortie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDetailSorties() {
        // Initialize the database
        detailSortieRepository.save(detailSortie).block();

        // Get all the detailSortieList
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
            .value(hasItem(detailSortie.getId().intValue()))
            .jsonPath("$.[*].pieceJointe")
            .value(hasItem(DEFAULT_PIECE_JOINTE))
            .jsonPath("$.[*].idPers")
            .value(hasItem(DEFAULT_ID_PERS));
    }

    @Test
    void getDetailSortie() {
        // Initialize the database
        detailSortieRepository.save(detailSortie).block();

        // Get the detailSortie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, detailSortie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(detailSortie.getId().intValue()))
            .jsonPath("$.pieceJointe")
            .value(is(DEFAULT_PIECE_JOINTE))
            .jsonPath("$.idPers")
            .value(is(DEFAULT_ID_PERS));
    }

    @Test
    void getNonExistingDetailSortie() {
        // Get the detailSortie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDetailSortie() throws Exception {
        // Initialize the database
        detailSortieRepository.save(detailSortie).block();

        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().collectList().block().size();

        // Update the detailSortie
        DetailSortie updatedDetailSortie = detailSortieRepository.findById(detailSortie.getId()).block();
        updatedDetailSortie.pieceJointe(UPDATED_PIECE_JOINTE).idPers(UPDATED_ID_PERS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDetailSortie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDetailSortie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
        DetailSortie testDetailSortie = detailSortieList.get(detailSortieList.size() - 1);
        assertThat(testDetailSortie.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testDetailSortie.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void putNonExistingDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().collectList().block().size();
        detailSortie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, detailSortie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detailSortie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().collectList().block().size();
        detailSortie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detailSortie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().collectList().block().size();
        detailSortie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detailSortie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDetailSortieWithPatch() throws Exception {
        // Initialize the database
        detailSortieRepository.save(detailSortie).block();

        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().collectList().block().size();

        // Update the detailSortie using partial update
        DetailSortie partialUpdatedDetailSortie = new DetailSortie();
        partialUpdatedDetailSortie.setId(detailSortie.getId());

        partialUpdatedDetailSortie.idPers(UPDATED_ID_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDetailSortie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailSortie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
        DetailSortie testDetailSortie = detailSortieList.get(detailSortieList.size() - 1);
        assertThat(testDetailSortie.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testDetailSortie.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void fullUpdateDetailSortieWithPatch() throws Exception {
        // Initialize the database
        detailSortieRepository.save(detailSortie).block();

        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().collectList().block().size();

        // Update the detailSortie using partial update
        DetailSortie partialUpdatedDetailSortie = new DetailSortie();
        partialUpdatedDetailSortie.setId(detailSortie.getId());

        partialUpdatedDetailSortie.pieceJointe(UPDATED_PIECE_JOINTE).idPers(UPDATED_ID_PERS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDetailSortie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailSortie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
        DetailSortie testDetailSortie = detailSortieList.get(detailSortieList.size() - 1);
        assertThat(testDetailSortie.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testDetailSortie.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    void patchNonExistingDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().collectList().block().size();
        detailSortie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, detailSortie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detailSortie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().collectList().block().size();
        detailSortie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detailSortie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().collectList().block().size();
        detailSortie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detailSortie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDetailSortie() {
        // Initialize the database
        detailSortieRepository.save(detailSortie).block();

        int databaseSizeBeforeDelete = detailSortieRepository.findAll().collectList().block().size();

        // Delete the detailSortie
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, detailSortie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll().collectList().block();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
