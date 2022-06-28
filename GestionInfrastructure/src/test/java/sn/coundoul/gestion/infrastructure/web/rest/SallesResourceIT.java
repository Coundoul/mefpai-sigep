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
import sn.coundoul.gestion.infrastructure.domain.Salles;
import sn.coundoul.gestion.infrastructure.domain.enumeration.Classe;
import sn.coundoul.gestion.infrastructure.repository.SallesRepository;

/**
 * Integration tests for the {@link SallesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SallesResourceIT {

    private static final String DEFAULT_NOM_SALLE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SALLE = "BBBBBBBBBB";

    private static final Classe DEFAULT_CLASSE = Classe.ClassePhysique;
    private static final Classe UPDATED_CLASSE = Classe.ClassePedagogique;

    private static final String ENTITY_API_URL = "/api/salles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SallesRepository sallesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSallesMockMvc;

    private Salles salles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salles createEntity(EntityManager em) {
        Salles salles = new Salles().nomSalle(DEFAULT_NOM_SALLE).classe(DEFAULT_CLASSE);
        return salles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salles createUpdatedEntity(EntityManager em) {
        Salles salles = new Salles().nomSalle(UPDATED_NOM_SALLE).classe(UPDATED_CLASSE);
        return salles;
    }

    @BeforeEach
    public void initTest() {
        salles = createEntity(em);
    }

    @Test
    @Transactional
    void createSalles() throws Exception {
        int databaseSizeBeforeCreate = sallesRepository.findAll().size();
        // Create the Salles
        restSallesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salles))
            )
            .andExpect(status().isCreated());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeCreate + 1);
        Salles testSalles = sallesList.get(sallesList.size() - 1);
        assertThat(testSalles.getNomSalle()).isEqualTo(DEFAULT_NOM_SALLE);
        assertThat(testSalles.getClasse()).isEqualTo(DEFAULT_CLASSE);
    }

    @Test
    @Transactional
    void createSallesWithExistingId() throws Exception {
        // Create the Salles with an existing ID
        salles.setId(1L);

        int databaseSizeBeforeCreate = sallesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSallesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomSalleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sallesRepository.findAll().size();
        // set the field null
        salles.setNomSalle(null);

        // Create the Salles, which fails.

        restSallesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salles))
            )
            .andExpect(status().isBadRequest());

        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClasseIsRequired() throws Exception {
        int databaseSizeBeforeTest = sallesRepository.findAll().size();
        // set the field null
        salles.setClasse(null);

        // Create the Salles, which fails.

        restSallesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salles))
            )
            .andExpect(status().isBadRequest());

        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSalles() throws Exception {
        // Initialize the database
        sallesRepository.saveAndFlush(salles);

        // Get all the sallesList
        restSallesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salles.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomSalle").value(hasItem(DEFAULT_NOM_SALLE)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE.toString())));
    }

    @Test
    @Transactional
    void getSalles() throws Exception {
        // Initialize the database
        sallesRepository.saveAndFlush(salles);

        // Get the salles
        restSallesMockMvc
            .perform(get(ENTITY_API_URL_ID, salles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salles.getId().intValue()))
            .andExpect(jsonPath("$.nomSalle").value(DEFAULT_NOM_SALLE))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSalles() throws Exception {
        // Get the salles
        restSallesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSalles() throws Exception {
        // Initialize the database
        sallesRepository.saveAndFlush(salles);

        int databaseSizeBeforeUpdate = sallesRepository.findAll().size();

        // Update the salles
        Salles updatedSalles = sallesRepository.findById(salles.getId()).get();
        // Disconnect from session so that the updates on updatedSalles are not directly saved in db
        em.detach(updatedSalles);
        updatedSalles.nomSalle(UPDATED_NOM_SALLE).classe(UPDATED_CLASSE);

        restSallesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSalles.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSalles))
            )
            .andExpect(status().isOk());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
        Salles testSalles = sallesList.get(sallesList.size() - 1);
        assertThat(testSalles.getNomSalle()).isEqualTo(UPDATED_NOM_SALLE);
        assertThat(testSalles.getClasse()).isEqualTo(UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void putNonExistingSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().size();
        salles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSallesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salles.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().size();
        salles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSallesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().size();
        salles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSallesMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salles))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSallesWithPatch() throws Exception {
        // Initialize the database
        sallesRepository.saveAndFlush(salles);

        int databaseSizeBeforeUpdate = sallesRepository.findAll().size();

        // Update the salles using partial update
        Salles partialUpdatedSalles = new Salles();
        partialUpdatedSalles.setId(salles.getId());

        restSallesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalles.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalles))
            )
            .andExpect(status().isOk());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
        Salles testSalles = sallesList.get(sallesList.size() - 1);
        assertThat(testSalles.getNomSalle()).isEqualTo(DEFAULT_NOM_SALLE);
        assertThat(testSalles.getClasse()).isEqualTo(DEFAULT_CLASSE);
    }

    @Test
    @Transactional
    void fullUpdateSallesWithPatch() throws Exception {
        // Initialize the database
        sallesRepository.saveAndFlush(salles);

        int databaseSizeBeforeUpdate = sallesRepository.findAll().size();

        // Update the salles using partial update
        Salles partialUpdatedSalles = new Salles();
        partialUpdatedSalles.setId(salles.getId());

        partialUpdatedSalles.nomSalle(UPDATED_NOM_SALLE).classe(UPDATED_CLASSE);

        restSallesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalles.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalles))
            )
            .andExpect(status().isOk());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
        Salles testSalles = sallesList.get(sallesList.size() - 1);
        assertThat(testSalles.getNomSalle()).isEqualTo(UPDATED_NOM_SALLE);
        assertThat(testSalles.getClasse()).isEqualTo(UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void patchNonExistingSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().size();
        salles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSallesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salles.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().size();
        salles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSallesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalles() throws Exception {
        int databaseSizeBeforeUpdate = sallesRepository.findAll().size();
        salles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSallesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salles))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salles in the database
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalles() throws Exception {
        // Initialize the database
        sallesRepository.saveAndFlush(salles);

        int databaseSizeBeforeDelete = sallesRepository.findAll().size();

        // Delete the salles
        restSallesMockMvc
            .perform(delete(ENTITY_API_URL_ID, salles.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Salles> sallesList = sallesRepository.findAll();
        assertThat(sallesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
