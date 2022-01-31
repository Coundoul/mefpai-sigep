package sn.coundoul.gestion.patrimoine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import sn.coundoul.gestion.patrimoine.IntegrationTest;
import sn.coundoul.gestion.patrimoine.domain.Bon;
import sn.coundoul.gestion.patrimoine.domain.enumeration.TypeBon;
import sn.coundoul.gestion.patrimoine.repository.BonRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link BonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Bon.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        bon = createEntity(em);
    }

    @Test
    void createBon() throws Exception {
        int databaseSizeBeforeCreate = bonRepository.findAll().collectList().block().size();
        // Create the Bon
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bon))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeCreate + 1);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getTypeBon()).isEqualTo(DEFAULT_TYPE_BON);
        assertThat(testBon.getQuantiteLivre()).isEqualTo(DEFAULT_QUANTITE_LIVRE);
        assertThat(testBon.getQuantiteCommande()).isEqualTo(DEFAULT_QUANTITE_COMMANDE);
        assertThat(testBon.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testBon.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    void createBonWithExistingId() throws Exception {
        // Create the Bon with an existing ID
        bon.setId(1L);

        int databaseSizeBeforeCreate = bonRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bon))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeBonIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonRepository.findAll().collectList().block().size();
        // set the field null
        bon.setTypeBon(null);

        // Create the Bon, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bon))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBons() {
        // Initialize the database
        bonRepository.save(bon).block();

        // Get all the bonList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(bon.getId().intValue()))
            .jsonPath("$.[*].typeBon")
            .value(hasItem(DEFAULT_TYPE_BON.toString()))
            .jsonPath("$.[*].quantiteLivre")
            .value(hasItem(DEFAULT_QUANTITE_LIVRE))
            .jsonPath("$.[*].quantiteCommande")
            .value(hasItem(DEFAULT_QUANTITE_COMMANDE))
            .jsonPath("$.[*].dateCreation")
            .value(hasItem(DEFAULT_DATE_CREATION.toString()))
            .jsonPath("$.[*].etat")
            .value(hasItem(DEFAULT_ETAT.booleanValue()));
    }

    @Test
    void getBon() {
        // Initialize the database
        bonRepository.save(bon).block();

        // Get the bon
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, bon.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(bon.getId().intValue()))
            .jsonPath("$.typeBon")
            .value(is(DEFAULT_TYPE_BON.toString()))
            .jsonPath("$.quantiteLivre")
            .value(is(DEFAULT_QUANTITE_LIVRE))
            .jsonPath("$.quantiteCommande")
            .value(is(DEFAULT_QUANTITE_COMMANDE))
            .jsonPath("$.dateCreation")
            .value(is(DEFAULT_DATE_CREATION.toString()))
            .jsonPath("$.etat")
            .value(is(DEFAULT_ETAT.booleanValue()));
    }

    @Test
    void getNonExistingBon() {
        // Get the bon
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBon() throws Exception {
        // Initialize the database
        bonRepository.save(bon).block();

        int databaseSizeBeforeUpdate = bonRepository.findAll().collectList().block().size();

        // Update the bon
        Bon updatedBon = bonRepository.findById(bon.getId()).block();
        updatedBon
            .typeBon(UPDATED_TYPE_BON)
            .quantiteLivre(UPDATED_QUANTITE_LIVRE)
            .quantiteCommande(UPDATED_QUANTITE_COMMANDE)
            .dateCreation(UPDATED_DATE_CREATION)
            .etat(UPDATED_ETAT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBon.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBon))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getTypeBon()).isEqualTo(UPDATED_TYPE_BON);
        assertThat(testBon.getQuantiteLivre()).isEqualTo(UPDATED_QUANTITE_LIVRE);
        assertThat(testBon.getQuantiteCommande()).isEqualTo(UPDATED_QUANTITE_COMMANDE);
        assertThat(testBon.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testBon.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    void putNonExistingBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().collectList().block().size();
        bon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, bon.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bon))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().collectList().block().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bon))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().collectList().block().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bon))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBonWithPatch() throws Exception {
        // Initialize the database
        bonRepository.save(bon).block();

        int databaseSizeBeforeUpdate = bonRepository.findAll().collectList().block().size();

        // Update the bon using partial update
        Bon partialUpdatedBon = new Bon();
        partialUpdatedBon.setId(bon.getId());

        partialUpdatedBon.typeBon(UPDATED_TYPE_BON).quantiteLivre(UPDATED_QUANTITE_LIVRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBon.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBon))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getTypeBon()).isEqualTo(UPDATED_TYPE_BON);
        assertThat(testBon.getQuantiteLivre()).isEqualTo(UPDATED_QUANTITE_LIVRE);
        assertThat(testBon.getQuantiteCommande()).isEqualTo(DEFAULT_QUANTITE_COMMANDE);
        assertThat(testBon.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testBon.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    void fullUpdateBonWithPatch() throws Exception {
        // Initialize the database
        bonRepository.save(bon).block();

        int databaseSizeBeforeUpdate = bonRepository.findAll().collectList().block().size();

        // Update the bon using partial update
        Bon partialUpdatedBon = new Bon();
        partialUpdatedBon.setId(bon.getId());

        partialUpdatedBon
            .typeBon(UPDATED_TYPE_BON)
            .quantiteLivre(UPDATED_QUANTITE_LIVRE)
            .quantiteCommande(UPDATED_QUANTITE_COMMANDE)
            .dateCreation(UPDATED_DATE_CREATION)
            .etat(UPDATED_ETAT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBon.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBon))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getTypeBon()).isEqualTo(UPDATED_TYPE_BON);
        assertThat(testBon.getQuantiteLivre()).isEqualTo(UPDATED_QUANTITE_LIVRE);
        assertThat(testBon.getQuantiteCommande()).isEqualTo(UPDATED_QUANTITE_COMMANDE);
        assertThat(testBon.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testBon.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    void patchNonExistingBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().collectList().block().size();
        bon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, bon.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bon))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().collectList().block().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bon))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().collectList().block().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bon))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBon() {
        // Initialize the database
        bonRepository.save(bon).block();

        int databaseSizeBeforeDelete = bonRepository.findAll().collectList().block().size();

        // Delete the bon
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, bon.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Bon> bonList = bonRepository.findAll().collectList().block();
        assertThat(bonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
