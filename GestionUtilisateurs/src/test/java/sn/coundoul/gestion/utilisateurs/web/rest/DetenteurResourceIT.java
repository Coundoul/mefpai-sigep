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
import sn.coundoul.gestion.utilisateurs.domain.Detenteur;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Direction;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;
import sn.coundoul.gestion.utilisateurs.repository.DetenteurRepository;

/**
 * Integration tests for the {@link DetenteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetenteurResourceIT {

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

    private static final String ENTITY_API_URL = "/api/detenteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetenteurRepository detenteurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetenteurMockMvc;

    private Detenteur detenteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Detenteur createEntity(EntityManager em) {
        Detenteur detenteur = new Detenteur()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return detenteur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Detenteur createUpdatedEntity(EntityManager em) {
        Detenteur detenteur = new Detenteur()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return detenteur;
    }

    @BeforeEach
    public void initTest() {
        detenteur = createEntity(em);
    }

    @Test
    @Transactional
    void createDetenteur() throws Exception {
        int databaseSizeBeforeCreate = detenteurRepository.findAll().size();
        // Create the Detenteur
        restDetenteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isCreated());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeCreate + 1);
        Detenteur testDetenteur = detenteurList.get(detenteurList.size() - 1);
        assertThat(testDetenteur.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testDetenteur.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testDetenteur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDetenteur.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testDetenteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDetenteur.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void createDetenteurWithExistingId() throws Exception {
        // Create the Detenteur with an existing ID
        detenteur.setId(1L);

        int databaseSizeBeforeCreate = detenteurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetenteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = detenteurRepository.findAll().size();
        // set the field null
        detenteur.setNomPers(null);

        // Create the Detenteur, which fails.

        restDetenteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isBadRequest());

        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = detenteurRepository.findAll().size();
        // set the field null
        detenteur.setPrenomPers(null);

        // Create the Detenteur, which fails.

        restDetenteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isBadRequest());

        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = detenteurRepository.findAll().size();
        // set the field null
        detenteur.setSexe(null);

        // Create the Detenteur, which fails.

        restDetenteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isBadRequest());

        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = detenteurRepository.findAll().size();
        // set the field null
        detenteur.setMobile(null);

        // Create the Detenteur, which fails.

        restDetenteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isBadRequest());

        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = detenteurRepository.findAll().size();
        // set the field null
        detenteur.setDirection(null);

        // Create the Detenteur, which fails.

        restDetenteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isBadRequest());

        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetenteurs() throws Exception {
        // Initialize the database
        detenteurRepository.saveAndFlush(detenteur);

        // Get all the detenteurList
        restDetenteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detenteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }

    @Test
    @Transactional
    void getDetenteur() throws Exception {
        // Initialize the database
        detenteurRepository.saveAndFlush(detenteur);

        // Get the detenteur
        restDetenteurMockMvc
            .perform(get(ENTITY_API_URL_ID, detenteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detenteur.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDetenteur() throws Exception {
        // Get the detenteur
        restDetenteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetenteur() throws Exception {
        // Initialize the database
        detenteurRepository.saveAndFlush(detenteur);

        int databaseSizeBeforeUpdate = detenteurRepository.findAll().size();

        // Update the detenteur
        Detenteur updatedDetenteur = detenteurRepository.findById(detenteur.getId()).get();
        // Disconnect from session so that the updates on updatedDetenteur are not directly saved in db
        em.detach(updatedDetenteur);
        updatedDetenteur
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restDetenteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetenteur.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetenteur))
            )
            .andExpect(status().isOk());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
        Detenteur testDetenteur = detenteurList.get(detenteurList.size() - 1);
        assertThat(testDetenteur.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testDetenteur.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testDetenteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDetenteur.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testDetenteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDetenteur.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void putNonExistingDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().size();
        detenteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetenteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detenteur.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().size();
        detenteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetenteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().size();
        detenteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetenteurMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetenteurWithPatch() throws Exception {
        // Initialize the database
        detenteurRepository.saveAndFlush(detenteur);

        int databaseSizeBeforeUpdate = detenteurRepository.findAll().size();

        // Update the detenteur using partial update
        Detenteur partialUpdatedDetenteur = new Detenteur();
        partialUpdatedDetenteur.setId(detenteur.getId());

        partialUpdatedDetenteur.mobile(UPDATED_MOBILE);

        restDetenteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetenteur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetenteur))
            )
            .andExpect(status().isOk());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
        Detenteur testDetenteur = detenteurList.get(detenteurList.size() - 1);
        assertThat(testDetenteur.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testDetenteur.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testDetenteur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDetenteur.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testDetenteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDetenteur.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void fullUpdateDetenteurWithPatch() throws Exception {
        // Initialize the database
        detenteurRepository.saveAndFlush(detenteur);

        int databaseSizeBeforeUpdate = detenteurRepository.findAll().size();

        // Update the detenteur using partial update
        Detenteur partialUpdatedDetenteur = new Detenteur();
        partialUpdatedDetenteur.setId(detenteur.getId());

        partialUpdatedDetenteur
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restDetenteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetenteur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetenteur))
            )
            .andExpect(status().isOk());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
        Detenteur testDetenteur = detenteurList.get(detenteurList.size() - 1);
        assertThat(testDetenteur.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testDetenteur.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testDetenteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDetenteur.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testDetenteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDetenteur.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void patchNonExistingDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().size();
        detenteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetenteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detenteur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().size();
        detenteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetenteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetenteur() throws Exception {
        int databaseSizeBeforeUpdate = detenteurRepository.findAll().size();
        detenteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetenteurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detenteur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Detenteur in the database
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetenteur() throws Exception {
        // Initialize the database
        detenteurRepository.saveAndFlush(detenteur);

        int databaseSizeBeforeDelete = detenteurRepository.findAll().size();

        // Delete the detenteur
        restDetenteurMockMvc
            .perform(delete(ENTITY_API_URL_ID, detenteur.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Detenteur> detenteurList = detenteurRepository.findAll();
        assertThat(detenteurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
