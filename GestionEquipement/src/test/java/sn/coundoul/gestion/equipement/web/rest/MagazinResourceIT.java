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
import sn.coundoul.gestion.equipement.domain.Magazin;
import sn.coundoul.gestion.equipement.repository.MagazinRepository;

/**
 * Integration tests for the {@link MagazinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MagazinResourceIT {

    private static final String DEFAULT_NOM_MAGAZIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_MAGAZIN = "BBBBBBBBBB";

    private static final Double DEFAULT_SURFACE_BATIE = 1D;
    private static final Double UPDATED_SURFACE_BATIE = 2D;

    private static final Double DEFAULT_SUPERFICIE = 1D;
    private static final Double UPDATED_SUPERFICIE = 2D;

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/magazins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MagazinRepository magazinRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMagazinMockMvc;

    private Magazin magazin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Magazin createEntity(EntityManager em) {
        Magazin magazin = new Magazin()
            .nomMagazin(DEFAULT_NOM_MAGAZIN)
            .surfaceBatie(DEFAULT_SURFACE_BATIE)
            .superficie(DEFAULT_SUPERFICIE)
            .idPers(DEFAULT_ID_PERS);
        return magazin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Magazin createUpdatedEntity(EntityManager em) {
        Magazin magazin = new Magazin()
            .nomMagazin(UPDATED_NOM_MAGAZIN)
            .surfaceBatie(UPDATED_SURFACE_BATIE)
            .superficie(UPDATED_SUPERFICIE)
            .idPers(UPDATED_ID_PERS);
        return magazin;
    }

    @BeforeEach
    public void initTest() {
        magazin = createEntity(em);
    }

    @Test
    @Transactional
    void createMagazin() throws Exception {
        int databaseSizeBeforeCreate = magazinRepository.findAll().size();
        // Create the Magazin
        restMagazinMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isCreated());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeCreate + 1);
        Magazin testMagazin = magazinList.get(magazinList.size() - 1);
        assertThat(testMagazin.getNomMagazin()).isEqualTo(DEFAULT_NOM_MAGAZIN);
        assertThat(testMagazin.getSurfaceBatie()).isEqualTo(DEFAULT_SURFACE_BATIE);
        assertThat(testMagazin.getSuperficie()).isEqualTo(DEFAULT_SUPERFICIE);
        assertThat(testMagazin.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    @Transactional
    void createMagazinWithExistingId() throws Exception {
        // Create the Magazin with an existing ID
        magazin.setId(1L);

        int databaseSizeBeforeCreate = magazinRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMagazinMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomMagazinIsRequired() throws Exception {
        int databaseSizeBeforeTest = magazinRepository.findAll().size();
        // set the field null
        magazin.setNomMagazin(null);

        // Create the Magazin, which fails.

        restMagazinMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isBadRequest());

        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSurfaceBatieIsRequired() throws Exception {
        int databaseSizeBeforeTest = magazinRepository.findAll().size();
        // set the field null
        magazin.setSurfaceBatie(null);

        // Create the Magazin, which fails.

        restMagazinMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isBadRequest());

        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSuperficieIsRequired() throws Exception {
        int databaseSizeBeforeTest = magazinRepository.findAll().size();
        // set the field null
        magazin.setSuperficie(null);

        // Create the Magazin, which fails.

        restMagazinMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isBadRequest());

        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = magazinRepository.findAll().size();
        // set the field null
        magazin.setIdPers(null);

        // Create the Magazin, which fails.

        restMagazinMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isBadRequest());

        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMagazins() throws Exception {
        // Initialize the database
        magazinRepository.saveAndFlush(magazin);

        // Get all the magazinList
        restMagazinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magazin.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomMagazin").value(hasItem(DEFAULT_NOM_MAGAZIN)))
            .andExpect(jsonPath("$.[*].surfaceBatie").value(hasItem(DEFAULT_SURFACE_BATIE.doubleValue())))
            .andExpect(jsonPath("$.[*].superficie").value(hasItem(DEFAULT_SUPERFICIE.doubleValue())))
            .andExpect(jsonPath("$.[*].idPers").value(hasItem(DEFAULT_ID_PERS)));
    }

    @Test
    @Transactional
    void getMagazin() throws Exception {
        // Initialize the database
        magazinRepository.saveAndFlush(magazin);

        // Get the magazin
        restMagazinMockMvc
            .perform(get(ENTITY_API_URL_ID, magazin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(magazin.getId().intValue()))
            .andExpect(jsonPath("$.nomMagazin").value(DEFAULT_NOM_MAGAZIN))
            .andExpect(jsonPath("$.surfaceBatie").value(DEFAULT_SURFACE_BATIE.doubleValue()))
            .andExpect(jsonPath("$.superficie").value(DEFAULT_SUPERFICIE.doubleValue()))
            .andExpect(jsonPath("$.idPers").value(DEFAULT_ID_PERS));
    }

    @Test
    @Transactional
    void getNonExistingMagazin() throws Exception {
        // Get the magazin
        restMagazinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMagazin() throws Exception {
        // Initialize the database
        magazinRepository.saveAndFlush(magazin);

        int databaseSizeBeforeUpdate = magazinRepository.findAll().size();

        // Update the magazin
        Magazin updatedMagazin = magazinRepository.findById(magazin.getId()).get();
        // Disconnect from session so that the updates on updatedMagazin are not directly saved in db
        em.detach(updatedMagazin);
        updatedMagazin
            .nomMagazin(UPDATED_NOM_MAGAZIN)
            .surfaceBatie(UPDATED_SURFACE_BATIE)
            .superficie(UPDATED_SUPERFICIE)
            .idPers(UPDATED_ID_PERS);

        restMagazinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMagazin.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMagazin))
            )
            .andExpect(status().isOk());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
        Magazin testMagazin = magazinList.get(magazinList.size() - 1);
        assertThat(testMagazin.getNomMagazin()).isEqualTo(UPDATED_NOM_MAGAZIN);
        assertThat(testMagazin.getSurfaceBatie()).isEqualTo(UPDATED_SURFACE_BATIE);
        assertThat(testMagazin.getSuperficie()).isEqualTo(UPDATED_SUPERFICIE);
        assertThat(testMagazin.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void putNonExistingMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().size();
        magazin.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMagazinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, magazin.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().size();
        magazin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagazinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().size();
        magazin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagazinMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMagazinWithPatch() throws Exception {
        // Initialize the database
        magazinRepository.saveAndFlush(magazin);

        int databaseSizeBeforeUpdate = magazinRepository.findAll().size();

        // Update the magazin using partial update
        Magazin partialUpdatedMagazin = new Magazin();
        partialUpdatedMagazin.setId(magazin.getId());

        partialUpdatedMagazin.surfaceBatie(UPDATED_SURFACE_BATIE).superficie(UPDATED_SUPERFICIE).idPers(UPDATED_ID_PERS);

        restMagazinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMagazin.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMagazin))
            )
            .andExpect(status().isOk());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
        Magazin testMagazin = magazinList.get(magazinList.size() - 1);
        assertThat(testMagazin.getNomMagazin()).isEqualTo(DEFAULT_NOM_MAGAZIN);
        assertThat(testMagazin.getSurfaceBatie()).isEqualTo(UPDATED_SURFACE_BATIE);
        assertThat(testMagazin.getSuperficie()).isEqualTo(UPDATED_SUPERFICIE);
        assertThat(testMagazin.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void fullUpdateMagazinWithPatch() throws Exception {
        // Initialize the database
        magazinRepository.saveAndFlush(magazin);

        int databaseSizeBeforeUpdate = magazinRepository.findAll().size();

        // Update the magazin using partial update
        Magazin partialUpdatedMagazin = new Magazin();
        partialUpdatedMagazin.setId(magazin.getId());

        partialUpdatedMagazin
            .nomMagazin(UPDATED_NOM_MAGAZIN)
            .surfaceBatie(UPDATED_SURFACE_BATIE)
            .superficie(UPDATED_SUPERFICIE)
            .idPers(UPDATED_ID_PERS);

        restMagazinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMagazin.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMagazin))
            )
            .andExpect(status().isOk());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
        Magazin testMagazin = magazinList.get(magazinList.size() - 1);
        assertThat(testMagazin.getNomMagazin()).isEqualTo(UPDATED_NOM_MAGAZIN);
        assertThat(testMagazin.getSurfaceBatie()).isEqualTo(UPDATED_SURFACE_BATIE);
        assertThat(testMagazin.getSuperficie()).isEqualTo(UPDATED_SUPERFICIE);
        assertThat(testMagazin.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void patchNonExistingMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().size();
        magazin.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMagazinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, magazin.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().size();
        magazin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagazinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMagazin() throws Exception {
        int databaseSizeBeforeUpdate = magazinRepository.findAll().size();
        magazin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagazinMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(magazin))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Magazin in the database
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMagazin() throws Exception {
        // Initialize the database
        magazinRepository.saveAndFlush(magazin);

        int databaseSizeBeforeDelete = magazinRepository.findAll().size();

        // Delete the magazin
        restMagazinMockMvc
            .perform(delete(ENTITY_API_URL_ID, magazin.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Magazin> magazinList = magazinRepository.findAll();
        assertThat(magazinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
