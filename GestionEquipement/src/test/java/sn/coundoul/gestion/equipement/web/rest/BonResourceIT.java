package sn.coundoul.gestion.equipement.web.rest;

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
import sn.coundoul.gestion.equipement.IntegrationTest;
import sn.coundoul.gestion.equipement.domain.Bon;
import sn.coundoul.gestion.equipement.domain.enumeration.TypeBon;
import sn.coundoul.gestion.equipement.repository.BonRepository;

/**
 * Integration tests for the {@link BonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BonResourceIT {

    private static final TypeBon DEFAULT_TYPE_BON = TypeBon.Entree;
    private static final TypeBon UPDATED_TYPE_BON = TypeBon.Sortie;

    private static final Integer DEFAULT_QUANTITE_LIVRE = 1;
    private static final Integer UPDATED_QUANTITE_LIVRE = 2;

    private static final Integer DEFAULT_QUANTITE_COMMANDE = 1;
    private static final Integer UPDATED_QUANTITE_COMMANDE = 2;

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ETAT = false;
    private static final Boolean UPDATED_ETAT = true;

    private static final String ENTITY_API_URL = "/api/bons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BonRepository bonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBonMockMvc;

    private Bon bon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bon createEntity(EntityManager em) {
        Bon bon = new Bon()
            .typeBon(DEFAULT_TYPE_BON)
            .quantiteLivre(DEFAULT_QUANTITE_LIVRE)
            .quantiteCommande(DEFAULT_QUANTITE_COMMANDE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .etat(DEFAULT_ETAT);
        return bon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bon createUpdatedEntity(EntityManager em) {
        Bon bon = new Bon()
            .typeBon(UPDATED_TYPE_BON)
            .quantiteLivre(UPDATED_QUANTITE_LIVRE)
            .quantiteCommande(UPDATED_QUANTITE_COMMANDE)
            .dateCreation(UPDATED_DATE_CREATION)
            .etat(UPDATED_ETAT);
        return bon;
    }

    @BeforeEach
    public void initTest() {
        bon = createEntity(em);
    }

    @Test
    @Transactional
    void createBon() throws Exception {
        int databaseSizeBeforeCreate = bonRepository.findAll().size();
        // Create the Bon
        restBonMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isCreated());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeCreate + 1);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getTypeBon()).isEqualTo(DEFAULT_TYPE_BON);
        assertThat(testBon.getQuantiteLivre()).isEqualTo(DEFAULT_QUANTITE_LIVRE);
        assertThat(testBon.getQuantiteCommande()).isEqualTo(DEFAULT_QUANTITE_COMMANDE);
        assertThat(testBon.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testBon.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void createBonWithExistingId() throws Exception {
        // Create the Bon with an existing ID
        bon.setId(1L);

        int databaseSizeBeforeCreate = bonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBonMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeBonIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonRepository.findAll().size();
        // set the field null
        bon.setTypeBon(null);

        // Create the Bon, which fails.

        restBonMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isBadRequest());

        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBons() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        // Get all the bonList
        restBonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bon.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeBon").value(hasItem(DEFAULT_TYPE_BON.toString())))
            .andExpect(jsonPath("$.[*].quantiteLivre").value(hasItem(DEFAULT_QUANTITE_LIVRE)))
            .andExpect(jsonPath("$.[*].quantiteCommande").value(hasItem(DEFAULT_QUANTITE_COMMANDE)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.booleanValue())));
    }

    @Test
    @Transactional
    void getBon() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        // Get the bon
        restBonMockMvc
            .perform(get(ENTITY_API_URL_ID, bon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bon.getId().intValue()))
            .andExpect(jsonPath("$.typeBon").value(DEFAULT_TYPE_BON.toString()))
            .andExpect(jsonPath("$.quantiteLivre").value(DEFAULT_QUANTITE_LIVRE))
            .andExpect(jsonPath("$.quantiteCommande").value(DEFAULT_QUANTITE_COMMANDE))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingBon() throws Exception {
        // Get the bon
        restBonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBon() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        int databaseSizeBeforeUpdate = bonRepository.findAll().size();

        // Update the bon
        Bon updatedBon = bonRepository.findById(bon.getId()).get();
        // Disconnect from session so that the updates on updatedBon are not directly saved in db
        em.detach(updatedBon);
        updatedBon
            .typeBon(UPDATED_TYPE_BON)
            .quantiteLivre(UPDATED_QUANTITE_LIVRE)
            .quantiteCommande(UPDATED_QUANTITE_COMMANDE)
            .dateCreation(UPDATED_DATE_CREATION)
            .etat(UPDATED_ETAT);

        restBonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBon.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBon))
            )
            .andExpect(status().isOk());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getTypeBon()).isEqualTo(UPDATED_TYPE_BON);
        assertThat(testBon.getQuantiteLivre()).isEqualTo(UPDATED_QUANTITE_LIVRE);
        assertThat(testBon.getQuantiteCommande()).isEqualTo(UPDATED_QUANTITE_COMMANDE);
        assertThat(testBon.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testBon.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void putNonExistingBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bon.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBonWithPatch() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        int databaseSizeBeforeUpdate = bonRepository.findAll().size();

        // Update the bon using partial update
        Bon partialUpdatedBon = new Bon();
        partialUpdatedBon.setId(bon.getId());

        partialUpdatedBon.typeBon(UPDATED_TYPE_BON).quantiteLivre(UPDATED_QUANTITE_LIVRE);

        restBonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBon.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBon))
            )
            .andExpect(status().isOk());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getTypeBon()).isEqualTo(UPDATED_TYPE_BON);
        assertThat(testBon.getQuantiteLivre()).isEqualTo(UPDATED_QUANTITE_LIVRE);
        assertThat(testBon.getQuantiteCommande()).isEqualTo(DEFAULT_QUANTITE_COMMANDE);
        assertThat(testBon.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testBon.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void fullUpdateBonWithPatch() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        int databaseSizeBeforeUpdate = bonRepository.findAll().size();

        // Update the bon using partial update
        Bon partialUpdatedBon = new Bon();
        partialUpdatedBon.setId(bon.getId());

        partialUpdatedBon
            .typeBon(UPDATED_TYPE_BON)
            .quantiteLivre(UPDATED_QUANTITE_LIVRE)
            .quantiteCommande(UPDATED_QUANTITE_COMMANDE)
            .dateCreation(UPDATED_DATE_CREATION)
            .etat(UPDATED_ETAT);

        restBonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBon.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBon))
            )
            .andExpect(status().isOk());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getTypeBon()).isEqualTo(UPDATED_TYPE_BON);
        assertThat(testBon.getQuantiteLivre()).isEqualTo(UPDATED_QUANTITE_LIVRE);
        assertThat(testBon.getQuantiteCommande()).isEqualTo(UPDATED_QUANTITE_COMMANDE);
        assertThat(testBon.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testBon.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void patchNonExistingBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bon.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBon() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        int databaseSizeBeforeDelete = bonRepository.findAll().size();

        // Delete the bon
        restBonMockMvc
            .perform(delete(ENTITY_API_URL_ID, bon.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
