package sn.coundoul.gestion.equipement.web.rest;

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
import sn.coundoul.gestion.equipement.IntegrationTest;
import sn.coundoul.gestion.equipement.domain.Affectations;
import sn.coundoul.gestion.equipement.domain.enumeration.Type;
import sn.coundoul.gestion.equipement.repository.AffectationsRepository;

/**
 * Integration tests for the {@link AffectationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AffectationsResourceIT {

    private static final Integer DEFAULT_QUANTITE_AFFECTER = 1;
    private static final Integer UPDATED_QUANTITE_AFFECTER = 2;

    private static final String DEFAULT_BENEFICIAIRE = "DAGE";
    private static final String UPDATED_BENEFICIAIRE = "DAGE";

    private static final Type DEFAULT_TYPE_ATTRIBUTION = Type.ReAffectation;
    private static final Type UPDATED_TYPE_ATTRIBUTION = Type.Affectation;

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final Instant DEFAULT_DATE_ATTRIBUTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ATTRIBUTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/affectations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AffectationsRepository affectationsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAffectationsMockMvc;

    private Affectations affectations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affectations createEntity(EntityManager em) {
        Affectations affectations = new Affectations()
            .quantiteAffecter(DEFAULT_QUANTITE_AFFECTER)
            .beneficiaire(DEFAULT_BENEFICIAIRE)
            .typeAttribution(DEFAULT_TYPE_ATTRIBUTION)
            .idPers(DEFAULT_ID_PERS)
            .dateAttribution(DEFAULT_DATE_ATTRIBUTION);
        return affectations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affectations createUpdatedEntity(EntityManager em) {
        Affectations affectations = new Affectations()
            .quantiteAffecter(UPDATED_QUANTITE_AFFECTER)
            .beneficiaire(DEFAULT_BENEFICIAIRE)
            .typeAttribution(UPDATED_TYPE_ATTRIBUTION)
            .idPers(UPDATED_ID_PERS)
            .dateAttribution(UPDATED_DATE_ATTRIBUTION);
        return affectations;
    }

    @BeforeEach
    public void initTest() {
        affectations = createEntity(em);
    }

    @Test
    @Transactional
    void createAffectations() throws Exception {
        int databaseSizeBeforeCreate = affectationsRepository.findAll().size();
        // Create the Affectations
        restAffectationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isCreated());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeCreate + 1);
        Affectations testAffectations = affectationsList.get(affectationsList.size() - 1);
        assertThat(testAffectations.getQuantiteAffecter()).isEqualTo(DEFAULT_QUANTITE_AFFECTER);
        assertThat(testAffectations.getBeneficiaire()).isEqualTo(DEFAULT_BENEFICIAIRE);
        assertThat(testAffectations.getTypeAttribution()).isEqualTo(DEFAULT_TYPE_ATTRIBUTION);
        assertThat(testAffectations.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
        assertThat(testAffectations.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
    }

    @Test
    @Transactional
    void createAffectationsWithExistingId() throws Exception {
        // Create the Affectations with an existing ID
        affectations.setId(1L);

        int databaseSizeBeforeCreate = affectationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffectationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantiteAffecterIsRequired() throws Exception {
        int databaseSizeBeforeTest = affectationsRepository.findAll().size();
        // set the field null
        affectations.setQuantiteAffecter(null);

        // Create the Affectations, which fails.

        restAffectationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isBadRequest());

        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeAttributionIsRequired() throws Exception {
        int databaseSizeBeforeTest = affectationsRepository.findAll().size();
        // set the field null
        affectations.setTypeAttribution(null);

        // Create the Affectations, which fails.

        restAffectationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isBadRequest());

        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = affectationsRepository.findAll().size();
        // set the field null
        affectations.setIdPers(null);

        // Create the Affectations, which fails.

        restAffectationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isBadRequest());

        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAffectations() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList
        restAffectationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affectations.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantiteAffecter").value(hasItem(DEFAULT_QUANTITE_AFFECTER)))
            .andExpect(jsonPath("$.[*].beneficiaire").value(hasItem(DEFAULT_BENEFICIAIRE.toString())))
            .andExpect(jsonPath("$.[*].typeAttribution").value(hasItem(DEFAULT_TYPE_ATTRIBUTION.toString())))
            .andExpect(jsonPath("$.[*].idPers").value(hasItem(DEFAULT_ID_PERS)))
            .andExpect(jsonPath("$.[*].dateAttribution").value(hasItem(DEFAULT_DATE_ATTRIBUTION.toString())));
    }

    @Test
    @Transactional
    void getAffectations() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get the affectations
        restAffectationsMockMvc
            .perform(get(ENTITY_API_URL_ID, affectations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(affectations.getId().intValue()))
            .andExpect(jsonPath("$.quantiteAffecter").value(DEFAULT_QUANTITE_AFFECTER))
            .andExpect(jsonPath("$.beneficiaire").value(DEFAULT_BENEFICIAIRE.toString()))
            .andExpect(jsonPath("$.typeAttribution").value(DEFAULT_TYPE_ATTRIBUTION.toString()))
            .andExpect(jsonPath("$.idPers").value(DEFAULT_ID_PERS))
            .andExpect(jsonPath("$.dateAttribution").value(DEFAULT_DATE_ATTRIBUTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAffectations() throws Exception {
        // Get the affectations
        restAffectationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAffectations() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();

        // Update the affectations
        Affectations updatedAffectations = affectationsRepository.findById(affectations.getId()).get();
        // Disconnect from session so that the updates on updatedAffectations are not directly saved in db
        em.detach(updatedAffectations);
        updatedAffectations
            .quantiteAffecter(UPDATED_QUANTITE_AFFECTER)
            .typeAttribution(UPDATED_TYPE_ATTRIBUTION)
            .beneficiaire(UPDATED_BENEFICIAIRE)
            .idPers(UPDATED_ID_PERS)
            .dateAttribution(UPDATED_DATE_ATTRIBUTION);

        restAffectationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAffectations.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAffectations))
            )
            .andExpect(status().isOk());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
        Affectations testAffectations = affectationsList.get(affectationsList.size() - 1);
        assertThat(testAffectations.getQuantiteAffecter()).isEqualTo(UPDATED_QUANTITE_AFFECTER);
        assertThat(testAffectations.getBeneficiaire()).isEqualTo(UPDATED_BENEFICIAIRE);
        assertThat(testAffectations.getTypeAttribution()).isEqualTo(UPDATED_TYPE_ATTRIBUTION);
        assertThat(testAffectations.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testAffectations.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
    }

    @Test
    @Transactional
    void putNonExistingAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();
        affectations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affectations.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();
        affectations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();
        affectations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAffectationsWithPatch() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();

        // Update the affectations using partial update
        Affectations partialUpdatedAffectations = new Affectations();
        partialUpdatedAffectations.setId(affectations.getId());

        restAffectationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectations.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffectations))
            )
            .andExpect(status().isOk());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
        Affectations testAffectations = affectationsList.get(affectationsList.size() - 1);
        assertThat(testAffectations.getQuantiteAffecter()).isEqualTo(DEFAULT_QUANTITE_AFFECTER);
        assertThat(testAffectations.getBeneficiaire()).isEqualTo(DEFAULT_BENEFICIAIRE);
        assertThat(testAffectations.getTypeAttribution()).isEqualTo(DEFAULT_TYPE_ATTRIBUTION);
        assertThat(testAffectations.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
        assertThat(testAffectations.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
    }

    @Test
    @Transactional
    void fullUpdateAffectationsWithPatch() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();

        // Update the affectations using partial update
        Affectations partialUpdatedAffectations = new Affectations();
        partialUpdatedAffectations.setId(affectations.getId());

        partialUpdatedAffectations
            .quantiteAffecter(UPDATED_QUANTITE_AFFECTER)
            .typeAttribution(UPDATED_TYPE_ATTRIBUTION)
            .idPers(UPDATED_ID_PERS)
            .dateAttribution(UPDATED_DATE_ATTRIBUTION);

        restAffectationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectations.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffectations))
            )
            .andExpect(status().isOk());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
        Affectations testAffectations = affectationsList.get(affectationsList.size() - 1);
        assertThat(testAffectations.getQuantiteAffecter()).isEqualTo(UPDATED_QUANTITE_AFFECTER);
        assertThat(testAffectations.getBeneficiaire()).isEqualTo(UPDATED_BENEFICIAIRE);
        assertThat(testAffectations.getTypeAttribution()).isEqualTo(UPDATED_TYPE_ATTRIBUTION);
        assertThat(testAffectations.getIdPers()).isEqualTo(UPDATED_ID_PERS);
        assertThat(testAffectations.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
    }

    @Test
    @Transactional
    void patchNonExistingAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();
        affectations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, affectations.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAffectations() throws Exception {
        
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();
        affectations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();
        affectations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affectations))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAffectations() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        int databaseSizeBeforeDelete = affectationsRepository.findAll().size();

        // Delete the affectations
        restAffectationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, affectations.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
