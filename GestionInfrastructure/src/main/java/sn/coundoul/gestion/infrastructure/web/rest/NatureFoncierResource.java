package sn.coundoul.gestion.infrastructure.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.coundoul.gestion.infrastructure.domain.NatureFoncier;
import sn.coundoul.gestion.infrastructure.repository.NatureFoncierRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.NatureFoncier}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NatureFoncierResource {

    private final Logger log = LoggerFactory.getLogger(NatureFoncierResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureNatureFoncier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NatureFoncierRepository natureFoncierRepository;

    public NatureFoncierResource(NatureFoncierRepository natureFoncierRepository) {
        this.natureFoncierRepository = natureFoncierRepository;
    }

    /**
     * {@code POST  /nature-fonciers} : Create a new natureFoncier.
     *
     * @param natureFoncier the natureFoncier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new natureFoncier, or with status {@code 400 (Bad Request)} if the natureFoncier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nature-fonciers")
    public ResponseEntity<NatureFoncier> createNatureFoncier(@Valid @RequestBody NatureFoncier natureFoncier) throws URISyntaxException {
        log.debug("REST request to save NatureFoncier : {}", natureFoncier);
        if (natureFoncier.getId() != null) {
            throw new BadRequestAlertException("A new natureFoncier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NatureFoncier result = natureFoncierRepository.save(natureFoncier);
        return ResponseEntity
            .created(new URI("/api/nature-fonciers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nature-fonciers/:id} : Updates an existing natureFoncier.
     *
     * @param id the id of the natureFoncier to save.
     * @param natureFoncier the natureFoncier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated natureFoncier,
     * or with status {@code 400 (Bad Request)} if the natureFoncier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the natureFoncier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nature-fonciers/{id}")
    public ResponseEntity<NatureFoncier> updateNatureFoncier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NatureFoncier natureFoncier
    ) throws URISyntaxException {
        log.debug("REST request to update NatureFoncier : {}, {}", id, natureFoncier);
        if (natureFoncier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, natureFoncier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!natureFoncierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NatureFoncier result = natureFoncierRepository.save(natureFoncier);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, natureFoncier.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nature-fonciers/:id} : Partial updates given fields of an existing natureFoncier, field will ignore if it is null
     *
     * @param id the id of the natureFoncier to save.
     * @param natureFoncier the natureFoncier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated natureFoncier,
     * or with status {@code 400 (Bad Request)} if the natureFoncier is not valid,
     * or with status {@code 404 (Not Found)} if the natureFoncier is not found,
     * or with status {@code 500 (Internal Server Error)} if the natureFoncier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nature-fonciers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<NatureFoncier> partialUpdateNatureFoncier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NatureFoncier natureFoncier
    ) throws URISyntaxException {
        log.debug("REST request to partial update NatureFoncier partially : {}, {}", id, natureFoncier);
        if (natureFoncier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, natureFoncier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!natureFoncierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NatureFoncier> result = natureFoncierRepository
            .findById(natureFoncier.getId())
            .map(
                existingNatureFoncier -> {
                    if (natureFoncier.getTypeFoncier() != null) {
                        existingNatureFoncier.setTypeFoncier(natureFoncier.getTypeFoncier());
                    }
                    if (natureFoncier.getPieceJointe() != null) {
                        existingNatureFoncier.setPieceJointe(natureFoncier.getPieceJointe());
                    }

                    return existingNatureFoncier;
                }
            )
            .map(natureFoncierRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, natureFoncier.getId().toString())
        );
    }

    /**
     * {@code GET  /nature-fonciers} : get all the natureFonciers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of natureFonciers in body.
     */
    @GetMapping("/nature-fonciers")
    public ResponseEntity<List<NatureFoncier>> getAllNatureFonciers(Pageable pageable) {
        log.debug("REST request to get a page of NatureFonciers");
        Page<NatureFoncier> page = natureFoncierRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nature-fonciers/:id} : get the "id" natureFoncier.
     *
     * @param id the id of the natureFoncier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the natureFoncier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nature-fonciers/{id}")
    public ResponseEntity<NatureFoncier> getNatureFoncier(@PathVariable Long id) {
        log.debug("REST request to get NatureFoncier : {}", id);
        Optional<NatureFoncier> natureFoncier = natureFoncierRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(natureFoncier);
    }

    /**
     * {@code DELETE  /nature-fonciers/:id} : delete the "id" natureFoncier.
     *
     * @param id the id of the natureFoncier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nature-fonciers/{id}")
    public ResponseEntity<Void> deleteNatureFoncier(@PathVariable Long id) {
        log.debug("REST request to delete NatureFoncier : {}", id);
        natureFoncierRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
