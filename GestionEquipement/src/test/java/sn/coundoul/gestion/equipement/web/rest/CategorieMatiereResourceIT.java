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
import sn.coundoul.gestion.equipement.domain.CategorieMatiere;
import sn.coundoul.gestion.equipement.repository.CategorieMatiereRepository;

/**
 * Integration tests for the {@link CategorieMatiereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategorieMatiereResourceIT {

    private static final String DEFAULT_CATEGORIE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categorie-matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategorieMatiereRepository categorieMatiereRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategorieMatiereMockMvc;

    private CategorieMatiere categorieMatiere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieMatiere createEntity(EntityManager em) {
        CategorieMatiere categorieMatiere = new CategorieMatiere().categorie(DEFAULT_CATEGORIE);
        return categorieMatiere;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieMatiere createUpdatedEntity(EntityManager em) {
        CategorieMatiere categorieMatiere = new CategorieMatiere().categorie(UPDATED_CATEGORIE);
        return categorieMatiere;
    }

    @BeforeEach
    public void initTest() {
        categorieMatiere = createEntity(em);
    }

    @Test
    @Transactional
    void createCategorieMatiere() throws Exception {
        int databaseSizeBeforeCreate = categorieMatiereRepository.findAll().size();
        // Create the CategorieMatiere
        restCategorieMatiereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            )
            .andExpect(status().isCreated());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeCreate + 1);
        CategorieMatiere testCategorieMatiere = categorieMatiereList.get(categorieMatiereList.size() - 1);
        assertThat(testCategorieMatiere.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
    }

    @Test
    @Transactional
    void createCategorieMatiereWithExistingId() throws Exception {
        // Create the CategorieMatiere with an existing ID
        categorieMatiere.setId(1L);

        int databaseSizeBeforeCreate = categorieMatiereRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorieMatiereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategorieIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieMatiereRepository.findAll().size();
        // set the field null
        categorieMatiere.setCategorie(null);

        // Create the CategorieMatiere, which fails.

        restCategorieMatiereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            )
            .andExpect(status().isBadRequest());

        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategorieMatieres() throws Exception {
        // Initialize the database
        categorieMatiereRepository.saveAndFlush(categorieMatiere);

        // Get all the categorieMatiereList
        restCategorieMatiereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorieMatiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE)));
    }

    @Test
    @Transactional
    void getCategorieMatiere() throws Exception {
        // Initialize the database
        categorieMatiereRepository.saveAndFlush(categorieMatiere);

        // Get the categorieMatiere
        restCategorieMatiereMockMvc
            .perform(get(ENTITY_API_URL_ID, categorieMatiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categorieMatiere.getId().intValue()))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE));
    }

    @Test
    @Transactional
    void getNonExistingCategorieMatiere() throws Exception {
        // Get the categorieMatiere
        restCategorieMatiereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategorieMatiere() throws Exception {
        // Initialize the database
        categorieMatiereRepository.saveAndFlush(categorieMatiere);

        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().size();

        // Update the categorieMatiere
        CategorieMatiere updatedCategorieMatiere = categorieMatiereRepository.findById(categorieMatiere.getId()).get();
        // Disconnect from session so that the updates on updatedCategorieMatiere are not directly saved in db
        em.detach(updatedCategorieMatiere);
        updatedCategorieMatiere.categorie(UPDATED_CATEGORIE);

        restCategorieMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategorieMatiere.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCategorieMatiere))
            )
            .andExpect(status().isOk());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
        CategorieMatiere testCategorieMatiere = categorieMatiereList.get(categorieMatiereList.size() - 1);
        assertThat(testCategorieMatiere.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
    }

    @Test
    @Transactional
    void putNonExistingCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categorieMatiere.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMatiereMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategorieMatiereWithPatch() throws Exception {
        // Initialize the database
        categorieMatiereRepository.saveAndFlush(categorieMatiere);

        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().size();

        // Update the categorieMatiere using partial update
        CategorieMatiere partialUpdatedCategorieMatiere = new CategorieMatiere();
        partialUpdatedCategorieMatiere.setId(categorieMatiere.getId());

        restCategorieMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorieMatiere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorieMatiere))
            )
            .andExpect(status().isOk());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
        CategorieMatiere testCategorieMatiere = categorieMatiereList.get(categorieMatiereList.size() - 1);
        assertThat(testCategorieMatiere.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
    }

    @Test
    @Transactional
    void fullUpdateCategorieMatiereWithPatch() throws Exception {
        // Initialize the database
        categorieMatiereRepository.saveAndFlush(categorieMatiere);

        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().size();

        // Update the categorieMatiere using partial update
        CategorieMatiere partialUpdatedCategorieMatiere = new CategorieMatiere();
        partialUpdatedCategorieMatiere.setId(categorieMatiere.getId());

        partialUpdatedCategorieMatiere.categorie(UPDATED_CATEGORIE);

        restCategorieMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorieMatiere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorieMatiere))
            )
            .andExpect(status().isOk());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
        CategorieMatiere testCategorieMatiere = categorieMatiereList.get(categorieMatiereList.size() - 1);
        assertThat(testCategorieMatiere.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
    }

    @Test
    @Transactional
    void patchNonExistingCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categorieMatiere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategorieMatiere() throws Exception {
        int databaseSizeBeforeUpdate = categorieMatiereRepository.findAll().size();
        categorieMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieMatiere))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategorieMatiere in the database
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategorieMatiere() throws Exception {
        // Initialize the database
        categorieMatiereRepository.saveAndFlush(categorieMatiere);

        int databaseSizeBeforeDelete = categorieMatiereRepository.findAll().size();

        // Delete the categorieMatiere
        restCategorieMatiereMockMvc
            .perform(delete(ENTITY_API_URL_ID, categorieMatiere.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategorieMatiere> categorieMatiereList = categorieMatiereRepository.findAll();
        assertThat(categorieMatiereList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
