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
import sn.coundoul.gestion.patrimoine.domain.Quartier;
import sn.coundoul.gestion.patrimoine.repository.QuartierRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Quartier}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QuartierResource {

    private final Logger log = LoggerFactory.getLogger(QuartierResource.class);

    private static final String ENTITY_NAME = "quartier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuartierRepository quartierRepository;

    public QuartierResource(QuartierRepository quartierRepository) {
        this.quartierRepository = quartierRepository;
    }

    /**
     * {@code POST  /quartiers} : Create a new quartier.
     *
     * @param quartier the quartier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quartier, or with status {@code 400 (Bad Request)} if the quartier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quartiers")
    public Mono<ResponseEntity<Quartier>> createQuartier(@Valid @RequestBody Quartier quartier) throws URISyntaxException {
        log.debug("REST request to save Quartier : {}", quartier);
        if (quartier.getId() != null) {
            throw new BadRequestAlertException("A new quartier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return quartierRepository
            .save(quartier)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/quartiers/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /quartiers/:id} : Updates an existing quartier.
     *
     * @param id the id of the quartier to save.
     * @param quartier the quartier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quartier,
     * or with status {@code 400 (Bad Request)} if the quartier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quartier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quartiers/{id}")
    public Mono<ResponseEntity<Quartier>> updateQuartier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Quartier quartier
    ) throws URISyntaxException {
        log.debug("REST request to update Quartier : {}, {}", id, quartier);
        if (quartier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quartier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return quartierRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return quartierRepository
                        .save(quartier)
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
     * {@code PATCH  /quartiers/:id} : Partial updates given fields of an existing quartier, field will ignore if it is null
     *
     * @param id the id of the quartier to save.
     * @param quartier the quartier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quartier,
     * or with status {@code 400 (Bad Request)} if the quartier is not valid,
     * or with status {@code 404 (Not Found)} if the quartier is not found,
     * or with status {@code 500 (Internal Server Error)} if the quartier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quartiers/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Quartier>> partialUpdateQuartier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Quartier quartier
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quartier partially : {}, {}", id, quartier);
        if (quartier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quartier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return quartierRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Quartier> result = quartierRepository
                        .findById(quartier.getId())
                        .map(
                            existingQuartier -> {
                                if (quartier.getNomQuartier() != null) {
                                    existingQuartier.setNomQuartier(quartier.getNomQuartier());
                                }

                                return existingQuartier;
                            }
                        )
                        .flatMap(quartierRepository::save);

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
     * {@code GET  /quartiers} : get all the quartiers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quartiers in body.
     */
    @GetMapping("/quartiers")
    public Mono<ResponseEntity<List<Quartier>>> getAllQuartiers(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Quartiers");
        return quartierRepository
            .count()
            .zipWith(quartierRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /quartiers/:id} : get the "id" quartier.
     *
     * @param id the id of the quartier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quartier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quartiers/{id}")
    public Mono<ResponseEntity<Quartier>> getQuartier(@PathVariable Long id) {
        log.debug("REST request to get Quartier : {}", id);
        Mono<Quartier> quartier = quartierRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(quartier);
    }

    /**
     * {@code DELETE  /quartiers/:id} : delete the "id" quartier.
     *
     * @param id the id of the quartier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quartiers/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteQuartier(@PathVariable Long id) {
        log.debug("REST request to delete Quartier : {}", id);
        return quartierRepository
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
