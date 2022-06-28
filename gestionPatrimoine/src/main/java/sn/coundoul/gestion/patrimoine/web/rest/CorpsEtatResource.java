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
import sn.coundoul.gestion.patrimoine.domain.CorpsEtat;
import sn.coundoul.gestion.patrimoine.repository.CorpsEtatRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.CorpsEtat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CorpsEtatResource {

    private final Logger log = LoggerFactory.getLogger(CorpsEtatResource.class);

    private static final String ENTITY_NAME = "corpsEtat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CorpsEtatRepository corpsEtatRepository;

    public CorpsEtatResource(CorpsEtatRepository corpsEtatRepository) {
        this.corpsEtatRepository = corpsEtatRepository;
    }

    /**
     * {@code POST  /corps-etats} : Create a new corpsEtat.
     *
     * @param corpsEtat the corpsEtat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new corpsEtat, or with status {@code 400 (Bad Request)} if the corpsEtat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/corps-etats")
    public Mono<ResponseEntity<CorpsEtat>> createCorpsEtat(@Valid @RequestBody CorpsEtat corpsEtat) throws URISyntaxException {
        log.debug("REST request to save CorpsEtat : {}", corpsEtat);
        if (corpsEtat.getId() != null) {
            throw new BadRequestAlertException("A new corpsEtat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return corpsEtatRepository
            .save(corpsEtat)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/corps-etats/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /corps-etats/:id} : Updates an existing corpsEtat.
     *
     * @param id the id of the corpsEtat to save.
     * @param corpsEtat the corpsEtat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corpsEtat,
     * or with status {@code 400 (Bad Request)} if the corpsEtat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the corpsEtat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/corps-etats/{id}")
    public Mono<ResponseEntity<CorpsEtat>> updateCorpsEtat(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CorpsEtat corpsEtat
    ) throws URISyntaxException {
        log.debug("REST request to update CorpsEtat : {}, {}", id, corpsEtat);
        if (corpsEtat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corpsEtat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return corpsEtatRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return corpsEtatRepository
                        .save(corpsEtat)
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
     * {@code PATCH  /corps-etats/:id} : Partial updates given fields of an existing corpsEtat, field will ignore if it is null
     *
     * @param id the id of the corpsEtat to save.
     * @param corpsEtat the corpsEtat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corpsEtat,
     * or with status {@code 400 (Bad Request)} if the corpsEtat is not valid,
     * or with status {@code 404 (Not Found)} if the corpsEtat is not found,
     * or with status {@code 500 (Internal Server Error)} if the corpsEtat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/corps-etats/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<CorpsEtat>> partialUpdateCorpsEtat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CorpsEtat corpsEtat
    ) throws URISyntaxException {
        log.debug("REST request to partial update CorpsEtat partially : {}, {}", id, corpsEtat);
        if (corpsEtat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corpsEtat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return corpsEtatRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<CorpsEtat> result = corpsEtatRepository
                        .findById(corpsEtat.getId())
                        .map(
                            existingCorpsEtat -> {
                                if (corpsEtat.getNomCorps() != null) {
                                    existingCorpsEtat.setNomCorps(corpsEtat.getNomCorps());
                                }
                                if (corpsEtat.getGrosOeuvre() != null) {
                                    existingCorpsEtat.setGrosOeuvre(corpsEtat.getGrosOeuvre());
                                }
                                if (corpsEtat.getDescriptionGrosOeuvre() != null) {
                                    existingCorpsEtat.setDescriptionGrosOeuvre(corpsEtat.getDescriptionGrosOeuvre());
                                }
                                if (corpsEtat.getSecondOeuvre() != null) {
                                    existingCorpsEtat.setSecondOeuvre(corpsEtat.getSecondOeuvre());
                                }
                                if (corpsEtat.getDescriptionSecondOeuvre() != null) {
                                    existingCorpsEtat.setDescriptionSecondOeuvre(corpsEtat.getDescriptionSecondOeuvre());
                                }
                                if (corpsEtat.getOservation() != null) {
                                    existingCorpsEtat.setOservation(corpsEtat.getOservation());
                                }
                                if (corpsEtat.getEtatCorps() != null) {
                                    existingCorpsEtat.setEtatCorps(corpsEtat.getEtatCorps());
                                }

                                return existingCorpsEtat;
                            }
                        )
                        .flatMap(corpsEtatRepository::save);

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
     * {@code GET  /corps-etats} : get all the corpsEtats.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of corpsEtats in body.
     */
    @GetMapping("/corps-etats")
    public Mono<ResponseEntity<List<CorpsEtat>>> getAllCorpsEtats(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of CorpsEtats");
        return corpsEtatRepository
            .count()
            .zipWith(corpsEtatRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /corps-etats/:id} : get the "id" corpsEtat.
     *
     * @param id the id of the corpsEtat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the corpsEtat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/corps-etats/{id}")
    public Mono<ResponseEntity<CorpsEtat>> getCorpsEtat(@PathVariable Long id) {
        log.debug("REST request to get CorpsEtat : {}", id);
        Mono<CorpsEtat> corpsEtat = corpsEtatRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(corpsEtat);
    }

    /**
     * {@code DELETE  /corps-etats/:id} : delete the "id" corpsEtat.
     *
     * @param id the id of the corpsEtat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/corps-etats/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCorpsEtat(@PathVariable Long id) {
        log.debug("REST request to delete CorpsEtat : {}", id);
        return corpsEtatRepository
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
