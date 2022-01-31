package sn.coundoul.gestion.infrastructure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.coundoul.gestion.infrastructure.web.rest.TestUtil.sameInstant;

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
import sn.coundoul.gestion.infrastructure.domain.Projets;
import sn.coundoul.gestion.infrastructure.domain.enumeration.TypeProjet;
import sn.coundoul.gestion.infrastructure.repository.ProjetsRepository;

/**
 * Integration tests for the {@link ProjetsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjetsResourceIT {

    private static final TypeProjet DEFAULT_TYPE_PROJET = TypeProjet.Construction;
    private static final TypeProjet UPDATED_TYPE_PROJET = TypeProjet.Rehabilitation;

    private static final String DEFAULT_NOM_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PROJET = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EXTENSION = false;
    private static final Boolean UPDATED_EXTENSION = true;

    private static final String ENTITY_API_URL = "/api/projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjetsRepository projetsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjetsMockMvc;

    private Projets projets;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projets createEntity(EntityManager em) {
        Projets projets = new Projets()
            .typeProjet(DEFAULT_TYPE_PROJET)
            .nomProjet(DEFAULT_NOM_PROJET)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .description(DEFAULT_DESCRIPTION)
            .extension(DEFAULT_EXTENSION);
        return projets;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projets createUpdatedEntity(EntityManager em) {
        Projets projets = new Projets()
            .typeProjet(UPDATED_TYPE_PROJET)
            .nomProjet(UPDATED_NOM_PROJET)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION)
            .extension(UPDATED_EXTENSION);
        return projets;
    }

    @BeforeEach
    public void initTest() {
        projets = createEntity(em);
    }

    @Test
    @Transactional
    void createProjets() throws Exception {
        int databaseSizeBeforeCreate = projetsRepository.findAll().size();
        // Create the Projets
        restProjetsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isCreated());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeCreate + 1);
        Projets testProjets = projetsList.get(projetsList.size() - 1);
        assertThat(testProjets.getTypeProjet()).isEqualTo(DEFAULT_TYPE_PROJET);
        assertThat(testProjets.getNomProjet()).isEqualTo(DEFAULT_NOM_PROJET);
        assertThat(testProjets.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testProjets.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testProjets.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjets.getExtension()).isEqualTo(DEFAULT_EXTENSION);
    }

    @Test
    @Transactional
    void createProjetsWithExistingId() throws Exception {
        // Create the Projets with an existing ID
        projets.setId(1L);

        int databaseSizeBeforeCreate = projetsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjetsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().size();
        // set the field null
        projets.setTypeProjet(null);

        // Create the Projets, which fails.

        restProjetsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().size();
        // set the field null
        projets.setNomProjet(null);

        // Create the Projets, which fails.

        restProjetsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().size();
        // set the field null
        projets.setDateDebut(null);

        // Create the Projets, which fails.

        restProjetsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().size();
        // set the field null
        projets.setDateFin(null);

        // Create the Projets, which fails.

        restProjetsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().size();
        // set the field null
        projets.setDescription(null);

        // Create the Projets, which fails.

        restProjetsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExtensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetsRepository.findAll().size();
        // set the field null
        projets.setExtension(null);

        // Create the Projets, which fails.

        restProjetsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjets() throws Exception {
        // Initialize the database
        projetsRepository.saveAndFlush(projets);

        // Get all the projetsList
        restProjetsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projets.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeProjet").value(hasItem(DEFAULT_TYPE_PROJET.toString())))
            .andExpect(jsonPath("$.[*].nomProjet").value(hasItem(DEFAULT_NOM_PROJET)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].extension").value(hasItem(DEFAULT_EXTENSION.booleanValue())));
    }

    @Test
    @Transactional
    void getProjets() throws Exception {
        // Initialize the database
        projetsRepository.saveAndFlush(projets);

        // Get the projets
        restProjetsMockMvc
            .perform(get(ENTITY_API_URL_ID, projets.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projets.getId().intValue()))
            .andExpect(jsonPath("$.typeProjet").value(DEFAULT_TYPE_PROJET.toString()))
            .andExpect(jsonPath("$.nomProjet").value(DEFAULT_NOM_PROJET))
            .andExpect(jsonPath("$.dateDebut").value(sameInstant(DEFAULT_DATE_DEBUT)))
            .andExpect(jsonPath("$.dateFin").value(sameInstant(DEFAULT_DATE_FIN)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.extension").value(DEFAULT_EXTENSION.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingProjets() throws Exception {
        // Get the projets
        restProjetsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProjets() throws Exception {
        // Initialize the database
        projetsRepository.saveAndFlush(projets);

        int databaseSizeBeforeUpdate = projetsRepository.findAll().size();

        // Update the projets
        Projets updatedProjets = projetsRepository.findById(projets.getId()).get();
        // Disconnect from session so that the updates on updatedProjets are not directly saved in db
        em.detach(updatedProjets);
        updatedProjets
            .typeProjet(UPDATED_TYPE_PROJET)
            .nomProjet(UPDATED_NOM_PROJET)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION)
            .extension(UPDATED_EXTENSION);

        restProjetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjets.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjets))
            )
            .andExpect(status().isOk());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
        Projets testProjets = projetsList.get(projetsList.size() - 1);
        assertThat(testProjets.getTypeProjet()).isEqualTo(UPDATED_TYPE_PROJET);
        assertThat(testProjets.getNomProjet()).isEqualTo(UPDATED_NOM_PROJET);
        assertThat(testProjets.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjets.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProjets.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjets.getExtension()).isEqualTo(UPDATED_EXTENSION);
    }

    @Test
    @Transactional
    void putNonExistingProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().size();
        projets.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projets.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().size();
        projets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().size();
        projets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetsMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjetsWithPatch() throws Exception {
        // Initialize the database
        projetsRepository.saveAndFlush(projets);

        int databaseSizeBeforeUpdate = projetsRepository.findAll().size();

        // Update the projets using partial update
        Projets partialUpdatedProjets = new Projets();
        partialUpdatedProjets.setId(projets.getId());

        partialUpdatedProjets.typeProjet(UPDATED_TYPE_PROJET).dateDebut(UPDATED_DATE_DEBUT);

        restProjetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjets.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjets))
            )
            .andExpect(status().isOk());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
        Projets testProjets = projetsList.get(projetsList.size() - 1);
        assertThat(testProjets.getTypeProjet()).isEqualTo(UPDATED_TYPE_PROJET);
        assertThat(testProjets.getNomProjet()).isEqualTo(DEFAULT_NOM_PROJET);
        assertThat(testProjets.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjets.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testProjets.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjets.getExtension()).isEqualTo(DEFAULT_EXTENSION);
    }

    @Test
    @Transactional
    void fullUpdateProjetsWithPatch() throws Exception {
        // Initialize the database
        projetsRepository.saveAndFlush(projets);

        int databaseSizeBeforeUpdate = projetsRepository.findAll().size();

        // Update the projets using partial update
        Projets partialUpdatedProjets = new Projets();
        partialUpdatedProjets.setId(projets.getId());

        partialUpdatedProjets
            .typeProjet(UPDATED_TYPE_PROJET)
            .nomProjet(UPDATED_NOM_PROJET)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION)
            .extension(UPDATED_EXTENSION);

        restProjetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjets.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjets))
            )
            .andExpect(status().isOk());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
        Projets testProjets = projetsList.get(projetsList.size() - 1);
        assertThat(testProjets.getTypeProjet()).isEqualTo(UPDATED_TYPE_PROJET);
        assertThat(testProjets.getNomProjet()).isEqualTo(UPDATED_NOM_PROJET);
        assertThat(testProjets.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjets.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProjets.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjets.getExtension()).isEqualTo(UPDATED_EXTENSION);
    }

    @Test
    @Transactional
    void patchNonExistingProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().size();
        projets.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projets.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().size();
        projets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjets() throws Exception {
        int databaseSizeBeforeUpdate = projetsRepository.findAll().size();
        projets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projets))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projets in the database
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjets() throws Exception {
        // Initialize the database
        projetsRepository.saveAndFlush(projets);

        int databaseSizeBeforeDelete = projetsRepository.findAll().size();

        // Delete the projets
        restProjetsMockMvc
            .perform(delete(ENTITY_API_URL_ID, projets.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Projets> projetsList = projetsRepository.findAll();
        assertThat(projetsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
