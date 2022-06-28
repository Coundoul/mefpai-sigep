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
import sn.coundoul.gestion.infrastructure.domain.Quartier;
import sn.coundoul.gestion.infrastructure.repository.QuartierRepository;

/**
 * Integration tests for the {@link QuartierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuartierResourceIT {

    private static final String DEFAULT_NOM_QUARTIER = "AAAAAAAAAA";
    private static final String UPDATED_NOM_QUARTIER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quartiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuartierRepository quartierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuartierMockMvc;

    private Quartier quartier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quartier createEntity(EntityManager em) {
        Quartier quartier = new Quartier().nomQuartier(DEFAULT_NOM_QUARTIER);
        return quartier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quartier createUpdatedEntity(EntityManager em) {
        Quartier quartier = new Quartier().nomQuartier(UPDATED_NOM_QUARTIER);
        return quartier;
    }

    @BeforeEach
    public void initTest() {
        quartier = createEntity(em);
    }

    @Test
    @Transactional
    void createQuartier() throws Exception {
        int databaseSizeBeforeCreate = quartierRepository.findAll().size();
        // Create the Quartier
        restQuartierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quartier))
            )
            .andExpect(status().isCreated());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeCreate + 1);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNomQuartier()).isEqualTo(DEFAULT_NOM_QUARTIER);
    }

    @Test
    @Transactional
    void createQuartierWithExistingId() throws Exception {
        // Create the Quartier with an existing ID
        quartier.setId(1L);

        int databaseSizeBeforeCreate = quartierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuartierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quartier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomQuartierIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartierRepository.findAll().size();
        // set the field null
        quartier.setNomQuartier(null);

        // Create the Quartier, which fails.

        restQuartierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quartier))
            )
            .andExpect(status().isBadRequest());

        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuartiers() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        // Get all the quartierList
        restQuartierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quartier.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomQuartier").value(hasItem(DEFAULT_NOM_QUARTIER)));
    }

    @Test
    @Transactional
    void getQuartier() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        // Get the quartier
        restQuartierMockMvc
            .perform(get(ENTITY_API_URL_ID, quartier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quartier.getId().intValue()))
            .andExpect(jsonPath("$.nomQuartier").value(DEFAULT_NOM_QUARTIER));
    }

    @Test
    @Transactional
    void getNonExistingQuartier() throws Exception {
        // Get the quartier
        restQuartierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuartier() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();

        // Update the quartier
        Quartier updatedQuartier = quartierRepository.findById(quartier.getId()).get();
        // Disconnect from session so that the updates on updatedQuartier are not directly saved in db
        em.detach(updatedQuartier);
        updatedQuartier.nomQuartier(UPDATED_NOM_QUARTIER);

        restQuartierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuartier.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuartier))
            )
            .andExpect(status().isOk());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNomQuartier()).isEqualTo(UPDATED_NOM_QUARTIER);
    }

    @Test
    @Transactional
    void putNonExistingQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quartier.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quartier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quartier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quartier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuartierWithPatch() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();

        // Update the quartier using partial update
        Quartier partialUpdatedQuartier = new Quartier();
        partialUpdatedQuartier.setId(quartier.getId());

        partialUpdatedQuartier.nomQuartier(UPDATED_NOM_QUARTIER);

        restQuartierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuartier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuartier))
            )
            .andExpect(status().isOk());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNomQuartier()).isEqualTo(UPDATED_NOM_QUARTIER);
    }

    @Test
    @Transactional
    void fullUpdateQuartierWithPatch() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();

        // Update the quartier using partial update
        Quartier partialUpdatedQuartier = new Quartier();
        partialUpdatedQuartier.setId(quartier.getId());

        partialUpdatedQuartier.nomQuartier(UPDATED_NOM_QUARTIER);

        restQuartierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuartier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuartier))
            )
            .andExpect(status().isOk());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNomQuartier()).isEqualTo(UPDATED_NOM_QUARTIER);
    }

    @Test
    @Transactional
    void patchNonExistingQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quartier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quartier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quartier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quartier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuartier() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        int databaseSizeBeforeDelete = quartierRepository.findAll().size();

        // Delete the quartier
        restQuartierMockMvc
            .perform(delete(ENTITY_API_URL_ID, quartier.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
