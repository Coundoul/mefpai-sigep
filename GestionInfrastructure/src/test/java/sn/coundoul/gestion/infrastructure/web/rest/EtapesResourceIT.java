package sn.coundoul.gestion.infrastructure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.coundoul.gestion.infrastructure.web.rest.TestUtil.sameInstant;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import sn.coundoul.gestion.infrastructure.domain.Etapes;
import sn.coundoul.gestion.infrastructure.repository.EtapesRepository;

/**
 * Integration tests for the {@link EtapesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EtapesResourceIT {

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NOM_TACHE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_TACHE = "BBBBBBBBBB";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/etapes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtapesRepository etapesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtapesMockMvc;

    private Etapes etapes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etapes createEntity(EntityManager em) {
        Etapes etapes = new Etapes()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .nomTache(DEFAULT_NOM_TACHE)
            .duration(DEFAULT_DURATION);
        return etapes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etapes createUpdatedEntity(EntityManager em) {
        Etapes etapes = new Etapes()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .nomTache(UPDATED_NOM_TACHE)
            .duration(UPDATED_DURATION);
        return etapes;
    }

    @BeforeEach
    public void initTest() {
        etapes = createEntity(em);
    }

    @Test
    @Transactional
    void createEtapes() throws Exception {
        int databaseSizeBeforeCreate = etapesRepository.findAll().size();
        // Create the Etapes
        restEtapesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isCreated());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeCreate + 1);
        Etapes testEtapes = etapesList.get(etapesList.size() - 1);
        assertThat(testEtapes.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testEtapes.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testEtapes.getNomTache()).isEqualTo(DEFAULT_NOM_TACHE);
        assertThat(testEtapes.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void createEtapesWithExistingId() throws Exception {
        // Create the Etapes with an existing ID
        etapes.setId(1L);

        int databaseSizeBeforeCreate = etapesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtapesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = etapesRepository.findAll().size();
        // set the field null
        etapes.setDateDebut(null);

        // Create the Etapes, which fails.

        restEtapesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isBadRequest());

        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = etapesRepository.findAll().size();
        // set the field null
        etapes.setDateFin(null);

        // Create the Etapes, which fails.

        restEtapesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isBadRequest());

        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomTacheIsRequired() throws Exception {
        int databaseSizeBeforeTest = etapesRepository.findAll().size();
        // set the field null
        etapes.setNomTache(null);

        // Create the Etapes, which fails.

        restEtapesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isBadRequest());

        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEtapes() throws Exception {
        // Initialize the database
        etapesRepository.saveAndFlush(etapes);

        // Get all the etapesList
        restEtapesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etapes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].nomTache").value(hasItem(DEFAULT_NOM_TACHE)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @Test
    @Transactional
    void getEtapes() throws Exception {
        // Initialize the database
        etapesRepository.saveAndFlush(etapes);

        // Get the etapes
        restEtapesMockMvc
            .perform(get(ENTITY_API_URL_ID, etapes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etapes.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(sameInstant(DEFAULT_DATE_DEBUT)))
            .andExpect(jsonPath("$.dateFin").value(sameInstant(DEFAULT_DATE_FIN)))
            .andExpect(jsonPath("$.nomTache").value(DEFAULT_NOM_TACHE))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEtapes() throws Exception {
        // Get the etapes
        restEtapesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEtapes() throws Exception {
        // Initialize the database
        etapesRepository.saveAndFlush(etapes);

        int databaseSizeBeforeUpdate = etapesRepository.findAll().size();

        // Update the etapes
        Etapes updatedEtapes = etapesRepository.findById(etapes.getId()).get();
        // Disconnect from session so that the updates on updatedEtapes are not directly saved in db
        em.detach(updatedEtapes);
        updatedEtapes.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).nomTache(UPDATED_NOM_TACHE).duration(UPDATED_DURATION);

        restEtapesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEtapes.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtapes))
            )
            .andExpect(status().isOk());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
        Etapes testEtapes = etapesList.get(etapesList.size() - 1);
        assertThat(testEtapes.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testEtapes.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testEtapes.getNomTache()).isEqualTo(UPDATED_NOM_TACHE);
        assertThat(testEtapes.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void putNonExistingEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().size();
        etapes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtapesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etapes.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().size();
        etapes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().size();
        etapes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapesMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtapesWithPatch() throws Exception {
        // Initialize the database
        etapesRepository.saveAndFlush(etapes);

        int databaseSizeBeforeUpdate = etapesRepository.findAll().size();

        // Update the etapes using partial update
        Etapes partialUpdatedEtapes = new Etapes();
        partialUpdatedEtapes.setId(etapes.getId());

        partialUpdatedEtapes.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).duration(UPDATED_DURATION);

        restEtapesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtapes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtapes))
            )
            .andExpect(status().isOk());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
        Etapes testEtapes = etapesList.get(etapesList.size() - 1);
        assertThat(testEtapes.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testEtapes.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testEtapes.getNomTache()).isEqualTo(DEFAULT_NOM_TACHE);
        assertThat(testEtapes.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void fullUpdateEtapesWithPatch() throws Exception {
        // Initialize the database
        etapesRepository.saveAndFlush(etapes);

        int databaseSizeBeforeUpdate = etapesRepository.findAll().size();

        // Update the etapes using partial update
        Etapes partialUpdatedEtapes = new Etapes();
        partialUpdatedEtapes.setId(etapes.getId());

        partialUpdatedEtapes.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).nomTache(UPDATED_NOM_TACHE).duration(UPDATED_DURATION);

        restEtapesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtapes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtapes))
            )
            .andExpect(status().isOk());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
        Etapes testEtapes = etapesList.get(etapesList.size() - 1);
        assertThat(testEtapes.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testEtapes.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testEtapes.getNomTache()).isEqualTo(UPDATED_NOM_TACHE);
        assertThat(testEtapes.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void patchNonExistingEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().size();
        etapes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtapesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etapes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().size();
        etapes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtapes() throws Exception {
        int databaseSizeBeforeUpdate = etapesRepository.findAll().size();
        etapes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etapes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etapes in the database
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtapes() throws Exception {
        // Initialize the database
        etapesRepository.saveAndFlush(etapes);

        int databaseSizeBeforeDelete = etapesRepository.findAll().size();

        // Delete the etapes
        restEtapesMockMvc
            .perform(delete(ENTITY_API_URL_ID, etapes.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etapes> etapesList = etapesRepository.findAll();
        assertThat(etapesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
