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
import sn.coundoul.gestion.infrastructure.domain.Intervenant;
import sn.coundoul.gestion.infrastructure.domain.enumeration.TypeMaitre;
import sn.coundoul.gestion.infrastructure.repository.IntervenantRepository;

/**
 * Integration tests for the {@link IntervenantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restIntervenantMockMvc;

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

    @BeforeEach
    public void initTest() {
        intervenant = createEntity(em);
    }

    @Test
    @Transactional
    void createIntervenant() throws Exception {
        int databaseSizeBeforeCreate = intervenantRepository.findAll().size();
        // Create the Intervenant
        restIntervenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isCreated());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
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
    @Transactional
    void createIntervenantWithExistingId() throws Exception {
        // Create the Intervenant with an existing ID
        intervenant.setId(1L);

        int databaseSizeBeforeCreate = intervenantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntervenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIntervenantIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().size();
        // set the field null
        intervenant.setNomIntervenant(null);

        // Create the Intervenant, which fails.

        restIntervenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIntervenantIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().size();
        // set the field null
        intervenant.setPrenomIntervenant(null);

        // Create the Intervenant, which fails.

        restIntervenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailProfessionnelIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().size();
        // set the field null
        intervenant.setEmailProfessionnel(null);

        // Create the Intervenant, which fails.

        restIntervenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRaisonSocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().size();
        // set the field null
        intervenant.setRaisonSocial(null);

        // Create the Intervenant, which fails.

        restIntervenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().size();
        // set the field null
        intervenant.setMaitre(null);

        // Create the Intervenant, which fails.

        restIntervenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = intervenantRepository.findAll().size();
        // set the field null
        intervenant.setRole(null);

        // Create the Intervenant, which fails.

        restIntervenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIntervenants() throws Exception {
        // Initialize the database
        intervenantRepository.saveAndFlush(intervenant);

        // Get all the intervenantList
        restIntervenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomIntervenant").value(hasItem(DEFAULT_NOM_INTERVENANT)))
            .andExpect(jsonPath("$.[*].prenomIntervenant").value(hasItem(DEFAULT_PRENOM_INTERVENANT)))
            .andExpect(jsonPath("$.[*].emailProfessionnel").value(hasItem(DEFAULT_EMAIL_PROFESSIONNEL)))
            .andExpect(jsonPath("$.[*].raisonSocial").value(hasItem(DEFAULT_RAISON_SOCIAL)))
            .andExpect(jsonPath("$.[*].maitre").value(hasItem(DEFAULT_MAITRE.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));
    }

    @Test
    @Transactional
    void getIntervenant() throws Exception {
        // Initialize the database
        intervenantRepository.saveAndFlush(intervenant);

        // Get the intervenant
        restIntervenantMockMvc
            .perform(get(ENTITY_API_URL_ID, intervenant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intervenant.getId().intValue()))
            .andExpect(jsonPath("$.nomIntervenant").value(DEFAULT_NOM_INTERVENANT))
            .andExpect(jsonPath("$.prenomIntervenant").value(DEFAULT_PRENOM_INTERVENANT))
            .andExpect(jsonPath("$.emailProfessionnel").value(DEFAULT_EMAIL_PROFESSIONNEL))
            .andExpect(jsonPath("$.raisonSocial").value(DEFAULT_RAISON_SOCIAL))
            .andExpect(jsonPath("$.maitre").value(DEFAULT_MAITRE.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }

    @Test
    @Transactional
    void getNonExistingIntervenant() throws Exception {
        // Get the intervenant
        restIntervenantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIntervenant() throws Exception {
        // Initialize the database
        intervenantRepository.saveAndFlush(intervenant);

        int databaseSizeBeforeUpdate = intervenantRepository.findAll().size();

        // Update the intervenant
        Intervenant updatedIntervenant = intervenantRepository.findById(intervenant.getId()).get();
        // Disconnect from session so that the updates on updatedIntervenant are not directly saved in db
        em.detach(updatedIntervenant);
        updatedIntervenant
            .nomIntervenant(UPDATED_NOM_INTERVENANT)
            .prenomIntervenant(UPDATED_PRENOM_INTERVENANT)
            .emailProfessionnel(UPDATED_EMAIL_PROFESSIONNEL)
            .raisonSocial(UPDATED_RAISON_SOCIAL)
            .maitre(UPDATED_MAITRE)
            .role(UPDATED_ROLE);

        restIntervenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIntervenant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIntervenant))
            )
            .andExpect(status().isOk());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
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
    @Transactional
    void putNonExistingIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().size();
        intervenant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntervenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intervenant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().size();
        intervenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntervenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().size();
        intervenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntervenantMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIntervenantWithPatch() throws Exception {
        // Initialize the database
        intervenantRepository.saveAndFlush(intervenant);

        int databaseSizeBeforeUpdate = intervenantRepository.findAll().size();

        // Update the intervenant using partial update
        Intervenant partialUpdatedIntervenant = new Intervenant();
        partialUpdatedIntervenant.setId(intervenant.getId());

        restIntervenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervenant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervenant))
            )
            .andExpect(status().isOk());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
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
    @Transactional
    void fullUpdateIntervenantWithPatch() throws Exception {
        // Initialize the database
        intervenantRepository.saveAndFlush(intervenant);

        int databaseSizeBeforeUpdate = intervenantRepository.findAll().size();

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

        restIntervenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervenant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervenant))
            )
            .andExpect(status().isOk());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
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
    @Transactional
    void patchNonExistingIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().size();
        intervenant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntervenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intervenant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().size();
        intervenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntervenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntervenant() throws Exception {
        int databaseSizeBeforeUpdate = intervenantRepository.findAll().size();
        intervenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntervenantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intervenant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervenant in the database
        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntervenant() throws Exception {
        // Initialize the database
        intervenantRepository.saveAndFlush(intervenant);

        int databaseSizeBeforeDelete = intervenantRepository.findAll().size();

        // Delete the intervenant
        restIntervenantMockMvc
            .perform(delete(ENTITY_API_URL_ID, intervenant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Intervenant> intervenantList = intervenantRepository.findAll();
        assertThat(intervenantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
