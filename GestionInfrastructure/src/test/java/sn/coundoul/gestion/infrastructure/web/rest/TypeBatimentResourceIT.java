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
import sn.coundoul.gestion.infrastructure.domain.TypeBatiment;
import sn.coundoul.gestion.infrastructure.repository.TypeBatimentRepository;

/**
 * Integration tests for the {@link TypeBatimentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeBatimentResourceIT {

    private static final String DEFAULT_TYPE_BA = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_BA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-batiments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeBatimentRepository typeBatimentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeBatimentMockMvc;

    private TypeBatiment typeBatiment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeBatiment createEntity(EntityManager em) {
        TypeBatiment typeBatiment = new TypeBatiment().typeBa(DEFAULT_TYPE_BA);
        return typeBatiment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeBatiment createUpdatedEntity(EntityManager em) {
        TypeBatiment typeBatiment = new TypeBatiment().typeBa(UPDATED_TYPE_BA);
        return typeBatiment;
    }

    @BeforeEach
    public void initTest() {
        typeBatiment = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeBatiment() throws Exception {
        int databaseSizeBeforeCreate = typeBatimentRepository.findAll().size();
        // Create the TypeBatiment
        restTypeBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBatiment))
            )
            .andExpect(status().isCreated());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeCreate + 1);
        TypeBatiment testTypeBatiment = typeBatimentList.get(typeBatimentList.size() - 1);
        assertThat(testTypeBatiment.getTypeBa()).isEqualTo(DEFAULT_TYPE_BA);
    }

    @Test
    @Transactional
    void createTypeBatimentWithExistingId() throws Exception {
        // Create the TypeBatiment with an existing ID
        typeBatiment.setId(1L);

        int databaseSizeBeforeCreate = typeBatimentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBatiment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeBaIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeBatimentRepository.findAll().size();
        // set the field null
        typeBatiment.setTypeBa(null);

        // Create the TypeBatiment, which fails.

        restTypeBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBatiment))
            )
            .andExpect(status().isBadRequest());

        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeBatiments() throws Exception {
        // Initialize the database
        typeBatimentRepository.saveAndFlush(typeBatiment);

        // Get all the typeBatimentList
        restTypeBatimentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeBatiment.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeBa").value(hasItem(DEFAULT_TYPE_BA)));
    }

    @Test
    @Transactional
    void getTypeBatiment() throws Exception {
        // Initialize the database
        typeBatimentRepository.saveAndFlush(typeBatiment);

        // Get the typeBatiment
        restTypeBatimentMockMvc
            .perform(get(ENTITY_API_URL_ID, typeBatiment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeBatiment.getId().intValue()))
            .andExpect(jsonPath("$.typeBa").value(DEFAULT_TYPE_BA));
    }

    @Test
    @Transactional
    void getNonExistingTypeBatiment() throws Exception {
        // Get the typeBatiment
        restTypeBatimentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeBatiment() throws Exception {
        // Initialize the database
        typeBatimentRepository.saveAndFlush(typeBatiment);

        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().size();

        // Update the typeBatiment
        TypeBatiment updatedTypeBatiment = typeBatimentRepository.findById(typeBatiment.getId()).get();
        // Disconnect from session so that the updates on updatedTypeBatiment are not directly saved in db
        em.detach(updatedTypeBatiment);
        updatedTypeBatiment.typeBa(UPDATED_TYPE_BA);

        restTypeBatimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeBatiment.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeBatiment))
            )
            .andExpect(status().isOk());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
        TypeBatiment testTypeBatiment = typeBatimentList.get(typeBatimentList.size() - 1);
        assertThat(testTypeBatiment.getTypeBa()).isEqualTo(UPDATED_TYPE_BA);
    }

    @Test
    @Transactional
    void putNonExistingTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().size();
        typeBatiment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeBatimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeBatiment.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBatiment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().size();
        typeBatiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeBatimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBatiment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().size();
        typeBatiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeBatimentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBatiment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeBatimentWithPatch() throws Exception {
        // Initialize the database
        typeBatimentRepository.saveAndFlush(typeBatiment);

        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().size();

        // Update the typeBatiment using partial update
        TypeBatiment partialUpdatedTypeBatiment = new TypeBatiment();
        partialUpdatedTypeBatiment.setId(typeBatiment.getId());

        partialUpdatedTypeBatiment.typeBa(UPDATED_TYPE_BA);

        restTypeBatimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeBatiment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeBatiment))
            )
            .andExpect(status().isOk());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
        TypeBatiment testTypeBatiment = typeBatimentList.get(typeBatimentList.size() - 1);
        assertThat(testTypeBatiment.getTypeBa()).isEqualTo(UPDATED_TYPE_BA);
    }

    @Test
    @Transactional
    void fullUpdateTypeBatimentWithPatch() throws Exception {
        // Initialize the database
        typeBatimentRepository.saveAndFlush(typeBatiment);

        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().size();

        // Update the typeBatiment using partial update
        TypeBatiment partialUpdatedTypeBatiment = new TypeBatiment();
        partialUpdatedTypeBatiment.setId(typeBatiment.getId());

        partialUpdatedTypeBatiment.typeBa(UPDATED_TYPE_BA);

        restTypeBatimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeBatiment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeBatiment))
            )
            .andExpect(status().isOk());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
        TypeBatiment testTypeBatiment = typeBatimentList.get(typeBatimentList.size() - 1);
        assertThat(testTypeBatiment.getTypeBa()).isEqualTo(UPDATED_TYPE_BA);
    }

    @Test
    @Transactional
    void patchNonExistingTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().size();
        typeBatiment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeBatimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeBatiment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeBatiment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().size();
        typeBatiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeBatimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeBatiment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeBatiment() throws Exception {
        int databaseSizeBeforeUpdate = typeBatimentRepository.findAll().size();
        typeBatiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeBatimentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeBatiment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeBatiment in the database
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeBatiment() throws Exception {
        // Initialize the database
        typeBatimentRepository.saveAndFlush(typeBatiment);

        int databaseSizeBeforeDelete = typeBatimentRepository.findAll().size();

        // Delete the typeBatiment
        restTypeBatimentMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeBatiment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeBatiment> typeBatimentList = typeBatimentRepository.findAll();
        assertThat(typeBatimentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
