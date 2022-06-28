package sn.coundoul.gestion.utilisateurs.web.rest;

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
import sn.coundoul.gestion.utilisateurs.IntegrationTest;
import sn.coundoul.gestion.utilisateurs.domain.Intendant;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;
import sn.coundoul.gestion.utilisateurs.repository.IntendantRepository;

/**
 * Integration tests for the {@link IntendantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IntendantResourceIT {

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

    private static final String ENTITY_API_URL = "/api/intendants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IntendantRepository intendantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIntendantMockMvc;

    private Intendant intendant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intendant createEntity(EntityManager em) {
        Intendant intendant = new Intendant()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE);
        return intendant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intendant createUpdatedEntity(EntityManager em) {
        Intendant intendant = new Intendant()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);
        return intendant;
    }

    @BeforeEach
    public void initTest() {
        intendant = createEntity(em);
    }

    @Test
    @Transactional
    void createIntendant() throws Exception {
        int databaseSizeBeforeCreate = intendantRepository.findAll().size();
        // Create the Intendant
        restIntendantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isCreated());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeCreate + 1);
        Intendant testIntendant = intendantList.get(intendantList.size() - 1);
        assertThat(testIntendant.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testIntendant.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testIntendant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testIntendant.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testIntendant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void createIntendantWithExistingId() throws Exception {
        // Create the Intendant with an existing ID
        intendant.setId(1L);

        int databaseSizeBeforeCreate = intendantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntendantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = intendantRepository.findAll().size();
        // set the field null
        intendant.setNomPers(null);

        // Create the Intendant, which fails.

        restIntendantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isBadRequest());

        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = intendantRepository.findAll().size();
        // set the field null
        intendant.setPrenomPers(null);

        // Create the Intendant, which fails.

        restIntendantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isBadRequest());

        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = intendantRepository.findAll().size();
        // set the field null
        intendant.setSexe(null);

        // Create the Intendant, which fails.

        restIntendantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isBadRequest());

        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = intendantRepository.findAll().size();
        // set the field null
        intendant.setMobile(null);

        // Create the Intendant, which fails.

        restIntendantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isBadRequest());

        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIntendants() throws Exception {
        // Initialize the database
        intendantRepository.saveAndFlush(intendant);

        // Get all the intendantList
        restIntendantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intendant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)));
    }

    @Test
    @Transactional
    void getIntendant() throws Exception {
        // Initialize the database
        intendantRepository.saveAndFlush(intendant);

        // Get the intendant
        restIntendantMockMvc
            .perform(get(ENTITY_API_URL_ID, intendant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intendant.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE));
    }

    @Test
    @Transactional
    void getNonExistingIntendant() throws Exception {
        // Get the intendant
        restIntendantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIntendant() throws Exception {
        // Initialize the database
        intendantRepository.saveAndFlush(intendant);

        int databaseSizeBeforeUpdate = intendantRepository.findAll().size();

        // Update the intendant
        Intendant updatedIntendant = intendantRepository.findById(intendant.getId()).get();
        // Disconnect from session so that the updates on updatedIntendant are not directly saved in db
        em.detach(updatedIntendant);
        updatedIntendant
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);

        restIntendantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIntendant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIntendant))
            )
            .andExpect(status().isOk());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
        Intendant testIntendant = intendantList.get(intendantList.size() - 1);
        assertThat(testIntendant.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testIntendant.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testIntendant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testIntendant.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testIntendant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void putNonExistingIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().size();
        intendant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntendantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intendant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().size();
        intendant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntendantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().size();
        intendant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntendantMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIntendantWithPatch() throws Exception {
        // Initialize the database
        intendantRepository.saveAndFlush(intendant);

        int databaseSizeBeforeUpdate = intendantRepository.findAll().size();

        // Update the intendant using partial update
        Intendant partialUpdatedIntendant = new Intendant();
        partialUpdatedIntendant.setId(intendant.getId());

        partialUpdatedIntendant.prenomPers(UPDATED_PRENOM_PERS).sexe(UPDATED_SEXE).mobile(UPDATED_MOBILE);

        restIntendantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntendant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntendant))
            )
            .andExpect(status().isOk());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
        Intendant testIntendant = intendantList.get(intendantList.size() - 1);
        assertThat(testIntendant.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testIntendant.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testIntendant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testIntendant.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testIntendant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void fullUpdateIntendantWithPatch() throws Exception {
        // Initialize the database
        intendantRepository.saveAndFlush(intendant);

        int databaseSizeBeforeUpdate = intendantRepository.findAll().size();

        // Update the intendant using partial update
        Intendant partialUpdatedIntendant = new Intendant();
        partialUpdatedIntendant.setId(intendant.getId());

        partialUpdatedIntendant
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);

        restIntendantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntendant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntendant))
            )
            .andExpect(status().isOk());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
        Intendant testIntendant = intendantList.get(intendantList.size() - 1);
        assertThat(testIntendant.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testIntendant.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testIntendant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testIntendant.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testIntendant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void patchNonExistingIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().size();
        intendant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntendantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intendant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().size();
        intendant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntendantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntendant() throws Exception {
        int databaseSizeBeforeUpdate = intendantRepository.findAll().size();
        intendant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntendantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intendant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intendant in the database
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntendant() throws Exception {
        // Initialize the database
        intendantRepository.saveAndFlush(intendant);

        int databaseSizeBeforeDelete = intendantRepository.findAll().size();

        // Delete the intendant
        restIntendantMockMvc
            .perform(delete(ENTITY_API_URL_ID, intendant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Intendant> intendantList = intendantRepository.findAll();
        assertThat(intendantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
