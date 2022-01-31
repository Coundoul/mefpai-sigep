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
import sn.coundoul.gestion.infrastructure.domain.NatureFoncier;
import sn.coundoul.gestion.infrastructure.repository.NatureFoncierRepository;

/**
 * Integration tests for the {@link NatureFoncierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NatureFoncierResourceIT {

    private static final String DEFAULT_TYPE_FONCIER = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_FONCIER = "BBBBBBBBBB";

    private static final String DEFAULT_PIECE_JOINTE = "AAAAAAAAAA";
    private static final String UPDATED_PIECE_JOINTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nature-fonciers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NatureFoncierRepository natureFoncierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNatureFoncierMockMvc;

    private NatureFoncier natureFoncier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NatureFoncier createEntity(EntityManager em) {
        NatureFoncier natureFoncier = new NatureFoncier().typeFoncier(DEFAULT_TYPE_FONCIER).pieceJointe(DEFAULT_PIECE_JOINTE);
        return natureFoncier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NatureFoncier createUpdatedEntity(EntityManager em) {
        NatureFoncier natureFoncier = new NatureFoncier().typeFoncier(UPDATED_TYPE_FONCIER).pieceJointe(UPDATED_PIECE_JOINTE);
        return natureFoncier;
    }

    @BeforeEach
    public void initTest() {
        natureFoncier = createEntity(em);
    }

    @Test
    @Transactional
    void createNatureFoncier() throws Exception {
        int databaseSizeBeforeCreate = natureFoncierRepository.findAll().size();
        // Create the NatureFoncier
        restNatureFoncierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureFoncier))
            )
            .andExpect(status().isCreated());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeCreate + 1);
        NatureFoncier testNatureFoncier = natureFoncierList.get(natureFoncierList.size() - 1);
        assertThat(testNatureFoncier.getTypeFoncier()).isEqualTo(DEFAULT_TYPE_FONCIER);
        assertThat(testNatureFoncier.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
    }

    @Test
    @Transactional
    void createNatureFoncierWithExistingId() throws Exception {
        // Create the NatureFoncier with an existing ID
        natureFoncier.setId(1L);

        int databaseSizeBeforeCreate = natureFoncierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNatureFoncierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureFoncier))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeFoncierIsRequired() throws Exception {
        int databaseSizeBeforeTest = natureFoncierRepository.findAll().size();
        // set the field null
        natureFoncier.setTypeFoncier(null);

        // Create the NatureFoncier, which fails.

        restNatureFoncierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureFoncier))
            )
            .andExpect(status().isBadRequest());

        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPieceJointeIsRequired() throws Exception {
        int databaseSizeBeforeTest = natureFoncierRepository.findAll().size();
        // set the field null
        natureFoncier.setPieceJointe(null);

        // Create the NatureFoncier, which fails.

        restNatureFoncierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureFoncier))
            )
            .andExpect(status().isBadRequest());

        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNatureFonciers() throws Exception {
        // Initialize the database
        natureFoncierRepository.saveAndFlush(natureFoncier);

        // Get all the natureFoncierList
        restNatureFoncierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(natureFoncier.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeFoncier").value(hasItem(DEFAULT_TYPE_FONCIER)))
            .andExpect(jsonPath("$.[*].pieceJointe").value(hasItem(DEFAULT_PIECE_JOINTE)));
    }

    @Test
    @Transactional
    void getNatureFoncier() throws Exception {
        // Initialize the database
        natureFoncierRepository.saveAndFlush(natureFoncier);

        // Get the natureFoncier
        restNatureFoncierMockMvc
            .perform(get(ENTITY_API_URL_ID, natureFoncier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(natureFoncier.getId().intValue()))
            .andExpect(jsonPath("$.typeFoncier").value(DEFAULT_TYPE_FONCIER))
            .andExpect(jsonPath("$.pieceJointe").value(DEFAULT_PIECE_JOINTE));
    }

    @Test
    @Transactional
    void getNonExistingNatureFoncier() throws Exception {
        // Get the natureFoncier
        restNatureFoncierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNatureFoncier() throws Exception {
        // Initialize the database
        natureFoncierRepository.saveAndFlush(natureFoncier);

        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().size();

        // Update the natureFoncier
        NatureFoncier updatedNatureFoncier = natureFoncierRepository.findById(natureFoncier.getId()).get();
        // Disconnect from session so that the updates on updatedNatureFoncier are not directly saved in db
        em.detach(updatedNatureFoncier);
        updatedNatureFoncier.typeFoncier(UPDATED_TYPE_FONCIER).pieceJointe(UPDATED_PIECE_JOINTE);

        restNatureFoncierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNatureFoncier.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNatureFoncier))
            )
            .andExpect(status().isOk());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
        NatureFoncier testNatureFoncier = natureFoncierList.get(natureFoncierList.size() - 1);
        assertThat(testNatureFoncier.getTypeFoncier()).isEqualTo(UPDATED_TYPE_FONCIER);
        assertThat(testNatureFoncier.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
    }

    @Test
    @Transactional
    void putNonExistingNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().size();
        natureFoncier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNatureFoncierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, natureFoncier.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureFoncier))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().size();
        natureFoncier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureFoncierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureFoncier))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().size();
        natureFoncier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureFoncierMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureFoncier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNatureFoncierWithPatch() throws Exception {
        // Initialize the database
        natureFoncierRepository.saveAndFlush(natureFoncier);

        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().size();

        // Update the natureFoncier using partial update
        NatureFoncier partialUpdatedNatureFoncier = new NatureFoncier();
        partialUpdatedNatureFoncier.setId(natureFoncier.getId());

        restNatureFoncierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNatureFoncier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNatureFoncier))
            )
            .andExpect(status().isOk());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
        NatureFoncier testNatureFoncier = natureFoncierList.get(natureFoncierList.size() - 1);
        assertThat(testNatureFoncier.getTypeFoncier()).isEqualTo(DEFAULT_TYPE_FONCIER);
        assertThat(testNatureFoncier.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
    }

    @Test
    @Transactional
    void fullUpdateNatureFoncierWithPatch() throws Exception {
        // Initialize the database
        natureFoncierRepository.saveAndFlush(natureFoncier);

        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().size();

        // Update the natureFoncier using partial update
        NatureFoncier partialUpdatedNatureFoncier = new NatureFoncier();
        partialUpdatedNatureFoncier.setId(natureFoncier.getId());

        partialUpdatedNatureFoncier.typeFoncier(UPDATED_TYPE_FONCIER).pieceJointe(UPDATED_PIECE_JOINTE);

        restNatureFoncierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNatureFoncier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNatureFoncier))
            )
            .andExpect(status().isOk());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
        NatureFoncier testNatureFoncier = natureFoncierList.get(natureFoncierList.size() - 1);
        assertThat(testNatureFoncier.getTypeFoncier()).isEqualTo(UPDATED_TYPE_FONCIER);
        assertThat(testNatureFoncier.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
    }

    @Test
    @Transactional
    void patchNonExistingNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().size();
        natureFoncier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNatureFoncierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, natureFoncier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(natureFoncier))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().size();
        natureFoncier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureFoncierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(natureFoncier))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNatureFoncier() throws Exception {
        int databaseSizeBeforeUpdate = natureFoncierRepository.findAll().size();
        natureFoncier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureFoncierMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(natureFoncier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NatureFoncier in the database
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNatureFoncier() throws Exception {
        // Initialize the database
        natureFoncierRepository.saveAndFlush(natureFoncier);

        int databaseSizeBeforeDelete = natureFoncierRepository.findAll().size();

        // Delete the natureFoncier
        restNatureFoncierMockMvc
            .perform(delete(ENTITY_API_URL_ID, natureFoncier.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NatureFoncier> natureFoncierList = natureFoncierRepository.findAll();
        assertThat(natureFoncierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
