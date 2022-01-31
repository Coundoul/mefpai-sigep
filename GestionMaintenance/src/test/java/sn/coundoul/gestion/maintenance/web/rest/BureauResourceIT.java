package sn.coundoul.gestion.maintenance.web.rest;

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
import sn.coundoul.gestion.maintenance.IntegrationTest;
import sn.coundoul.gestion.maintenance.domain.Bureau;
import sn.coundoul.gestion.maintenance.domain.enumeration.Direction;
import sn.coundoul.gestion.maintenance.domain.enumeration.NomS;
import sn.coundoul.gestion.maintenance.repository.BureauRepository;

/**
 * Integration tests for the {@link BureauResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BureauResourceIT {

    private static final NomS DEFAULT_NOM_STRUCTURE = NomS.Etablissement;
    private static final NomS UPDATED_NOM_STRUCTURE = NomS.MEFPAI;

    private static final Direction DEFAULT_DIRECTION = Direction.DAGE;
    private static final Direction UPDATED_DIRECTION = Direction.DFPT;

    private static final String DEFAULT_NOM_ETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ETABLISSEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bureaus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BureauRepository bureauRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBureauMockMvc;

    private Bureau bureau;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bureau createEntity(EntityManager em) {
        Bureau bureau = new Bureau()
            .nomStructure(DEFAULT_NOM_STRUCTURE)
            .direction(DEFAULT_DIRECTION)
            .nomEtablissement(DEFAULT_NOM_ETABLISSEMENT);
        return bureau;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bureau createUpdatedEntity(EntityManager em) {
        Bureau bureau = new Bureau()
            .nomStructure(UPDATED_NOM_STRUCTURE)
            .direction(UPDATED_DIRECTION)
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT);
        return bureau;
    }

    @BeforeEach
    public void initTest() {
        bureau = createEntity(em);
    }

    @Test
    @Transactional
    void createBureau() throws Exception {
        int databaseSizeBeforeCreate = bureauRepository.findAll().size();
        // Create the Bureau
        restBureauMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bureau))
            )
            .andExpect(status().isCreated());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeCreate + 1);
        Bureau testBureau = bureauList.get(bureauList.size() - 1);
        assertThat(testBureau.getNomStructure()).isEqualTo(DEFAULT_NOM_STRUCTURE);
        assertThat(testBureau.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testBureau.getNomEtablissement()).isEqualTo(DEFAULT_NOM_ETABLISSEMENT);
    }

    @Test
    @Transactional
    void createBureauWithExistingId() throws Exception {
        // Create the Bureau with an existing ID
        bureau.setId(1L);

        int databaseSizeBeforeCreate = bureauRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBureauMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bureau))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomStructureIsRequired() throws Exception {
        int databaseSizeBeforeTest = bureauRepository.findAll().size();
        // set the field null
        bureau.setNomStructure(null);

        // Create the Bureau, which fails.

        restBureauMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bureau))
            )
            .andExpect(status().isBadRequest());

        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBureaus() throws Exception {
        // Initialize the database
        bureauRepository.saveAndFlush(bureau);

        // Get all the bureauList
        restBureauMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bureau.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomStructure").value(hasItem(DEFAULT_NOM_STRUCTURE.toString())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].nomEtablissement").value(hasItem(DEFAULT_NOM_ETABLISSEMENT)));
    }

    @Test
    @Transactional
    void getBureau() throws Exception {
        // Initialize the database
        bureauRepository.saveAndFlush(bureau);

        // Get the bureau
        restBureauMockMvc
            .perform(get(ENTITY_API_URL_ID, bureau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bureau.getId().intValue()))
            .andExpect(jsonPath("$.nomStructure").value(DEFAULT_NOM_STRUCTURE.toString()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()))
            .andExpect(jsonPath("$.nomEtablissement").value(DEFAULT_NOM_ETABLISSEMENT));
    }

    @Test
    @Transactional
    void getNonExistingBureau() throws Exception {
        // Get the bureau
        restBureauMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBureau() throws Exception {
        // Initialize the database
        bureauRepository.saveAndFlush(bureau);

        int databaseSizeBeforeUpdate = bureauRepository.findAll().size();

        // Update the bureau
        Bureau updatedBureau = bureauRepository.findById(bureau.getId()).get();
        // Disconnect from session so that the updates on updatedBureau are not directly saved in db
        em.detach(updatedBureau);
        updatedBureau.nomStructure(UPDATED_NOM_STRUCTURE).direction(UPDATED_DIRECTION).nomEtablissement(UPDATED_NOM_ETABLISSEMENT);

        restBureauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBureau.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBureau))
            )
            .andExpect(status().isOk());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
        Bureau testBureau = bureauList.get(bureauList.size() - 1);
        assertThat(testBureau.getNomStructure()).isEqualTo(UPDATED_NOM_STRUCTURE);
        assertThat(testBureau.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testBureau.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
    }

    @Test
    @Transactional
    void putNonExistingBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().size();
        bureau.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBureauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bureau.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bureau))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().size();
        bureau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBureauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bureau))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().size();
        bureau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBureauMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bureau))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBureauWithPatch() throws Exception {
        // Initialize the database
        bureauRepository.saveAndFlush(bureau);

        int databaseSizeBeforeUpdate = bureauRepository.findAll().size();

        // Update the bureau using partial update
        Bureau partialUpdatedBureau = new Bureau();
        partialUpdatedBureau.setId(bureau.getId());

        restBureauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBureau.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBureau))
            )
            .andExpect(status().isOk());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
        Bureau testBureau = bureauList.get(bureauList.size() - 1);
        assertThat(testBureau.getNomStructure()).isEqualTo(DEFAULT_NOM_STRUCTURE);
        assertThat(testBureau.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testBureau.getNomEtablissement()).isEqualTo(DEFAULT_NOM_ETABLISSEMENT);
    }

    @Test
    @Transactional
    void fullUpdateBureauWithPatch() throws Exception {
        // Initialize the database
        bureauRepository.saveAndFlush(bureau);

        int databaseSizeBeforeUpdate = bureauRepository.findAll().size();

        // Update the bureau using partial update
        Bureau partialUpdatedBureau = new Bureau();
        partialUpdatedBureau.setId(bureau.getId());

        partialUpdatedBureau.nomStructure(UPDATED_NOM_STRUCTURE).direction(UPDATED_DIRECTION).nomEtablissement(UPDATED_NOM_ETABLISSEMENT);

        restBureauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBureau.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBureau))
            )
            .andExpect(status().isOk());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
        Bureau testBureau = bureauList.get(bureauList.size() - 1);
        assertThat(testBureau.getNomStructure()).isEqualTo(UPDATED_NOM_STRUCTURE);
        assertThat(testBureau.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testBureau.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().size();
        bureau.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBureauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bureau.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bureau))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().size();
        bureau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBureauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bureau))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBureau() throws Exception {
        int databaseSizeBeforeUpdate = bureauRepository.findAll().size();
        bureau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBureauMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bureau))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bureau in the database
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBureau() throws Exception {
        // Initialize the database
        bureauRepository.saveAndFlush(bureau);

        int databaseSizeBeforeDelete = bureauRepository.findAll().size();

        // Delete the bureau
        restBureauMockMvc
            .perform(delete(ENTITY_API_URL_ID, bureau.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bureau> bureauList = bureauRepository.findAll();
        assertThat(bureauList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
