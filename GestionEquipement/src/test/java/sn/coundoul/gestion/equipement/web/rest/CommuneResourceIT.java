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
import sn.coundoul.gestion.equipement.domain.Commune;
import sn.coundoul.gestion.equipement.repository.CommuneRepository;

/**
 * Integration tests for the {@link CommuneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommuneResourceIT {

    private static final String DEFAULT_NOM_COMMUNE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMMUNE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/communes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommuneRepository communeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommuneMockMvc;

    private Commune commune;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commune createEntity(EntityManager em) {
        Commune commune = new Commune().nomCommune(DEFAULT_NOM_COMMUNE);
        return commune;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commune createUpdatedEntity(EntityManager em) {
        Commune commune = new Commune().nomCommune(UPDATED_NOM_COMMUNE);
        return commune;
    }

    @BeforeEach
    public void initTest() {
        commune = createEntity(em);
    }

    @Test
    @Transactional
    void createCommune() throws Exception {
        int databaseSizeBeforeCreate = communeRepository.findAll().size();
        // Create the Commune
        restCommuneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isCreated());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeCreate + 1);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getNomCommune()).isEqualTo(DEFAULT_NOM_COMMUNE);
    }

    @Test
    @Transactional
    void createCommuneWithExistingId() throws Exception {
        // Create the Commune with an existing ID
        commune.setId(1L);

        int databaseSizeBeforeCreate = communeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommuneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomCommuneIsRequired() throws Exception {
        int databaseSizeBeforeTest = communeRepository.findAll().size();
        // set the field null
        commune.setNomCommune(null);

        // Create the Commune, which fails.

        restCommuneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommunes() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList
        restCommuneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commune.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCommune").value(hasItem(DEFAULT_NOM_COMMUNE)));
    }

    @Test
    @Transactional
    void getCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get the commune
        restCommuneMockMvc
            .perform(get(ENTITY_API_URL_ID, commune.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commune.getId().intValue()))
            .andExpect(jsonPath("$.nomCommune").value(DEFAULT_NOM_COMMUNE));
    }

    @Test
    @Transactional
    void getNonExistingCommune() throws Exception {
        // Get the commune
        restCommuneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune
        Commune updatedCommune = communeRepository.findById(commune.getId()).get();
        // Disconnect from session so that the updates on updatedCommune are not directly saved in db
        em.detach(updatedCommune);
        updatedCommune.nomCommune(UPDATED_NOM_COMMUNE);

        restCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommune.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommune))
            )
            .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getNomCommune()).isEqualTo(UPDATED_NOM_COMMUNE);
    }

    @Test
    @Transactional
    void putNonExistingCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commune.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommuneWithPatch() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune using partial update
        Commune partialUpdatedCommune = new Commune();
        partialUpdatedCommune.setId(commune.getId());

        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommune.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommune))
            )
            .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getNomCommune()).isEqualTo(DEFAULT_NOM_COMMUNE);
    }

    @Test
    @Transactional
    void fullUpdateCommuneWithPatch() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune using partial update
        Commune partialUpdatedCommune = new Commune();
        partialUpdatedCommune.setId(commune.getId());

        partialUpdatedCommune.nomCommune(UPDATED_NOM_COMMUNE);

        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommune.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommune))
            )
            .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getNomCommune()).isEqualTo(UPDATED_NOM_COMMUNE);
    }

    @Test
    @Transactional
    void patchNonExistingCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commune.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeDelete = communeRepository.findAll().size();

        // Delete the commune
        restCommuneMockMvc
            .perform(delete(ENTITY_API_URL_ID, commune.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
