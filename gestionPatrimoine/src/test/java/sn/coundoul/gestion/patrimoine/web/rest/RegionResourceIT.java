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
import sn.coundoul.gestion.patrimoine.IntegrationTest;
import sn.coundoul.gestion.patrimoine.domain.Region;
import sn.coundoul.gestion.patrimoine.domain.enumeration.NomRegion;
import sn.coundoul.gestion.patrimoine.repository.RegionRepository;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Integration tests for the {@link RegionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class RegionResourceIT {

    private static final NomRegion DEFAULT_NOM_REGION = NomRegion.Dakar;
    private static final NomRegion UPDATED_NOM_REGION = NomRegion.Thies;

    private static final String ENTITY_API_URL = "/api/regions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Region region;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Region createEntity(EntityManager em) {
        Region region = new Region().nomRegion(DEFAULT_NOM_REGION);
        return region;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Region createUpdatedEntity(EntityManager em) {
        Region region = new Region().nomRegion(UPDATED_NOM_REGION);
        return region;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Region.class).block();
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
        region = createEntity(em);
    }

    @Test
    void createRegion() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().collectList().block().size();
        // Create the Region
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(region))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate + 1);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getNomRegion()).isEqualTo(DEFAULT_NOM_REGION);
    }

    @Test
    void createRegionWithExistingId() throws Exception {
        // Create the Region with an existing ID
        region.setId(1L);

        int databaseSizeBeforeCreate = regionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(region))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = regionRepository.findAll().collectList().block().size();
        // set the field null
        region.setNomRegion(null);

        // Create the Region, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(region))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRegions() {
        // Initialize the database
        regionRepository.save(region).block();

        // Get all the regionList
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
            .value(hasItem(region.getId().intValue()))
            .jsonPath("$.[*].nomRegion")
            .value(hasItem(DEFAULT_NOM_REGION.toString()));
    }

    @Test
    void getRegion() {
        // Initialize the database
        regionRepository.save(region).block();

        // Get the region
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, region.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(region.getId().intValue()))
            .jsonPath("$.nomRegion")
            .value(is(DEFAULT_NOM_REGION.toString()));
    }

    @Test
    void getNonExistingRegion() {
        // Get the region
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewRegion() throws Exception {
        // Initialize the database
        regionRepository.save(region).block();

        int databaseSizeBeforeUpdate = regionRepository.findAll().collectList().block().size();

        // Update the region
        Region updatedRegion = regionRepository.findById(region.getId()).block();
        updatedRegion.nomRegion(UPDATED_NOM_REGION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRegion.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRegion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getNomRegion()).isEqualTo(UPDATED_NOM_REGION);
    }

    @Test
    void putNonExistingRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().collectList().block().size();
        region.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, region.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(region))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().collectList().block().size();
        region.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(region))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().collectList().block().size();
        region.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(region))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRegionWithPatch() throws Exception {
        // Initialize the database
        regionRepository.save(region).block();

        int databaseSizeBeforeUpdate = regionRepository.findAll().collectList().block().size();

        // Update the region using partial update
        Region partialUpdatedRegion = new Region();
        partialUpdatedRegion.setId(region.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRegion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRegion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getNomRegion()).isEqualTo(DEFAULT_NOM_REGION);
    }

    @Test
    void fullUpdateRegionWithPatch() throws Exception {
        // Initialize the database
        regionRepository.save(region).block();

        int databaseSizeBeforeUpdate = regionRepository.findAll().collectList().block().size();

        // Update the region using partial update
        Region partialUpdatedRegion = new Region();
        partialUpdatedRegion.setId(region.getId());

        partialUpdatedRegion.nomRegion(UPDATED_NOM_REGION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRegion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRegion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getNomRegion()).isEqualTo(UPDATED_NOM_REGION);
    }

    @Test
    void patchNonExistingRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().collectList().block().size();
        region.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, region.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(region))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().collectList().block().size();
        region.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(region))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().collectList().block().size();
        region.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(region))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRegion() {
        // Initialize the database
        regionRepository.save(region).block();

        int databaseSizeBeforeDelete = regionRepository.findAll().collectList().block().size();

        // Delete the region
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, region.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Region> regionList = regionRepository.findAll().collectList().block();
        assertThat(regionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}