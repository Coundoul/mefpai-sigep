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
import sn.coundoul.gestion.equipement.domain.UtilisateurFinal;
import sn.coundoul.gestion.equipement.repository.UtilisateurFinalRepository;

/**
 * Integration tests for the {@link UtilisateurFinalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restUtilisateurFinalMockMvc;

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

    @BeforeEach
    public void initTest() {
        utilisateurFinal = createEntity(em);
    }

    @Test
    @Transactional
    void createUtilisateurFinal() throws Exception {
        int databaseSizeBeforeCreate = utilisateurFinalRepository.findAll().size();
        // Create the UtilisateurFinal
        restUtilisateurFinalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isCreated());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
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
    @Transactional
    void createUtilisateurFinalWithExistingId() throws Exception {
        // Create the UtilisateurFinal with an existing ID
        utilisateurFinal.setId(1L);

        int databaseSizeBeforeCreate = utilisateurFinalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilisateurFinalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomUtilisateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurFinalRepository.findAll().size();
        // set the field null
        utilisateurFinal.setNomUtilisateur(null);

        // Create the UtilisateurFinal, which fails.

        restUtilisateurFinalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isBadRequest());

        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomUtilisateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurFinalRepository.findAll().size();
        // set the field null
        utilisateurFinal.setPrenomUtilisateur(null);

        // Create the UtilisateurFinal, which fails.

        restUtilisateurFinalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isBadRequest());

        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailInstitutionnelIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurFinalRepository.findAll().size();
        // set the field null
        utilisateurFinal.setEmailInstitutionnel(null);

        // Create the UtilisateurFinal, which fails.

        restUtilisateurFinalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isBadRequest());

        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurFinalRepository.findAll().size();
        // set the field null
        utilisateurFinal.setMobile(null);

        // Create the UtilisateurFinal, which fails.

        restUtilisateurFinalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isBadRequest());

        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurFinalRepository.findAll().size();
        // set the field null
        utilisateurFinal.setSexe(null);

        // Create the UtilisateurFinal, which fails.

        restUtilisateurFinalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isBadRequest());

        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUtilisateurFinals() throws Exception {
        // Initialize the database
        utilisateurFinalRepository.saveAndFlush(utilisateurFinal);

        // Get all the utilisateurFinalList
        restUtilisateurFinalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateurFinal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomUtilisateur").value(hasItem(DEFAULT_NOM_UTILISATEUR)))
            .andExpect(jsonPath("$.[*].prenomUtilisateur").value(hasItem(DEFAULT_PRENOM_UTILISATEUR)))
            .andExpect(jsonPath("$.[*].emailInstitutionnel").value(hasItem(DEFAULT_EMAIL_INSTITUTIONNEL)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT)))
            .andExpect(jsonPath("$.[*].serviceDep").value(hasItem(DEFAULT_SERVICE_DEP)));
    }

    @Test
    @Transactional
    void getUtilisateurFinal() throws Exception {
        // Initialize the database
        utilisateurFinalRepository.saveAndFlush(utilisateurFinal);

        // Get the utilisateurFinal
        restUtilisateurFinalMockMvc
            .perform(get(ENTITY_API_URL_ID, utilisateurFinal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilisateurFinal.getId().intValue()))
            .andExpect(jsonPath("$.nomUtilisateur").value(DEFAULT_NOM_UTILISATEUR))
            .andExpect(jsonPath("$.prenomUtilisateur").value(DEFAULT_PRENOM_UTILISATEUR))
            .andExpect(jsonPath("$.emailInstitutionnel").value(DEFAULT_EMAIL_INSTITUTIONNEL))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT))
            .andExpect(jsonPath("$.serviceDep").value(DEFAULT_SERVICE_DEP));
    }

    @Test
    @Transactional
    void getNonExistingUtilisateurFinal() throws Exception {
        // Get the utilisateurFinal
        restUtilisateurFinalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUtilisateurFinal() throws Exception {
        // Initialize the database
        utilisateurFinalRepository.saveAndFlush(utilisateurFinal);

        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().size();

        // Update the utilisateurFinal
        UtilisateurFinal updatedUtilisateurFinal = utilisateurFinalRepository.findById(utilisateurFinal.getId()).get();
        // Disconnect from session so that the updates on updatedUtilisateurFinal are not directly saved in db
        em.detach(updatedUtilisateurFinal);
        updatedUtilisateurFinal
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .prenomUtilisateur(UPDATED_PRENOM_UTILISATEUR)
            .emailInstitutionnel(UPDATED_EMAIL_INSTITUTIONNEL)
            .mobile(UPDATED_MOBILE)
            .sexe(UPDATED_SEXE)
            .departement(UPDATED_DEPARTEMENT)
            .serviceDep(UPDATED_SERVICE_DEP);

        restUtilisateurFinalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUtilisateurFinal.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUtilisateurFinal))
            )
            .andExpect(status().isOk());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
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
    @Transactional
    void putNonExistingUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurFinalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurFinal.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurFinalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurFinalMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilisateurFinalWithPatch() throws Exception {
        // Initialize the database
        utilisateurFinalRepository.saveAndFlush(utilisateurFinal);

        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().size();

        // Update the utilisateurFinal using partial update
        UtilisateurFinal partialUpdatedUtilisateurFinal = new UtilisateurFinal();
        partialUpdatedUtilisateurFinal.setId(utilisateurFinal.getId());

        partialUpdatedUtilisateurFinal
            .emailInstitutionnel(UPDATED_EMAIL_INSTITUTIONNEL)
            .mobile(UPDATED_MOBILE)
            .sexe(UPDATED_SEXE)
            .departement(UPDATED_DEPARTEMENT);

        restUtilisateurFinalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateurFinal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisateurFinal))
            )
            .andExpect(status().isOk());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
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
    @Transactional
    void fullUpdateUtilisateurFinalWithPatch() throws Exception {
        // Initialize the database
        utilisateurFinalRepository.saveAndFlush(utilisateurFinal);

        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().size();

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

        restUtilisateurFinalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateurFinal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisateurFinal))
            )
            .andExpect(status().isOk());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
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
    @Transactional
    void patchNonExistingUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurFinalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilisateurFinal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurFinalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilisateurFinal() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurFinalRepository.findAll().size();
        utilisateurFinal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurFinalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurFinal))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtilisateurFinal in the database
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtilisateurFinal() throws Exception {
        // Initialize the database
        utilisateurFinalRepository.saveAndFlush(utilisateurFinal);

        int databaseSizeBeforeDelete = utilisateurFinalRepository.findAll().size();

        // Delete the utilisateurFinal
        restUtilisateurFinalMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilisateurFinal.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UtilisateurFinal> utilisateurFinalList = utilisateurFinalRepository.findAll();
        assertThat(utilisateurFinalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
