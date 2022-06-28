package sn.coundoul.gestion.patrimoine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
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
import org.springframework.util.Base64Utils;
import sn.coundoul.gestion.patrimoine.IntegrationTest;
import sn.coundoul.gestion.patrimoine.domain.Equipement;
import sn.coundoul.gestion.patrimoine.repository.EquipementRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link EquipementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
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
    private WebTestClient webTestClient;

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
            .prixUnitaire(DEFAULT_PRIX_UNITAIRE)
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
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .typeMatiere(UPDATED_TYPE_MATIERE)
            .quantite(UPDATED_QUANTITE)
            .etatMatiere(UPDATED_ETAT_MATIERE)
            .group(UPDATED_GROUP)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        return equipement;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Equipement.class).block();
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
        equipement = createEntity(em);
    }

    @Test
    void createEquipement() throws Exception {
        int databaseSizeBeforeCreate = equipementRepository.findAll().collectList().block().size();
        // Create the Equipement
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeCreate + 1);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testEquipement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEquipement.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
        assertThat(testEquipement.getTypeMatiere()).isEqualTo(DEFAULT_TYPE_MATIERE);
        assertThat(testEquipement.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testEquipement.getEtatMatiere()).isEqualTo(DEFAULT_ETAT_MATIERE);
        assertThat(testEquipement.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testEquipement.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testEquipement.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    void createEquipementWithExistingId() throws Exception {
        // Create the Equipement with an existing ID
        equipement.setId(1L);

        int databaseSizeBeforeCreate = equipementRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().collectList().block().size();
        // set the field null
        equipement.setReference(null);

        // Create the Equipement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrixUnitaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().collectList().block().size();
        // set the field null
        equipement.setPrixUnitaire(null);

        // Create the Equipement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeMatiereIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().collectList().block().size();
        // set the field null
        equipement.setTypeMatiere(null);

        // Create the Equipement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().collectList().block().size();
        // set the field null
        equipement.setQuantite(null);

        // Create the Equipement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEtatMatiereIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().collectList().block().size();
        // set the field null
        equipement.setEtatMatiere(null);

        // Create the Equipement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipementRepository.findAll().collectList().block().size();
        // set the field null
        equipement.setGroup(null);

        // Create the Equipement, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEquipements() {
        // Initialize the database
        equipementRepository.save(equipement).block();

        // Get all the equipementList
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
            .value(hasItem(equipement.getId().intValue()))
            .jsonPath("$.[*].reference")
            .value(hasItem(DEFAULT_REFERENCE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].prixUnitaire")
            .value(hasItem(DEFAULT_PRIX_UNITAIRE))
            .jsonPath("$.[*].typeMatiere")
            .value(hasItem(DEFAULT_TYPE_MATIERE))
            .jsonPath("$.[*].quantite")
            .value(hasItem(DEFAULT_QUANTITE))
            .jsonPath("$.[*].etatMatiere")
            .value(hasItem(DEFAULT_ETAT_MATIERE))
            .jsonPath("$.[*].group")
            .value(hasItem(DEFAULT_GROUP.booleanValue()))
            .jsonPath("$.[*].photoContentType")
            .value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].photo")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO)));
    }

    @Test
    void getEquipement() {
        // Initialize the database
        equipementRepository.save(equipement).block();

        // Get the equipement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, equipement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(equipement.getId().intValue()))
            .jsonPath("$.reference")
            .value(is(DEFAULT_REFERENCE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.prixUnitaire")
            .value(is(DEFAULT_PRIX_UNITAIRE))
            .jsonPath("$.typeMatiere")
            .value(is(DEFAULT_TYPE_MATIERE))
            .jsonPath("$.quantite")
            .value(is(DEFAULT_QUANTITE))
            .jsonPath("$.etatMatiere")
            .value(is(DEFAULT_ETAT_MATIERE))
            .jsonPath("$.group")
            .value(is(DEFAULT_GROUP.booleanValue()))
            .jsonPath("$.photoContentType")
            .value(is(DEFAULT_PHOTO_CONTENT_TYPE))
            .jsonPath("$.photo")
            .value(is(Base64Utils.encodeToString(DEFAULT_PHOTO)));
    }

    @Test
    void getNonExistingEquipement() {
        // Get the equipement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewEquipement() throws Exception {
        // Initialize the database
        equipementRepository.save(equipement).block();

        int databaseSizeBeforeUpdate = equipementRepository.findAll().collectList().block().size();

        // Update the equipement
        Equipement updatedEquipement = equipementRepository.findById(equipement.getId()).block();
        updatedEquipement
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .typeMatiere(UPDATED_TYPE_MATIERE)
            .quantite(UPDATED_QUANTITE)
            .etatMatiere(UPDATED_ETAT_MATIERE)
            .group(UPDATED_GROUP)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEquipement.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedEquipement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testEquipement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipement.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
        assertThat(testEquipement.getTypeMatiere()).isEqualTo(UPDATED_TYPE_MATIERE);
        assertThat(testEquipement.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testEquipement.getEtatMatiere()).isEqualTo(UPDATED_ETAT_MATIERE);
        assertThat(testEquipement.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testEquipement.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testEquipement.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    void putNonExistingEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().collectList().block().size();
        equipement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, equipement.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().collectList().block().size();
        equipement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().collectList().block().size();
        equipement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEquipementWithPatch() throws Exception {
        // Initialize the database
        equipementRepository.save(equipement).block();

        int databaseSizeBeforeUpdate = equipementRepository.findAll().collectList().block().size();

        // Update the equipement using partial update
        Equipement partialUpdatedEquipement = new Equipement();
        partialUpdatedEquipement.setId(equipement.getId());

        partialUpdatedEquipement.typeMatiere(UPDATED_TYPE_MATIERE).etatMatiere(UPDATED_ETAT_MATIERE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEquipement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testEquipement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEquipement.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
        assertThat(testEquipement.getTypeMatiere()).isEqualTo(UPDATED_TYPE_MATIERE);
        assertThat(testEquipement.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testEquipement.getEtatMatiere()).isEqualTo(UPDATED_ETAT_MATIERE);
        assertThat(testEquipement.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testEquipement.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testEquipement.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    void fullUpdateEquipementWithPatch() throws Exception {
        // Initialize the database
        equipementRepository.save(equipement).block();

        int databaseSizeBeforeUpdate = equipementRepository.findAll().collectList().block().size();

        // Update the equipement using partial update
        Equipement partialUpdatedEquipement = new Equipement();
        partialUpdatedEquipement.setId(equipement.getId());

        partialUpdatedEquipement
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .typeMatiere(UPDATED_TYPE_MATIERE)
            .quantite(UPDATED_QUANTITE)
            .etatMatiere(UPDATED_ETAT_MATIERE)
            .group(UPDATED_GROUP)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEquipement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testEquipement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipement.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
        assertThat(testEquipement.getTypeMatiere()).isEqualTo(UPDATED_TYPE_MATIERE);
        assertThat(testEquipement.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testEquipement.getEtatMatiere()).isEqualTo(UPDATED_ETAT_MATIERE);
        assertThat(testEquipement.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testEquipement.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testEquipement.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    void patchNonExistingEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().collectList().block().size();
        equipement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, equipement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().collectList().block().size();
        equipement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().collectList().block().size();
        equipement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(equipement))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEquipement() {
        // Initialize the database
        equipementRepository.save(equipement).block();

        int databaseSizeBeforeDelete = equipementRepository.findAll().collectList().block().size();

        // Delete the equipement
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, equipement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Equipement> equipementList = equipementRepository.findAll().collectList().block();
        assertThat(equipementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
