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
import org.springframework.util.Base64Utils;
import sn.coundoul.gestion.equipement.IntegrationTest;
import sn.coundoul.gestion.equipement.domain.Equipement;
import sn.coundoul.gestion.equipement.repository.EquipementRepository;

/**
 * Integration tests for the {@link EquipementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EquipementResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIX_UNITAIRE = 1;
    private static final Integer UPDATED_PRIX_UNITAIRE = 2;

    private static final String DEFAULT_TYPE_MATIERE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_MATIERE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final String DEFAULT_ETAT_MATIERE = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_MATIERE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_GROUP = false;
    private static final Boolean UPDATED_GROUP = true;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/equipements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EquipementRepository equipementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipementMockMvc;

    private Equipement equipement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipement createEntity(EntityManager em) {
        Equipement equipement = new Equipement()
            .reference(DEFAULT_REFERENCE)
            .description(DEFAULT_DESCRIPTION)
            .typeMatiere(DEFAULT_TYPE_MATIERE)
            .quantite(DEFAULT_QUANTITE)
            .etatMatiere(DEFAULT_ETAT_MATIERE)
            .group(DEFAULT_GROUP)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return equipement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipement createUpdatedEntity(EntityManager em) {
        Equipement equipement = new Equipement()
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .typeMatiere(UPDATED_TYPE_MATIERE)
            .quantite(UPDATED_QUANTITE)
            .etatMatiere(UPDATED_ETAT_MATIERE)
            .group(UPDATED_GROUP)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        return equipement;
    }

    @BeforeEach
    public void initTest() {
        equipement = createEntity(em);
    }

    @Test
    @Transactional
    void createEquipement() throws Exception {
        int databaseSizeBeforeCreate = equipementRepository.findAll().size();
        // Create the Equipement
        restEquipementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isCreated());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeCreate + 1);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testEquipement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEquipement.getTypeMatiere()).isEqualTo(DEFAULT_TYPE_MATIERE);
        assertThat(testEquipement.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testEquipement.getEtatMatiere()).isEqualTo(DEFAULT_ETAT_MATIERE);
        assertThat(testEquipement.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testEquipement.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testEquipement.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createEquipementWithExistingId() throws Exception {
        // Create the Equipement with an existing ID
        equipement.setId(1L);

        int databaseSizeBeforeCreate = equipementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().size();
        // set the field null
        equipement.setReference(null);

        // Create the Equipement, which fails.

        restEquipementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixUnitaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().size();

        // Create the Equipement, which fails.

        restEquipementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeMatiereIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().size();
        // set the field null
        equipement.setTypeMatiere(null);

        // Create the Equipement, which fails.

        restEquipementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().size();
        // set the field null
        equipement.setQuantite(null);

        // Create the Equipement, which fails.

        restEquipementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatMatiereIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().size();
        // set the field null
        equipement.setEtatMatiere(null);

        // Create the Equipement, which fails.

        restEquipementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().size();
        // set the field null
        equipement.setGroup(null);

        // Create the Equipement, which fails.

        restEquipementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEquipements() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList
        restEquipementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipement.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].typeMatiere").value(hasItem(DEFAULT_TYPE_MATIERE)))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].etatMatiere").value(hasItem(DEFAULT_ETAT_MATIERE)))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.booleanValue())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))));
    }

    @Test
    @Transactional
    void getEquipement() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get the equipement
        restEquipementMockMvc
            .perform(get(ENTITY_API_URL_ID, equipement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipement.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.typeMatiere").value(DEFAULT_TYPE_MATIERE))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.etatMatiere").value(DEFAULT_ETAT_MATIERE))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.booleanValue()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)));
    }

    @Test
    @Transactional
    void getNonExistingEquipement() throws Exception {
        // Get the equipement
        restEquipementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEquipement() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();

        // Update the equipement
        Equipement updatedEquipement = equipementRepository.findById(equipement.getId()).get();
        // Disconnect from session so that the updates on updatedEquipement are not directly saved in db
        em.detach(updatedEquipement);
        updatedEquipement
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .typeMatiere(UPDATED_TYPE_MATIERE)
            .quantite(UPDATED_QUANTITE)
            .etatMatiere(UPDATED_ETAT_MATIERE)
            .group(UPDATED_GROUP)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restEquipementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEquipement.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEquipement))
            )
            .andExpect(status().isOk());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testEquipement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipement.getTypeMatiere()).isEqualTo(UPDATED_TYPE_MATIERE);
        assertThat(testEquipement.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testEquipement.getEtatMatiere()).isEqualTo(UPDATED_ETAT_MATIERE);
        assertThat(testEquipement.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testEquipement.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testEquipement.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipement.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquipementWithPatch() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();

        // Update the equipement using partial update
        Equipement partialUpdatedEquipement = new Equipement();
        partialUpdatedEquipement.setId(equipement.getId());

        partialUpdatedEquipement.typeMatiere(UPDATED_TYPE_MATIERE).etatMatiere(UPDATED_ETAT_MATIERE);

        restEquipementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipement))
            )
            .andExpect(status().isOk());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testEquipement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEquipement.getTypeMatiere()).isEqualTo(UPDATED_TYPE_MATIERE);
        assertThat(testEquipement.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testEquipement.getEtatMatiere()).isEqualTo(UPDATED_ETAT_MATIERE);
        assertThat(testEquipement.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testEquipement.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testEquipement.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateEquipementWithPatch() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();

        // Update the equipement using partial update
        Equipement partialUpdatedEquipement = new Equipement();
        partialUpdatedEquipement.setId(equipement.getId());

        partialUpdatedEquipement
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .typeMatiere(UPDATED_TYPE_MATIERE)
            .quantite(UPDATED_QUANTITE)
            .etatMatiere(UPDATED_ETAT_MATIERE)
            .group(UPDATED_GROUP)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restEquipementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipement))
            )
            .andExpect(status().isOk());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testEquipement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipement.getTypeMatiere()).isEqualTo(UPDATED_TYPE_MATIERE);
        assertThat(testEquipement.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testEquipement.getEtatMatiere()).isEqualTo(UPDATED_ETAT_MATIERE);
        assertThat(testEquipement.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testEquipement.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testEquipement.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equipement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquipement() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        int databaseSizeBeforeDelete = equipementRepository.findAll().size();

        // Delete the equipement
        restEquipementMockMvc
            .perform(delete(ENTITY_API_URL_ID, equipement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
