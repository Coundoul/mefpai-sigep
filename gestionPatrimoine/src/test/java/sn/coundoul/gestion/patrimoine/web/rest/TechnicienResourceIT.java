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
import sn.coundoul.gestion.patrimoine.domain.Technicien;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.TechnicienRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link TechnicienResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class TechnicienResourceIT {

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

    private static final String ENTITY_API_URL = "/api/techniciens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TechnicienRepository technicienRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Technicien technicien;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Technicien createEntity(EntityManager em) {
        Technicien technicien = new Technicien()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return technicien;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Technicien createUpdatedEntity(EntityManager em) {
        Technicien technicien = new Technicien()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return technicien;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Technicien.class).block();
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
        technicien = createEntity(em);
    }

    @Test
    void createTechnicien() throws Exception {
        int databaseSizeBeforeCreate = technicienRepository.findAll().collectList().block().size();
        // Create the Technicien
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeCreate + 1);
        Technicien testTechnicien = technicienList.get(technicienList.size() - 1);
        assertThat(testTechnicien.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testTechnicien.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testTechnicien.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testTechnicien.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testTechnicien.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testTechnicien.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void createTechnicienWithExistingId() throws Exception {
        // Create the Technicien with an existing ID
        technicien.setId(1L);

        int databaseSizeBeforeCreate = technicienRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicienRepository.findAll().collectList().block().size();
        // set the field null
        technicien.setNomPers(null);

        // Create the Technicien, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicienRepository.findAll().collectList().block().size();
        // set the field null
        technicien.setPrenomPers(null);

        // Create the Technicien, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicienRepository.findAll().collectList().block().size();
        // set the field null
        technicien.setSexe(null);

        // Create the Technicien, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicienRepository.findAll().collectList().block().size();
        // set the field null
        technicien.setMobile(null);

        // Create the Technicien, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTechniciens() {
        // Initialize the database
        technicienRepository.save(technicien).block();

        // Get all the technicienList
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
            .value(hasItem(technicien.getId().intValue()))
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
    void getTechnicien() {
        // Initialize the database
        technicienRepository.save(technicien).block();

        // Get the technicien
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, technicien.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(technicien.getId().intValue()))
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
    void getNonExistingTechnicien() {
        // Get the technicien
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTechnicien() throws Exception {
        // Initialize the database
        technicienRepository.save(technicien).block();

        int databaseSizeBeforeUpdate = technicienRepository.findAll().collectList().block().size();

        // Update the technicien
        Technicien updatedTechnicien = technicienRepository.findById(technicien.getId()).block();
        updatedTechnicien
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTechnicien.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTechnicien))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate);
        Technicien testTechnicien = technicienList.get(technicienList.size() - 1);
        assertThat(testTechnicien.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testTechnicien.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testTechnicien.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testTechnicien.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testTechnicien.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testTechnicien.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void putNonExistingTechnicien() throws Exception {
        int databaseSizeBeforeUpdate = technicienRepository.findAll().collectList().block().size();
        technicien.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, technicien.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTechnicien() throws Exception {
        int databaseSizeBeforeUpdate = technicienRepository.findAll().collectList().block().size();
        technicien.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTechnicien() throws Exception {
        int databaseSizeBeforeUpdate = technicienRepository.findAll().collectList().block().size();
        technicien.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTechnicienWithPatch() throws Exception {
        // Initialize the database
        technicienRepository.save(technicien).block();

        int databaseSizeBeforeUpdate = technicienRepository.findAll().collectList().block().size();

        // Update the technicien using partial update
        Technicien partialUpdatedTechnicien = new Technicien();
        partialUpdatedTechnicien.setId(technicien.getId());

        partialUpdatedTechnicien.prenomPers(UPDATED_PRENOM_PERS).sexe(UPDATED_SEXE).direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTechnicien.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTechnicien))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate);
        Technicien testTechnicien = technicienList.get(technicienList.size() - 1);
        assertThat(testTechnicien.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testTechnicien.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testTechnicien.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testTechnicien.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testTechnicien.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testTechnicien.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void fullUpdateTechnicienWithPatch() throws Exception {
        // Initialize the database
        technicienRepository.save(technicien).block();

        int databaseSizeBeforeUpdate = technicienRepository.findAll().collectList().block().size();

        // Update the technicien using partial update
        Technicien partialUpdatedTechnicien = new Technicien();
        partialUpdatedTechnicien.setId(technicien.getId());

        partialUpdatedTechnicien
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTechnicien.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTechnicien))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate);
        Technicien testTechnicien = technicienList.get(technicienList.size() - 1);
        assertThat(testTechnicien.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testTechnicien.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testTechnicien.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testTechnicien.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testTechnicien.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testTechnicien.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void patchNonExistingTechnicien() throws Exception {
        int databaseSizeBeforeUpdate = technicienRepository.findAll().collectList().block().size();
        technicien.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, technicien.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTechnicien() throws Exception {
        int databaseSizeBeforeUpdate = technicienRepository.findAll().collectList().block().size();
        technicien.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTechnicien() throws Exception {
        int databaseSizeBeforeUpdate = technicienRepository.findAll().collectList().block().size();
        technicien.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicien))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTechnicien() {
        // Initialize the database
        technicienRepository.save(technicien).block();

        int databaseSizeBeforeDelete = technicienRepository.findAll().collectList().block().size();

        // Delete the technicien
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, technicien.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Technicien> technicienList = technicienRepository.findAll().collectList().block();
        assertThat(technicienList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
