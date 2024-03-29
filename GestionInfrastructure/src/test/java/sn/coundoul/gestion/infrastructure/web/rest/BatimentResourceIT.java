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
import org.springframework.util.Base64Utils;
import org.springframework.transaction.annotation.Transactional;
import sn.coundoul.gestion.infrastructure.IntegrationTest;
import sn.coundoul.gestion.infrastructure.domain.Batiment;
import sn.coundoul.gestion.infrastructure.repository.BatimentRepository;

/**
 * Integration tests for the {@link BatimentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BatimentResourceIT {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Double DEFAULT_NBR_PIECE = 1D;
    private static final Double UPDATED_NBR_PIECE = 2D;

    private static final Double DEFAULT_SURFACE = 1D;
    private static final Double UPDATED_SURFACE = 2D;

    private static final String DEFAULT_SOURCE_FINANCEMENT = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_FINANCEMENT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ETAT_GROS_OEUVRE = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_GROS_OEUVRE = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT_SECOND_OEUVRE = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_SECOND_OEUVRE = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/batiments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BatimentRepository batimentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBatimentMockMvc;

    private Batiment batiment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batiment createEntity(EntityManager em) {
        Batiment batiment = new Batiment()
            .designation(DEFAULT_DESIGNATION)
            .nbrPiece(DEFAULT_NBR_PIECE)
            .surface(DEFAULT_SURFACE)
            .sourceFinancement(DEFAULT_SOURCE_FINANCEMENT)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .observation(DEFAULT_OBSERVATION);
        return batiment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batiment createUpdatedEntity(EntityManager em) {
        Batiment batiment = new Batiment()
            .designation(DEFAULT_DESIGNATION)
            .nbrPiece(DEFAULT_NBR_PIECE)
            .surface(DEFAULT_SURFACE)
            .sourceFinancement(DEFAULT_SOURCE_FINANCEMENT)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .observation(DEFAULT_OBSERVATION);
        return batiment;
    }

    @BeforeEach
    public void initTest() {
        batiment = createEntity(em);
    }

    @Test
    @Transactional
    void createBatiment() throws Exception {
        int databaseSizeBeforeCreate = batimentRepository.findAll().size();
        // Create the Batiment
        restBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isCreated());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeCreate + 1);
        Batiment testBatiment = batimentList.get(batimentList.size() - 1);
        assertThat(testBatiment.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testBatiment.getNbrPiece()).isEqualTo(DEFAULT_NBR_PIECE);
        assertThat(testBatiment.getSurface()).isEqualTo(DEFAULT_SURFACE);
        assertThat(testBatiment.getSourceFinancement()).isEqualTo(DEFAULT_SOURCE_FINANCEMENT);
        assertThat(testBatiment.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testBatiment.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testBatiment.getEtatGrosOeuvre()).isEqualTo(DEFAULT_ETAT_GROS_OEUVRE);
        assertThat(testBatiment.getEtatSecondOeuvre()).isEqualTo(DEFAULT_ETAT_SECOND_OEUVRE);
        assertThat(testBatiment.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void createBatimentWithExistingId() throws Exception {
        // Create the Batiment with an existing ID
        batiment.setId(1L);

        int databaseSizeBeforeCreate = batimentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomBatimentIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().size();
        // set the field null
        batiment.setDesignation(null);

        // Create the Batiment, which fails.

        restBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbrPieceIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().size();
        // set the field null
        batiment.setNbrPiece(null);

        // Create the Batiment, which fails.

        restBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().size();
        // set the field null
        batiment.setDesignation(null);

        // Create the Batiment, which fails.

        restBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSurfaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().size();
        // set the field null
        batiment.setSurface(null);

        // Create the Batiment, which fails.

        restBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatGeneralIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().size();
        // set the field null
        batiment.setEtatGrosOeuvre(null);

        // Create the Batiment, which fails.

        restBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreSalleIsRequired() throws Exception {
        int databaseSizeBeforeTest = batimentRepository.findAll().size();
        // set the field null
        batiment.setObservation(null);

        // Create the Batiment, which fails.

        restBatimentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBatiments() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);

        // Get all the batimentList
        restBatimentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batiment.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.[*].nbrPiece").value(DEFAULT_NBR_PIECE.doubleValue()))
            .andExpect(jsonPath("$.[*].surface").value(DEFAULT_SURFACE.doubleValue()))
            .andExpect(jsonPath("$.[*].sourceFinancement").value(DEFAULT_SOURCE_FINANCEMENT))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].etatGrosOeuvre").value(DEFAULT_ETAT_GROS_OEUVRE))
            .andExpect(jsonPath("$.[*].etatSecondOeuvre").value(DEFAULT_ETAT_SECOND_OEUVRE))
            .andExpect(jsonPath("$.[*].observation").value(DEFAULT_OBSERVATION));
    }

    @Test
    @Transactional
    void getBatiment() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);

        // Get the batiment
        restBatimentMockMvc
            .perform(get(ENTITY_API_URL_ID, batiment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(batiment.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.nbrPiece").value(DEFAULT_NBR_PIECE.doubleValue()))
            .andExpect(jsonPath("$.surface").value(DEFAULT_SURFACE.doubleValue()))
            .andExpect(jsonPath("$.sourceFinancement").value(DEFAULT_SOURCE_FINANCEMENT))
            .andExpect(jsonPath("$.photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.etatGrosOeuvre").value(DEFAULT_ETAT_GROS_OEUVRE))
            .andExpect(jsonPath("$.etatSecondOeuvre").value(DEFAULT_ETAT_SECOND_OEUVRE))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }

    @Test
    @Transactional
    void getNonExistingBatiment() throws Exception {
        // Get the batiment
        restBatimentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBatiment() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);

        int databaseSizeBeforeUpdate = batimentRepository.findAll().size();

        // Update the batiment
        Batiment updatedBatiment = batimentRepository.findById(batiment.getId()).get();
        // Disconnect from session so that the updates on updatedBatiment are not directly saved in db
        em.detach(updatedBatiment);
        updatedBatiment
            .designation(DEFAULT_DESIGNATION)
            .nbrPiece(DEFAULT_NBR_PIECE)
            .surface(DEFAULT_SURFACE)
            .sourceFinancement(DEFAULT_SOURCE_FINANCEMENT)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .observation(DEFAULT_OBSERVATION);

        restBatimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBatiment.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBatiment))
            )
            .andExpect(status().isOk());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
        Batiment testBatiment = batimentList.get(batimentList.size() - 1);
        assertThat(testBatiment.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testBatiment.getNbrPiece()).isEqualTo(DEFAULT_NBR_PIECE);
        assertThat(testBatiment.getSurface()).isEqualTo(DEFAULT_SURFACE);
        assertThat(testBatiment.getSourceFinancement()).isEqualTo(DEFAULT_SOURCE_FINANCEMENT);
        assertThat(testBatiment.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testBatiment.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testBatiment.getEtatGrosOeuvre()).isEqualTo(DEFAULT_ETAT_GROS_OEUVRE);
        assertThat(testBatiment.getEtatSecondOeuvre()).isEqualTo(DEFAULT_ETAT_SECOND_OEUVRE);
        assertThat(testBatiment.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void putNonExistingBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().size();
        batiment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, batiment.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().size();
        batiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().size();
        batiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatimentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBatimentWithPatch() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);

        int databaseSizeBeforeUpdate = batimentRepository.findAll().size();

        // Update the batiment using partial update
        Batiment partialUpdatedBatiment = new Batiment();
        partialUpdatedBatiment.setId(batiment.getId());

        partialUpdatedBatiment
            .designation(DEFAULT_DESIGNATION)
            .nbrPiece(DEFAULT_NBR_PIECE)
            .surface(DEFAULT_SURFACE)
            .sourceFinancement(DEFAULT_SOURCE_FINANCEMENT)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .observation(DEFAULT_OBSERVATION);

        restBatimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatiment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatiment))
            )
            .andExpect(status().isOk());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
        Batiment testBatiment = batimentList.get(batimentList.size() - 1);
        assertThat(testBatiment.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testBatiment.getNbrPiece()).isEqualTo(DEFAULT_NBR_PIECE);
        assertThat(testBatiment.getSurface()).isEqualTo(DEFAULT_SURFACE);
        assertThat(testBatiment.getSourceFinancement()).isEqualTo(DEFAULT_SOURCE_FINANCEMENT);
        assertThat(testBatiment.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testBatiment.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testBatiment.getEtatGrosOeuvre()).isEqualTo(DEFAULT_ETAT_GROS_OEUVRE);
        assertThat(testBatiment.getEtatSecondOeuvre()).isEqualTo(DEFAULT_ETAT_SECOND_OEUVRE);
        assertThat(testBatiment.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void fullUpdateBatimentWithPatch() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);

        int databaseSizeBeforeUpdate = batimentRepository.findAll().size();

        // Update the batiment using partial update
        Batiment partialUpdatedBatiment = new Batiment();
        partialUpdatedBatiment.setId(batiment.getId());

        partialUpdatedBatiment
            .designation(DEFAULT_DESIGNATION)
            .nbrPiece(DEFAULT_NBR_PIECE)
            .surface(DEFAULT_SURFACE)
            .sourceFinancement(DEFAULT_SOURCE_FINANCEMENT)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .observation(DEFAULT_OBSERVATION);

        restBatimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatiment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatiment))
            )
            .andExpect(status().isOk());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
        Batiment testBatiment = batimentList.get(batimentList.size() - 1);
        assertThat(testBatiment.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testBatiment.getNbrPiece()).isEqualTo(UPDATED_NBR_PIECE);
        assertThat(testBatiment.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testBatiment.getSourceFinancement()).isEqualTo(UPDATED_SOURCE_FINANCEMENT);
        assertThat(testBatiment.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testBatiment.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testBatiment.getEtatGrosOeuvre()).isEqualTo(UPDATED_ETAT_GROS_OEUVRE);
        assertThat(testBatiment.getEtatSecondOeuvre()).isEqualTo(UPDATED_ETAT_SECOND_OEUVRE);
        assertThat(testBatiment.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void patchNonExistingBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().size();
        batiment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, batiment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().size();
        batiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBatiment() throws Exception {
        int databaseSizeBeforeUpdate = batimentRepository.findAll().size();
        batiment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatimentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batiment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Batiment in the database
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBatiment() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);

        int databaseSizeBeforeDelete = batimentRepository.findAll().size();

        // Delete the batiment
        restBatimentMockMvc
            .perform(delete(ENTITY_API_URL_ID, batiment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Batiment> batimentList = batimentRepository.findAll();
        assertThat(batimentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
