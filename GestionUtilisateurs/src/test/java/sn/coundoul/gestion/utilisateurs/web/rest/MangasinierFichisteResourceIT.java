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
import sn.coundoul.gestion.utilisateurs.domain.MangasinierFichiste;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Direction;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;
import sn.coundoul.gestion.utilisateurs.repository.MangasinierFichisteRepository;

/**
 * Integration tests for the {@link MangasinierFichisteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MangasinierFichisteResourceIT {

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

    private static final String ENTITY_API_URL = "/api/mangasinier-fichistes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MangasinierFichisteRepository mangasinierFichisteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMangasinierFichisteMockMvc;

    private MangasinierFichiste mangasinierFichiste;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MangasinierFichiste createEntity(EntityManager em) {
        MangasinierFichiste mangasinierFichiste = new MangasinierFichiste()
            .nomPers(DEFAULT_NOM_PERS)
            .prenomPers(DEFAULT_PRENOM_PERS)
            .sexe(DEFAULT_SEXE)
            .mobile(DEFAULT_MOBILE)
            .adresse(DEFAULT_ADRESSE)
            .direction(DEFAULT_DIRECTION);
        return mangasinierFichiste;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MangasinierFichiste createUpdatedEntity(EntityManager em) {
        MangasinierFichiste mangasinierFichiste = new MangasinierFichiste()
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);
        return mangasinierFichiste;
    }

    @BeforeEach
    public void initTest() {
        mangasinierFichiste = createEntity(em);
    }

    @Test
    @Transactional
    void createMangasinierFichiste() throws Exception {
        int databaseSizeBeforeCreate = mangasinierFichisteRepository.findAll().size();
        // Create the MangasinierFichiste
        restMangasinierFichisteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isCreated());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeCreate + 1);
        MangasinierFichiste testMangasinierFichiste = mangasinierFichisteList.get(mangasinierFichisteList.size() - 1);
        assertThat(testMangasinierFichiste.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testMangasinierFichiste.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testMangasinierFichiste.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testMangasinierFichiste.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testMangasinierFichiste.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testMangasinierFichiste.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void createMangasinierFichisteWithExistingId() throws Exception {
        // Create the MangasinierFichiste with an existing ID
        mangasinierFichiste.setId(1L);

        int databaseSizeBeforeCreate = mangasinierFichisteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMangasinierFichisteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = mangasinierFichisteRepository.findAll().size();
        // set the field null
        mangasinierFichiste.setNomPers(null);

        // Create the MangasinierFichiste, which fails.

        restMangasinierFichisteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isBadRequest());

        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = mangasinierFichisteRepository.findAll().size();
        // set the field null
        mangasinierFichiste.setPrenomPers(null);

        // Create the MangasinierFichiste, which fails.

        restMangasinierFichisteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isBadRequest());

        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = mangasinierFichisteRepository.findAll().size();
        // set the field null
        mangasinierFichiste.setSexe(null);

        // Create the MangasinierFichiste, which fails.

        restMangasinierFichisteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isBadRequest());

        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = mangasinierFichisteRepository.findAll().size();
        // set the field null
        mangasinierFichiste.setMobile(null);

        // Create the MangasinierFichiste, which fails.

        restMangasinierFichisteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isBadRequest());

        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = mangasinierFichisteRepository.findAll().size();
        // set the field null
        mangasinierFichiste.setDirection(null);

        // Create the MangasinierFichiste, which fails.

        restMangasinierFichisteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isBadRequest());

        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMangasinierFichistes() throws Exception {
        // Initialize the database
        mangasinierFichisteRepository.saveAndFlush(mangasinierFichiste);

        // Get all the mangasinierFichisteList
        restMangasinierFichisteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mangasinierFichiste.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }

    @Test
    @Transactional
    void getMangasinierFichiste() throws Exception {
        // Initialize the database
        mangasinierFichisteRepository.saveAndFlush(mangasinierFichiste);

        // Get the mangasinierFichiste
        restMangasinierFichisteMockMvc
            .perform(get(ENTITY_API_URL_ID, mangasinierFichiste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mangasinierFichiste.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMangasinierFichiste() throws Exception {
        // Get the mangasinierFichiste
        restMangasinierFichisteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMangasinierFichiste() throws Exception {
        // Initialize the database
        mangasinierFichisteRepository.saveAndFlush(mangasinierFichiste);

        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().size();

        // Update the mangasinierFichiste
        MangasinierFichiste updatedMangasinierFichiste = mangasinierFichisteRepository.findById(mangasinierFichiste.getId()).get();
        // Disconnect from session so that the updates on updatedMangasinierFichiste are not directly saved in db
        em.detach(updatedMangasinierFichiste);
        updatedMangasinierFichiste
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restMangasinierFichisteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMangasinierFichiste.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMangasinierFichiste))
            )
            .andExpect(status().isOk());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
        MangasinierFichiste testMangasinierFichiste = mangasinierFichisteList.get(mangasinierFichisteList.size() - 1);
        assertThat(testMangasinierFichiste.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testMangasinierFichiste.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testMangasinierFichiste.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testMangasinierFichiste.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testMangasinierFichiste.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testMangasinierFichiste.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void putNonExistingMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMangasinierFichisteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mangasinierFichiste.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMangasinierFichisteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMangasinierFichisteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMangasinierFichisteWithPatch() throws Exception {
        // Initialize the database
        mangasinierFichisteRepository.saveAndFlush(mangasinierFichiste);

        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().size();

        // Update the mangasinierFichiste using partial update
        MangasinierFichiste partialUpdatedMangasinierFichiste = new MangasinierFichiste();
        partialUpdatedMangasinierFichiste.setId(mangasinierFichiste.getId());

        partialUpdatedMangasinierFichiste
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .mobile(UPDATED_MOBILE)
            .direction(UPDATED_DIRECTION);

        restMangasinierFichisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMangasinierFichiste.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMangasinierFichiste))
            )
            .andExpect(status().isOk());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
        MangasinierFichiste testMangasinierFichiste = mangasinierFichisteList.get(mangasinierFichisteList.size() - 1);
        assertThat(testMangasinierFichiste.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testMangasinierFichiste.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testMangasinierFichiste.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testMangasinierFichiste.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testMangasinierFichiste.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testMangasinierFichiste.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void fullUpdateMangasinierFichisteWithPatch() throws Exception {
        // Initialize the database
        mangasinierFichisteRepository.saveAndFlush(mangasinierFichiste);

        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().size();

        // Update the mangasinierFichiste using partial update
        MangasinierFichiste partialUpdatedMangasinierFichiste = new MangasinierFichiste();
        partialUpdatedMangasinierFichiste.setId(mangasinierFichiste.getId());

        partialUpdatedMangasinierFichiste
            .nomPers(UPDATED_NOM_PERS)
            .prenomPers(UPDATED_PRENOM_PERS)
            .sexe(UPDATED_SEXE)
            .mobile(UPDATED_MOBILE)
            .adresse(UPDATED_ADRESSE)
            .direction(UPDATED_DIRECTION);

        restMangasinierFichisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMangasinierFichiste.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMangasinierFichiste))
            )
            .andExpect(status().isOk());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
        MangasinierFichiste testMangasinierFichiste = mangasinierFichisteList.get(mangasinierFichisteList.size() - 1);
        assertThat(testMangasinierFichiste.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testMangasinierFichiste.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testMangasinierFichiste.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testMangasinierFichiste.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testMangasinierFichiste.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testMangasinierFichiste.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void patchNonExistingMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMangasinierFichisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mangasinierFichiste.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMangasinierFichisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMangasinierFichiste() throws Exception {
        int databaseSizeBeforeUpdate = mangasinierFichisteRepository.findAll().size();
        mangasinierFichiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMangasinierFichisteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mangasinierFichiste))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MangasinierFichiste in the database
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMangasinierFichiste() throws Exception {
        // Initialize the database
        mangasinierFichisteRepository.saveAndFlush(mangasinierFichiste);

        int databaseSizeBeforeDelete = mangasinierFichisteRepository.findAll().size();

        // Delete the mangasinierFichiste
        restMangasinierFichisteMockMvc
            .perform(delete(ENTITY_API_URL_ID, mangasinierFichiste.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MangasinierFichiste> mangasinierFichisteList = mangasinierFichisteRepository.findAll();
        assertThat(mangasinierFichisteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
