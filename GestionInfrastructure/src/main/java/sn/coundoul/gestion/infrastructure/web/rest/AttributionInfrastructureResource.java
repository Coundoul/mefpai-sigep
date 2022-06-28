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
import sn.coundoul.gestion.infrastructure.domain.AttributionInfrastructure;
import sn.coundoul.gestion.infrastructure.repository.AttributionInfrastructureRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.AttributionInfrastructure}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AttributionInfrastructureResource {

    private final Logger log = LoggerFactory.getLogger(AttributionInfrastructureResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureAttributionInfrastructure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributionInfrastructureRepository attributionInfrastructureRepository;

    public AttributionInfrastructureResource(AttributionInfrastructureRepository attributionInfrastructureRepository) {
        this.attributionInfrastructureRepository = attributionInfrastructureRepository;
    }

    /**
     * {@code POST  /attribution-infrastructures} : Create a new attributionInfrastructure.
     *
     * @param attributionInfrastructure the attributionInfrastructure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributionInfrastructure, or with status {@code 400 (Bad Request)} if the attributionInfrastructure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attribution-infrastructures")
    public ResponseEntity<AttributionInfrastructure> createAttributionInfrastructure(
        @Valid @RequestBody AttributionInfrastructure attributionInfrastructure
    ) throws URISyntaxException {
        log.debug("REST request to save AttributionInfrastructure : {}", attributionInfrastructure);
        if (attributionInfrastructure.getId() != null) {
            throw new BadRequestAlertException("A new attributionInfrastructure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttributionInfrastructure result = attributionInfrastructureRepository.save(attributionInfrastructure);
        return ResponseEntity
            .created(new URI("/api/attribution-infrastructures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attribution-infrastructures/:id} : Updates an existing attributionInfrastructure.
     *
     * @param id the id of the attributionInfrastructure to save.
     * @param attributionInfrastructure the attributionInfrastructure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributionInfrastructure,
     * or with status {@code 400 (Bad Request)} if the attributionInfrastructure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributionInfrastructure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attribution-infrastructures/{id}")
    public ResponseEntity<AttributionInfrastructure> updateAttributionInfrastructure(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AttributionInfrastructure attributionInfrastructure
    ) throws URISyntaxException {
        log.debug("REST request to update AttributionInfrastructure : {}, {}", id, attributionInfrastructure);
        if (attributionInfrastructure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributionInfrastructure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributionInfrastructureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AttributionInfrastructure result = attributionInfrastructureRepository.save(attributionInfrastructure);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attributionInfrastructure.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attribution-infrastructures/:id} : Partial updates given fields of an existing attributionInfrastructure, field will ignore if it is null
     *
     * @param id the id of the attributionInfrastructure to save.
     * @param attributionInfrastructure the attributionInfrastructure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributionInfrastructure,
     * or with status {@code 400 (Bad Request)} if the attributionInfrastructure is not valid,
     * or with status {@code 404 (Not Found)} if the attributionInfrastructure is not found,
     * or with status {@code 500 (Internal Server Error)} if the attributionInfrastructure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attribution-infrastructures/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AttributionInfrastructure> partialUpdateAttributionInfrastructure(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AttributionInfrastructure attributionInfrastructure
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttributionInfrastructure partially : {}, {}", id, attributionInfrastructure);
        if (attributionInfrastructure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributionInfrastructure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributionInfrastructureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttributionInfrastructure> result = attributionInfrastructureRepository
            .findById(attributionInfrastructure.getId())
            .map(
                existingAttributionInfrastructure -> {
                    if (attributionInfrastructure.getDateAttribution() != null) {
                        existingAttributionInfrastructure.setDateAttribution(attributionInfrastructure.getDateAttribution());
                    }
                    if (attributionInfrastructure.getQuantite() != null) {
                        existingAttributionInfrastructure.setQuantite(attributionInfrastructure.getQuantite());
                    }
                    if (attributionInfrastructure.getIdEquipement() != null) {
                        existingAttributionInfrastructure.setIdEquipement(attributionInfrastructure.getIdEquipement());
                    }
                    if (attributionInfrastructure.getIdPers() != null) {
                        existingAttributionInfrastructure.setIdPers(attributionInfrastructure.getIdPers());
                    }

                    return existingAttributionInfrastructure;
                }
            )
            .map(attributionInfrastructureRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attributionInfrastructure.getId().toString())
        );
    }

    /**
     * {@code GET  /attribution-infrastructures} : get all the attributionInfrastructures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributionInfrastructures in body.
     */
    @GetMapping("/attribution-infrastructures")
    public ResponseEntity<List<AttributionInfrastructure>> getAllAttributionInfrastructures(Pageable pageable) {
        log.debug("REST request to get a page of AttributionInfrastructures");
        Page<AttributionInfrastructure> page = attributionInfrastructureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attribution-infrastructures/:id} : get the "id" attributionInfrastructure.
     *
     * @param id the id of the attributionInfrastructure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributionInfrastructure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attribution-infrastructures/{id}")
    public ResponseEntity<AttributionInfrastructure> getAttributionInfrastructure(@PathVariable Long id) {
        log.debug("REST request to get AttributionInfrastructure : {}", id);
        Optional<AttributionInfrastructure> attributionInfrastructure = attributionInfrastructureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attributionInfrastructure);
    }

    /**
     * {@code DELETE  /attribution-infrastructures/:id} : delete the "id" attributionInfrastructure.
     *
     * @param id the id of the attributionInfrastructure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attribution-infrastructures/{id}")
    public ResponseEntity<Void> deleteAttributionInfrastructure(@PathVariable Long id) {
        log.debug("REST request to delete AttributionInfrastructure : {}", id);
        attributionInfrastructureRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
