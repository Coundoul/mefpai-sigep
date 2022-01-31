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
import sn.coundoul.gestion.infrastructure.domain.FiliereStabilise;
import sn.coundoul.gestion.infrastructure.repository.FiliereStabiliseRepository;

/**
 * Integration tests for the {@link FiliereStabiliseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FiliereStabiliseResourceIT {

    private static final String DEFAULT_NOM_FILIERE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FILIERE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/filiere-stabilises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiliereStabiliseRepository filiereStabiliseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiliereStabiliseMockMvc;

    private FiliereStabilise filiereStabilise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiliereStabilise createEntity(EntityManager em) {
        FiliereStabilise filiereStabilise = new FiliereStabilise().nomFiliere(DEFAULT_NOM_FILIERE);
        return filiereStabilise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiliereStabilise createUpdatedEntity(EntityManager em) {
        FiliereStabilise filiereStabilise = new FiliereStabilise().nomFiliere(UPDATED_NOM_FILIERE);
        return filiereStabilise;
    }

    @BeforeEach
    public void initTest() {
        filiereStabilise = createEntity(em);
    }

    @Test
    @Transactional
    void createFiliereStabilise() throws Exception {
        int databaseSizeBeforeCreate = filiereStabiliseRepository.findAll().size();
        // Create the FiliereStabilise
        restFiliereStabiliseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            )
            .andExpect(status().isCreated());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeCreate + 1);
        FiliereStabilise testFiliereStabilise = filiereStabiliseList.get(filiereStabiliseList.size() - 1);
        assertThat(testFiliereStabilise.getNomFiliere()).isEqualTo(DEFAULT_NOM_FILIERE);
    }

    @Test
    @Transactional
    void createFiliereStabiliseWithExistingId() throws Exception {
        // Create the FiliereStabilise with an existing ID
        filiereStabilise.setId(1L);

        int databaseSizeBeforeCreate = filiereStabiliseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiliereStabiliseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFiliereIsRequired() throws Exception {
        int databaseSizeBeforeTest = filiereStabiliseRepository.findAll().size();
        // set the field null
        filiereStabilise.setNomFiliere(null);

        // Create the FiliereStabilise, which fails.

        restFiliereStabiliseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            )
            .andExpect(status().isBadRequest());

        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiliereStabilises() throws Exception {
        // Initialize the database
        filiereStabiliseRepository.saveAndFlush(filiereStabilise);

        // Get all the filiereStabiliseList
        restFiliereStabiliseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filiereStabilise.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFiliere").value(hasItem(DEFAULT_NOM_FILIERE)));
    }

    @Test
    @Transactional
    void getFiliereStabilise() throws Exception {
        // Initialize the database
        filiereStabiliseRepository.saveAndFlush(filiereStabilise);

        // Get the filiereStabilise
        restFiliereStabiliseMockMvc
            .perform(get(ENTITY_API_URL_ID, filiereStabilise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filiereStabilise.getId().intValue()))
            .andExpect(jsonPath("$.nomFiliere").value(DEFAULT_NOM_FILIERE));
    }

    @Test
    @Transactional
    void getNonExistingFiliereStabilise() throws Exception {
        // Get the filiereStabilise
        restFiliereStabiliseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFiliereStabilise() throws Exception {
        // Initialize the database
        filiereStabiliseRepository.saveAndFlush(filiereStabilise);

        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().size();

        // Update the filiereStabilise
        FiliereStabilise updatedFiliereStabilise = filiereStabiliseRepository.findById(filiereStabilise.getId()).get();
        // Disconnect from session so that the updates on updatedFiliereStabilise are not directly saved in db
        em.detach(updatedFiliereStabilise);
        updatedFiliereStabilise.nomFiliere(UPDATED_NOM_FILIERE);

        restFiliereStabiliseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFiliereStabilise.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFiliereStabilise))
            )
            .andExpect(status().isOk());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
        FiliereStabilise testFiliereStabilise = filiereStabiliseList.get(filiereStabiliseList.size() - 1);
        assertThat(testFiliereStabilise.getNomFiliere()).isEqualTo(UPDATED_NOM_FILIERE);
    }

    @Test
    @Transactional
    void putNonExistingFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiliereStabiliseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filiereStabilise.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereStabiliseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereStabiliseMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFiliereStabiliseWithPatch() throws Exception {
        // Initialize the database
        filiereStabiliseRepository.saveAndFlush(filiereStabilise);

        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().size();

        // Update the filiereStabilise using partial update
        FiliereStabilise partialUpdatedFiliereStabilise = new FiliereStabilise();
        partialUpdatedFiliereStabilise.setId(filiereStabilise.getId());

        partialUpdatedFiliereStabilise.nomFiliere(UPDATED_NOM_FILIERE);

        restFiliereStabiliseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiliereStabilise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiliereStabilise))
            )
            .andExpect(status().isOk());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
        FiliereStabilise testFiliereStabilise = filiereStabiliseList.get(filiereStabiliseList.size() - 1);
        assertThat(testFiliereStabilise.getNomFiliere()).isEqualTo(UPDATED_NOM_FILIERE);
    }

    @Test
    @Transactional
    void fullUpdateFiliereStabiliseWithPatch() throws Exception {
        // Initialize the database
        filiereStabiliseRepository.saveAndFlush(filiereStabilise);

        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().size();

        // Update the filiereStabilise using partial update
        FiliereStabilise partialUpdatedFiliereStabilise = new FiliereStabilise();
        partialUpdatedFiliereStabilise.setId(filiereStabilise.getId());

        partialUpdatedFiliereStabilise.nomFiliere(UPDATED_NOM_FILIERE);

        restFiliereStabiliseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiliereStabilise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiliereStabilise))
            )
            .andExpect(status().isOk());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
        FiliereStabilise testFiliereStabilise = filiereStabiliseList.get(filiereStabiliseList.size() - 1);
        assertThat(testFiliereStabilise.getNomFiliere()).isEqualTo(UPDATED_NOM_FILIERE);
    }

    @Test
    @Transactional
    void patchNonExistingFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiliereStabiliseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filiereStabilise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereStabiliseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiliereStabilise() throws Exception {
        int databaseSizeBeforeUpdate = filiereStabiliseRepository.findAll().size();
        filiereStabilise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereStabiliseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filiereStabilise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiliereStabilise in the database
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFiliereStabilise() throws Exception {
        // Initialize the database
        filiereStabiliseRepository.saveAndFlush(filiereStabilise);

        int databaseSizeBeforeDelete = filiereStabiliseRepository.findAll().size();

        // Delete the filiereStabilise
        restFiliereStabiliseMockMvc
            .perform(delete(ENTITY_API_URL_ID, filiereStabilise.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FiliereStabilise> filiereStabiliseList = filiereStabiliseRepository.findAll();
        assertThat(filiereStabiliseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
