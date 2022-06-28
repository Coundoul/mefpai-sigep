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
import sn.coundoul.gestion.utilisateurs.domain.OrdonnaceurMatiere;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Direction;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;
import sn.coundoul.gestion.utilisateurs.repository.OrdonnaceurMatiereRepository;

/**
 * Integration tests for the {@link OrdonnaceurMatiereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdonnaceurMatiereResourceIT {

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

    private static final String ENTITY_API_URL = "/api/ordonnaceur-matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdonnaceurMatiereRepository ordonnaceurMatiereRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdonnaceurMatiereMockMvc;

    private OrdonnaceurMatiere ordonnaceurMatiere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdonnaceurMatiere createEntity(EntityManager em) {
        OrdonnaceurMatiere ordonnaceurMatiere = new OrdonnaceurMatiere()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return ordonnaceurMatiere;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdonnaceurMatiere createUpdatedEntity(EntityManager em) {
        OrdonnaceurMatiere ordonnaceurMatiere = new OrdonnaceurMatiere()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return ordonnaceurMatiere;
    }

    @BeforeEach
    public void initTest() {
        ordonnaceurMatiere = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeCreate = ordonnaceurMatiereRepository.findAll().size();
        // Create the OrdonnaceurMatiere
        restOrdonnaceurMatiereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isCreated());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeCreate + 1);
        OrdonnaceurMatiere testOrdonnaceurMatiere = ordonnaceurMatiereList.get(ordonnaceurMatiereList.size() - 1);
        assertThat(testOrdonnaceurMatiere.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testOrdonnaceurMatiere.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testOrdonnaceurMatiere.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testOrdonnaceurMatiere.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testOrdonnaceurMatiere.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testOrdonnaceurMatiere.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void createOrdonnaceurMatiereWithExistingId() throws Exception {
        // Create the OrdonnaceurMatiere with an existing ID
        ordonnaceurMatiere.setId(1L);

        int databaseSizeBeforeCreate = ordonnaceurMatiereRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdonnaceurMatiereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordonnaceurMatiereRepository.findAll().size();
        // set the field null
        ordonnaceurMatiere.setNomPers(null);

        // Create the OrdonnaceurMatiere, which fails.

        restOrdonnaceurMatiereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isBadRequest());

        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordonnaceurMatiereRepository.findAll().size();
        // set the field null
        ordonnaceurMatiere.setPrenomPers(null);

        // Create the OrdonnaceurMatiere, which fails.

        restOrdonnaceurMatiereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isBadRequest());

        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordonnaceurMatiereRepository.findAll().size();
        // set the field null
        ordonnaceurMatiere.setSexe(null);

        // Create the OrdonnaceurMatiere, which fails.

        restOrdonnaceurMatiereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isBadRequest());

        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordonnaceurMatiereRepository.findAll().size();
        // set the field null
        ordonnaceurMatiere.setMobile(null);

        // Create the OrdonnaceurMatiere, which fails.

        restOrdonnaceurMatiereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isBadRequest());

        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrdonnaceurMatieres() throws Exception {
        // Initialize the database
        ordonnaceurMatiereRepository.saveAndFlush(ordonnaceurMatiere);

        // Get all the ordonnaceurMatiereList
        restOrdonnaceurMatiereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordonnaceurMatiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }

    @Test
    @Transactional
    void getOrdonnaceurMatiere() throws Exception {
        // Initialize the database
        ordonnaceurMatiereRepository.saveAndFlush(ordonnaceurMatiere);

        // Get the ordonnaceurMatiere
        restOrdonnaceurMatiereMockMvc
            .perform(get(ENTITY_API_URL_ID, ordonnaceurMatiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordonnaceurMatiere.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrdonnaceurMatiere() throws Exception {
        // Get the ordonnaceurMatiere
        restOrdonnaceurMatiereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdonnaceurMatiere() throws Exception {
        // Initialize the database
        ordonnaceurMatiereRepository.saveAndFlush(ordonnaceurMatiere);

        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().size();

        // Update the ordonnaceurMatiere
        OrdonnaceurMatiere updatedOrdonnaceurMatiere = ordonnaceurMatiereRepository.findById(ordonnaceurMatiere.getId()).get();
        // Disconnect from session so that the updates on updatedOrdonnaceurMatiere are not directly saved in db
        em.detach(updatedOrdonnaceurMatiere);
        updatedOrdonnaceurMatiere
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restOrdonnaceurMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdonnaceurMatiere.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdonnaceurMatiere))
            )
            .andExpect(status().isOk());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
        OrdonnaceurMatiere testOrdonnaceurMatiere = ordonnaceurMatiereList.get(ordonnaceurMatiereList.size() - 1);
        assertThat(testOrdonnaceurMatiere.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testOrdonnaceurMatiere.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testOrdonnaceurMatiere.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testOrdonnaceurMatiere.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testOrdonnaceurMatiere.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testOrdonnaceurMatiere.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void putNonExistingOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdonnaceurMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordonnaceurMatiere.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdonnaceurMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdonnaceurMatiereMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdonnaceurMatiereWithPatch() throws Exception {
        // Initialize the database
        ordonnaceurMatiereRepository.saveAndFlush(ordonnaceurMatiere);

        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().size();

        // Update the ordonnaceurMatiere using partial update
        OrdonnaceurMatiere partialUpdatedOrdonnaceurMatiere = new OrdonnaceurMatiere();
        partialUpdatedOrdonnaceurMatiere.setId(ordonnaceurMatiere.getId());

        partialUpdatedOrdonnaceurMatiere.mobile(UPDATED_MOBILE).adresse(UPDATED_ADRESSE).direction(UPDATED_DIRECTION);

        restOrdonnaceurMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdonnaceurMatiere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdonnaceurMatiere))
            )
            .andExpect(status().isOk());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
        OrdonnaceurMatiere testOrdonnaceurMatiere = ordonnaceurMatiereList.get(ordonnaceurMatiereList.size() - 1);
        assertThat(testOrdonnaceurMatiere.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testOrdonnaceurMatiere.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testOrdonnaceurMatiere.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testOrdonnaceurMatiere.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testOrdonnaceurMatiere.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testOrdonnaceurMatiere.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void fullUpdateOrdonnaceurMatiereWithPatch() throws Exception {
        // Initialize the database
        ordonnaceurMatiereRepository.saveAndFlush(ordonnaceurMatiere);

        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().size();

        // Update the ordonnaceurMatiere using partial update
        OrdonnaceurMatiere partialUpdatedOrdonnaceurMatiere = new OrdonnaceurMatiere();
        partialUpdatedOrdonnaceurMatiere.setId(ordonnaceurMatiere.getId());

        partialUpdatedOrdonnaceurMatiere
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restOrdonnaceurMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdonnaceurMatiere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdonnaceurMatiere))
            )
            .andExpect(status().isOk());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
        OrdonnaceurMatiere testOrdonnaceurMatiere = ordonnaceurMatiereList.get(ordonnaceurMatiereList.size() - 1);
        assertThat(testOrdonnaceurMatiere.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testOrdonnaceurMatiere.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testOrdonnaceurMatiere.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testOrdonnaceurMatiere.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testOrdonnaceurMatiere.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testOrdonnaceurMatiere.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void patchNonExistingOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdonnaceurMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordonnaceurMatiere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdonnaceurMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdonnaceurMatiere() throws Exception {
        int databaseSizeBeforeUpdate = ordonnaceurMatiereRepository.findAll().size();
        ordonnaceurMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdonnaceurMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordonnaceurMatiere))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdonnaceurMatiere in the database
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdonnaceurMatiere() throws Exception {
        // Initialize the database
        ordonnaceurMatiereRepository.saveAndFlush(ordonnaceurMatiere);

        int databaseSizeBeforeDelete = ordonnaceurMatiereRepository.findAll().size();

        // Delete the ordonnaceurMatiere
        restOrdonnaceurMatiereMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordonnaceurMatiere.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdonnaceurMatiere> ordonnaceurMatiereList = ordonnaceurMatiereRepository.findAll();
        assertThat(ordonnaceurMatiereList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
