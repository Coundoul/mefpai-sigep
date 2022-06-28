package sn.coundoul.gestion.patrimoine.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.FicheTechniqueMaintenance;
import sn.coundoul.gestion.patrimoine.repository.FicheTechniqueMaintenanceRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.FicheTechniqueMaintenance}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FicheTechniqueMaintenanceResource {

    private final Logger log = LoggerFactory.getLogger(FicheTechniqueMaintenanceResource.class);

    private static final String ENTITY_NAME = "ficheTechniqueMaintenance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FicheTechniqueMaintenanceRepository ficheTechniqueMaintenanceRepository;

    public FicheTechniqueMaintenanceResource(FicheTechniqueMaintenanceRepository ficheTechniqueMaintenanceRepository) {
        this.ficheTechniqueMaintenanceRepository = ficheTechniqueMaintenanceRepository;
    }

    /**
     * {@code POST  /fiche-technique-maintenances} : Create a new ficheTechniqueMaintenance.
     *
     * @param ficheTechniqueMaintenance the ficheTechniqueMaintenance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ficheTechniqueMaintenance, or with status {@code 400 (Bad Request)} if the ficheTechniqueMaintenance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fiche-technique-maintenances")
    public Mono<ResponseEntity<FicheTechniqueMaintenance>> createFicheTechniqueMaintenance(
        @Valid @RequestBody FicheTechniqueMaintenance ficheTechniqueMaintenance
    ) throws URISyntaxException {
        log.debug("REST request to save FicheTechniqueMaintenance : {}", ficheTechniqueMaintenance);
        if (ficheTechniqueMaintenance.getId() != null) {
            throw new BadRequestAlertException("A new ficheTechniqueMaintenance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ficheTechniqueMaintenanceRepository
            .save(ficheTechniqueMaintenance)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/fiche-technique-maintenances/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /fiche-technique-maintenances/:id} : Updates an existing ficheTechniqueMaintenance.
     *
     * @param id the id of the ficheTechniqueMaintenance to save.
     * @param ficheTechniqueMaintenance the ficheTechniqueMaintenance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ficheTechniqueMaintenance,
     * or with status {@code 400 (Bad Request)} if the ficheTechniqueMaintenance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ficheTechniqueMaintenance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fiche-technique-maintenances/{id}")
    public Mono<ResponseEntity<FicheTechniqueMaintenance>> updateFicheTechniqueMaintenance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FicheTechniqueMaintenance ficheTechniqueMaintenance
    ) throws URISyntaxException {
        log.debug("REST request to update FicheTechniqueMaintenance : {}, {}", id, ficheTechniqueMaintenance);
        if (ficheTechniqueMaintenance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ficheTechniqueMaintenance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ficheTechniqueMaintenanceRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return ficheTechniqueMaintenanceRepository
                        .save(ficheTechniqueMaintenance)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            result ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString())
                                    )
                                    .body(result)
                        );
                }
            );
    }

    /**
     * {@code PATCH  /fiche-technique-maintenances/:id} : Partial updates given fields of an existing ficheTechniqueMaintenance, field will ignore if it is null
     *
     * @param id the id of the ficheTechniqueMaintenance to save.
     * @param ficheTechniqueMaintenance the ficheTechniqueMaintenance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ficheTechniqueMaintenance,
     * or with status {@code 400 (Bad Request)} if the ficheTechniqueMaintenance is not valid,
     * or with status {@code 404 (Not Found)} if the ficheTechniqueMaintenance is not found,
     * or with status {@code 500 (Internal Server Error)} if the ficheTechniqueMaintenance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fiche-technique-maintenances/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<FicheTechniqueMaintenance>> partialUpdateFicheTechniqueMaintenance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FicheTechniqueMaintenance ficheTechniqueMaintenance
    ) throws URISyntaxException {
        log.debug("REST request to partial update FicheTechniqueMaintenance partially : {}, {}", id, ficheTechniqueMaintenance);
        if (ficheTechniqueMaintenance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ficheTechniqueMaintenance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ficheTechniqueMaintenanceRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<FicheTechniqueMaintenance> result = ficheTechniqueMaintenanceRepository
                        .findById(ficheTechniqueMaintenance.getId())
                        .map(
                            existingFicheTechniqueMaintenance -> {
                                if (ficheTechniqueMaintenance.getPieceJointe() != null) {
                                    existingFicheTechniqueMaintenance.setPieceJointe(ficheTechniqueMaintenance.getPieceJointe());
                                }
                                if (ficheTechniqueMaintenance.getIdPers() != null) {
                                    existingFicheTechniqueMaintenance.setIdPers(ficheTechniqueMaintenance.getIdPers());
                                }
                                if (ficheTechniqueMaintenance.getDateDepot() != null) {
                                    existingFicheTechniqueMaintenance.setDateDepot(ficheTechniqueMaintenance.getDateDepot());
                                }

                                return existingFicheTechniqueMaintenance;
                            }
                        )
                        .flatMap(ficheTechniqueMaintenanceRepository::save);

                    return result
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            res ->
                                ResponseEntity
                                    .ok()
                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                    .body(res)
                        );
                }
            );
    }

    /**
     * {@code GET  /fiche-technique-maintenances} : get all the ficheTechniqueMaintenances.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ficheTechniqueMaintenances in body.
     */
    @GetMapping("/fiche-technique-maintenances")
    public Mono<ResponseEntity<List<FicheTechniqueMaintenance>>> getAllFicheTechniqueMaintenances(
        Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of FicheTechniqueMaintenances");
        return ficheTechniqueMaintenanceRepository
            .count()
            .zipWith(ficheTechniqueMaintenanceRepository.findAllBy(pageable).collectList())
            .map(
                countWithEntities -> {
                    return ResponseEntity
                        .ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                UriComponentsBuilder.fromHttpRequest(request),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2());
                }
            );
    }

    /**
     * {@code GET  /fiche-technique-maintenances/:id} : get the "id" ficheTechniqueMaintenance.
     *
     * @param id the id of the ficheTechniqueMaintenance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ficheTechniqueMaintenance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fiche-technique-maintenances/{id}")
    public Mono<ResponseEntity<FicheTechniqueMaintenance>> getFicheTechniqueMaintenance(@PathVariable Long id) {
        log.debug("REST request to get FicheTechniqueMaintenance : {}", id);
        Mono<FicheTechniqueMaintenance> ficheTechniqueMaintenance = ficheTechniqueMaintenanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ficheTechniqueMaintenance);
    }

    /**
     * {@code DELETE  /fiche-technique-maintenances/:id} : delete the "id" ficheTechniqueMaintenance.
     *
     * @param id the id of the ficheTechniqueMaintenance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fiche-technique-maintenances/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFicheTechniqueMaintenance(@PathVariable Long id) {
        log.debug("REST request to delete FicheTechniqueMaintenance : {}", id);
        return ficheTechniqueMaintenanceRepository
            .deleteById(id)
            .map(
                result ->
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
            );
    }
}
