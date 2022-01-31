package sn.coundoul.gestion.infrastructure.web.rest;

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
import sn.coundoul.gestion.infrastructure.IntegrationTest;
import sn.coundoul.gestion.infrastructure.domain.Formateurs;
import sn.coundoul.gestion.infrastructure.repository.FormateursRepository;

/**
 * Integration tests for the {@link FormateursResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restFormateursMockMvc;

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

    @BeforeEach
    public void initTest() {
        formateurs = createEntity(em);
    }

    @Test
    @Transactional
    void createFormateurs() throws Exception {
        int databaseSizeBeforeCreate = formateursRepository.findAll().size();
        // Create the Formateurs
        restFormateursMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isCreated());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
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
    @Transactional
    void createFormateursWithExistingId() throws Exception {
        // Create the Formateurs with an existing ID
        formateurs.setId(1L);

        int databaseSizeBeforeCreate = formateursRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormateursMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFormateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().size();
        // set the field null
        formateurs.setNomFormateur(null);

        // Create the Formateurs, which fails.

        restFormateursMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomFormateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().size();
        // set the field null
        formateurs.setPrenomFormateur(null);

        // Create the Formateurs, which fails.

        restFormateursMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().size();
        // set the field null
        formateurs.setEmail(null);

        // Create the Formateurs, which fails.

        restFormateursMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumb1IsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().size();
        // set the field null
        formateurs.setNumb1(null);

        // Create the Formateurs, which fails.

        restFormateursMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().size();
        // set the field null
        formateurs.setAdresse(null);

        // Create the Formateurs, which fails.

        restFormateursMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().size();
        // set the field null
        formateurs.setVille(null);

        // Create the Formateurs, which fails.

        restFormateursMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecialiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateursRepository.findAll().size();
        // set the field null
        formateurs.setSpecialite(null);

        // Create the Formateurs, which fails.

        restFormateursMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormateurs() throws Exception {
        // Initialize the database
        formateursRepository.saveAndFlush(formateurs);

        // Get all the formateursList
        restFormateursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formateurs.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFormateur").value(hasItem(DEFAULT_NOM_FORMATEUR)))
            .andExpect(jsonPath("$.[*].prenomFormateur").value(hasItem(DEFAULT_PRENOM_FORMATEUR)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].numb1").value(hasItem(DEFAULT_NUMB_1)))
            .andExpect(jsonPath("$.[*].numb2").value(hasItem(DEFAULT_NUMB_2)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE)));
    }

    @Test
    @Transactional
    void getFormateurs() throws Exception {
        // Initialize the database
        formateursRepository.saveAndFlush(formateurs);

        // Get the formateurs
        restFormateursMockMvc
            .perform(get(ENTITY_API_URL_ID, formateurs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formateurs.getId().intValue()))
            .andExpect(jsonPath("$.nomFormateur").value(DEFAULT_NOM_FORMATEUR))
            .andExpect(jsonPath("$.prenomFormateur").value(DEFAULT_PRENOM_FORMATEUR))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.numb1").value(DEFAULT_NUMB_1))
            .andExpect(jsonPath("$.numb2").value(DEFAULT_NUMB_2))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.specialite").value(DEFAULT_SPECIALITE));
    }

    @Test
    @Transactional
    void getNonExistingFormateurs() throws Exception {
        // Get the formateurs
        restFormateursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormateurs() throws Exception {
        // Initialize the database
        formateursRepository.saveAndFlush(formateurs);

        int databaseSizeBeforeUpdate = formateursRepository.findAll().size();

        // Update the formateurs
        Formateurs updatedFormateurs = formateursRepository.findById(formateurs.getId()).get();
        // Disconnect from session so that the updates on updatedFormateurs are not directly saved in db
        em.detach(updatedFormateurs);
        updatedFormateurs
            .nomFormateur(UPDATED_NOM_FORMATEUR)
            .prenomFormateur(UPDATED_PRENOM_FORMATEUR)
            .email(UPDATED_EMAIL)
            .numb1(UPDATED_NUMB_1)
            .numb2(UPDATED_NUMB_2)
            .adresse(UPDATED_ADRESSE)
            .ville(UPDATED_VILLE)
            .specialite(UPDATED_SPECIALITE);

        restFormateursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFormateurs.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFormateurs))
            )
            .andExpect(status().isOk());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
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
    @Transactional
    void putNonExistingFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().size();
        formateurs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormateursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formateurs.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().size();
        formateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormateursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().size();
        formateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormateursMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormateursWithPatch() throws Exception {
        // Initialize the database
        formateursRepository.saveAndFlush(formateurs);

        int databaseSizeBeforeUpdate = formateursRepository.findAll().size();

        // Update the formateurs using partial update
        Formateurs partialUpdatedFormateurs = new Formateurs();
        partialUpdatedFormateurs.setId(formateurs.getId());

        partialUpdatedFormateurs
            .nomFormateur(UPDATED_NOM_FORMATEUR)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .ville(UPDATED_VILLE)
            .specialite(UPDATED_SPECIALITE);

        restFormateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormateurs.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormateurs))
            )
            .andExpect(status().isOk());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
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
    @Transactional
    void fullUpdateFormateursWithPatch() throws Exception {
        // Initialize the database
        formateursRepository.saveAndFlush(formateurs);

        int databaseSizeBeforeUpdate = formateursRepository.findAll().size();

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

        restFormateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormateurs.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormateurs))
            )
            .andExpect(status().isOk());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
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
    @Transactional
    void patchNonExistingFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().size();
        formateurs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formateurs.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().size();
        formateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormateurs() throws Exception {
        int databaseSizeBeforeUpdate = formateursRepository.findAll().size();
        formateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormateursMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formateurs))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formateurs in the database
        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormateurs() throws Exception {
        // Initialize the database
        formateursRepository.saveAndFlush(formateurs);

        int databaseSizeBeforeDelete = formateursRepository.findAll().size();

        // Delete the formateurs
        restFormateursMockMvc
            .perform(delete(ENTITY_API_URL_ID, formateurs.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formateurs> formateursList = formateursRepository.findAll();
        assertThat(formateursList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
