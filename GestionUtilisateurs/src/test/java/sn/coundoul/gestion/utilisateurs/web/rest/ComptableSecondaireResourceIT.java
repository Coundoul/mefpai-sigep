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
import sn.coundoul.gestion.utilisateurs.domain.ComptableSecondaire;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Direction;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;
import sn.coundoul.gestion.utilisateurs.repository.ComptableSecondaireRepository;

/**
 * Integration tests for the {@link ComptableSecondaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComptableSecondaireResourceIT {

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

    private static final String ENTITY_API_URL = "/api/comptable-secondaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComptableSecondaireRepository comptableSecondaireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComptableSecondaireMockMvc;

    private ComptableSecondaire comptableSecondaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComptableSecondaire createEntity(EntityManager em) {
        ComptableSecondaire comptableSecondaire = new ComptableSecondaire()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return comptableSecondaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComptableSecondaire createUpdatedEntity(EntityManager em) {
        ComptableSecondaire comptableSecondaire = new ComptableSecondaire()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return comptableSecondaire;
    }

    @BeforeEach
    public void initTest() {
        comptableSecondaire = createEntity(em);
    }

    @Test
    @Transactional
    void createComptableSecondaire() throws Exception {
        int databaseSizeBeforeCreate = comptableSecondaireRepository.findAll().size();
        // Create the ComptableSecondaire
        restComptableSecondaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isCreated());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeCreate + 1);
        ComptableSecondaire testComptableSecondaire = comptableSecondaireList.get(comptableSecondaireList.size() - 1);
        assertThat(testComptableSecondaire.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testComptableSecondaire.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testComptableSecondaire.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testComptableSecondaire.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testComptableSecondaire.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testComptableSecondaire.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void createComptableSecondaireWithExistingId() throws Exception {
        // Create the ComptableSecondaire with an existing ID
        comptableSecondaire.setId(1L);

        int databaseSizeBeforeCreate = comptableSecondaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComptableSecondaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptableSecondaireRepository.findAll().size();
        // set the field null
        comptableSecondaire.setNomPers(null);

        // Create the ComptableSecondaire, which fails.

        restComptableSecondaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isBadRequest());

        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptableSecondaireRepository.findAll().size();
        // set the field null
        comptableSecondaire.setPrenomPers(null);

        // Create the ComptableSecondaire, which fails.

        restComptableSecondaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isBadRequest());

        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptableSecondaireRepository.findAll().size();
        // set the field null
        comptableSecondaire.setSexe(null);

        // Create the ComptableSecondaire, which fails.

        restComptableSecondaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isBadRequest());

        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptableSecondaireRepository.findAll().size();
        // set the field null
        comptableSecondaire.setMobile(null);

        // Create the ComptableSecondaire, which fails.

        restComptableSecondaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isBadRequest());

        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptableSecondaireRepository.findAll().size();
        // set the field null
        comptableSecondaire.setDirection(null);

        // Create the ComptableSecondaire, which fails.

        restComptableSecondaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isBadRequest());

        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComptableSecondaires() throws Exception {
        // Initialize the database
        comptableSecondaireRepository.saveAndFlush(comptableSecondaire);

        // Get all the comptableSecondaireList
        restComptableSecondaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comptableSecondaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }

    @Test
    @Transactional
    void getComptableSecondaire() throws Exception {
        // Initialize the database
        comptableSecondaireRepository.saveAndFlush(comptableSecondaire);

        // Get the comptableSecondaire
        restComptableSecondaireMockMvc
            .perform(get(ENTITY_API_URL_ID, comptableSecondaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comptableSecondaire.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingComptableSecondaire() throws Exception {
        // Get the comptableSecondaire
        restComptableSecondaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComptableSecondaire() throws Exception {
        // Initialize the database
        comptableSecondaireRepository.saveAndFlush(comptableSecondaire);

        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().size();

        // Update the comptableSecondaire
        ComptableSecondaire updatedComptableSecondaire = comptableSecondaireRepository.findById(comptableSecondaire.getId()).get();
        // Disconnect from session so that the updates on updatedComptableSecondaire are not directly saved in db
        em.detach(updatedComptableSecondaire);
        updatedComptableSecondaire
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restComptableSecondaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComptableSecondaire.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComptableSecondaire))
            )
            .andExpect(status().isOk());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
        ComptableSecondaire testComptableSecondaire = comptableSecondaireList.get(comptableSecondaireList.size() - 1);
        assertThat(testComptableSecondaire.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testComptableSecondaire.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testComptableSecondaire.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testComptableSecondaire.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testComptableSecondaire.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testComptableSecondaire.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void putNonExistingComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComptableSecondaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comptableSecondaire.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComptableSecondaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComptableSecondaireMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComptableSecondaireWithPatch() throws Exception {
        // Initialize the database
        comptableSecondaireRepository.saveAndFlush(comptableSecondaire);

        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().size();

        // Update the comptableSecondaire using partial update
        ComptableSecondaire partialUpdatedComptableSecondaire = new ComptableSecondaire();
        partialUpdatedComptableSecondaire.setId(comptableSecondaire.getId());

        partialUpdatedComptableSecondaire.prenomPers(UPDATED_PRENOM_PERS);

        restComptableSecondaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComptableSecondaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComptableSecondaire))
            )
            .andExpect(status().isOk());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
        ComptableSecondaire testComptableSecondaire = comptableSecondaireList.get(comptableSecondaireList.size() - 1);
        assertThat(testComptableSecondaire.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testComptableSecondaire.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testComptableSecondaire.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testComptableSecondaire.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testComptableSecondaire.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testComptableSecondaire.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void fullUpdateComptableSecondaireWithPatch() throws Exception {
        // Initialize the database
        comptableSecondaireRepository.saveAndFlush(comptableSecondaire);

        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().size();

        // Update the comptableSecondaire using partial update
        ComptableSecondaire partialUpdatedComptableSecondaire = new ComptableSecondaire();
        partialUpdatedComptableSecondaire.setId(comptableSecondaire.getId());

        partialUpdatedComptableSecondaire
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restComptableSecondaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComptableSecondaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComptableSecondaire))
            )
            .andExpect(status().isOk());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
        ComptableSecondaire testComptableSecondaire = comptableSecondaireList.get(comptableSecondaireList.size() - 1);
        assertThat(testComptableSecondaire.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testComptableSecondaire.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testComptableSecondaire.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testComptableSecondaire.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testComptableSecondaire.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testComptableSecondaire.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void patchNonExistingComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComptableSecondaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comptableSecondaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComptableSecondaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComptableSecondaire() throws Exception {
        int databaseSizeBeforeUpdate = comptableSecondaireRepository.findAll().size();
        comptableSecondaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComptableSecondaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comptableSecondaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComptableSecondaire in the database
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComptableSecondaire() throws Exception {
        // Initialize the database
        comptableSecondaireRepository.saveAndFlush(comptableSecondaire);

        int databaseSizeBeforeDelete = comptableSecondaireRepository.findAll().size();

        // Delete the comptableSecondaire
        restComptableSecondaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, comptableSecondaire.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComptableSecondaire> comptableSecondaireList = comptableSecondaireRepository.findAll();
        assertThat(comptableSecondaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
