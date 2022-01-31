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
import sn.coundoul.gestion.patrimoine.domain.Bureau;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.NomS;
import sn.coundoul.gestion.patrimoine.repository.BureauRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link BureauResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class BureauResourceIT {

    private static final NomS DEFAULT_NOM_STRUCTURE = NomS.Etablissement;
    private static final NomS UPDATED_NOM_STRUCTURE = NomS.MEFPAI;

    private static final Direction DEFAULT_DIRECTION = Direction.DAGE;
    private static final Direction UPDATED_DIRECTION = Direction.DFPT;

    private static final String DEFAULT_NOM_ETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ETABLISSEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bureaus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BureauRepository bureauRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Bureau bureau;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bureau createEntity(EntityManager em) {
        Bureau bureau = new Bureau()
            .nomStructure(DEFAULT_NOM_STRUCTURE)
            .direction(DEFAULT_DIRECTION)
            .nomEtablissement(DEFAULT_NOM_ETABLISSEMENT);
        return bureau;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bureau createUpdatedEntity(EntityManager em) {
        Bureau bureau = new Bureau()
            .nomStructure(UPDATED_NOM_STRUCTURE)
            .direction(UPDATED_DIRECTION)
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT);
        return bureau;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Bureau.class).block();
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
        bureau = createEntity(em);
    }

    @Test
    void createBureau() throws Exception {
        int databaseSizeBeforeCreate = bureauRepository.findAll().collectList().block().size();
        // Create the Bureau
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bureau))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeCreate + 1);
        Bureau testBureau = bureauList.get(bureauList.size() - 1);
        assertThat(testBureau.getNomStructure()).isEqualTo(DEFAULT_NOM_STRUCTURE);
        assertThat(testBureau.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testBureau.getNomEtablissement()).isEqualTo(DEFAULT_NOM_ETABLISSEMENT);
    }

    @Test
    void createBureauWithExistingId() throws Exception {
        // Create the Bureau with an existing ID
        bureau.setId(1L);

        int databaseSizeBeforeCreate = bureauRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bureau))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomStructureIsRequired() throws Exception {
        int databaseSizeBeforeTest = bureauRepository.findAll().collectList().block().size();
        // set the field null
        bureau.setNomStructure(null);

        // Create the Bureau, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bureau))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBureaus() {
        // Initialize the database
        bureauRepository.save(bureau).block();

        // Get all the bureauList
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
            .value(hasItem(bureau.getId().intValue()))
            .jsonPath("$.[*].nomStructure")
            .value(hasItem(DEFAULT_NOM_STRUCTURE.toString()))
            .jsonPath("$.[*].direction")
            .value(hasItem(DEFAULT_DIRECTION.toString()))
            .jsonPath("$.[*].nomEtablissement")
            .value(hasItem(DEFAULT_NOM_ETABLISSEMENT));
    }

    @Test
    void getBureau() {
        // Initialize the database
        bureauRepository.save(bureau).block();

        // Get the bureau
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, bureau.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(bureau.getId().intValue()))
            .jsonPath("$.nomStructure")
            .value(is(DEFAULT_NOM_STRUCTURE.toString()))
            .jsonPath("$.direction")
            .value(is(DEFAULT_DIRECTION.toString()))
            .jsonPath("$.nomEtablissement")
            .value(is(DEFAULT_NOM_ETABLISSEMENT));
    }

    @Test
    void getNonExistingBureau() {
        // Get the bureau
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBureau() throws Exception {
        // Initialize the database
        bureauRepository.save(bureau).block();

        int databaseSizeBeforeUpdate = bureauRepository.findAll().collectList().block().size();

        // Update the bureau
        Bureau updatedBureau = bureauRepository.findById(bureau.getId()).block();
        updatedBureau.nomStructure(UPDATED_NOM_STRUCTURE).direction(UPDATED_DIRECTION).nomEtablissement(UPDATED_NOM_ETABLISSEMENT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBureau.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBureau))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
        Bureau testBureau = bureauList.get(bureauList.size() - 1);
        assertThat(testBureau.getNomStructure()).isEqualTo(UPDATED_NOM_STRUCTURE);
        assertThat(testBureau.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testBureau.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
    }

    @Test
    void putNonExistingBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().collectList().block().size();
        bureau.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, bureau.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bureau))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().collectList().block().size();
        bureau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bureau))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().collectList().block().size();
        bureau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bureau))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBureauWithPatch() throws Exception {
        // Initialize the database
        bureauRepository.save(bureau).block();

        int databaseSizeBeforeUpdate = bureauRepository.findAll().collectList().block().size();

        // Update the bureau using partial update
        Bureau partialUpdatedBureau = new Bureau();
        partialUpdatedBureau.setId(bureau.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBureau.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBureau))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
        Bureau testBureau = bureauList.get(bureauList.size() - 1);
        assertThat(testBureau.getNomStructure()).isEqualTo(DEFAULT_NOM_STRUCTURE);
        assertThat(testBureau.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testBureau.getNomEtablissement()).isEqualTo(DEFAULT_NOM_ETABLISSEMENT);
    }

    @Test
    void fullUpdateBureauWithPatch() throws Exception {
        // Initialize the database
        bureauRepository.save(bureau).block();

        int databaseSizeBeforeUpdate = bureauRepository.findAll().collectList().block().size();

        // Update the bureau using partial update
        Bureau partialUpdatedBureau = new Bureau();
        partialUpdatedBureau.setId(bureau.getId());

        partialUpdatedBureau.nomStructure(UPDATED_NOM_STRUCTURE).direction(UPDATED_DIRECTION).nomEtablissement(UPDATED_NOM_ETABLISSEMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBureau.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBureau))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
        Bureau testBureau = bureauList.get(bureauList.size() - 1);
        assertThat(testBureau.getNomStructure()).isEqualTo(UPDATED_NOM_STRUCTURE);
        assertThat(testBureau.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testBureau.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
    }

    @Test
    void patchNonExistingBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().collectList().block().size();
        bureau.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, bureau.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bureau))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().collectList().block().size();
        bureau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bureau))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().collectList().block().size();
        bureau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bureau))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBureau() {
        // Initialize the database
        bureauRepository.save(bureau).block();

        int databaseSizeBeforeDelete = bureauRepository.findAll().collectList().block().size();

        // Delete the bureau
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, bureau.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Bureau> bureauList = bureauRepository.findAll().collectList().block();
        assertThat(bureauList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
