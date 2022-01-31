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
import sn.coundoul.gestion.utilisateurs.domain.ChefProjet;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Direction;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;
import sn.coundoul.gestion.utilisateurs.repository.ChefProjetRepository;

/**
 * Integration tests for the {@link ChefProjetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChefProjetResourceIT {

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

    private static final Direction DEFAULT_DIRECTION = Direction.DAGE;
    private static final Direction UPDATED_DIRECTION = Direction.DFPT;

    private static final String ENTITY_API_URL = "/api/chef-projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChefProjetRepository chefProjetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChefProjetMockMvc;

    private ChefProjet chefProjet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefProjet createEntity(EntityManager em) {
        ChefProjet chefProjet = new ChefProjet()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return chefProjet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefProjet createUpdatedEntity(EntityManager em) {
        ChefProjet chefProjet = new ChefProjet()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return chefProjet;
    }

    @BeforeEach
    public void initTest() {
        chefProjet = createEntity(em);
    }

    @Test
    @Transactional
    void createChefProjet() throws Exception {
        int databaseSizeBeforeCreate = chefProjetRepository.findAll().size();
        // Create the ChefProjet
        restChefProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isCreated());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeCreate + 1);
        ChefProjet testChefProjet = chefProjetList.get(chefProjetList.size() - 1);
        assertThat(testChefProjet.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testChefProjet.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testChefProjet.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefProjet.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testChefProjet.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testChefProjet.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void createChefProjetWithExistingId() throws Exception {
        // Create the ChefProjet with an existing ID
        chefProjet.setId(1L);

        int databaseSizeBeforeCreate = chefProjetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChefProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProjetRepository.findAll().size();
        // set the field null
        chefProjet.setNomPers(null);

        // Create the ChefProjet, which fails.

        restChefProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isBadRequest());

        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProjetRepository.findAll().size();
        // set the field null
        chefProjet.setPrenomPers(null);

        // Create the ChefProjet, which fails.

        restChefProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isBadRequest());

        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProjetRepository.findAll().size();
        // set the field null
        chefProjet.setSexe(null);

        // Create the ChefProjet, which fails.

        restChefProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isBadRequest());

        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProjetRepository.findAll().size();
        // set the field null
        chefProjet.setMobile(null);

        // Create the ChefProjet, which fails.

        restChefProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isBadRequest());

        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChefProjets() throws Exception {
        // Initialize the database
        chefProjetRepository.saveAndFlush(chefProjet);

        // Get all the chefProjetList
        restChefProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chefProjet.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }

    @Test
    @Transactional
    void getChefProjet() throws Exception {
        // Initialize the database
        chefProjetRepository.saveAndFlush(chefProjet);

        // Get the chefProjet
        restChefProjetMockMvc
            .perform(get(ENTITY_API_URL_ID, chefProjet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chefProjet.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChefProjet() throws Exception {
        // Get the chefProjet
        restChefProjetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChefProjet() throws Exception {
        // Initialize the database
        chefProjetRepository.saveAndFlush(chefProjet);

        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().size();

        // Update the chefProjet
        ChefProjet updatedChefProjet = chefProjetRepository.findById(chefProjet.getId()).get();
        // Disconnect from session so that the updates on updatedChefProjet are not directly saved in db
        em.detach(updatedChefProjet);
        updatedChefProjet
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restChefProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChefProjet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChefProjet))
            )
            .andExpect(status().isOk());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
        ChefProjet testChefProjet = chefProjetList.get(chefProjetList.size() - 1);
        assertThat(testChefProjet.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefProjet.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefProjet.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefProjet.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefProjet.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChefProjet.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void putNonExistingChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().size();
        chefProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chefProjet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().size();
        chefProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().size();
        chefProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefProjetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChefProjetWithPatch() throws Exception {
        // Initialize the database
        chefProjetRepository.saveAndFlush(chefProjet);

        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().size();

        // Update the chefProjet using partial update
        ChefProjet partialUpdatedChefProjet = new ChefProjet();
        partialUpdatedChefProjet.setId(chefProjet.getId());

        partialUpdatedChefProjet.prenomPers(UPDATED_PRENOM_PERS).mobile(UPDATED_MOBILE).adresse(UPDATED_ADRESSE);

        restChefProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChefProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChefProjet))
            )
            .andExpect(status().isOk());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
        ChefProjet testChefProjet = chefProjetList.get(chefProjetList.size() - 1);
        assertThat(testChefProjet.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testChefProjet.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefProjet.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefProjet.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefProjet.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChefProjet.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void fullUpdateChefProjetWithPatch() throws Exception {
        // Initialize the database
        chefProjetRepository.saveAndFlush(chefProjet);

        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().size();

        // Update the chefProjet using partial update
        ChefProjet partialUpdatedChefProjet = new ChefProjet();
        partialUpdatedChefProjet.setId(chefProjet.getId());

        partialUpdatedChefProjet
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restChefProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChefProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChefProjet))
            )
            .andExpect(status().isOk());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
        ChefProjet testChefProjet = chefProjetList.get(chefProjetList.size() - 1);
        assertThat(testChefProjet.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testChefProjet.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testChefProjet.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefProjet.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testChefProjet.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChefProjet.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void patchNonExistingChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().size();
        chefProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chefProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().size();
        chefProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = chefProjetRepository.findAll().size();
        chefProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefProjetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefProjet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChefProjet in the database
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChefProjet() throws Exception {
        // Initialize the database
        chefProjetRepository.saveAndFlush(chefProjet);

        int databaseSizeBeforeDelete = chefProjetRepository.findAll().size();

        // Delete the chefProjet
        restChefProjetMockMvc
            .perform(delete(ENTITY_API_URL_ID, chefProjet.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChefProjet> chefProjetList = chefProjetRepository.findAll();
        assertThat(chefProjetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
