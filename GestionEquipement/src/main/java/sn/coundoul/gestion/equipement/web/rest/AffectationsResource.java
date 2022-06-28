package sn.coundoul.gestion.equipement.web.rest;

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
import sn.coundoul.gestion.equipement.domain.Affectations;
import sn.coundoul.gestion.equipement.repository.AffectationsRepository;
import sn.coundoul.gestion.equipement.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.equipement.domain.Affectations}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AffectationsResource {

    private final Logger log = LoggerFactory.getLogger(AffectationsResource.class);

    private static final String ENTITY_NAME = "gestionEquipementAffectations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AffectationsRepository affectationsRepository;

    public AffectationsResource(AffectationsRepository affectationsRepository) {
        this.affectationsRepository = affectationsRepository;
    }

    /**
     * {@code POST  /affectations} : Create a new affectations.
     *
     * @param affectations the affectations to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affectations, or with status {@code 400 (Bad Request)} if the affectations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/affectations")
    public ResponseEntity<Affectations> createAffectations(@Valid @RequestBody Affectations affectations) throws URISyntaxException {
        log.debug("REST request to save Affectations : {}", affectations);
        if (affectations.getId() != null) {
            throw new BadRequestAlertException("A new affectations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Affectations result = affectationsRepository.save(affectations);
        return ResponseEntity
            .created(new URI("/api/affectations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /affectations/:id} : Updates an existing affectations.
     *
     * @param id the id of the affectations to save.
     * @param affectations the affectations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectations,
     * or with status {@code 400 (Bad Request)} if the affectations is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affectations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/affectations/{id}")
    public ResponseEntity<Affectations> updateAffectations(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Affectations affectations
    ) throws URISyntaxException {
        log.debug("REST request to update Affectations : {}, {}", id, affectations);
        if (affectations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Affectations result = affectationsRepository.save(affectations);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectations.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /affectations/:id} : Partial updates given fields of an existing affectations, field will ignore if it is null
     *
     * @param id the id of the affectations to save.
     * @param affectations the affectations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectations,
     * or with status {@code 400 (Bad Request)} if the affectations is not valid,
     * or with status {@code 404 (Not Found)} if the affectations is not found,
     * or with status {@code 500 (Internal Server Error)} if the affectations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/affectations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Affectations> partialUpdateAffectations(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Affectations affectations
    ) throws URISyntaxException {
        log.debug("REST request to partial update Affectations partially : {}, {}", id, affectations);
        if (affectations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Affectations> result = affectationsRepository
            .findById(affectations.getId())
            .map(
                existingAffectations -> {
                    if (affectations.getQuantiteAffecter() != null) {
                        existingAffectations.setQuantiteAffecter(affectations.getQuantiteAffecter());
                    }
                    if (affectations.getTypeAttribution() != null) {
                        existingAffectations.setTypeAttribution(affectations.getTypeAttribution());
                    }
                    if (affectations.getIdPers() != null) {
                        existingAffectations.setIdPers(affectations.getIdPers());
                    }
                    if (affectations.getDateAttribution() != null) {
                        existingAffectations.setDateAttribution(affectations.getDateAttribution());
                    }

                    return existingAffectations;
                }
            )
            .map(affectationsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectations.getId().toString())
        );
    }

    /**
     * {@code GET  /affectations} : get all the affectations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affectations in body.
     */
    @GetMapping("/affectations")
    public ResponseEntity<List<Affectations>> getAllAffectations(Pageable pageable) {
        log.debug("REST request to get a page of Affectations");
        Page<Affectations> page = affectationsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /affectations/:id} : get the "id" affectations.
     *
     * @param id the id of the affectations to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affectations, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/affectations/{id}")
    public ResponseEntity<Affectations> getAffectations(@PathVariable Long id) {
        log.debug("REST request to get Affectations : {}", id);
        Optional<Affectations> affectations = affectationsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(affectations);
    }

    /**
     * {@code DELETE  /affectations/:id} : delete the "id" affectations.
     *
     * @param id the id of the affectations to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/affectations/{id}")
    public ResponseEntity<Void> deleteAffectations(@PathVariable Long id) {
        log.debug("REST request to delete Affectations : {}", id);
        affectationsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
