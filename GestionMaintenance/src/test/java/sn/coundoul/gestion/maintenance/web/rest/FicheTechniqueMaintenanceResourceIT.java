package sn.coundoul.gestion.maintenance.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.coundoul.gestion.maintenance.IntegrationTest;
import sn.coundoul.gestion.maintenance.domain.FicheTechniqueMaintenance;
import sn.coundoul.gestion.maintenance.repository.FicheTechniqueMaintenanceRepository;

/**
 * Integration tests for the {@link FicheTechniqueMaintenanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FicheTechniqueMaintenanceResourceIT {

    private static final String DEFAULT_PIECE_JOINTE = "AAAAAAAAAA";
    private static final String UPDATED_PIECE_JOINTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final Instant DEFAULT_DATE_DEPOT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEPOT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/fiche-technique-maintenances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FicheTechniqueMaintenanceRepository ficheTechniqueMaintenanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFicheTechniqueMaintenanceMockMvc;

    private FicheTechniqueMaintenance ficheTechniqueMaintenance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FicheTechniqueMaintenance createEntity(EntityManager em) {
        FicheTechniqueMaintenance ficheTechniqueMaintenance = new FicheTechniqueMaintenance()
            .pieceJointe(DEFAULT_PIECE_JOINTE)
            .idPers(DEFAULT_ID_PERS)
            .dateDepot(DEFAULT_DATE_DEPOT);
        return ficheTechniqueMaintenance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FicheTechniqueMaintenance createUpdatedEntity(EntityManager em) {
        FicheTechniqueMaintenance ficheTechniqueMaintenance = new FicheTechniqueMaintenance()
            .pieceJointe(UPDATED_PIECE_JOINTE)
            .idPers(UPDATED_ID_PERS)
            .dateDepot(UPDATED_DATE_DEPOT);
        return ficheTechniqueMaintenance;
    }

    @BeforeEach
    public void initTest() {
        ficheTechniqueMaintenance = createEntity(em);
    }

    @Test
    @Transactional
    void createFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeCreate = ficheTechniqueMaintenanceRepository.findAll().size();
        // Create the FicheTechniqueMaintenance
        restFicheTechniqueMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            )
            .andExpect(status().isCreated());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeCreate + 1);
        FicheTechniqueMaintenance testFicheTechniqueMaintenance = ficheTechniqueMaintenanceList.get(
            ficheTechniqueMaintenanceList.size() - 1
        );
        assertThat(testFicheTechniqueMaintenance.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testFicheTechniqueMaintenance.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
        assertThat(testFicheTechniqueMaintenance.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
    }

    @Test
    @Transactional
    void createFicheTechniqueMaintenanceWithExistingId() throws Exception {
        // Create the FicheTechniqueMaintenance with an existing ID
        ficheTechniqueMaintenance.setId(1L);

        int databaseSizeBeforeCreate = ficheTechniqueMaintenanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFicheTechniqueMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            )
            .andExpect(status().isBadRequest());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPieceJointeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheTechniqueMaintenanceRepository.findAll().size();
        // set the field null
        ficheTechniqueMaintenance.setPieceJointe(null);

        // Create the FicheTechniqueMaintenance, which fails.

        restFicheTechniqueMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            )
            .andExpect(status().isBadRequest());

        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheTechniqueMaintenanceRepository.findAll().size();
        // set the field null
        ficheTechniqueMaintenance.setIdPers(null);

        // Create the FicheTechniqueMaintenance, which fails.

        restFicheTechniqueMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            )
            .andExpect(status().isBadRequest());

        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFicheTechniqueMaintenances() throws Exception {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.saveAndFlush(ficheTechniqueMaintenance);

        // Get all the ficheTechniqueMaintenanceList
        restFicheTechniqueMaintenanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ficheTechniqueMaintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].pieceJointe").value(hasItem(DEFAULT_PIECE_JOINTE)))
            .andExpect(jsonPath("$.[*].idPers").value(hasItem(DEFAULT_ID_PERS)))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(DEFAULT_DATE_DEPOT.toString())));
    }

    @Test
    @Transactional
    void getFicheTechniqueMaintenance() throws Exception {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.saveAndFlush(ficheTechniqueMaintenance);

        // Get the ficheTechniqueMaintenance
        restFicheTechniqueMaintenanceMockMvc
            .perform(get(ENTITY_API_URL_ID, ficheTechniqueMaintenance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ficheTechniqueMaintenance.getId().intValue()))
            .andExpect(jsonPath("$.pieceJointe").value(DEFAULT_PIECE_JOINTE))
            .andExpect(jsonPath("$.idPers").value(DEFAULT_ID_PERS))
            .andExpect(jsonPath("$.dateDepot").value(DEFAULT_DATE_DEPOT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFicheTechniqueMaintenance() throws Exception {
        // Get the ficheTechniqueMaintenance
        restFicheTechniqueMaintenanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFicheTechniqueMaintenance() throws Exception {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.saveAndFlush(ficheTechniqueMaintenance);

        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().size();

        // Update the ficheTechniqueMaintenance
        FicheTechniqueMaintenance updatedFicheTechniqueMaintenance = ficheTechniqueMaintenanceRepository
            .findById(ficheTechniqueMaintenance.getId())
            .get();
        // Disconnect from session so that the updates on updatedFicheTechniqueMaintenance are not directly saved in db
        em.detach(updatedFicheTechniqueMaintenance);
        updatedFicheTechniqueMaintenance.pieceJointe(UPDATED_PIECE_JOINTE).idPers(UPDATED_ID_PERS).dateDepot(UPDATED_DATE_DEPOT);

        restFicheTechniqueMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFicheTechniqueMaintenance.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFicheTechniqueMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        FicheTechniqueMaintenance testFicheTechniqueMaintenance = ficheTechniqueMaintenanceList.get(
            ficheTechniqueMaintenanceList.size() - 1
        );
        assertThat(testFicheTechniqueMaintenance.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testFicheTechniqueMaintenance.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testFicheTechniqueMaintenance.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
    }

    @Test
    @Transactional
    void putNonExistingFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFicheTechniqueMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ficheTechniqueMaintenance.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            )
            .andExpect(status().isBadRequest());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFicheTechniqueMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            )
            .andExpect(status().isBadRequest());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFicheTechniqueMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFicheTechniqueMaintenanceWithPatch() throws Exception {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.saveAndFlush(ficheTechniqueMaintenance);

        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().size();

        // Update the ficheTechniqueMaintenance using partial update
        FicheTechniqueMaintenance partialUpdatedFicheTechniqueMaintenance = new FicheTechniqueMaintenance();
        partialUpdatedFicheTechniqueMaintenance.setId(ficheTechniqueMaintenance.getId());

        partialUpdatedFicheTechniqueMaintenance.idPers(UPDATED_ID_PERS);

        restFicheTechniqueMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFicheTechniqueMaintenance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFicheTechniqueMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        FicheTechniqueMaintenance testFicheTechniqueMaintenance = ficheTechniqueMaintenanceList.get(
            ficheTechniqueMaintenanceList.size() - 1
        );
        assertThat(testFicheTechniqueMaintenance.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testFicheTechniqueMaintenance.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testFicheTechniqueMaintenance.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
    }

    @Test
    @Transactional
    void fullUpdateFicheTechniqueMaintenanceWithPatch() throws Exception {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.saveAndFlush(ficheTechniqueMaintenance);

        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().size();

        // Update the ficheTechniqueMaintenance using partial update
        FicheTechniqueMaintenance partialUpdatedFicheTechniqueMaintenance = new FicheTechniqueMaintenance();
        partialUpdatedFicheTechniqueMaintenance.setId(ficheTechniqueMaintenance.getId());

        partialUpdatedFicheTechniqueMaintenance.pieceJointe(UPDATED_PIECE_JOINTE).idPers(UPDATED_ID_PERS).dateDepot(UPDATED_DATE_DEPOT);

        restFicheTechniqueMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFicheTechniqueMaintenance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFicheTechniqueMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        FicheTechniqueMaintenance testFicheTechniqueMaintenance = ficheTechniqueMaintenanceList.get(
            ficheTechniqueMaintenanceList.size() - 1
        );
        assertThat(testFicheTechniqueMaintenance.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testFicheTechniqueMaintenance.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testFicheTechniqueMaintenance.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
    }

    @Test
    @Transactional
    void patchNonExistingFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFicheTechniqueMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ficheTechniqueMaintenance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            )
            .andExpect(status().isBadRequest());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFicheTechniqueMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            )
            .andExpect(status().isBadRequest());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFicheTechniqueMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = ficheTechniqueMaintenanceRepository.findAll().size();
        ficheTechniqueMaintenance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFicheTechniqueMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ficheTechniqueMaintenance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FicheTechniqueMaintenance in the database
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFicheTechniqueMaintenance() throws Exception {
        // Initialize the database
        ficheTechniqueMaintenanceRepository.saveAndFlush(ficheTechniqueMaintenance);

        int databaseSizeBeforeDelete = ficheTechniqueMaintenanceRepository.findAll().size();

        // Delete the ficheTechniqueMaintenance
        restFicheTechniqueMaintenanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, ficheTechniqueMaintenance.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FicheTechniqueMaintenance> ficheTechniqueMaintenanceList = ficheTechniqueMaintenanceRepository.findAll();
        assertThat(ficheTechniqueMaintenanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
