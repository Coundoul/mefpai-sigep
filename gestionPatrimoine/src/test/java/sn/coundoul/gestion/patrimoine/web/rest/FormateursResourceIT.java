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
import sn.coundoul.gestion.patrimoine.domain.Formateurs;
import sn.coundoul.gestion.patrimoine.repository.FormateursRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link FormateursResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class FormateursResourceIT {

    private static final String DEFAULT_NOM_FORMATEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FORMATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_FORMATEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_FORMATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NUMB_1 = "AAAAAAAAAA";
    private static final String UPDATED_NUMB_1 = "BBBBBBBBBB";

    private static final String DEFAULT_NUMB_2 = "AAAAAAAAAA";
    private static final String UPDATED_NUMB_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALITE = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/formateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormateursRepository formateursRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Formateurs formateurs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formateurs createEntity(EntityManager em) {
        Formateurs formateurs = new Formateurs()
            .nomFormateur(DEFAULT_NOM_FORMATEUR)
            .prenomFormateur(DEFAULT_PRENOM_FORMATEUR)
            .email(DEFAULT_EMAIL)
            .numb1(DEFAULT_NUMB_1)
            .numb2(DEFAULT_NUMB_2)
            .adresse(DEFAULT_ADRESSE)
            .ville(DEFAULT_VILLE)
            .specialite(DEFAULT_SPECIALITE);
        return formateurs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formateurs createUpdatedEntity(EntityManager em) {
        Formateurs formateurs = new Formateurs()
            .nomFormateur(UPDATED_NOM_FORMATEUR)
            .prenomFormateur(UPDATED_PRENOM_FORMATEUR)
            .email(UPDATED_EMAIL)
            .numb1(UPDATED_NUMB_1)
            .numb2(UPDATED_NUMB_2)
            .adresse(UPDATED_ADRESSE)
            .ville(UPDATED_VILLE)
            .specialite(UPDATED_SPECIALITE);
        return formateurs;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Formateurs.class).block();
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
        formateurs = createEntity(em);
    }

    @Test
    void createFormateurs() throws Exception {
        int databaseSizeBeforeCreate = formateursRepository.findAll().collectList().block().size();
        // Create the Formateurs
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeCreate + 1);
        Formateurs testFormateurs = formateursList.get(formateursList.size() - 1);
        assertThat(testFormateurs.getNomFormateur()).isEqualTo(DEFAULT_NOM_FORMATEUR);
        assertThat(testFormateurs.getPrenomFormateur()).isEqualTo(DEFAULT_PRENOM_FORMATEUR);
        assertThat(testFormateurs.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFormateurs.getNumb1()).isEqualTo(DEFAULT_NUMB_1);
        assertThat(testFormateurs.getNumb2()).isEqualTo(DEFAULT_NUMB_2);
        assertThat(testFormateurs.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testFormateurs.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testFormateurs.getSpecialite()).isEqualTo(DEFAULT_SPECIALITE);
    }

    @Test
    void createFormateursWithExistingId() throws Exception {
        // Create the Formateurs with an existing ID
        formateurs.setId(1L);

        int databaseSizeBeforeCreate = formateursRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomFormateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().collectList().block().size();
        // set the field null
        formateurs.setNomFormateur(null);

        // Create the Formateurs, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomFormateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().collectList().block().size();
        // set the field null
        formateurs.setPrenomFormateur(null);

        // Create the Formateurs, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().collectList().block().size();
        // set the field null
        formateurs.setEmail(null);

        // Create the Formateurs, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNumb1IsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().collectList().block().size();
        // set the field null
        formateurs.setNumb1(null);

        // Create the Formateurs, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().collectList().block().size();
        // set the field null
        formateurs.setAdresse(null);

        // Create the Formateurs, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().collectList().block().size();
        // set the field null
        formateurs.setVille(null);

        // Create the Formateurs, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSpecialiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().collectList().block().size();
        // set the field null
        formateurs.setSpecialite(null);

        // Create the Formateurs, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFormateurs() {
        // Initialize the database
        formateursRepository.save(formateurs).block();

        // Get all the formateursList
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
            .value(hasItem(formateurs.getId().intValue()))
            .jsonPath("$.[*].nomFormateur")
            .value(hasItem(DEFAULT_NOM_FORMATEUR))
            .jsonPath("$.[*].prenomFormateur")
            .value(hasItem(DEFAULT_PRENOM_FORMATEUR))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].numb1")
            .value(hasItem(DEFAULT_NUMB_1))
            .jsonPath("$.[*].numb2")
            .value(hasItem(DEFAULT_NUMB_2))
            .jsonPath("$.[*].adresse")
            .value(hasItem(DEFAULT_ADRESSE))
            .jsonPath("$.[*].ville")
            .value(hasItem(DEFAULT_VILLE))
            .jsonPath("$.[*].specialite")
            .value(hasItem(DEFAULT_SPECIALITE));
    }

    @Test
    void getFormateurs() {
        // Initialize the database
        formateursRepository.save(formateurs).block();

        // Get the formateurs
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, formateurs.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(formateurs.getId().intValue()))
            .jsonPath("$.nomFormateur")
            .value(is(DEFAULT_NOM_FORMATEUR))
            .jsonPath("$.prenomFormateur")
            .value(is(DEFAULT_PRENOM_FORMATEUR))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.numb1")
            .value(is(DEFAULT_NUMB_1))
            .jsonPath("$.numb2")
            .value(is(DEFAULT_NUMB_2))
            .jsonPath("$.adresse")
            .value(is(DEFAULT_ADRESSE))
            .jsonPath("$.ville")
            .value(is(DEFAULT_VILLE))
            .jsonPath("$.specialite")
            .value(is(DEFAULT_SPECIALITE));
    }

    @Test
    void getNonExistingFormateurs() {
        // Get the formateurs
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFormateurs() throws Exception {
        // Initialize the database
        formateursRepository.save(formateurs).block();

        int databaseSizeBeforeUpdate = formateursRepository.findAll().collectList().block().size();

        // Update the formateurs
        Formateurs updatedFormateurs = formateursRepository.findById(formateurs.getId()).block();
        updatedFormateurs
            .nomFormateur(UPDATED_NOM_FORMATEUR)
            .prenomFormateur(UPDATED_PRENOM_FORMATEUR)
            .email(UPDATED_EMAIL)
            .numb1(UPDATED_NUMB_1)
            .numb2(UPDATED_NUMB_2)
            .adresse(UPDATED_ADRESSE)
            .ville(UPDATED_VILLE)
            .specialite(UPDATED_SPECIALITE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFormateurs.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFormateurs))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
        Formateurs testFormateurs = formateursList.get(formateursList.size() - 1);
        assertThat(testFormateurs.getNomFormateur()).isEqualTo(UPDATED_NOM_FORMATEUR);
        assertThat(testFormateurs.getPrenomFormateur()).isEqualTo(UPDATED_PRENOM_FORMATEUR);
        assertThat(testFormateurs.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFormateurs.getNumb1()).isEqualTo(UPDATED_NUMB_1);
        assertThat(testFormateurs.getNumb2()).isEqualTo(UPDATED_NUMB_2);
        assertThat(testFormateurs.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFormateurs.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFormateurs.getSpecialite()).isEqualTo(UPDATED_SPECIALITE);
    }

    @Test
    void putNonExistingFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().collectList().block().size();
        formateurs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, formateurs.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().collectList().block().size();
        formateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().collectList().block().size();
        formateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFormateursWithPatch() throws Exception {
        // Initialize the database
        formateursRepository.save(formateurs).block();

        int databaseSizeBeforeUpdate = formateursRepository.findAll().collectList().block().size();

        // Update the formateurs using partial update
        Formateurs partialUpdatedFormateurs = new Formateurs();
        partialUpdatedFormateurs.setId(formateurs.getId());

        partialUpdatedFormateurs
            .nomFormateur(UPDATED_NOM_FORMATEUR)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .ville(UPDATED_VILLE)
            .specialite(UPDATED_SPECIALITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFormateurs.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFormateurs))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
        Formateurs testFormateurs = formateursList.get(formateursList.size() - 1);
        assertThat(testFormateurs.getNomFormateur()).isEqualTo(UPDATED_NOM_FORMATEUR);
        assertThat(testFormateurs.getPrenomFormateur()).isEqualTo(DEFAULT_PRENOM_FORMATEUR);
        assertThat(testFormateurs.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFormateurs.getNumb1()).isEqualTo(DEFAULT_NUMB_1);
        assertThat(testFormateurs.getNumb2()).isEqualTo(DEFAULT_NUMB_2);
        assertThat(testFormateurs.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFormateurs.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFormateurs.getSpecialite()).isEqualTo(UPDATED_SPECIALITE);
    }

    @Test
    void fullUpdateFormateursWithPatch() throws Exception {
        // Initialize the database
        formateursRepository.save(formateurs).block();

        int databaseSizeBeforeUpdate = formateursRepository.findAll().collectList().block().size();

        // Update the formateurs using partial update
        Formateurs partialUpdatedFormateurs = new Formateurs();
        partialUpdatedFormateurs.setId(formateurs.getId());

        partialUpdatedFormateurs
            .nomFormateur(UPDATED_NOM_FORMATEUR)
            .prenomFormateur(UPDATED_PRENOM_FORMATEUR)
            .email(UPDATED_EMAIL)
            .numb1(UPDATED_NUMB_1)
            .numb2(UPDATED_NUMB_2)
            .adresse(UPDATED_ADRESSE)
            .ville(UPDATED_VILLE)
            .specialite(UPDATED_SPECIALITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFormateurs.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFormateurs))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
        Formateurs testFormateurs = formateursList.get(formateursList.size() - 1);
        assertThat(testFormateurs.getNomFormateur()).isEqualTo(UPDATED_NOM_FORMATEUR);
        assertThat(testFormateurs.getPrenomFormateur()).isEqualTo(UPDATED_PRENOM_FORMATEUR);
        assertThat(testFormateurs.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFormateurs.getNumb1()).isEqualTo(UPDATED_NUMB_1);
        assertThat(testFormateurs.getNumb2()).isEqualTo(UPDATED_NUMB_2);
        assertThat(testFormateurs.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFormateurs.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFormateurs.getSpecialite()).isEqualTo(UPDATED_SPECIALITE);
    }

    @Test
    void patchNonExistingFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().collectList().block().size();
        formateurs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, formateurs.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().collectList().block().size();
        formateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().collectList().block().size();
        formateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(formateurs))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFormateurs() {
        // Initialize the database
        formateursRepository.save(formateurs).block();

        int databaseSizeBeforeDelete = formateursRepository.findAll().collectList().block().size();

        // Delete the formateurs
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, formateurs.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Formateurs> formateursList = formateursRepository.findAll().collectList().block();
        assertThat(formateursList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
