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
import sn.coundoul.gestion.patrimoine.domain.Departement;
import sn.coundoul.gestion.patrimoine.repository.DepartementRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Departement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DepartementResource {

    private final Logger log = LoggerFactory.getLogger(DepartementResource.class);

    private static final String ENTITY_NAME = "departement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartementRepository departementRepository;

    public DepartementResource(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    /**
     * {@code POST  /departements} : Create a new departement.
     *
     * @param departement the departement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departement, or with status {@code 400 (Bad Request)} if the departement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departements")
    public Mono<ResponseEntity<Departement>> createDepartement(@Valid @RequestBody Departement departement) throws URISyntaxException {
        log.debug("REST request to save Departement : {}", departement);
        if (departement.getId() != null) {
            throw new BadRequestAlertException("A new departement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return departementRepository
            .save(departement)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/departements/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /departements/:id} : Updates an existing departement.
     *
     * @param id the id of the departement to save.
     * @param departement the departement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departement,
     * or with status {@code 400 (Bad Request)} if the departement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departements/{id}")
    public Mono<ResponseEntity<Departement>> updateDepartement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Departement departement
    ) throws URISyntaxException {
        log.debug("REST request to update Departement : {}, {}", id, departement);
        if (departement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return departementRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return departementRepository
                        .save(departement)
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
     * {@code PATCH  /departements/:id} : Partial updates given fields of an existing departement, field will ignore if it is null
     *
     * @param id the id of the departement to save.
     * @param departement the departement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departement,
     * or with status {@code 400 (Bad Request)} if the departement is not valid,
     * or with status {@code 404 (Not Found)} if the departement is not found,
     * or with status {@code 500 (Internal Server Error)} if the departement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/departements/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Departement>> partialUpdateDepartement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Departement departement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Departement partially : {}, {}", id, departement);
        if (departement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return departementRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Departement> result = departementRepository
                        .findById(departement.getId())
                        .map(
                            existingDepartement -> {
                                if (departement.getNomDepartement() != null) {
                                    existingDepartement.setNomDepartement(departement.getNomDepartement());
                                }

                                return existingDepartement;
                            }
                        )
                        .flatMap(departementRepository::save);

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
     * {@code GET  /departements} : get all the departements.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departements in body.
     */
    @GetMapping("/departements")
    public Mono<ResponseEntity<List<Departement>>> getAllDepartements(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Departements");
        return departementRepository
            .count()
            .zipWith(departementRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /departements/:id} : get the "id" departement.
     *
     * @param id the id of the departement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departements/{id}")
    public Mono<ResponseEntity<Departement>> getDepartement(@PathVariable Long id) {
        log.debug("REST request to get Departement : {}", id);
        Mono<Departement> departement = departementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(departement);
    }

    /**
     * {@code DELETE  /departements/:id} : delete the "id" departement.
     *
     * @param id the id of the departement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departements/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDepartement(@PathVariable Long id) {
        log.debug("REST request to delete Departement : {}", id);
        return departementRepository
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
