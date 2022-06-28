package sn.coundoul.gestion.infrastructure.web.rest;

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
import sn.coundoul.gestion.infrastructure.IntegrationTest;
import sn.coundoul.gestion.infrastructure.domain.ProjetAttribution;
import sn.coundoul.gestion.infrastructure.repository.ProjetAttributionRepository;

/**
 * Integration tests for the {@link ProjetAttributionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjetAttributionResourceIT {

    private static final Instant DEFAULT_DATE_ATTRIBUTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ATTRIBUTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Integer DEFAULT_ID_EQUIPEMENT = 1;
    private static final Integer UPDATED_ID_EQUIPEMENT = 2;

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/projet-attributions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjetAttributionRepository projetAttributionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjetAttributionMockMvc;

    private ProjetAttribution projetAttribution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjetAttribution createEntity(EntityManager em) {
        ProjetAttribution projetAttribution = new ProjetAttribution()
            .dateAttribution(DEFAULT_DATE_ATTRIBUTION)
            .quantite(DEFAULT_QUANTITE)
            .idEquipement(DEFAULT_ID_EQUIPEMENT)
            .idPers(DEFAULT_ID_PERS);
        return projetAttribution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjetAttribution createUpdatedEntity(EntityManager em) {
        ProjetAttribution projetAttribution = new ProjetAttribution()
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);
        return projetAttribution;
    }

    @BeforeEach
    public void initTest() {
        projetAttribution = createEntity(em);
    }

    @Test
    @Transactional
    void createProjetAttribution() throws Exception {
        int databaseSizeBeforeCreate = projetAttributionRepository.findAll().size();
        // Create the ProjetAttribution
        restProjetAttributionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isCreated());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeCreate + 1);
        ProjetAttribution testProjetAttribution = projetAttributionList.get(projetAttributionList.size() - 1);
        assertThat(testProjetAttribution.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
        assertThat(testProjetAttribution.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testProjetAttribution.getIdEquipement()).isEqualTo(DEFAULT_ID_EQUIPEMENT);
        assertThat(testProjetAttribution.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    @Transactional
    void createProjetAttributionWithExistingId() throws Exception {
        // Create the ProjetAttribution with an existing ID
        projetAttribution.setId(1L);

        int databaseSizeBeforeCreate = projetAttributionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjetAttributionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetAttributionRepository.findAll().size();
        // set the field null
        projetAttribution.setQuantite(null);

        // Create the ProjetAttribution, which fails.

        restProjetAttributionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isBadRequest());

        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdEquipementIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetAttributionRepository.findAll().size();
        // set the field null
        projetAttribution.setIdEquipement(null);

        // Create the ProjetAttribution, which fails.

        restProjetAttributionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isBadRequest());

        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetAttributionRepository.findAll().size();
        // set the field null
        projetAttribution.setIdPers(null);

        // Create the ProjetAttribution, which fails.

        restProjetAttributionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isBadRequest());

        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjetAttributions() throws Exception {
        // Initialize the database
        projetAttributionRepository.saveAndFlush(projetAttribution);

        // Get all the projetAttributionList
        restProjetAttributionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projetAttribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAttribution").value(hasItem(DEFAULT_DATE_ATTRIBUTION.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].idEquipement").value(hasItem(DEFAULT_ID_EQUIPEMENT)))
            .andExpect(jsonPath("$.[*].idPers").value(hasItem(DEFAULT_ID_PERS)));
    }

    @Test
    @Transactional
    void getProjetAttribution() throws Exception {
        // Initialize the database
        projetAttributionRepository.saveAndFlush(projetAttribution);

        // Get the projetAttribution
        restProjetAttributionMockMvc
            .perform(get(ENTITY_API_URL_ID, projetAttribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projetAttribution.getId().intValue()))
            .andExpect(jsonPath("$.dateAttribution").value(DEFAULT_DATE_ATTRIBUTION.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.idEquipement").value(DEFAULT_ID_EQUIPEMENT))
            .andExpect(jsonPath("$.idPers").value(DEFAULT_ID_PERS));
    }

    @Test
    @Transactional
    void getNonExistingProjetAttribution() throws Exception {
        // Get the projetAttribution
        restProjetAttributionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProjetAttribution() throws Exception {
        // Initialize the database
        projetAttributionRepository.saveAndFlush(projetAttribution);

        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().size();

        // Update the projetAttribution
        ProjetAttribution updatedProjetAttribution = projetAttributionRepository.findById(projetAttribution.getId()).get();
        // Disconnect from session so that the updates on updatedProjetAttribution are not directly saved in db
        em.detach(updatedProjetAttribution);
        updatedProjetAttribution
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);

        restProjetAttributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjetAttribution.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjetAttribution))
            )
            .andExpect(status().isOk());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
        ProjetAttribution testProjetAttribution = projetAttributionList.get(projetAttributionList.size() - 1);
        assertThat(testProjetAttribution.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
        assertThat(testProjetAttribution.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testProjetAttribution.getIdEquipement()).isEqualTo(UPDATED_ID_EQUIPEMENT);
        assertThat(testProjetAttribution.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void putNonExistingProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().size();
        projetAttribution.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetAttributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projetAttribution.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().size();
        projetAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetAttributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().size();
        projetAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetAttributionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjetAttributionWithPatch() throws Exception {
        // Initialize the database
        projetAttributionRepository.saveAndFlush(projetAttribution);

        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().size();

        // Update the projetAttribution using partial update
        ProjetAttribution partialUpdatedProjetAttribution = new ProjetAttribution();
        partialUpdatedProjetAttribution.setId(projetAttribution.getId());

        partialUpdatedProjetAttribution.quantite(UPDATED_QUANTITE).idEquipement(UPDATED_ID_EQUIPEMENT);

        restProjetAttributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjetAttribution.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjetAttribution))
            )
            .andExpect(status().isOk());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
        ProjetAttribution testProjetAttribution = projetAttributionList.get(projetAttributionList.size() - 1);
        assertThat(testProjetAttribution.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
        assertThat(testProjetAttribution.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testProjetAttribution.getIdEquipement()).isEqualTo(UPDATED_ID_EQUIPEMENT);
        assertThat(testProjetAttribution.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    @Transactional
    void fullUpdateProjetAttributionWithPatch() throws Exception {
        // Initialize the database
        projetAttributionRepository.saveAndFlush(projetAttribution);

        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().size();

        // Update the projetAttribution using partial update
        ProjetAttribution partialUpdatedProjetAttribution = new ProjetAttribution();
        partialUpdatedProjetAttribution.setId(projetAttribution.getId());

        partialUpdatedProjetAttribution
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);

        restProjetAttributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjetAttribution.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjetAttribution))
            )
            .andExpect(status().isOk());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
        ProjetAttribution testProjetAttribution = projetAttributionList.get(projetAttributionList.size() - 1);
        assertThat(testProjetAttribution.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
        assertThat(testProjetAttribution.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testProjetAttribution.getIdEquipement()).isEqualTo(UPDATED_ID_EQUIPEMENT);
        assertThat(testProjetAttribution.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void patchNonExistingProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().size();
        projetAttribution.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetAttributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projetAttribution.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().size();
        projetAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetAttributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjetAttribution() throws Exception {
        int databaseSizeBeforeUpdate = projetAttributionRepository.findAll().size();
        projetAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetAttributionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projetAttribution))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjetAttribution in the database
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjetAttribution() throws Exception {
        // Initialize the database
        projetAttributionRepository.saveAndFlush(projetAttribution);

        int databaseSizeBeforeDelete = projetAttributionRepository.findAll().size();

        // Delete the projetAttribution
        restProjetAttributionMockMvc
            .perform(delete(ENTITY_API_URL_ID, projetAttribution.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProjetAttribution> projetAttributionList = projetAttributionRepository.findAll();
        assertThat(projetAttributionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
