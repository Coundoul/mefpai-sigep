package sn.coundoul.gestion.utilisateurs.web.rest;

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
import sn.coundoul.gestion.utilisateurs.IntegrationTest;
import sn.coundoul.gestion.utilisateurs.domain.ChefEtablissement;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;
import sn.coundoul.gestion.utilisateurs.repository.ChefEtablissementRepository;

/**
 * Integration tests for the {@link ChefEtablissementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChefEtablissementResourceIT {

    private static final String DEFAULT_NOM_PERS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PERS = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_PERS = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_PERS = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.Masculin;
    private static final Sexe UPDATED_SEXE = Sexe.Feminin;

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chef-etablissements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChefEtablissementRepository chefEtablissementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChefEtablissementMockMvc;

    private ChefEtablissement chefEtablissement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefEtablissement createEntity(EntityManager em) {
        ChefEtablissement chefEtablissement = new ChefEtablissement()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE);
        return chefEtablissement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefEtablissement createUpdatedEntity(EntityManager em) {
        ChefEtablissement chefEtablissement = new ChefEtablissement()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);
        return chefEtablissement;
    }

    @BeforeEach
    public void initTest() {
        chefEtablissement = createEntity(em);
    }

    @Test
    @Transactional
    void createChefEtablissement() throws Exception {
        int databaseSizeBeforeCreate = chefEtablissementRepository.findAll().size();
        // Create the ChefEtablissement
        restChefEtablissementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isCreated());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeCreate + 1);
        ChefEtablissement testChefEtablissement = chefEtablissementList.get(chefEtablissementList.size() - 1);
        assertThat(testChefEtablissement.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testChefEtablissement.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testChefEtablissement.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefEtablissement.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testChefEtablissement.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void createChefEtablissementWithExistingId() throws Exception {
        // Create the ChefEtablissement with an existing ID
        chefEtablissement.setId(1L);

        int databaseSizeBeforeCreate = chefEtablissementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChefEtablissementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefEtablissementRepository.findAll().size();
        // set the field null
        chefEtablissement.setNomPers(null);

        // Create the ChefEtablissement, which fails.

        restChefEtablissementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isBadRequest());

        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefEtablissementRepository.findAll().size();
        // set the field null
        chefEtablissement.setPrenomPers(null);

        // Create the ChefEtablissement, which fails.

        restChefEtablissementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isBadRequest());

        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefEtablissementRepository.findAll().size();
        // set the field null
        chefEtablissement.setSexe(null);

        // Create the ChefEtablissement, which fails.

        restChefEtablissementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isBadRequest());

        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefEtablissementRepository.findAll().size();
        // set the field null
        chefEtablissement.setMobile(null);

        // Create the ChefEtablissement, which fails.

        restChefEtablissementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isBadRequest());

        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChefEtablissements() throws Exception {
        // Initialize the database
        chefEtablissementRepository.saveAndFlush(chefEtablissement);

        // Get all the chefEtablissementList
        restChefEtablissementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chefEtablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)));
    }

    @Test
    @Transactional
    void getChefEtablissement() throws Exception {
        // Initialize the database
        chefEtablissementRepository.saveAndFlush(chefEtablissement);

        // Get the chefEtablissement
        restChefEtablissementMockMvc
            .perform(get(ENTITY_API_URL_ID, chefEtablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chefEtablissement.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE));
    }

    @Test
    @Transactional
    void getNonExistingChefEtablissement() throws Exception {
        // Get the chefEtablissement
        restChefEtablissementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChefEtablissement() throws Exception {
        // Initialize the database
        chefEtablissementRepository.saveAndFlush(chefEtablissement);

        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().size();

        // Update the chefEtablissement
        ChefEtablissement updatedChefEtablissement = chefEtablissementRepository.findById(chefEtablissement.getId()).get();
        // Disconnect from session so that the updates on updatedChefEtablissement are not directly saved in db
        em.detach(updatedChefEtablissement);
        updatedChefEtablissement
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);

        restChefEtablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChefEtablissement.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChefEtablissement))
            )
            .andExpect(status().isOk());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
        ChefEtablissement testChefEtablissement = chefEtablissementList.get(chefEtablissementList.size() - 1);
        assertThat(testChefEtablissement.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefEtablissement.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefEtablissement.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefEtablissement.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefEtablissement.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void putNonExistingChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefEtablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chefEtablissement.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefEtablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefEtablissementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChefEtablissementWithPatch() throws Exception {
        // Initialize the database
        chefEtablissementRepository.saveAndFlush(chefEtablissement);

        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().size();

        // Update the chefEtablissement using partial update
        ChefEtablissement partialUpdatedChefEtablissement = new ChefEtablissement();
        partialUpdatedChefEtablissement.setId(chefEtablissement.getId());

        partialUpdatedChefEtablissement.nomPers(UPDATED_NOM_PERS).prenomPers(UPDATED_PRENOM_PERS);

        restChefEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChefEtablissement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChefEtablissement))
            )
            .andExpect(status().isOk());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
        ChefEtablissement testChefEtablissement = chefEtablissementList.get(chefEtablissementList.size() - 1);
        assertThat(testChefEtablissement.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefEtablissement.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefEtablissement.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefEtablissement.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testChefEtablissement.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void fullUpdateChefEtablissementWithPatch() throws Exception {
        // Initialize the database
        chefEtablissementRepository.saveAndFlush(chefEtablissement);

        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().size();

        // Update the chefEtablissement using partial update
        ChefEtablissement partialUpdatedChefEtablissement = new ChefEtablissement();
        partialUpdatedChefEtablissement.setId(chefEtablissement.getId());

        partialUpdatedChefEtablissement
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE);

        restChefEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChefEtablissement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChefEtablissement))
            )
            .andExpect(status().isOk());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
        ChefEtablissement testChefEtablissement = chefEtablissementList.get(chefEtablissementList.size() - 1);
        assertThat(testChefEtablissement.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefEtablissement.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefEtablissement.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefEtablissement.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefEtablissement.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void patchNonExistingChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chefEtablissement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChefEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = chefEtablissementRepository.findAll().size();
        chefEtablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefEtablissement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChefEtablissement in the database
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChefEtablissement() throws Exception {
        // Initialize the database
        chefEtablissementRepository.saveAndFlush(chefEtablissement);

        int databaseSizeBeforeDelete = chefEtablissementRepository.findAll().size();

        // Delete the chefEtablissement
        restChefEtablissementMockMvc
            .perform(delete(ENTITY_API_URL_ID, chefEtablissement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChefEtablissement> chefEtablissementList = chefEtablissementRepository.findAll();
        assertThat(chefEtablissementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
