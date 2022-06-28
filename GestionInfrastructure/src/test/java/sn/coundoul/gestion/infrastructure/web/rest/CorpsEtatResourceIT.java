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
import sn.coundoul.gestion.infrastructure.domain.CorpsEtat;
import sn.coundoul.gestion.infrastructure.repository.CorpsEtatRepository;

/**
 * Integration tests for the {@link CorpsEtatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CorpsEtatResourceIT {

    private static final String DEFAULT_NOM_CORPS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CORPS = "BBBBBBBBBB";

    private static final String DEFAULT_GROS_OEUVRE = "AAAAAAAAAA";
    private static final String UPDATED_GROS_OEUVRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_GROS_OEUVRE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_GROS_OEUVRE = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_OEUVRE = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_OEUVRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_SECOND_OEUVRE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_SECOND_OEUVRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OSERVATION = false;
    private static final Boolean UPDATED_OSERVATION = true;

    private static final Boolean DEFAULT_ETAT_CORPS = false;
    private static final Boolean UPDATED_ETAT_CORPS = true;

    private static final String ENTITY_API_URL = "/api/corps-etats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CorpsEtatRepository corpsEtatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCorpsEtatMockMvc;

    private CorpsEtat corpsEtat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CorpsEtat createEntity(EntityManager em) {
        CorpsEtat corpsEtat = new CorpsEtat()
            .nomCorps(DEFAULT_NOM_CORPS)
            .grosOeuvre(DEFAULT_GROS_OEUVRE)
            .descriptionGrosOeuvre(DEFAULT_DESCRIPTION_GROS_OEUVRE)
            .secondOeuvre(DEFAULT_SECOND_OEUVRE)
            .descriptionSecondOeuvre(DEFAULT_DESCRIPTION_SECOND_OEUVRE)
            .oservation(DEFAULT_OSERVATION)
            .etatCorps(DEFAULT_ETAT_CORPS);
        return corpsEtat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CorpsEtat createUpdatedEntity(EntityManager em) {
        CorpsEtat corpsEtat = new CorpsEtat()
            .nomCorps(UPDATED_NOM_CORPS)
            .grosOeuvre(UPDATED_GROS_OEUVRE)
            .descriptionGrosOeuvre(UPDATED_DESCRIPTION_GROS_OEUVRE)
            .secondOeuvre(UPDATED_SECOND_OEUVRE)
            .descriptionSecondOeuvre(UPDATED_DESCRIPTION_SECOND_OEUVRE)
            .oservation(UPDATED_OSERVATION)
            .etatCorps(UPDATED_ETAT_CORPS);
        return corpsEtat;
    }

    @BeforeEach
    public void initTest() {
        corpsEtat = createEntity(em);
    }

    @Test
    @Transactional
    void createCorpsEtat() throws Exception {
        int databaseSizeBeforeCreate = corpsEtatRepository.findAll().size();
        // Create the CorpsEtat
        restCorpsEtatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isCreated());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeCreate + 1);
        CorpsEtat testCorpsEtat = corpsEtatList.get(corpsEtatList.size() - 1);
        assertThat(testCorpsEtat.getNomCorps()).isEqualTo(DEFAULT_NOM_CORPS);
        assertThat(testCorpsEtat.getGrosOeuvre()).isEqualTo(DEFAULT_GROS_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionGrosOeuvre()).isEqualTo(DEFAULT_DESCRIPTION_GROS_OEUVRE);
        assertThat(testCorpsEtat.getSecondOeuvre()).isEqualTo(DEFAULT_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionSecondOeuvre()).isEqualTo(DEFAULT_DESCRIPTION_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getOservation()).isEqualTo(DEFAULT_OSERVATION);
        assertThat(testCorpsEtat.getEtatCorps()).isEqualTo(DEFAULT_ETAT_CORPS);
    }

    @Test
    @Transactional
    void createCorpsEtatWithExistingId() throws Exception {
        // Create the CorpsEtat with an existing ID
        corpsEtat.setId(1L);

        int databaseSizeBeforeCreate = corpsEtatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorpsEtatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomCorpsIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().size();
        // set the field null
        corpsEtat.setNomCorps(null);

        // Create the CorpsEtat, which fails.

        restCorpsEtatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGrosOeuvreIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().size();
        // set the field null
        corpsEtat.setGrosOeuvre(null);

        // Create the CorpsEtat, which fails.

        restCorpsEtatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionGrosOeuvreIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().size();
        // set the field null
        corpsEtat.setDescriptionGrosOeuvre(null);

        // Create the CorpsEtat, which fails.

        restCorpsEtatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSecondOeuvreIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().size();
        // set the field null
        corpsEtat.setSecondOeuvre(null);

        // Create the CorpsEtat, which fails.

        restCorpsEtatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionSecondOeuvreIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().size();
        // set the field null
        corpsEtat.setDescriptionSecondOeuvre(null);

        // Create the CorpsEtat, which fails.

        restCorpsEtatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOservationIsRequired() throws Exception {
        int databaseSizeBeforeTest = corpsEtatRepository.findAll().size();
        // set the field null
        corpsEtat.setOservation(null);

        // Create the CorpsEtat, which fails.

        restCorpsEtatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCorpsEtats() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);

        // Get all the corpsEtatList
        restCorpsEtatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corpsEtat.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCorps").value(hasItem(DEFAULT_NOM_CORPS)))
            .andExpect(jsonPath("$.[*].grosOeuvre").value(hasItem(DEFAULT_GROS_OEUVRE)))
            .andExpect(jsonPath("$.[*].descriptionGrosOeuvre").value(hasItem(DEFAULT_DESCRIPTION_GROS_OEUVRE)))
            .andExpect(jsonPath("$.[*].secondOeuvre").value(hasItem(DEFAULT_SECOND_OEUVRE)))
            .andExpect(jsonPath("$.[*].descriptionSecondOeuvre").value(hasItem(DEFAULT_DESCRIPTION_SECOND_OEUVRE)))
            .andExpect(jsonPath("$.[*].oservation").value(hasItem(DEFAULT_OSERVATION.booleanValue())))
            .andExpect(jsonPath("$.[*].etatCorps").value(hasItem(DEFAULT_ETAT_CORPS.booleanValue())));
    }

    @Test
    @Transactional
    void getCorpsEtat() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);

        // Get the corpsEtat
        restCorpsEtatMockMvc
            .perform(get(ENTITY_API_URL_ID, corpsEtat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(corpsEtat.getId().intValue()))
            .andExpect(jsonPath("$.nomCorps").value(DEFAULT_NOM_CORPS))
            .andExpect(jsonPath("$.grosOeuvre").value(DEFAULT_GROS_OEUVRE))
            .andExpect(jsonPath("$.descriptionGrosOeuvre").value(DEFAULT_DESCRIPTION_GROS_OEUVRE))
            .andExpect(jsonPath("$.secondOeuvre").value(DEFAULT_SECOND_OEUVRE))
            .andExpect(jsonPath("$.descriptionSecondOeuvre").value(DEFAULT_DESCRIPTION_SECOND_OEUVRE))
            .andExpect(jsonPath("$.oservation").value(DEFAULT_OSERVATION.booleanValue()))
            .andExpect(jsonPath("$.etatCorps").value(DEFAULT_ETAT_CORPS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCorpsEtat() throws Exception {
        // Get the corpsEtat
        restCorpsEtatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCorpsEtat() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);

        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().size();

        // Update the corpsEtat
        CorpsEtat updatedCorpsEtat = corpsEtatRepository.findById(corpsEtat.getId()).get();
        // Disconnect from session so that the updates on updatedCorpsEtat are not directly saved in db
        em.detach(updatedCorpsEtat);
        updatedCorpsEtat
            .nomCorps(UPDATED_NOM_CORPS)
            .grosOeuvre(UPDATED_GROS_OEUVRE)
            .descriptionGrosOeuvre(UPDATED_DESCRIPTION_GROS_OEUVRE)
            .secondOeuvre(UPDATED_SECOND_OEUVRE)
            .descriptionSecondOeuvre(UPDATED_DESCRIPTION_SECOND_OEUVRE)
            .oservation(UPDATED_OSERVATION)
            .etatCorps(UPDATED_ETAT_CORPS);

        restCorpsEtatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCorpsEtat.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCorpsEtat))
            )
            .andExpect(status().isOk());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
        CorpsEtat testCorpsEtat = corpsEtatList.get(corpsEtatList.size() - 1);
        assertThat(testCorpsEtat.getNomCorps()).isEqualTo(UPDATED_NOM_CORPS);
        assertThat(testCorpsEtat.getGrosOeuvre()).isEqualTo(UPDATED_GROS_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionGrosOeuvre()).isEqualTo(UPDATED_DESCRIPTION_GROS_OEUVRE);
        assertThat(testCorpsEtat.getSecondOeuvre()).isEqualTo(UPDATED_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionSecondOeuvre()).isEqualTo(UPDATED_DESCRIPTION_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getOservation()).isEqualTo(UPDATED_OSERVATION);
        assertThat(testCorpsEtat.getEtatCorps()).isEqualTo(UPDATED_ETAT_CORPS);
    }

    @Test
    @Transactional
    void putNonExistingCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().size();
        corpsEtat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorpsEtatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, corpsEtat.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().size();
        corpsEtat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorpsEtatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().size();
        corpsEtat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorpsEtatMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCorpsEtatWithPatch() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);

        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().size();

        // Update the corpsEtat using partial update
        CorpsEtat partialUpdatedCorpsEtat = new CorpsEtat();
        partialUpdatedCorpsEtat.setId(corpsEtat.getId());

        partialUpdatedCorpsEtat.nomCorps(UPDATED_NOM_CORPS).oservation(UPDATED_OSERVATION).etatCorps(UPDATED_ETAT_CORPS);

        restCorpsEtatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorpsEtat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCorpsEtat))
            )
            .andExpect(status().isOk());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
        CorpsEtat testCorpsEtat = corpsEtatList.get(corpsEtatList.size() - 1);
        assertThat(testCorpsEtat.getNomCorps()).isEqualTo(UPDATED_NOM_CORPS);
        assertThat(testCorpsEtat.getGrosOeuvre()).isEqualTo(DEFAULT_GROS_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionGrosOeuvre()).isEqualTo(DEFAULT_DESCRIPTION_GROS_OEUVRE);
        assertThat(testCorpsEtat.getSecondOeuvre()).isEqualTo(DEFAULT_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionSecondOeuvre()).isEqualTo(DEFAULT_DESCRIPTION_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getOservation()).isEqualTo(UPDATED_OSERVATION);
        assertThat(testCorpsEtat.getEtatCorps()).isEqualTo(UPDATED_ETAT_CORPS);
    }

    @Test
    @Transactional
    void fullUpdateCorpsEtatWithPatch() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);

        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().size();

        // Update the corpsEtat using partial update
        CorpsEtat partialUpdatedCorpsEtat = new CorpsEtat();
        partialUpdatedCorpsEtat.setId(corpsEtat.getId());

        partialUpdatedCorpsEtat
            .nomCorps(UPDATED_NOM_CORPS)
            .grosOeuvre(UPDATED_GROS_OEUVRE)
            .descriptionGrosOeuvre(UPDATED_DESCRIPTION_GROS_OEUVRE)
            .secondOeuvre(UPDATED_SECOND_OEUVRE)
            .descriptionSecondOeuvre(UPDATED_DESCRIPTION_SECOND_OEUVRE)
            .oservation(UPDATED_OSERVATION)
            .etatCorps(UPDATED_ETAT_CORPS);

        restCorpsEtatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorpsEtat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCorpsEtat))
            )
            .andExpect(status().isOk());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
        CorpsEtat testCorpsEtat = corpsEtatList.get(corpsEtatList.size() - 1);
        assertThat(testCorpsEtat.getNomCorps()).isEqualTo(UPDATED_NOM_CORPS);
        assertThat(testCorpsEtat.getGrosOeuvre()).isEqualTo(UPDATED_GROS_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionGrosOeuvre()).isEqualTo(UPDATED_DESCRIPTION_GROS_OEUVRE);
        assertThat(testCorpsEtat.getSecondOeuvre()).isEqualTo(UPDATED_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getDescriptionSecondOeuvre()).isEqualTo(UPDATED_DESCRIPTION_SECOND_OEUVRE);
        assertThat(testCorpsEtat.getOservation()).isEqualTo(UPDATED_OSERVATION);
        assertThat(testCorpsEtat.getEtatCorps()).isEqualTo(UPDATED_ETAT_CORPS);
    }

    @Test
    @Transactional
    void patchNonExistingCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().size();
        corpsEtat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorpsEtatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, corpsEtat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().size();
        corpsEtat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorpsEtatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCorpsEtat() throws Exception {
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().size();
        corpsEtat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorpsEtatMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(corpsEtat))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCorpsEtat() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);

        int databaseSizeBeforeDelete = corpsEtatRepository.findAll().size();

        // Delete the corpsEtat
        restCorpsEtatMockMvc
            .perform(delete(ENTITY_API_URL_ID, corpsEtat.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CorpsEtat> corpsEtatList = corpsEtatRepository.findAll();
        assertThat(corpsEtatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
