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
import sn.coundoul.gestion.infrastructure.domain.ContratProjet;
import sn.coundoul.gestion.infrastructure.repository.ContratProjetRepository;

/**
 * Integration tests for the {@link ContratProjetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContratProjetResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contrat-projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContratProjetRepository contratProjetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContratProjetMockMvc;

    private ContratProjet contratProjet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContratProjet createEntity(EntityManager em) {
        ContratProjet contratProjet = new ContratProjet().nom(DEFAULT_NOM);
        return contratProjet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContratProjet createUpdatedEntity(EntityManager em) {
        ContratProjet contratProjet = new ContratProjet().nom(UPDATED_NOM);
        return contratProjet;
    }

    @BeforeEach
    public void initTest() {
        contratProjet = createEntity(em);
    }

    @Test
    @Transactional
    void createContratProjet() throws Exception {
        int databaseSizeBeforeCreate = contratProjetRepository.findAll().size();
        // Create the ContratProjet
        restContratProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratProjet))
            )
            .andExpect(status().isCreated());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeCreate + 1);
        ContratProjet testContratProjet = contratProjetList.get(contratProjetList.size() - 1);
        assertThat(testContratProjet.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createContratProjetWithExistingId() throws Exception {
        // Create the ContratProjet with an existing ID
        contratProjet.setId(1L);

        int databaseSizeBeforeCreate = contratProjetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContratProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratProjetRepository.findAll().size();
        // set the field null
        contratProjet.setNom(null);

        // Create the ContratProjet, which fails.

        restContratProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratProjet))
            )
            .andExpect(status().isBadRequest());

        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContratProjets() throws Exception {
        // Initialize the database
        contratProjetRepository.saveAndFlush(contratProjet);

        // Get all the contratProjetList
        restContratProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contratProjet.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getContratProjet() throws Exception {
        // Initialize the database
        contratProjetRepository.saveAndFlush(contratProjet);

        // Get the contratProjet
        restContratProjetMockMvc
            .perform(get(ENTITY_API_URL_ID, contratProjet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contratProjet.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingContratProjet() throws Exception {
        // Get the contratProjet
        restContratProjetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContratProjet() throws Exception {
        // Initialize the database
        contratProjetRepository.saveAndFlush(contratProjet);

        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().size();

        // Update the contratProjet
        ContratProjet updatedContratProjet = contratProjetRepository.findById(contratProjet.getId()).get();
        // Disconnect from session so that the updates on updatedContratProjet are not directly saved in db
        em.detach(updatedContratProjet);
        updatedContratProjet.nom(UPDATED_NOM);

        restContratProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContratProjet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContratProjet))
            )
            .andExpect(status().isOk());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
        ContratProjet testContratProjet = contratProjetList.get(contratProjetList.size() - 1);
        assertThat(testContratProjet.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().size();
        contratProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contratProjet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().size();
        contratProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().size();
        contratProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratProjetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratProjet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContratProjetWithPatch() throws Exception {
        // Initialize the database
        contratProjetRepository.saveAndFlush(contratProjet);

        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().size();

        // Update the contratProjet using partial update
        ContratProjet partialUpdatedContratProjet = new ContratProjet();
        partialUpdatedContratProjet.setId(contratProjet.getId());

        restContratProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContratProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContratProjet))
            )
            .andExpect(status().isOk());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
        ContratProjet testContratProjet = contratProjetList.get(contratProjetList.size() - 1);
        assertThat(testContratProjet.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdateContratProjetWithPatch() throws Exception {
        // Initialize the database
        contratProjetRepository.saveAndFlush(contratProjet);

        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().size();

        // Update the contratProjet using partial update
        ContratProjet partialUpdatedContratProjet = new ContratProjet();
        partialUpdatedContratProjet.setId(contratProjet.getId());

        partialUpdatedContratProjet.nom(UPDATED_NOM);

        restContratProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContratProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContratProjet))
            )
            .andExpect(status().isOk());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
        ContratProjet testContratProjet = contratProjetList.get(contratProjetList.size() - 1);
        assertThat(testContratProjet.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().size();
        contratProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contratProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contratProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().size();
        contratProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contratProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContratProjet() throws Exception {
        int databaseSizeBeforeUpdate = contratProjetRepository.findAll().size();
        contratProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratProjetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contratProjet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContratProjet in the database
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContratProjet() throws Exception {
        // Initialize the database
        contratProjetRepository.saveAndFlush(contratProjet);

        int databaseSizeBeforeDelete = contratProjetRepository.findAll().size();

        // Delete the contratProjet
        restContratProjetMockMvc
            .perform(delete(ENTITY_API_URL_ID, contratProjet.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContratProjet> contratProjetList = contratProjetRepository.findAll();
        assertThat(contratProjetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
