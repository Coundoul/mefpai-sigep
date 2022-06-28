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
import sn.coundoul.gestion.utilisateurs.domain.ComptablePrincipale;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Direction;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;
import sn.coundoul.gestion.utilisateurs.repository.ComptablePrincipaleRepository;

/**
 * Integration tests for the {@link ComptablePrincipaleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComptablePrincipaleResourceIT {

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

    private static final String ENTITY_API_URL = "/api/comptable-principales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComptablePrincipaleRepository comptablePrincipaleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComptablePrincipaleMockMvc;

    private ComptablePrincipale comptablePrincipale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComptablePrincipale createEntity(EntityManager em) {
        ComptablePrincipale comptablePrincipale = new ComptablePrincipale()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return comptablePrincipale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComptablePrincipale createUpdatedEntity(EntityManager em) {
        ComptablePrincipale comptablePrincipale = new ComptablePrincipale()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return comptablePrincipale;
    }

    @BeforeEach
    public void initTest() {
        comptablePrincipale = createEntity(em);
    }

    @Test
    @Transactional
    void createComptablePrincipale() throws Exception {
        int databaseSizeBeforeCreate = comptablePrincipaleRepository.findAll().size();
        // Create the ComptablePrincipale
        restComptablePrincipaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isCreated());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeCreate + 1);
        ComptablePrincipale testComptablePrincipale = comptablePrincipaleList.get(comptablePrincipaleList.size() - 1);
        assertThat(testComptablePrincipale.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testComptablePrincipale.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testComptablePrincipale.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testComptablePrincipale.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testComptablePrincipale.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testComptablePrincipale.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void createComptablePrincipaleWithExistingId() throws Exception {
        // Create the ComptablePrincipale with an existing ID
        comptablePrincipale.setId(1L);

        int databaseSizeBeforeCreate = comptablePrincipaleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComptablePrincipaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptablePrincipaleRepository.findAll().size();
        // set the field null
        comptablePrincipale.setNomPers(null);

        // Create the ComptablePrincipale, which fails.

        restComptablePrincipaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isBadRequest());

        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptablePrincipaleRepository.findAll().size();
        // set the field null
        comptablePrincipale.setPrenomPers(null);

        // Create the ComptablePrincipale, which fails.

        restComptablePrincipaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isBadRequest());

        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptablePrincipaleRepository.findAll().size();
        // set the field null
        comptablePrincipale.setSexe(null);

        // Create the ComptablePrincipale, which fails.

        restComptablePrincipaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isBadRequest());

        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptablePrincipaleRepository.findAll().size();
        // set the field null
        comptablePrincipale.setMobile(null);

        // Create the ComptablePrincipale, which fails.

        restComptablePrincipaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isBadRequest());

        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = comptablePrincipaleRepository.findAll().size();
        // set the field null
        comptablePrincipale.setDirection(null);

        // Create the ComptablePrincipale, which fails.

        restComptablePrincipaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isBadRequest());

        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComptablePrincipales() throws Exception {
        // Initialize the database
        comptablePrincipaleRepository.saveAndFlush(comptablePrincipale);

        // Get all the comptablePrincipaleList
        restComptablePrincipaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comptablePrincipale.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }

    @Test
    @Transactional
    void getComptablePrincipale() throws Exception {
        // Initialize the database
        comptablePrincipaleRepository.saveAndFlush(comptablePrincipale);

        // Get the comptablePrincipale
        restComptablePrincipaleMockMvc
            .perform(get(ENTITY_API_URL_ID, comptablePrincipale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comptablePrincipale.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingComptablePrincipale() throws Exception {
        // Get the comptablePrincipale
        restComptablePrincipaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComptablePrincipale() throws Exception {
        // Initialize the database
        comptablePrincipaleRepository.saveAndFlush(comptablePrincipale);

        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().size();

        // Update the comptablePrincipale
        ComptablePrincipale updatedComptablePrincipale = comptablePrincipaleRepository.findById(comptablePrincipale.getId()).get();
        // Disconnect from session so that the updates on updatedComptablePrincipale are not directly saved in db
        em.detach(updatedComptablePrincipale);
        updatedComptablePrincipale
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restComptablePrincipaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComptablePrincipale.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComptablePrincipale))
            )
            .andExpect(status().isOk());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
        ComptablePrincipale testComptablePrincipale = comptablePrincipaleList.get(comptablePrincipaleList.size() - 1);
        assertThat(testComptablePrincipale.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testComptablePrincipale.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testComptablePrincipale.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testComptablePrincipale.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testComptablePrincipale.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testComptablePrincipale.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void putNonExistingComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComptablePrincipaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comptablePrincipale.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComptablePrincipaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComptablePrincipaleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComptablePrincipaleWithPatch() throws Exception {
        // Initialize the database
        comptablePrincipaleRepository.saveAndFlush(comptablePrincipale);

        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().size();

        // Update the comptablePrincipale using partial update
        ComptablePrincipale partialUpdatedComptablePrincipale = new ComptablePrincipale();
        partialUpdatedComptablePrincipale.setId(comptablePrincipale.getId());

        partialUpdatedComptablePrincipale.direction(UPDATED_DIRECTION);

        restComptablePrincipaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComptablePrincipale.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComptablePrincipale))
            )
            .andExpect(status().isOk());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
        ComptablePrincipale testComptablePrincipale = comptablePrincipaleList.get(comptablePrincipaleList.size() - 1);
        assertThat(testComptablePrincipale.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testComptablePrincipale.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testComptablePrincipale.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testComptablePrincipale.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testComptablePrincipale.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testComptablePrincipale.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void fullUpdateComptablePrincipaleWithPatch() throws Exception {
        // Initialize the database
        comptablePrincipaleRepository.saveAndFlush(comptablePrincipale);

        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().size();

        // Update the comptablePrincipale using partial update
        ComptablePrincipale partialUpdatedComptablePrincipale = new ComptablePrincipale();
        partialUpdatedComptablePrincipale.setId(comptablePrincipale.getId());

        partialUpdatedComptablePrincipale
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restComptablePrincipaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComptablePrincipale.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComptablePrincipale))
            )
            .andExpect(status().isOk());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
        ComptablePrincipale testComptablePrincipale = comptablePrincipaleList.get(comptablePrincipaleList.size() - 1);
        assertThat(testComptablePrincipale.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testComptablePrincipale.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testComptablePrincipale.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testComptablePrincipale.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testComptablePrincipale.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testComptablePrincipale.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void patchNonExistingComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComptablePrincipaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comptablePrincipale.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComptablePrincipaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComptablePrincipale() throws Exception {
        int databaseSizeBeforeUpdate = comptablePrincipaleRepository.findAll().size();
        comptablePrincipale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComptablePrincipaleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comptablePrincipale))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComptablePrincipale in the database
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComptablePrincipale() throws Exception {
        // Initialize the database
        comptablePrincipaleRepository.saveAndFlush(comptablePrincipale);

        int databaseSizeBeforeDelete = comptablePrincipaleRepository.findAll().size();

        // Delete the comptablePrincipale
        restComptablePrincipaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, comptablePrincipale.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComptablePrincipale> comptablePrincipaleList = comptablePrincipaleRepository.findAll();
        assertThat(comptablePrincipaleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
