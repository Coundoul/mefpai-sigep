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
import sn.coundoul.gestion.patrimoine.domain.Projets;
import sn.coundoul.gestion.patrimoine.repository.ProjetsRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Projets}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProjetsResource {

    private final Logger log = LoggerFactory.getLogger(ProjetsResource.class);

    private static final String ENTITY_NAME = "projets";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjetsRepository projetsRepository;

    public ProjetsResource(ProjetsRepository projetsRepository) {
        this.projetsRepository = projetsRepository;
    }

    /**
     * {@code POST  /projets} : Create a new projets.
     *
     * @param projets the projets to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projets, or with status {@code 400 (Bad Request)} if the projets has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/projets")
    public Mono<ResponseEntity<Projets>> createProjets(@Valid @RequestBody Projets projets) throws URISyntaxException {
        log.debug("REST request to save Projets : {}", projets);
        if (projets.getId() != null) {
            throw new BadRequestAlertException("A new projets cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return projetsRepository
            .save(projets)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/projets/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /projets/:id} : Updates an existing projets.
     *
     * @param id the id of the projets to save.
     * @param projets the projets to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projets,
     * or with status {@code 400 (Bad Request)} if the projets is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projets couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/projets/{id}")
    public Mono<ResponseEntity<Projets>> updateProjets(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Projets projets
    ) throws URISyntaxException {
        log.debug("REST request to update Projets : {}, {}", id, projets);
        if (projets.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projets.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return projetsRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return projetsRepository
                        .save(projets)
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
     * {@code PATCH  /projets/:id} : Partial updates given fields of an existing projets, field will ignore if it is null
     *
     * @param id the id of the projets to save.
     * @param projets the projets to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projets,
     * or with status {@code 400 (Bad Request)} if the projets is not valid,
     * or with status {@code 404 (Not Found)} if the projets is not found,
     * or with status {@code 500 (Internal Server Error)} if the projets couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/projets/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Projets>> partialUpdateProjets(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Projets projets
    ) throws URISyntaxException {
        log.debug("REST request to partial update Projets partially : {}, {}", id, projets);
        if (projets.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projets.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return projetsRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Projets> result = projetsRepository
                        .findById(projets.getId())
                        .map(
                            existingProjets -> {
                                if (projets.getTypeProjet() != null) {
                                    existingProjets.setTypeProjet(projets.getTypeProjet());
                                }
                                if (projets.getNomProjet() != null) {
                                    existingProjets.setNomProjet(projets.getNomProjet());
                                }
                                if (projets.getDateDebut() != null) {
                                    existingProjets.setDateDebut(projets.getDateDebut());
                                }
                                if (projets.getDateFin() != null) {
                                    existingProjets.setDateFin(projets.getDateFin());
                                }
                                if (projets.getDescription() != null) {
                                    existingProjets.setDescription(projets.getDescription());
                                }
                                if (projets.getExtension() != null) {
                                    existingProjets.setExtension(projets.getExtension());
                                }

                                return existingProjets;
                            }
                        )
                        .flatMap(projetsRepository::save);

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
     * {@code GET  /projets} : get all the projets.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projets in body.
     */
    @GetMapping("/projets")
    public Mono<ResponseEntity<List<Projets>>> getAllProjets(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Projets");
        return projetsRepository
            .count()
            .zipWith(projetsRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /projets/:id} : get the "id" projets.
     *
     * @param id the id of the projets to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projets, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/projets/{id}")
    public Mono<ResponseEntity<Projets>> getProjets(@PathVariable Long id) {
        log.debug("REST request to get Projets : {}", id);
        Mono<Projets> projets = projetsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(projets);
    }

    /**
     * {@code DELETE  /projets/:id} : delete the "id" projets.
     *
     * @param id the id of the projets to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/projets/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteProjets(@PathVariable Long id) {
        log.debug("REST request to delete Projets : {}", id);
        return projetsRepository
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
