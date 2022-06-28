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
import sn.coundoul.gestion.patrimoine.domain.Intervenant;
import sn.coundoul.gestion.patrimoine.domain.enumeration.TypeMaitre;
import sn.coundoul.gestion.patrimoine.repository.IntervenantRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link IntervenantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class IntervenantResourceIT {

    private static final String DEFAULT_NOM_INTERVENANT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_INTERVENANT = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_INTERVENANT = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_INTERVENANT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_PROFESSIONNEL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_PROFESSIONNEL = "BBBBBBBBBB";

    private static final String DEFAULT_RAISON_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIAL = "BBBBBBBBBB";

    private static final TypeMaitre DEFAULT_MAITRE = TypeMaitre.MaitreOuvrage;
    private static final TypeMaitre UPDATED_MAITRE = TypeMaitre.MaitreOeuvre;

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/intervenants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IntervenantRepository intervenantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Intervenant intervenant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervenant createEntity(EntityManager em) {
        Intervenant intervenant = new Intervenant()
            .nomIntervenant(DEFAULT_NOM_INTERVENANT)
            .prenomIntervenant(DEFAULT_PRENOM_INTERVENANT)
            .emailProfessionnel(DEFAULT_EMAIL_PROFESSIONNEL)
            .raisonSocial(DEFAULT_RAISON_SOCIAL)
            .maitre(DEFAULT_MAITRE)
            .role(DEFAULT_ROLE);
        return intervenant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervenant createUpdatedEntity(EntityManager em) {
        Intervenant intervenant = new Intervenant()
            .nomIntervenant(UPDATED_NOM_INTERVENANT)
            .prenomIntervenant(UPDATED_PRENOM_INTERVENANT)
            .emailProfessionnel(UPDATED_EMAIL_PROFESSIONNEL)
            .raisonSocial(UPDATED_RAISON_SOCIAL)
            .maitre(UPDATED_MAITRE)
            .role(UPDATED_ROLE);
        return intervenant;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Intervenant.class).block();
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
        intervenant = createEntity(em);
    }

    @Test
    void createIntervenant() throws Exception {
        int databaseSizeBeforeCreate = intervenantRepository.findAll().collectList().block().size();
        // Create the Intervenant
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeCreate + 1);
        Intervenant testIntervenant = intervenantList.get(intervenantList.size() - 1);
        assertThat(testIntervenant.getNomIntervenant()).isEqualTo(DEFAULT_NOM_INTERVENANT);
        assertThat(testIntervenant.getPrenomIntervenant()).isEqualTo(DEFAULT_PRENOM_INTERVENANT);
        assertThat(testIntervenant.getEmailProfessionnel()).isEqualTo(DEFAULT_EMAIL_PROFESSIONNEL);
        assertThat(testIntervenant.getRaisonSocial()).isEqualTo(DEFAULT_RAISON_SOCIAL);
        assertThat(testIntervenant.getMaitre()).isEqualTo(DEFAULT_MAITRE);
        assertThat(testIntervenant.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    void createIntervenantWithExistingId() throws Exception {
        // Create the Intervenant with an existing ID
        intervenant.setId(1L);

        int databaseSizeBeforeCreate = intervenantRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomIntervenantIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().collectList().block().size();
        // set the field null
        intervenant.setNomIntervenant(null);

        // Create the Intervenant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomIntervenantIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().collectList().block().size();
        // set the field null
        intervenant.setPrenomIntervenant(null);

        // Create the Intervenant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailProfessionnelIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().collectList().block().size();
        // set the field null
        intervenant.setEmailProfessionnel(null);

        // Create the Intervenant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRaisonSocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().collectList().block().size();
        // set the field null
        intervenant.setRaisonSocial(null);

        // Create the Intervenant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMaitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().collectList().block().size();
        // set the field null
        intervenant.setMaitre(null);

        // Create the Intervenant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().collectList().block().size();
        // set the field null
        intervenant.setRole(null);

        // Create the Intervenant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllIntervenants() {
        // Initialize the database
        intervenantRepository.save(intervenant).block();

        // Get all the intervenantList
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
            .value(hasItem(intervenant.getId().intValue()))
            .jsonPath("$.[*].nomIntervenant")
            .value(hasItem(DEFAULT_NOM_INTERVENANT))
            .jsonPath("$.[*].prenomIntervenant")
            .value(hasItem(DEFAULT_PRENOM_INTERVENANT))
            .jsonPath("$.[*].emailProfessionnel")
            .value(hasItem(DEFAULT_EMAIL_PROFESSIONNEL))
            .jsonPath("$.[*].raisonSocial")
            .value(hasItem(DEFAULT_RAISON_SOCIAL))
            .jsonPath("$.[*].maitre")
            .value(hasItem(DEFAULT_MAITRE.toString()))
            .jsonPath("$.[*].role")
            .value(hasItem(DEFAULT_ROLE));
    }

    @Test
    void getIntervenant() {
        // Initialize the database
        intervenantRepository.save(intervenant).block();

        // Get the intervenant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, intervenant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(intervenant.getId().intValue()))
            .jsonPath("$.nomIntervenant")
            .value(is(DEFAULT_NOM_INTERVENANT))
            .jsonPath("$.prenomIntervenant")
            .value(is(DEFAULT_PRENOM_INTERVENANT))
            .jsonPath("$.emailProfessionnel")
            .value(is(DEFAULT_EMAIL_PROFESSIONNEL))
            .jsonPath("$.raisonSocial")
            .value(is(DEFAULT_RAISON_SOCIAL))
            .jsonPath("$.maitre")
            .value(is(DEFAULT_MAITRE.toString()))
            .jsonPath("$.role")
            .value(is(DEFAULT_ROLE));
    }

    @Test
    void getNonExistingIntervenant() {
        // Get the intervenant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIntervenant() throws Exception {
        // Initialize the database
        intervenantRepository.save(intervenant).block();

        int databaseSizeBeforeUpdate = intervenantRepository.findAll().collectList().block().size();

        // Update the intervenant
        Intervenant updatedIntervenant = intervenantRepository.findById(intervenant.getId()).block();
        updatedIntervenant
            .nomIntervenant(UPDATED_NOM_INTERVENANT)
            .prenomIntervenant(UPDATED_PRENOM_INTERVENANT)
            .emailProfessionnel(UPDATED_EMAIL_PROFESSIONNEL)
            .raisonSocial(UPDATED_RAISON_SOCIAL)
            .maitre(UPDATED_MAITRE)
            .role(UPDATED_ROLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIntervenant.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIntervenant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
        Intervenant testIntervenant = intervenantList.get(intervenantList.size() - 1);
        assertThat(testIntervenant.getNomIntervenant()).isEqualTo(UPDATED_NOM_INTERVENANT);
        assertThat(testIntervenant.getPrenomIntervenant()).isEqualTo(UPDATED_PRENOM_INTERVENANT);
        assertThat(testIntervenant.getEmailProfessionnel()).isEqualTo(UPDATED_EMAIL_PROFESSIONNEL);
        assertThat(testIntervenant.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
        assertThat(testIntervenant.getMaitre()).isEqualTo(UPDATED_MAITRE);
        assertThat(testIntervenant.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    void putNonExistingIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().collectList().block().size();
        intervenant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, intervenant.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().collectList().block().size();
        intervenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().collectList().block().size();
        intervenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIntervenantWithPatch() throws Exception {
        // Initialize the database
        intervenantRepository.save(intervenant).block();

        int databaseSizeBeforeUpdate = intervenantRepository.findAll().collectList().block().size();

        // Update the intervenant using partial update
        Intervenant partialUpdatedIntervenant = new Intervenant();
        partialUpdatedIntervenant.setId(intervenant.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntervenant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervenant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
        Intervenant testIntervenant = intervenantList.get(intervenantList.size() - 1);
        assertThat(testIntervenant.getNomIntervenant()).isEqualTo(DEFAULT_NOM_INTERVENANT);
        assertThat(testIntervenant.getPrenomIntervenant()).isEqualTo(DEFAULT_PRENOM_INTERVENANT);
        assertThat(testIntervenant.getEmailProfessionnel()).isEqualTo(DEFAULT_EMAIL_PROFESSIONNEL);
        assertThat(testIntervenant.getRaisonSocial()).isEqualTo(DEFAULT_RAISON_SOCIAL);
        assertThat(testIntervenant.getMaitre()).isEqualTo(DEFAULT_MAITRE);
        assertThat(testIntervenant.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    void fullUpdateIntervenantWithPatch() throws Exception {
        // Initialize the database
        intervenantRepository.save(intervenant).block();

        int databaseSizeBeforeUpdate = intervenantRepository.findAll().collectList().block().size();

        // Update the intervenant using partial update
        Intervenant partialUpdatedIntervenant = new Intervenant();
        partialUpdatedIntervenant.setId(intervenant.getId());

        partialUpdatedIntervenant
            .nomIntervenant(UPDATED_NOM_INTERVENANT)
            .prenomIntervenant(UPDATED_PRENOM_INTERVENANT)
            .emailProfessionnel(UPDATED_EMAIL_PROFESSIONNEL)
            .raisonSocial(UPDATED_RAISON_SOCIAL)
            .maitre(UPDATED_MAITRE)
            .role(UPDATED_ROLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntervenant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervenant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
        Intervenant testIntervenant = intervenantList.get(intervenantList.size() - 1);
        assertThat(testIntervenant.getNomIntervenant()).isEqualTo(UPDATED_NOM_INTERVENANT);
        assertThat(testIntervenant.getPrenomIntervenant()).isEqualTo(UPDATED_PRENOM_INTERVENANT);
        assertThat(testIntervenant.getEmailProfessionnel()).isEqualTo(UPDATED_EMAIL_PROFESSIONNEL);
        assertThat(testIntervenant.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
        assertThat(testIntervenant.getMaitre()).isEqualTo(UPDATED_MAITRE);
        assertThat(testIntervenant.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    void patchNonExistingIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().collectList().block().size();
        intervenant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, intervenant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().collectList().block().size();
        intervenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().collectList().block().size();
        intervenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(intervenant))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIntervenant() {
        // Initialize the database
        intervenantRepository.save(intervenant).block();

        int databaseSizeBeforeDelete = intervenantRepository.findAll().collectList().block().size();

        // Delete the intervenant
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, intervenant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Intervenant> intervenantList = intervenantRepository.findAll().collectList().block();
        assertThat(intervenantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
