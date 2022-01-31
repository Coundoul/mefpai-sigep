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
import sn.coundoul.gestion.equipement.domain.DetailSortie;
import sn.coundoul.gestion.equipement.repository.DetailSortieRepository;

/**
 * Integration tests for the {@link DetailSortieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetailSortieResourceIT {

    private static final String DEFAULT_PIECE_JOINTE = "AAAAAAAAAA";
    private static final String UPDATED_PIECE_JOINTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/detail-sorties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetailSortieRepository detailSortieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailSortieMockMvc;

    private DetailSortie detailSortie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailSortie createEntity(EntityManager em) {
        DetailSortie detailSortie = new DetailSortie().pieceJointe(DEFAULT_PIECE_JOINTE).idPers(DEFAULT_ID_PERS);
        return detailSortie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailSortie createUpdatedEntity(EntityManager em) {
        DetailSortie detailSortie = new DetailSortie().pieceJointe(UPDATED_PIECE_JOINTE).idPers(UPDATED_ID_PERS);
        return detailSortie;
    }

    @BeforeEach
    public void initTest() {
        detailSortie = createEntity(em);
    }

    @Test
    @Transactional
    void createDetailSortie() throws Exception {
        int databaseSizeBeforeCreate = detailSortieRepository.findAll().size();
        // Create the DetailSortie
        restDetailSortieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailSortie))
            )
            .andExpect(status().isCreated());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeCreate + 1);
        DetailSortie testDetailSortie = detailSortieList.get(detailSortieList.size() - 1);
        assertThat(testDetailSortie.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testDetailSortie.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    @Transactional
    void createDetailSortieWithExistingId() throws Exception {
        // Create the DetailSortie with an existing ID
        detailSortie.setId(1L);

        int databaseSizeBeforeCreate = detailSortieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailSortieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailSortie))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPieceJointeIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailSortieRepository.findAll().size();
        // set the field null
        detailSortie.setPieceJointe(null);

        // Create the DetailSortie, which fails.

        restDetailSortieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailSortie))
            )
            .andExpect(status().isBadRequest());

        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailSortieRepository.findAll().size();
        // set the field null
        detailSortie.setIdPers(null);

        // Create the DetailSortie, which fails.

        restDetailSortieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailSortie))
            )
            .andExpect(status().isBadRequest());

        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetailSorties() throws Exception {
        // Initialize the database
        detailSortieRepository.saveAndFlush(detailSortie);

        // Get all the detailSortieList
        restDetailSortieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailSortie.getId().intValue())))
            .andExpect(jsonPath("$.[*].pieceJointe").value(hasItem(DEFAULT_PIECE_JOINTE)))
            .andExpect(jsonPath("$.[*].idPers").value(hasItem(DEFAULT_ID_PERS)));
    }

    @Test
    @Transactional
    void getDetailSortie() throws Exception {
        // Initialize the database
        detailSortieRepository.saveAndFlush(detailSortie);

        // Get the detailSortie
        restDetailSortieMockMvc
            .perform(get(ENTITY_API_URL_ID, detailSortie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailSortie.getId().intValue()))
            .andExpect(jsonPath("$.pieceJointe").value(DEFAULT_PIECE_JOINTE))
            .andExpect(jsonPath("$.idPers").value(DEFAULT_ID_PERS));
    }

    @Test
    @Transactional
    void getNonExistingDetailSortie() throws Exception {
        // Get the detailSortie
        restDetailSortieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetailSortie() throws Exception {
        // Initialize the database
        detailSortieRepository.saveAndFlush(detailSortie);

        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().size();

        // Update the detailSortie
        DetailSortie updatedDetailSortie = detailSortieRepository.findById(detailSortie.getId()).get();
        // Disconnect from session so that the updates on updatedDetailSortie are not directly saved in db
        em.detach(updatedDetailSortie);
        updatedDetailSortie.pieceJointe(UPDATED_PIECE_JOINTE).idPers(UPDATED_ID_PERS);

        restDetailSortieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetailSortie.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetailSortie))
            )
            .andExpect(status().isOk());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
        DetailSortie testDetailSortie = detailSortieList.get(detailSortieList.size() - 1);
        assertThat(testDetailSortie.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testDetailSortie.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void putNonExistingDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().size();
        detailSortie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailSortieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailSortie.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailSortie))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().size();
        detailSortie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailSortieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailSortie))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().size();
        detailSortie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailSortieMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailSortie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetailSortieWithPatch() throws Exception {
        // Initialize the database
        detailSortieRepository.saveAndFlush(detailSortie);

        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().size();

        // Update the detailSortie using partial update
        DetailSortie partialUpdatedDetailSortie = new DetailSortie();
        partialUpdatedDetailSortie.setId(detailSortie.getId());

        partialUpdatedDetailSortie.idPers(UPDATED_ID_PERS);

        restDetailSortieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailSortie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailSortie))
            )
            .andExpect(status().isOk());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
        DetailSortie testDetailSortie = detailSortieList.get(detailSortieList.size() - 1);
        assertThat(testDetailSortie.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testDetailSortie.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void fullUpdateDetailSortieWithPatch() throws Exception {
        // Initialize the database
        detailSortieRepository.saveAndFlush(detailSortie);

        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().size();

        // Update the detailSortie using partial update
        DetailSortie partialUpdatedDetailSortie = new DetailSortie();
        partialUpdatedDetailSortie.setId(detailSortie.getId());

        partialUpdatedDetailSortie.pieceJointe(UPDATED_PIECE_JOINTE).idPers(UPDATED_ID_PERS);

        restDetailSortieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailSortie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailSortie))
            )
            .andExpect(status().isOk());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
        DetailSortie testDetailSortie = detailSortieList.get(detailSortieList.size() - 1);
        assertThat(testDetailSortie.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testDetailSortie.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void patchNonExistingDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().size();
        detailSortie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailSortieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detailSortie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailSortie))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().size();
        detailSortie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailSortieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailSortie))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetailSortie() throws Exception {
        int databaseSizeBeforeUpdate = detailSortieRepository.findAll().size();
        detailSortie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailSortieMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailSortie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailSortie in the database
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetailSortie() throws Exception {
        // Initialize the database
        detailSortieRepository.saveAndFlush(detailSortie);

        int databaseSizeBeforeDelete = detailSortieRepository.findAll().size();

        // Delete the detailSortie
        restDetailSortieMockMvc
            .perform(delete(ENTITY_API_URL_ID, detailSortie.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetailSortie> detailSortieList = detailSortieRepository.findAll();
        assertThat(detailSortieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
