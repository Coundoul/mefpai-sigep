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
import sn.coundoul.gestion.infrastructure.domain.AttributionInfrastructure;
import sn.coundoul.gestion.infrastructure.repository.AttributionInfrastructureRepository;

/**
 * Integration tests for the {@link AttributionInfrastructureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributionInfrastructureResourceIT {

    private static final Instant DEFAULT_DATE_ATTRIBUTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ATTRIBUTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Integer DEFAULT_ID_EQUIPEMENT = 1;
    private static final Integer UPDATED_ID_EQUIPEMENT = 2;

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/attribution-infrastructures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttributionInfrastructureRepository attributionInfrastructureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttributionInfrastructureMockMvc;

    private AttributionInfrastructure attributionInfrastructure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributionInfrastructure createEntity(EntityManager em) {
        AttributionInfrastructure attributionInfrastructure = new AttributionInfrastructure()
            .dateAttribution(DEFAULT_DATE_ATTRIBUTION)
            .quantite(DEFAULT_QUANTITE)
            .idEquipement(DEFAULT_ID_EQUIPEMENT)
            .idPers(DEFAULT_ID_PERS);
        return attributionInfrastructure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributionInfrastructure createUpdatedEntity(EntityManager em) {
        AttributionInfrastructure attributionInfrastructure = new AttributionInfrastructure()
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);
        return attributionInfrastructure;
    }

    @BeforeEach
    public void initTest() {
        attributionInfrastructure = createEntity(em);
    }

    @Test
    @Transactional
    void createAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeCreate = attributionInfrastructureRepository.findAll().size();
        // Create the AttributionInfrastructure
        restAttributionInfrastructureMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isCreated());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeCreate + 1);
        AttributionInfrastructure testAttributionInfrastructure = attributionInfrastructureList.get(
            attributionInfrastructureList.size() - 1
        );
        assertThat(testAttributionInfrastructure.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
        assertThat(testAttributionInfrastructure.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testAttributionInfrastructure.getIdEquipement()).isEqualTo(DEFAULT_ID_EQUIPEMENT);
        assertThat(testAttributionInfrastructure.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    @Transactional
    void createAttributionInfrastructureWithExistingId() throws Exception {
        // Create the AttributionInfrastructure with an existing ID
        attributionInfrastructure.setId(1L);

        int databaseSizeBeforeCreate = attributionInfrastructureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributionInfrastructureMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributionInfrastructureRepository.findAll().size();
        // set the field null
        attributionInfrastructure.setQuantite(null);

        // Create the AttributionInfrastructure, which fails.

        restAttributionInfrastructureMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isBadRequest());

        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdEquipementIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributionInfrastructureRepository.findAll().size();
        // set the field null
        attributionInfrastructure.setIdEquipement(null);

        // Create the AttributionInfrastructure, which fails.

        restAttributionInfrastructureMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isBadRequest());

        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributionInfrastructureRepository.findAll().size();
        // set the field null
        attributionInfrastructure.setIdPers(null);

        // Create the AttributionInfrastructure, which fails.

        restAttributionInfrastructureMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isBadRequest());

        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAttributionInfrastructures() throws Exception {
        // Initialize the database
        attributionInfrastructureRepository.saveAndFlush(attributionInfrastructure);

        // Get all the attributionInfrastructureList
        restAttributionInfrastructureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributionInfrastructure.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAttribution").value(hasItem(DEFAULT_DATE_ATTRIBUTION.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].idEquipement").value(hasItem(DEFAULT_ID_EQUIPEMENT)))
            .andExpect(jsonPath("$.[*].idPers").value(hasItem(DEFAULT_ID_PERS)));
    }

    @Test
    @Transactional
    void getAttributionInfrastructure() throws Exception {
        // Initialize the database
        attributionInfrastructureRepository.saveAndFlush(attributionInfrastructure);

        // Get the attributionInfrastructure
        restAttributionInfrastructureMockMvc
            .perform(get(ENTITY_API_URL_ID, attributionInfrastructure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attributionInfrastructure.getId().intValue()))
            .andExpect(jsonPath("$.dateAttribution").value(DEFAULT_DATE_ATTRIBUTION.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.idEquipement").value(DEFAULT_ID_EQUIPEMENT))
            .andExpect(jsonPath("$.idPers").value(DEFAULT_ID_PERS));
    }

    @Test
    @Transactional
    void getNonExistingAttributionInfrastructure() throws Exception {
        // Get the attributionInfrastructure
        restAttributionInfrastructureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAttributionInfrastructure() throws Exception {
        // Initialize the database
        attributionInfrastructureRepository.saveAndFlush(attributionInfrastructure);

        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().size();

        // Update the attributionInfrastructure
        AttributionInfrastructure updatedAttributionInfrastructure = attributionInfrastructureRepository
            .findById(attributionInfrastructure.getId())
            .get();
        // Disconnect from session so that the updates on updatedAttributionInfrastructure are not directly saved in db
        em.detach(updatedAttributionInfrastructure);
        updatedAttributionInfrastructure
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);

        restAttributionInfrastructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttributionInfrastructure.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttributionInfrastructure))
            )
            .andExpect(status().isOk());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
        AttributionInfrastructure testAttributionInfrastructure = attributionInfrastructureList.get(
            attributionInfrastructureList.size() - 1
        );
        assertThat(testAttributionInfrastructure.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
        assertThat(testAttributionInfrastructure.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testAttributionInfrastructure.getIdEquipement()).isEqualTo(UPDATED_ID_EQUIPEMENT);
        assertThat(testAttributionInfrastructure.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void putNonExistingAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributionInfrastructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributionInfrastructure.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributionInfrastructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributionInfrastructureMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttributionInfrastructureWithPatch() throws Exception {
        // Initialize the database
        attributionInfrastructureRepository.saveAndFlush(attributionInfrastructure);

        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().size();

        // Update the attributionInfrastructure using partial update
        AttributionInfrastructure partialUpdatedAttributionInfrastructure = new AttributionInfrastructure();
        partialUpdatedAttributionInfrastructure.setId(attributionInfrastructure.getId());

        partialUpdatedAttributionInfrastructure.quantite(UPDATED_QUANTITE);

        restAttributionInfrastructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributionInfrastructure.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributionInfrastructure))
            )
            .andExpect(status().isOk());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
        AttributionInfrastructure testAttributionInfrastructure = attributionInfrastructureList.get(
            attributionInfrastructureList.size() - 1
        );
        assertThat(testAttributionInfrastructure.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
        assertThat(testAttributionInfrastructure.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testAttributionInfrastructure.getIdEquipement()).isEqualTo(DEFAULT_ID_EQUIPEMENT);
        assertThat(testAttributionInfrastructure.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    @Transactional
    void fullUpdateAttributionInfrastructureWithPatch() throws Exception {
        // Initialize the database
        attributionInfrastructureRepository.saveAndFlush(attributionInfrastructure);

        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().size();

        // Update the attributionInfrastructure using partial update
        AttributionInfrastructure partialUpdatedAttributionInfrastructure = new AttributionInfrastructure();
        partialUpdatedAttributionInfrastructure.setId(attributionInfrastructure.getId());

        partialUpdatedAttributionInfrastructure
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .quantite(UPDATED_QUANTITE)
            .idEquipement(UPDATED_ID_EQUIPEMENT)
            .idPers(UPDATED_ID_PERS);

        restAttributionInfrastructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributionInfrastructure.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributionInfrastructure))
            )
            .andExpect(status().isOk());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
        AttributionInfrastructure testAttributionInfrastructure = attributionInfrastructureList.get(
            attributionInfrastructureList.size() - 1
        );
        assertThat(testAttributionInfrastructure.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
        assertThat(testAttributionInfrastructure.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testAttributionInfrastructure.getIdEquipement()).isEqualTo(UPDATED_ID_EQUIPEMENT);
        assertThat(testAttributionInfrastructure.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void patchNonExistingAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributionInfrastructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attributionInfrastructure.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributionInfrastructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttributionInfrastructure() throws Exception {
        int databaseSizeBeforeUpdate = attributionInfrastructureRepository.findAll().size();
        attributionInfrastructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributionInfrastructureMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributionInfrastructure))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributionInfrastructure in the database
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttributionInfrastructure() throws Exception {
        // Initialize the database
        attributionInfrastructureRepository.saveAndFlush(attributionInfrastructure);

        int databaseSizeBeforeDelete = attributionInfrastructureRepository.findAll().size();

        // Delete the attributionInfrastructure
        restAttributionInfrastructureMockMvc
            .perform(delete(ENTITY_API_URL_ID, attributionInfrastructure.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttributionInfrastructure> attributionInfrastructureList = attributionInfrastructureRepository.findAll();
        assertThat(attributionInfrastructureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
