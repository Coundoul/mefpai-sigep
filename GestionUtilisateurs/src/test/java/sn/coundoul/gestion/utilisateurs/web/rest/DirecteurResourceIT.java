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
import sn.coundoul.gestion.utilisateurs.domain.Directeur;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Direction;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;
import sn.coundoul.gestion.utilisateurs.repository.DirecteurRepository;

/**
 * Integration tests for the {@link DirecteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DirecteurResourceIT {

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

    private static final String ENTITY_API_URL = "/api/directeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DirecteurRepository directeurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDirecteurMockMvc;

    private Directeur directeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directeur createEntity(EntityManager em) {
        Directeur directeur = new Directeur()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return directeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directeur createUpdatedEntity(EntityManager em) {
        Directeur directeur = new Directeur()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return directeur;
    }

    @BeforeEach
    public void initTest() {
        directeur = createEntity(em);
    }

    @Test
    @Transactional
    void createDirecteur() throws Exception {
        int databaseSizeBeforeCreate = directeurRepository.findAll().size();
        // Create the Directeur
        restDirecteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isCreated());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeCreate + 1);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testDirecteur.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testDirecteur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDirecteur.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testDirecteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDirecteur.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void createDirecteurWithExistingId() throws Exception {
        // Create the Directeur with an existing ID
        directeur.setId(1L);

        int databaseSizeBeforeCreate = directeurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirecteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setNomPers(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setPrenomPers(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setSexe(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setMobile(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setDirection(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDirecteurs() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        // Get all the directeurList
        restDirecteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }

    @Test
    @Transactional
    void getDirecteur() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        // Get the directeur
        restDirecteurMockMvc
            .perform(get(ENTITY_API_URL_ID, directeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(directeur.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDirecteur() throws Exception {
        // Get the directeur
        restDirecteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDirecteur() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();

        // Update the directeur
        Directeur updatedDirecteur = directeurRepository.findById(directeur.getId()).get();
        // Disconnect from session so that the updates on updatedDirecteur are not directly saved in db
        em.detach(updatedDirecteur);
        updatedDirecteur
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restDirecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDirecteur.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDirecteur))
            )
            .andExpect(status().isOk());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testDirecteur.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testDirecteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDirecteur.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testDirecteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDirecteur.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void putNonExistingDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directeur.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDirecteurWithPatch() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();

        // Update the directeur using partial update
        Directeur partialUpdatedDirecteur = new Directeur();
        partialUpdatedDirecteur.setId(directeur.getId());

        partialUpdatedDirecteur.nomPers(UPDATED_NOM_PERS).prenomPers(UPDATED_PRENOM_PERS).direction(UPDATED_DIRECTION);

        restDirecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirecteur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirecteur))
            )
            .andExpect(status().isOk());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testDirecteur.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testDirecteur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDirecteur.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testDirecteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDirecteur.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void fullUpdateDirecteurWithPatch() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();

        // Update the directeur using partial update
        Directeur partialUpdatedDirecteur = new Directeur();
        partialUpdatedDirecteur.setId(directeur.getId());

        partialUpdatedDirecteur
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restDirecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirecteur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirecteur))
            )
            .andExpect(status().isOk());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testDirecteur.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testDirecteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDirecteur.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testDirecteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDirecteur.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void patchNonExistingDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, directeur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDirecteur() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        int databaseSizeBeforeDelete = directeurRepository.findAll().size();

        // Delete the directeur
        restDirecteurMockMvc
            .perform(delete(ENTITY_API_URL_ID, directeur.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
