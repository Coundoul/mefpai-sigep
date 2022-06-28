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
import sn.coundoul.gestion.patrimoine.domain.Fournisseur;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.FournisseurRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link FournisseurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class FournisseurResourceIT {

    private static final String DEFAULT_CODE_FOURNISSEUER = "AAAAAAAAAA";
    private static final String UPDATED_CODE_FOURNISSEUER = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_FOURNISSEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FOURNISSEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_FOURNISSEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_FOURNISSEUR = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.Masculin;
    private static final Sexe UPDATED_SEXE = Sexe.Feminin;

    private static final String DEFAULT_RAISON_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_1 = "AAAAAAAAAA";
    private static final String UPDATED_NUM_1 = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_2 = "AAAAAAAAAA";
    private static final String UPDATED_NUM_2 = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fournisseurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Fournisseur fournisseur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .codeFournisseuer(DEFAULT_CODE_FOURNISSEUER)
            .nomFournisseur(DEFAULT_NOM_FOURNISSEUR)
            .prenomFournisseur(DEFAULT_PRENOM_FOURNISSEUR)
            .sexe(DEFAULT_SEXE)
            .raisonSocial(DEFAULT_RAISON_SOCIAL)
            .adresse(DEFAULT_ADRESSE)
            .num1(DEFAULT_NUM_1)
            .num2(DEFAULT_NUM_2)
            .ville(DEFAULT_VILLE)
            .email(DEFAULT_EMAIL);
        return fournisseur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createUpdatedEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .codeFournisseuer(UPDATED_CODE_FOURNISSEUER)
            .nomFournisseur(UPDATED_NOM_FOURNISSEUR)
            .prenomFournisseur(UPDATED_PRENOM_FOURNISSEUR)
            .sexe(UPDATED_SEXE)
            .raisonSocial(UPDATED_RAISON_SOCIAL)
            .adresse(UPDATED_ADRESSE)
            .num1(UPDATED_NUM_1)
            .num2(UPDATED_NUM_2)
            .ville(UPDATED_VILLE)
            .email(UPDATED_EMAIL);
        return fournisseur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Fournisseur.class).block();
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
        fournisseur = createEntity(em);
    }

    @Test
    void createFournisseur() throws Exception {
        int databaseSizeBeforeCreate = fournisseurRepository.findAll().collectList().block().size();
        // Create the Fournisseur
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate + 1);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCodeFournisseuer()).isEqualTo(DEFAULT_CODE_FOURNISSEUER);
        assertThat(testFournisseur.getNomFournisseur()).isEqualTo(DEFAULT_NOM_FOURNISSEUR);
        assertThat(testFournisseur.getPrenomFournisseur()).isEqualTo(DEFAULT_PRENOM_FOURNISSEUR);
        assertThat(testFournisseur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testFournisseur.getRaisonSocial()).isEqualTo(DEFAULT_RAISON_SOCIAL);
        assertThat(testFournisseur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testFournisseur.getNum1()).isEqualTo(DEFAULT_NUM_1);
        assertThat(testFournisseur.getNum2()).isEqualTo(DEFAULT_NUM_2);
        assertThat(testFournisseur.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testFournisseur.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void createFournisseurWithExistingId() throws Exception {
        // Create the Fournisseur with an existing ID
        fournisseur.setId(1L);

        int databaseSizeBeforeCreate = fournisseurRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodeFournisseuerIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().collectList().block().size();
        // set the field null
        fournisseur.setCodeFournisseuer(null);

        // Create the Fournisseur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNomFournisseurIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().collectList().block().size();
        // set the field null
        fournisseur.setNomFournisseur(null);

        // Create the Fournisseur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomFournisseurIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().collectList().block().size();
        // set the field null
        fournisseur.setPrenomFournisseur(null);

        // Create the Fournisseur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().collectList().block().size();
        // set the field null
        fournisseur.setSexe(null);

        // Create the Fournisseur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRaisonSocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().collectList().block().size();
        // set the field null
        fournisseur.setRaisonSocial(null);

        // Create the Fournisseur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().collectList().block().size();
        // set the field null
        fournisseur.setAdresse(null);

        // Create the Fournisseur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNum1IsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().collectList().block().size();
        // set the field null
        fournisseur.setNum1(null);

        // Create the Fournisseur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().collectList().block().size();
        // set the field null
        fournisseur.setVille(null);

        // Create the Fournisseur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().collectList().block().size();
        // set the field null
        fournisseur.setEmail(null);

        // Create the Fournisseur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFournisseurs() {
        // Initialize the database
        fournisseurRepository.save(fournisseur).block();

        // Get all the fournisseurList
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
            .value(hasItem(fournisseur.getId().intValue()))
            .jsonPath("$.[*].codeFournisseuer")
            .value(hasItem(DEFAULT_CODE_FOURNISSEUER))
            .jsonPath("$.[*].nomFournisseur")
            .value(hasItem(DEFAULT_NOM_FOURNISSEUR))
            .jsonPath("$.[*].prenomFournisseur")
            .value(hasItem(DEFAULT_PRENOM_FOURNISSEUR))
            .jsonPath("$.[*].sexe")
            .value(hasItem(DEFAULT_SEXE.toString()))
            .jsonPath("$.[*].raisonSocial")
            .value(hasItem(DEFAULT_RAISON_SOCIAL))
            .jsonPath("$.[*].adresse")
            .value(hasItem(DEFAULT_ADRESSE))
            .jsonPath("$.[*].num1")
            .value(hasItem(DEFAULT_NUM_1))
            .jsonPath("$.[*].num2")
            .value(hasItem(DEFAULT_NUM_2))
            .jsonPath("$.[*].ville")
            .value(hasItem(DEFAULT_VILLE))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL));
    }

    @Test
    void getFournisseur() {
        // Initialize the database
        fournisseurRepository.save(fournisseur).block();

        // Get the fournisseur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, fournisseur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(fournisseur.getId().intValue()))
            .jsonPath("$.codeFournisseuer")
            .value(is(DEFAULT_CODE_FOURNISSEUER))
            .jsonPath("$.nomFournisseur")
            .value(is(DEFAULT_NOM_FOURNISSEUR))
            .jsonPath("$.prenomFournisseur")
            .value(is(DEFAULT_PRENOM_FOURNISSEUR))
            .jsonPath("$.sexe")
            .value(is(DEFAULT_SEXE.toString()))
            .jsonPath("$.raisonSocial")
            .value(is(DEFAULT_RAISON_SOCIAL))
            .jsonPath("$.adresse")
            .value(is(DEFAULT_ADRESSE))
            .jsonPath("$.num1")
            .value(is(DEFAULT_NUM_1))
            .jsonPath("$.num2")
            .value(is(DEFAULT_NUM_2))
            .jsonPath("$.ville")
            .value(is(DEFAULT_VILLE))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL));
    }

    @Test
    void getNonExistingFournisseur() {
        // Get the fournisseur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.save(fournisseur).block();

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().collectList().block().size();

        // Update the fournisseur
        Fournisseur updatedFournisseur = fournisseurRepository.findById(fournisseur.getId()).block();
        updatedFournisseur
            .codeFournisseuer(UPDATED_CODE_FOURNISSEUER)
            .nomFournisseur(UPDATED_NOM_FOURNISSEUR)
            .prenomFournisseur(UPDATED_PRENOM_FOURNISSEUR)
            .sexe(UPDATED_SEXE)
            .raisonSocial(UPDATED_RAISON_SOCIAL)
            .adresse(UPDATED_ADRESSE)
            .num1(UPDATED_NUM_1)
            .num2(UPDATED_NUM_2)
            .ville(UPDATED_VILLE)
            .email(UPDATED_EMAIL);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFournisseur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFournisseur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCodeFournisseuer()).isEqualTo(UPDATED_CODE_FOURNISSEUER);
        assertThat(testFournisseur.getNomFournisseur()).isEqualTo(UPDATED_NOM_FOURNISSEUR);
        assertThat(testFournisseur.getPrenomFournisseur()).isEqualTo(UPDATED_PRENOM_FOURNISSEUR);
        assertThat(testFournisseur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testFournisseur.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
        assertThat(testFournisseur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFournisseur.getNum1()).isEqualTo(UPDATED_NUM_1);
        assertThat(testFournisseur.getNum2()).isEqualTo(UPDATED_NUM_2);
        assertThat(testFournisseur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFournisseur.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void putNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().collectList().block().size();
        fournisseur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fournisseur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().collectList().block().size();
        fournisseur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().collectList().block().size();
        fournisseur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFournisseurWithPatch() throws Exception {
        // Initialize the database
        fournisseurRepository.save(fournisseur).block();

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().collectList().block().size();

        // Update the fournisseur using partial update
        Fournisseur partialUpdatedFournisseur = new Fournisseur();
        partialUpdatedFournisseur.setId(fournisseur.getId());

        partialUpdatedFournisseur
            .codeFournisseuer(UPDATED_CODE_FOURNISSEUER)
            .nomFournisseur(UPDATED_NOM_FOURNISSEUR)
            .sexe(UPDATED_SEXE)
            .raisonSocial(UPDATED_RAISON_SOCIAL)
            .adresse(UPDATED_ADRESSE)
            .num2(UPDATED_NUM_2)
            .email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFournisseur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFournisseur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCodeFournisseuer()).isEqualTo(UPDATED_CODE_FOURNISSEUER);
        assertThat(testFournisseur.getNomFournisseur()).isEqualTo(UPDATED_NOM_FOURNISSEUR);
        assertThat(testFournisseur.getPrenomFournisseur()).isEqualTo(DEFAULT_PRENOM_FOURNISSEUR);
        assertThat(testFournisseur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testFournisseur.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
        assertThat(testFournisseur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFournisseur.getNum1()).isEqualTo(DEFAULT_NUM_1);
        assertThat(testFournisseur.getNum2()).isEqualTo(UPDATED_NUM_2);
        assertThat(testFournisseur.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testFournisseur.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void fullUpdateFournisseurWithPatch() throws Exception {
        // Initialize the database
        fournisseurRepository.save(fournisseur).block();

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().collectList().block().size();

        // Update the fournisseur using partial update
        Fournisseur partialUpdatedFournisseur = new Fournisseur();
        partialUpdatedFournisseur.setId(fournisseur.getId());

        partialUpdatedFournisseur
            .codeFournisseuer(UPDATED_CODE_FOURNISSEUER)
            .nomFournisseur(UPDATED_NOM_FOURNISSEUR)
            .prenomFournisseur(UPDATED_PRENOM_FOURNISSEUR)
            .sexe(UPDATED_SEXE)
            .raisonSocial(UPDATED_RAISON_SOCIAL)
            .adresse(UPDATED_ADRESSE)
            .num1(UPDATED_NUM_1)
            .num2(UPDATED_NUM_2)
            .ville(UPDATED_VILLE)
            .email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFournisseur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFournisseur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCodeFournisseuer()).isEqualTo(UPDATED_CODE_FOURNISSEUER);
        assertThat(testFournisseur.getNomFournisseur()).isEqualTo(UPDATED_NOM_FOURNISSEUR);
        assertThat(testFournisseur.getPrenomFournisseur()).isEqualTo(UPDATED_PRENOM_FOURNISSEUR);
        assertThat(testFournisseur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testFournisseur.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
        assertThat(testFournisseur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFournisseur.getNum1()).isEqualTo(UPDATED_NUM_1);
        assertThat(testFournisseur.getNum2()).isEqualTo(UPDATED_NUM_2);
        assertThat(testFournisseur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFournisseur.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void patchNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().collectList().block().size();
        fournisseur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, fournisseur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().collectList().block().size();
        fournisseur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().collectList().block().size();
        fournisseur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(fournisseur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFournisseur() {
        // Initialize the database
        fournisseurRepository.save(fournisseur).block();

        int databaseSizeBeforeDelete = fournisseurRepository.findAll().collectList().block().size();

        // Delete the fournisseur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, fournisseur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll().collectList().block();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
