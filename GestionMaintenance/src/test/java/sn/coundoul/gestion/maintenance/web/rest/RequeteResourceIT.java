package sn.coundoul.gestion.maintenance.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.coundoul.gestion.maintenance.IntegrationTest;
import sn.coundoul.gestion.maintenance.domain.Requete;
import sn.coundoul.gestion.maintenance.repository.RequeteRepository;

/**
 * Integration tests for the {@link RequeteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequeteResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_TYPE_PANNE = 1D;
    private static final Double UPDATED_TYPE_PANNE = 2D;

    private static final Double DEFAULT_DATE_POST = 1D;
    private static final Double UPDATED_DATE_POST = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ETAT_TRAITE = false;
    private static final Boolean UPDATED_ETAT_TRAITE = true;

    private static final Instant DEFAULT_DATE_LANCEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_LANCEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ID_PERS = 1;
    private static final Integer UPDATED_ID_PERS = 2;

    private static final String ENTITY_API_URL = "/api/requetes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RequeteRepository requeteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequeteMockMvc;

    private Requete requete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requete createEntity(EntityManager em) {
        Requete requete = new Requete()
            .type(DEFAULT_TYPE)
            .typePanne(DEFAULT_TYPE_PANNE)
            .datePost(DEFAULT_DATE_POST)
            .description(DEFAULT_DESCRIPTION)
            .etatTraite(DEFAULT_ETAT_TRAITE)
            .dateLancement(DEFAULT_DATE_LANCEMENT)
            .idPers(DEFAULT_ID_PERS);
        return requete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requete createUpdatedEntity(EntityManager em) {
        Requete requete = new Requete()
            .type(UPDATED_TYPE)
            .typePanne(UPDATED_TYPE_PANNE)
            .datePost(UPDATED_DATE_POST)
            .description(UPDATED_DESCRIPTION)
            .etatTraite(UPDATED_ETAT_TRAITE)
            .dateLancement(UPDATED_DATE_LANCEMENT)
            .idPers(UPDATED_ID_PERS);
        return requete;
    }

    @BeforeEach
    public void initTest() {
        requete = createEntity(em);
    }

    @Test
    @Transactional
    void createRequete() throws Exception {
        int databaseSizeBeforeCreate = requeteRepository.findAll().size();
        // Create the Requete
        restRequeteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isCreated());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeCreate + 1);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRequete.getTypePanne()).isEqualTo(DEFAULT_TYPE_PANNE);
        assertThat(testRequete.getDatePost()).isEqualTo(DEFAULT_DATE_POST);
        assertThat(testRequete.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRequete.getEtatTraite()).isEqualTo(DEFAULT_ETAT_TRAITE);
        assertThat(testRequete.getDateLancement()).isEqualTo(DEFAULT_DATE_LANCEMENT);
        assertThat(testRequete.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    @Transactional
    void createRequeteWithExistingId() throws Exception {
        // Create the Requete with an existing ID
        requete.setId(1L);

        int databaseSizeBeforeCreate = requeteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequeteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().size();
        // set the field null
        requete.setType(null);

        // Create the Requete, which fails.

        restRequeteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypePanneIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().size();
        // set the field null
        requete.setTypePanne(null);

        // Create the Requete, which fails.

        restRequeteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDatePostIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().size();
        // set the field null
        requete.setDatePost(null);

        // Create the Requete, which fails.

        restRequeteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().size();
        // set the field null
        requete.setDescription(null);

        // Create the Requete, which fails.

        restRequeteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().size();
        // set the field null
        requete.setIdPers(null);

        // Create the Requete, which fails.

        restRequeteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRequetes() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        // Get all the requeteList
        restRequeteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requete.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].typePanne").value(hasItem(DEFAULT_TYPE_PANNE.doubleValue())))
            .andExpect(jsonPath("$.[*].datePost").value(hasItem(DEFAULT_DATE_POST.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].etatTraite").value(hasItem(DEFAULT_ETAT_TRAITE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateLancement").value(hasItem(DEFAULT_DATE_LANCEMENT.toString())))
            .andExpect(jsonPath("$.[*].idPers").value(hasItem(DEFAULT_ID_PERS)));
    }

    @Test
    @Transactional
    void getRequete() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        // Get the requete
        restRequeteMockMvc
            .perform(get(ENTITY_API_URL_ID, requete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requete.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.typePanne").value(DEFAULT_TYPE_PANNE.doubleValue()))
            .andExpect(jsonPath("$.datePost").value(DEFAULT_DATE_POST.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.etatTraite").value(DEFAULT_ETAT_TRAITE.booleanValue()))
            .andExpect(jsonPath("$.dateLancement").value(DEFAULT_DATE_LANCEMENT.toString()))
            .andExpect(jsonPath("$.idPers").value(DEFAULT_ID_PERS));
    }

    @Test
    @Transactional
    void getNonExistingRequete() throws Exception {
        // Get the requete
        restRequeteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRequete() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();

        // Update the requete
        Requete updatedRequete = requeteRepository.findById(requete.getId()).get();
        // Disconnect from session so that the updates on updatedRequete are not directly saved in db
        em.detach(updatedRequete);
        updatedRequete
            .type(UPDATED_TYPE)
            .typePanne(UPDATED_TYPE_PANNE)
            .datePost(UPDATED_DATE_POST)
            .description(UPDATED_DESCRIPTION)
            .etatTraite(UPDATED_ETAT_TRAITE)
            .dateLancement(UPDATED_DATE_LANCEMENT)
            .idPers(UPDATED_ID_PERS);

        restRequeteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRequete.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRequete))
            )
            .andExpect(status().isOk());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequete.getTypePanne()).isEqualTo(UPDATED_TYPE_PANNE);
        assertThat(testRequete.getDatePost()).isEqualTo(UPDATED_DATE_POST);
        assertThat(testRequete.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequete.getEtatTraite()).isEqualTo(UPDATED_ETAT_TRAITE);
        assertThat(testRequete.getDateLancement()).isEqualTo(UPDATED_DATE_LANCEMENT);
        assertThat(testRequete.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void putNonExistingRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requete.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRequeteWithPatch() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();

        // Update the requete using partial update
        Requete partialUpdatedRequete = new Requete();
        partialUpdatedRequete.setId(requete.getId());

        partialUpdatedRequete.datePost(UPDATED_DATE_POST).description(UPDATED_DESCRIPTION);

        restRequeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequete))
            )
            .andExpect(status().isOk());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRequete.getTypePanne()).isEqualTo(DEFAULT_TYPE_PANNE);
        assertThat(testRequete.getDatePost()).isEqualTo(UPDATED_DATE_POST);
        assertThat(testRequete.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequete.getEtatTraite()).isEqualTo(DEFAULT_ETAT_TRAITE);
        assertThat(testRequete.getDateLancement()).isEqualTo(DEFAULT_DATE_LANCEMENT);
        assertThat(testRequete.getIdPers()).isEqualTo(DEFAULT_ID_PERS);
    }

    @Test
    @Transactional
    void fullUpdateRequeteWithPatch() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();

        // Update the requete using partial update
        Requete partialUpdatedRequete = new Requete();
        partialUpdatedRequete.setId(requete.getId());

        partialUpdatedRequete
            .type(UPDATED_TYPE)
            .typePanne(UPDATED_TYPE_PANNE)
            .datePost(UPDATED_DATE_POST)
            .description(UPDATED_DESCRIPTION)
            .etatTraite(UPDATED_ETAT_TRAITE)
            .dateLancement(UPDATED_DATE_LANCEMENT)
            .idPers(UPDATED_ID_PERS);

        restRequeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequete))
            )
            .andExpect(status().isOk());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequete.getTypePanne()).isEqualTo(UPDATED_TYPE_PANNE);
        assertThat(testRequete.getDatePost()).isEqualTo(UPDATED_DATE_POST);
        assertThat(testRequete.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequete.getEtatTraite()).isEqualTo(UPDATED_ETAT_TRAITE);
        assertThat(testRequete.getDateLancement()).isEqualTo(UPDATED_DATE_LANCEMENT);
        assertThat(testRequete.getIdPers()).isEqualTo(UPDATED_ID_PERS);
    }

    @Test
    @Transactional
    void patchNonExistingRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRequete() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        int databaseSizeBeforeDelete = requeteRepository.findAll().size();

        // Delete the requete
        restRequeteMockMvc
            .perform(delete(ENTITY_API_URL_ID, requete.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
