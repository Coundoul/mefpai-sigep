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
import sn.coundoul.gestion.infrastructure.domain.Atelier;
import sn.coundoul.gestion.infrastructure.repository.AtelierRepository;

/**
 * Integration tests for the {@link AtelierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AtelierResourceIT {

    private static final String DEFAULT_NOM_ATELIER = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ATELIER = "BBBBBBBBBB";

    private static final Double DEFAULT_SURFACE = 1D;
    private static final Double UPDATED_SURFACE = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ateliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AtelierRepository atelierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAtelierMockMvc;

    private Atelier atelier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atelier createEntity(EntityManager em) {
        Atelier atelier = new Atelier().nomAtelier(DEFAULT_NOM_ATELIER).surface(DEFAULT_SURFACE).description(DEFAULT_DESCRIPTION);
        return atelier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atelier createUpdatedEntity(EntityManager em) {
        Atelier atelier = new Atelier().nomAtelier(UPDATED_NOM_ATELIER).surface(UPDATED_SURFACE).description(UPDATED_DESCRIPTION);
        return atelier;
    }

    @BeforeEach
    public void initTest() {
        atelier = createEntity(em);
    }

    @Test
    @Transactional
    void createAtelier() throws Exception {
        int databaseSizeBeforeCreate = atelierRepository.findAll().size();
        // Create the Atelier
        restAtelierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isCreated());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeCreate + 1);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getNomAtelier()).isEqualTo(DEFAULT_NOM_ATELIER);
        assertThat(testAtelier.getSurface()).isEqualTo(DEFAULT_SURFACE);
        assertThat(testAtelier.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createAtelierWithExistingId() throws Exception {
        // Create the Atelier with an existing ID
        atelier.setId(1L);

        int databaseSizeBeforeCreate = atelierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAtelierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomAtelierIsRequired() throws Exception {
        int databaseSizeBeforeTest = atelierRepository.findAll().size();
        // set the field null
        atelier.setNomAtelier(null);

        // Create the Atelier, which fails.

        restAtelierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSurfaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = atelierRepository.findAll().size();
        // set the field null
        atelier.setSurface(null);

        // Create the Atelier, which fails.

        restAtelierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = atelierRepository.findAll().size();
        // set the field null
        atelier.setDescription(null);

        // Create the Atelier, which fails.

        restAtelierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAteliers() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        // Get all the atelierList
        restAtelierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atelier.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomAtelier").value(hasItem(DEFAULT_NOM_ATELIER)))
            .andExpect(jsonPath("$.[*].surface").value(hasItem(DEFAULT_SURFACE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getAtelier() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        // Get the atelier
        restAtelierMockMvc
            .perform(get(ENTITY_API_URL_ID, atelier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(atelier.getId().intValue()))
            .andExpect(jsonPath("$.nomAtelier").value(DEFAULT_NOM_ATELIER))
            .andExpect(jsonPath("$.surface").value(DEFAULT_SURFACE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingAtelier() throws Exception {
        // Get the atelier
        restAtelierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAtelier() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();

        // Update the atelier
        Atelier updatedAtelier = atelierRepository.findById(atelier.getId()).get();
        // Disconnect from session so that the updates on updatedAtelier are not directly saved in db
        em.detach(updatedAtelier);
        updatedAtelier.nomAtelier(UPDATED_NOM_ATELIER).surface(UPDATED_SURFACE).description(UPDATED_DESCRIPTION);

        restAtelierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAtelier.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAtelier))
            )
            .andExpect(status().isOk());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getNomAtelier()).isEqualTo(UPDATED_NOM_ATELIER);
        assertThat(testAtelier.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testAtelier.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, atelier.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAtelierWithPatch() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();

        // Update the atelier using partial update
        Atelier partialUpdatedAtelier = new Atelier();
        partialUpdatedAtelier.setId(atelier.getId());

        partialUpdatedAtelier.nomAtelier(UPDATED_NOM_ATELIER).surface(UPDATED_SURFACE).description(UPDATED_DESCRIPTION);

        restAtelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtelier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtelier))
            )
            .andExpect(status().isOk());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getNomAtelier()).isEqualTo(UPDATED_NOM_ATELIER);
        assertThat(testAtelier.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testAtelier.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateAtelierWithPatch() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();

        // Update the atelier using partial update
        Atelier partialUpdatedAtelier = new Atelier();
        partialUpdatedAtelier.setId(atelier.getId());

        partialUpdatedAtelier.nomAtelier(UPDATED_NOM_ATELIER).surface(UPDATED_SURFACE).description(UPDATED_DESCRIPTION);

        restAtelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtelier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtelier))
            )
            .andExpect(status().isOk());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getNomAtelier()).isEqualTo(UPDATED_NOM_ATELIER);
        assertThat(testAtelier.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testAtelier.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, atelier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAtelier() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        int databaseSizeBeforeDelete = atelierRepository.findAll().size();

        // Delete the atelier
        restAtelierMockMvc
            .perform(delete(ENTITY_API_URL_ID, atelier.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
