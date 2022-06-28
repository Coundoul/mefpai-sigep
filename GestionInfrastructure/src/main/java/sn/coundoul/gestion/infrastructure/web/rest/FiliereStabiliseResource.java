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
import sn.coundoul.gestion.infrastructure.domain.FiliereStabilise;
import sn.coundoul.gestion.infrastructure.repository.FiliereStabiliseRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.FiliereStabilise}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FiliereStabiliseResource {

    private final Logger log = LoggerFactory.getLogger(FiliereStabiliseResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureFiliereStabilise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FiliereStabiliseRepository filiereStabiliseRepository;

    public FiliereStabiliseResource(FiliereStabiliseRepository filiereStabiliseRepository) {
        this.filiereStabiliseRepository = filiereStabiliseRepository;
    }

    /**
     * {@code POST  /filiere-stabilises} : Create a new filiereStabilise.
     *
     * @param filiereStabilise the filiereStabilise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filiereStabilise, or with status {@code 400 (Bad Request)} if the filiereStabilise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/filiere-stabilises")
    public ResponseEntity<FiliereStabilise> createFiliereStabilise(@Valid @RequestBody FiliereStabilise filiereStabilise)
        throws URISyntaxException {
        log.debug("REST request to save FiliereStabilise : {}", filiereStabilise);
        if (filiereStabilise.getId() != null) {
            throw new BadRequestAlertException("A new filiereStabilise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FiliereStabilise result = filiereStabiliseRepository.save(filiereStabilise);
        return ResponseEntity
            .created(new URI("/api/filiere-stabilises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /filiere-stabilises/:id} : Updates an existing filiereStabilise.
     *
     * @param id the id of the filiereStabilise to save.
     * @param filiereStabilise the filiereStabilise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filiereStabilise,
     * or with status {@code 400 (Bad Request)} if the filiereStabilise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filiereStabilise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/filiere-stabilises/{id}")
    public ResponseEntity<FiliereStabilise> updateFiliereStabilise(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FiliereStabilise filiereStabilise
    ) throws URISyntaxException {
        log.debug("REST request to update FiliereStabilise : {}, {}", id, filiereStabilise);
        if (filiereStabilise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filiereStabilise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filiereStabiliseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FiliereStabilise result = filiereStabiliseRepository.save(filiereStabilise);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filiereStabilise.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /filiere-stabilises/:id} : Partial updates given fields of an existing filiereStabilise, field will ignore if it is null
     *
     * @param id the id of the filiereStabilise to save.
     * @param filiereStabilise the filiereStabilise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filiereStabilise,
     * or with status {@code 400 (Bad Request)} if the filiereStabilise is not valid,
     * or with status {@code 404 (Not Found)} if the filiereStabilise is not found,
     * or with status {@code 500 (Internal Server Error)} if the filiereStabilise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/filiere-stabilises/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FiliereStabilise> partialUpdateFiliereStabilise(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FiliereStabilise filiereStabilise
    ) throws URISyntaxException {
        log.debug("REST request to partial update FiliereStabilise partially : {}, {}", id, filiereStabilise);
        if (filiereStabilise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filiereStabilise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filiereStabiliseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FiliereStabilise> result = filiereStabiliseRepository
            .findById(filiereStabilise.getId())
            .map(
                existingFiliereStabilise -> {
                    if (filiereStabilise.getNomFiliere() != null) {
                        existingFiliereStabilise.setNomFiliere(filiereStabilise.getNomFiliere());
                    }

                    return existingFiliereStabilise;
                }
            )
            .map(filiereStabiliseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filiereStabilise.getId().toString())
        );
    }

    /**
     * {@code GET  /filiere-stabilises} : get all the filiereStabilises.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filiereStabilises in body.
     */
    @GetMapping("/filiere-stabilises")
    public ResponseEntity<List<FiliereStabilise>> getAllFiliereStabilises(Pageable pageable) {
        log.debug("REST request to get a page of FiliereStabilises");
        Page<FiliereStabilise> page = filiereStabiliseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /filiere-stabilises/:id} : get the "id" filiereStabilise.
     *
     * @param id the id of the filiereStabilise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filiereStabilise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/filiere-stabilises/{id}")
    public ResponseEntity<FiliereStabilise> getFiliereStabilise(@PathVariable Long id) {
        log.debug("REST request to get FiliereStabilise : {}", id);
        Optional<FiliereStabilise> filiereStabilise = filiereStabiliseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(filiereStabilise);
    }

    /**
     * {@code DELETE  /filiere-stabilises/:id} : delete the "id" filiereStabilise.
     *
     * @param id the id of the filiereStabilise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/filiere-stabilises/{id}")
    public ResponseEntity<Void> deleteFiliereStabilise(@PathVariable Long id) {
        log.debug("REST request to delete FiliereStabilise : {}", id);
        filiereStabiliseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
