package sn.coundoul.gestion.equipement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.coundoul.gestion.equipement.IntegrationTest;
import sn.coundoul.gestion.equipement.domain.Fournisseur;
import sn.coundoul.gestion.equipement.domain.enumeration.Sexe;
import sn.coundoul.gestion.equipement.repository.FournisseurRepository;

/**
 * Integration tests for the {@link FournisseurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restFournisseurMockMvc;

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

    @BeforeEach
    public void initTest() {
        fournisseur = createEntity(em);
    }

    @Test
    @Transactional
    void createFournisseur() throws Exception {
        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();
        // Create the Fournisseur
        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isCreated());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
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
    @Transactional
    void createFournisseurWithExistingId() throws Exception {
        // Create the Fournisseur with an existing ID
        fournisseur.setId(1L);

        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeFournisseuerIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setCodeFournisseuer(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomFournisseurIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setNomFournisseur(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomFournisseurIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setPrenomFournisseur(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setSexe(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRaisonSocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setRaisonSocial(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setAdresse(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNum1IsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setNum1(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setVille(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setEmail(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFournisseurs() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeFournisseuer").value(hasItem(DEFAULT_CODE_FOURNISSEUER)))
            .andExpect(jsonPath("$.[*].nomFournisseur").value(hasItem(DEFAULT_NOM_FOURNISSEUR)))
            .andExpect(jsonPath("$.[*].prenomFournisseur").value(hasItem(DEFAULT_PRENOM_FOURNISSEUR)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].raisonSocial").value(hasItem(DEFAULT_RAISON_SOCIAL)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].num1").value(hasItem(DEFAULT_NUM_1)))
            .andExpect(jsonPath("$.[*].num2").value(hasItem(DEFAULT_NUM_2)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get the fournisseur
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL_ID, fournisseur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fournisseur.getId().intValue()))
            .andExpect(jsonPath("$.codeFournisseuer").value(DEFAULT_CODE_FOURNISSEUER))
            .andExpect(jsonPath("$.nomFournisseur").value(DEFAULT_NOM_FOURNISSEUR))
            .andExpect(jsonPath("$.prenomFournisseur").value(DEFAULT_PRENOM_FOURNISSEUR))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.raisonSocial").value(DEFAULT_RAISON_SOCIAL))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.num1").value(DEFAULT_NUM_1))
            .andExpect(jsonPath("$.num2").value(DEFAULT_NUM_2))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingFournisseur() throws Exception {
        // Get the fournisseur
        restFournisseurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Update the fournisseur
        Fournisseur updatedFournisseur = fournisseurRepository.findById(fournisseur.getId()).get();
        // Disconnect from session so that the updates on updatedFournisseur are not directly saved in db
        em.detach(updatedFournisseur);
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

        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFournisseur.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFournisseur))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
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
    @Transactional
    void putNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fournisseur.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFournisseurWithPatch() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

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

        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFournisseur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFournisseur))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
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
    @Transactional
    void fullUpdateFournisseurWithPatch() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

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

        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFournisseur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFournisseur))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
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
    @Transactional
    void patchNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fournisseur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fournisseur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeDelete = fournisseurRepository.findAll().size();

        // Delete the fournisseur
        restFournisseurMockMvc
            .perform(delete(ENTITY_API_URL_ID, fournisseur.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
