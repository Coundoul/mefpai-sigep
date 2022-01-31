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
import sn.coundoul.gestion.patrimoine.domain.UtilisateurFinal;
import sn.coundoul.gestion.patrimoine.repository.UtilisateurFinalRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link UtilisateurFinalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class UtilisateurFinalResourceIT {

    private static final String DEFAULT_NOM_UTILISATEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_UTILISATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_UTILISATEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_UTILISATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_INSTITUTIONNEL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_INSTITUTIONNEL = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_SEXE = "AAAAAAAAAA";
    private static final String UPDATED_SEXE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTEMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_DEP = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_DEP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/utilisateur-finals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UtilisateurFinalRepository utilisateurFinalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UtilisateurFinal utilisateurFinal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtilisateurFinal createEntity(EntityManager em) {
        UtilisateurFinal utilisateurFinal = new UtilisateurFinal()
            .nomUtilisateur(DEFAULT_NOM_UTILISATEUR)
            .prenomUtilisateur(DEFAULT_PRENOM_UTILISATEUR)
            .emailInstitutionnel(DEFAULT_EMAIL_INSTITUTIONNEL)
            .mobile(DEFAULT_MOBILE)
            .sexe(DEFAULT_SEXE)
            .departement(DEFAULT_DEPARTEMENT)
            .serviceDep(DEFAULT_SERVICE_DEP);
        return utilisateurFinal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtilisateurFinal createUpdatedEntity(EntityManager em) {
        UtilisateurFinal utilisateurFinal = new UtilisateurFinal()
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .prenomUtilisateur(UPDATED_PRENOM_UTILISATEUR)
            .emailInstitutionnel(UPDATED_EMAIL_INSTITUTIONNEL)
            .mobile(UPDATED_MOBILE)
            .sexe(UPDATED_SEXE)
            .departement(UPDATED_DEPARTEMENT)
            .serviceDep(UPDATED_SERVICE_DEP);
        return utilisateurFinal;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UtilisateurFinal.class).block();
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
        utilisateurFinal = createEntity(em);
    }

    @Test
    void createUtilisateurFinal() throws Exception {
        int databaseSizeBeforeCreate = utilisateurFinalRepository.findAll().collectList().block().size();
        // Create the UtilisateurFinal
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeCreate + 1);
        UtilisateurFinal testUtilisateurFinal = utilisateurFinalList.get(utilisateurFinalList.size() - 1);
        assertThat(testUtilisateurFinal.getNomUtilisateur()).isEqualTo(DEFAULT_NOM_UTILISATEUR);
        assertThat(testUtilisateurFinal.getPrenomUtilisateur()).isEqualTo(DEFAULT_PRENOM_UTILISATEUR);
        assertThat(testUtilisateurFinal.getEmailInstitutionnel()).isEqualTo(DEFAULT_EMAIL_INSTITUTIONNEL);
        assertThat(testUtilisateurFinal.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testUtilisateurFinal.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testUtilisateurFinal.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testUtilisateurFinal.getServiceDep()).isEqualTo(DEFAULT_SERVICE_DEP);
    }

    @Test
    void createUtilisateurFinalWithExistingId() throws Exception {
        // Create the UtilisateurFinal with an existing ID
        utilisateurFinal.setId(1L);

        int databaseSizeBeforeCreate = utilisateurFinalRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomUtilisateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurFinalRepository.findAll().collectList().block().size();
        // set the field null
        utilisateurFinal.setNomUtilisateur(null);

        // Create the UtilisateurFinal, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomUtilisateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurFinalRepository.findAll().collectList().block().size();
        // set the field null
        utilisateurFinal.setPrenomUtilisateur(null);

        // Create the UtilisateurFinal, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailInstitutionnelIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurFinalRepository.findAll().collectList().block().size();
        // set the field null
        utilisateurFinal.setEmailInstitutionnel(null);

        // Create the UtilisateurFinal, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurFinalRepository.findAll().collectList().block().size();
        // set the field null
        utilisateurFinal.setMobile(null);

        // Create the UtilisateurFinal, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurFinalRepository.findAll().collectList().block().size();
        // set the field null
        utilisateurFinal.setSexe(null);

        // Create the UtilisateurFinal, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUtilisateurFinals() {
        // Initialize the database
        utilisateurFinalRepository.save(utilisateurFinal).block();

        // Get all the utilisateurFinalList
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
            .value(hasItem(utilisateurFinal.getId().intValue()))
            .jsonPath("$.[*].nomUtilisateur")
            .value(hasItem(DEFAULT_NOM_UTILISATEUR))
            .jsonPath("$.[*].prenomUtilisateur")
            .value(hasItem(DEFAULT_PRENOM_UTILISATEUR))
            .jsonPath("$.[*].emailInstitutionnel")
            .value(hasItem(DEFAULT_EMAIL_INSTITUTIONNEL))
            .jsonPath("$.[*].mobile")
            .value(hasItem(DEFAULT_MOBILE))
            .jsonPath("$.[*].sexe")
            .value(hasItem(DEFAULT_SEXE))
            .jsonPath("$.[*].departement")
            .value(hasItem(DEFAULT_DEPARTEMENT))
            .jsonPath("$.[*].serviceDep")
            .value(hasItem(DEFAULT_SERVICE_DEP));
    }

    @Test
    void getUtilisateurFinal() {
        // Initialize the database
        utilisateurFinalRepository.save(utilisateurFinal).block();

        // Get the utilisateurFinal
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, utilisateurFinal.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(utilisateurFinal.getId().intValue()))
            .jsonPath("$.nomUtilisateur")
            .value(is(DEFAULT_NOM_UTILISATEUR))
            .jsonPath("$.prenomUtilisateur")
            .value(is(DEFAULT_PRENOM_UTILISATEUR))
            .jsonPath("$.emailInstitutionnel")
            .value(is(DEFAULT_EMAIL_INSTITUTIONNEL))
            .jsonPath("$.mobile")
            .value(is(DEFAULT_MOBILE))
            .jsonPath("$.sexe")
            .value(is(DEFAULT_SEXE))
            .jsonPath("$.departement")
            .value(is(DEFAULT_DEPARTEMENT))
            .jsonPath("$.serviceDep")
            .value(is(DEFAULT_SERVICE_DEP));
    }

    @Test
    void getNonExistingUtilisateurFinal() {
        // Get the utilisateurFinal
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewUtilisateurFinal() throws Exception {
        // Initialize the database
        utilisateurFinalRepository.save(utilisateurFinal).block();

        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().collectList().block().size();

        // Update the utilisateurFinal
        UtilisateurFinal updatedUtilisateurFinal = utilisateurFinalRepository.findById(utilisateurFinal.getId()).block();
        updatedUtilisateurFinal
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .prenomUtilisateur(UPDATED_PRENOM_UTILISATEUR)
            .emailInstitutionnel(UPDATED_EMAIL_INSTITUTIONNEL)
            .mobile(UPDATED_MOBILE)
            .sexe(UPDATED_SEXE)
            .departement(UPDATED_DEPARTEMENT)
            .serviceDep(UPDATED_SERVICE_DEP);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedUtilisateurFinal.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedUtilisateurFinal))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
        UtilisateurFinal testUtilisateurFinal = utilisateurFinalList.get(utilisateurFinalList.size() - 1);
        assertThat(testUtilisateurFinal.getNomUtilisateur()).isEqualTo(UPDATED_NOM_UTILISATEUR);
        assertThat(testUtilisateurFinal.getPrenomUtilisateur()).isEqualTo(UPDATED_PRENOM_UTILISATEUR);
        assertThat(testUtilisateurFinal.getEmailInstitutionnel()).isEqualTo(UPDATED_EMAIL_INSTITUTIONNEL);
        assertThat(testUtilisateurFinal.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testUtilisateurFinal.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testUtilisateurFinal.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testUtilisateurFinal.getServiceDep()).isEqualTo(UPDATED_SERVICE_DEP);
    }

    @Test
    void putNonExistingUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().collectList().block().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, utilisateurFinal.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().collectList().block().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().collectList().block().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUtilisateurFinalWithPatch() throws Exception {
        // Initialize the database
        utilisateurFinalRepository.save(utilisateurFinal).block();

        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().collectList().block().size();

        // Update the utilisateurFinal using partial update
        UtilisateurFinal partialUpdatedUtilisateurFinal = new UtilisateurFinal();
        partialUpdatedUtilisateurFinal.setId(utilisateurFinal.getId());

        partialUpdatedUtilisateurFinal
            .emailInstitutionnel(UPDATED_EMAIL_INSTITUTIONNEL)
            .mobile(UPDATED_MOBILE)
            .sexe(UPDATED_SEXE)
            .departement(UPDATED_DEPARTEMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUtilisateurFinal.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisateurFinal))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
        UtilisateurFinal testUtilisateurFinal = utilisateurFinalList.get(utilisateurFinalList.size() - 1);
        assertThat(testUtilisateurFinal.getNomUtilisateur()).isEqualTo(DEFAULT_NOM_UTILISATEUR);
        assertThat(testUtilisateurFinal.getPrenomUtilisateur()).isEqualTo(DEFAULT_PRENOM_UTILISATEUR);
        assertThat(testUtilisateurFinal.getEmailInstitutionnel()).isEqualTo(UPDATED_EMAIL_INSTITUTIONNEL);
        assertThat(testUtilisateurFinal.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testUtilisateurFinal.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testUtilisateurFinal.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testUtilisateurFinal.getServiceDep()).isEqualTo(DEFAULT_SERVICE_DEP);
    }

    @Test
    void fullUpdateUtilisateurFinalWithPatch() throws Exception {
        // Initialize the database
        utilisateurFinalRepository.save(utilisateurFinal).block();

        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().collectList().block().size();

        // Update the utilisateurFinal using partial update
        UtilisateurFinal partialUpdatedUtilisateurFinal = new UtilisateurFinal();
        partialUpdatedUtilisateurFinal.setId(utilisateurFinal.getId());

        partialUpdatedUtilisateurFinal
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .prenomUtilisateur(UPDATED_PRENOM_UTILISATEUR)
            .emailInstitutionnel(UPDATED_EMAIL_INSTITUTIONNEL)
            .mobile(UPDATED_MOBILE)
            .sexe(UPDATED_SEXE)
            .departement(UPDATED_DEPARTEMENT)
            .serviceDep(UPDATED_SERVICE_DEP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUtilisateurFinal.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisateurFinal))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
        UtilisateurFinal testUtilisateurFinal = utilisateurFinalList.get(utilisateurFinalList.size() - 1);
        assertThat(testUtilisateurFinal.getNomUtilisateur()).isEqualTo(UPDATED_NOM_UTILISATEUR);
        assertThat(testUtilisateurFinal.getPrenomUtilisateur()).isEqualTo(UPDATED_PRENOM_UTILISATEUR);
        assertThat(testUtilisateurFinal.getEmailInstitutionnel()).isEqualTo(UPDATED_EMAIL_INSTITUTIONNEL);
        assertThat(testUtilisateurFinal.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testUtilisateurFinal.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testUtilisateurFinal.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testUtilisateurFinal.getServiceDep()).isEqualTo(UPDATED_SERVICE_DEP);
    }

    @Test
    void patchNonExistingUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().collectList().block().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, utilisateurFinal.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().collectList().block().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().collectList().block().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUtilisateurFinal() {
        // Initialize the database
        utilisateurFinalRepository.save(utilisateurFinal).block();

        int databaseSizeBeforeDelete = utilisateurFinalRepository.findAll().collectList().block().size();

        // Delete the utilisateurFinal
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, utilisateurFinal.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll().collectList().block();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
