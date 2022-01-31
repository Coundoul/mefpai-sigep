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
import sn.coundoul.gestion.utilisateurs.domain.ChefMaintenance;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Direction;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;
import sn.coundoul.gestion.utilisateurs.repository.ChefMaintenanceRepository;

/**
 * Integration tests for the {@link ChefMaintenanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChefMaintenanceResourceIT {

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

    private static final String ENTITY_API_URL = "/api/chef-maintenances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChefMaintenanceRepository chefMaintenanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChefMaintenanceMockMvc;

    private ChefMaintenance chefMaintenance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefMaintenance createEntity(EntityManager em) {
        ChefMaintenance chefMaintenance = new ChefMaintenance()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return chefMaintenance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefMaintenance createUpdatedEntity(EntityManager em) {
        ChefMaintenance chefMaintenance = new ChefMaintenance()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return chefMaintenance;
    }

    @BeforeEach
    public void initTest() {
        chefMaintenance = createEntity(em);
    }

    @Test
    @Transactional
    void createChefMaintenance() throws Exception {
        int databaseSizeBeforeCreate = chefMaintenanceRepository.findAll().size();
        // Create the ChefMaintenance
        restChefMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isCreated());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeCreate + 1);
        ChefMaintenance testChefMaintenance = chefMaintenanceList.get(chefMaintenanceList.size() - 1);
        assertThat(testChefMaintenance.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testChefMaintenance.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testChefMaintenance.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefMaintenance.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testChefMaintenance.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testChefMaintenance.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void createChefMaintenanceWithExistingId() throws Exception {
        // Create the ChefMaintenance with an existing ID
        chefMaintenance.setId(1L);

        int databaseSizeBeforeCreate = chefMaintenanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChefMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefMaintenanceRepository.findAll().size();
        // set the field null
        chefMaintenance.setNomPers(null);

        // Create the ChefMaintenance, which fails.

        restChefMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isBadRequest());

        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefMaintenanceRepository.findAll().size();
        // set the field null
        chefMaintenance.setPrenomPers(null);

        // Create the ChefMaintenance, which fails.

        restChefMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isBadRequest());

        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefMaintenanceRepository.findAll().size();
        // set the field null
        chefMaintenance.setSexe(null);

        // Create the ChefMaintenance, which fails.

        restChefMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isBadRequest());

        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefMaintenanceRepository.findAll().size();
        // set the field null
        chefMaintenance.setMobile(null);

        // Create the ChefMaintenance, which fails.

        restChefMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isBadRequest());

        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChefMaintenances() throws Exception {
        // Initialize the database
        chefMaintenanceRepository.saveAndFlush(chefMaintenance);

        // Get all the chefMaintenanceList
        restChefMaintenanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chefMaintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }

    @Test
    @Transactional
    void getChefMaintenance() throws Exception {
        // Initialize the database
        chefMaintenanceRepository.saveAndFlush(chefMaintenance);

        // Get the chefMaintenance
        restChefMaintenanceMockMvc
            .perform(get(ENTITY_API_URL_ID, chefMaintenance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chefMaintenance.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChefMaintenance() throws Exception {
        // Get the chefMaintenance
        restChefMaintenanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChefMaintenance() throws Exception {
        // Initialize the database
        chefMaintenanceRepository.saveAndFlush(chefMaintenance);

        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().size();

        // Update the chefMaintenance
        ChefMaintenance updatedChefMaintenance = chefMaintenanceRepository.findById(chefMaintenance.getId()).get();
        // Disconnect from session so that the updates on updatedChefMaintenance are not directly saved in db
        em.detach(updatedChefMaintenance);
        updatedChefMaintenance
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restChefMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChefMaintenance.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChefMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        ChefMaintenance testChefMaintenance = chefMaintenanceList.get(chefMaintenanceList.size() - 1);
        assertThat(testChefMaintenance.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefMaintenance.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefMaintenance.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefMaintenance.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefMaintenance.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChefMaintenance.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void putNonExistingChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chefMaintenance.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChefMaintenanceWithPatch() throws Exception {
        // Initialize the database
        chefMaintenanceRepository.saveAndFlush(chefMaintenance);

        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().size();

        // Update the chefMaintenance using partial update
        ChefMaintenance partialUpdatedChefMaintenance = new ChefMaintenance();
        partialUpdatedChefMaintenance.setId(chefMaintenance.getId());

        partialUpdatedChefMaintenance.nomPers(UPDATED_NOM_PERS).mobile(UPDATED_MOBILE);

        restChefMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChefMaintenance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChefMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        ChefMaintenance testChefMaintenance = chefMaintenanceList.get(chefMaintenanceList.size() - 1);
        assertThat(testChefMaintenance.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefMaintenance.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testChefMaintenance.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefMaintenance.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefMaintenance.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testChefMaintenance.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void fullUpdateChefMaintenanceWithPatch() throws Exception {
        // Initialize the database
        chefMaintenanceRepository.saveAndFlush(chefMaintenance);

        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().size();

        // Update the chefMaintenance using partial update
        ChefMaintenance partialUpdatedChefMaintenance = new ChefMaintenance();
        partialUpdatedChefMaintenance.setId(chefMaintenance.getId());

        partialUpdatedChefMaintenance
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restChefMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChefMaintenance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChefMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        ChefMaintenance testChefMaintenance = chefMaintenanceList.get(chefMaintenanceList.size() - 1);
        assertThat(testChefMaintenance.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefMaintenance.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefMaintenance.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefMaintenance.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefMaintenance.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChefMaintenance.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void patchNonExistingChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chefMaintenance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChefMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = chefMaintenanceRepository.findAll().size();
        chefMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefMaintenance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChefMaintenance in the database
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChefMaintenance() throws Exception {
        // Initialize the database
        chefMaintenanceRepository.saveAndFlush(chefMaintenance);

        int databaseSizeBeforeDelete = chefMaintenanceRepository.findAll().size();

        // Delete the chefMaintenance
        restChefMaintenanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, chefMaintenance.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChefMaintenance> chefMaintenanceList = chefMaintenanceRepository.findAll();
        assertThat(chefMaintenanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
