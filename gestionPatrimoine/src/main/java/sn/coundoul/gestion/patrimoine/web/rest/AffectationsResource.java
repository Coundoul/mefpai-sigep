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
import sn.coundoul.gestion.patrimoine.domain.Affectations;
import sn.coundoul.gestion.patrimoine.repository.AffectationsRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Affectations}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AffectationsResource {

    private final Logger log = LoggerFactory.getLogger(AffectationsResource.class);

    private static final String ENTITY_NAME = "affectations";

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
    public Mono<ResponseEntity<Affectations>> createAffectations(@Valid @RequestBody Affectations affectations) throws URISyntaxException {
        log.debug("REST request to save Affectations : {}", affectations);
        if (affectations.getId() != null) {
            throw new BadRequestAlertException("A new affectations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return affectationsRepository
            .save(affectations)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/affectations/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
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
    public Mono<ResponseEntity<Affectations>> updateAffectations(
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

        return affectationsRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return affectationsRepository
                        .save(affectations)
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
    public Mono<ResponseEntity<Affectations>> partialUpdateAffectations(
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

        return affectationsRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Affectations> result = affectationsRepository
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
                        .flatMap(affectationsRepository::save);

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
     * {@code GET  /affectations} : get all the affectations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affectations in body.
     */
    @GetMapping("/affectations")
    public Mono<ResponseEntity<List<Affectations>>> getAllAffectations(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Affectations");
        return affectationsRepository
            .count()
            .zipWith(affectationsRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /affectations/:id} : get the "id" affectations.
     *
     * @param id the id of the affectations to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affectations, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/affectations/{id}")
    public Mono<ResponseEntity<Affectations>> getAffectations(@PathVariable Long id) {
        log.debug("REST request to get Affectations : {}", id);
        Mono<Affectations> affectations = affectationsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(affectations);
    }

    /**
     * {@code DELETE  /affectations/:id} : delete the "id" affectations.
     *
     * @param id the id of the affectations to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/affectations/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAffectations(@PathVariable Long id) {
        log.debug("REST request to delete Affectations : {}", id);
        return affectationsRepository
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
