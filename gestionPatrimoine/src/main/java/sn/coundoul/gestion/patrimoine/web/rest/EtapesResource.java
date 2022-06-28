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
import sn.coundoul.gestion.patrimoine.domain.Etapes;
import sn.coundoul.gestion.patrimoine.repository.EtapesRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Etapes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EtapesResource {

    private final Logger log = LoggerFactory.getLogger(EtapesResource.class);

    private static final String ENTITY_NAME = "etapes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtapesRepository etapesRepository;

    public EtapesResource(EtapesRepository etapesRepository) {
        this.etapesRepository = etapesRepository;
    }

    /**
     * {@code POST  /etapes} : Create a new etapes.
     *
     * @param etapes the etapes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etapes, or with status {@code 400 (Bad Request)} if the etapes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etapes")
    public Mono<ResponseEntity<Etapes>> createEtapes(@Valid @RequestBody Etapes etapes) throws URISyntaxException {
        log.debug("REST request to save Etapes : {}", etapes);
        if (etapes.getId() != null) {
            throw new BadRequestAlertException("A new etapes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return etapesRepository
            .save(etapes)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/etapes/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /etapes/:id} : Updates an existing etapes.
     *
     * @param id the id of the etapes to save.
     * @param etapes the etapes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etapes,
     * or with status {@code 400 (Bad Request)} if the etapes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etapes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etapes/{id}")
    public Mono<ResponseEntity<Etapes>> updateEtapes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Etapes etapes
    ) throws URISyntaxException {
        log.debug("REST request to update Etapes : {}, {}", id, etapes);
        if (etapes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etapes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return etapesRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return etapesRepository
                        .save(etapes)
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
     * {@code PATCH  /etapes/:id} : Partial updates given fields of an existing etapes, field will ignore if it is null
     *
     * @param id the id of the etapes to save.
     * @param etapes the etapes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etapes,
     * or with status {@code 400 (Bad Request)} if the etapes is not valid,
     * or with status {@code 404 (Not Found)} if the etapes is not found,
     * or with status {@code 500 (Internal Server Error)} if the etapes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/etapes/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Etapes>> partialUpdateEtapes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Etapes etapes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Etapes partially : {}, {}", id, etapes);
        if (etapes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etapes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return etapesRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Etapes> result = etapesRepository
                        .findById(etapes.getId())
                        .map(
                            existingEtapes -> {
                                if (etapes.getDateDebut() != null) {
                                    existingEtapes.setDateDebut(etapes.getDateDebut());
                                }
                                if (etapes.getDateFin() != null) {
                                    existingEtapes.setDateFin(etapes.getDateFin());
                                }
                                if (etapes.getNomTache() != null) {
                                    existingEtapes.setNomTache(etapes.getNomTache());
                                }
                                if (etapes.getDuration() != null) {
                                    existingEtapes.setDuration(etapes.getDuration());
                                }

                                return existingEtapes;
                            }
                        )
                        .flatMap(etapesRepository::save);

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
     * {@code GET  /etapes} : get all the etapes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etapes in body.
     */
    @GetMapping("/etapes")
    public Mono<ResponseEntity<List<Etapes>>> getAllEtapes(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Etapes");
        return etapesRepository
            .count()
            .zipWith(etapesRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /etapes/:id} : get the "id" etapes.
     *
     * @param id the id of the etapes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etapes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etapes/{id}")
    public Mono<ResponseEntity<Etapes>> getEtapes(@PathVariable Long id) {
        log.debug("REST request to get Etapes : {}", id);
        Mono<Etapes> etapes = etapesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(etapes);
    }

    /**
     * {@code DELETE  /etapes/:id} : delete the "id" etapes.
     *
     * @param id the id of the etapes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etapes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteEtapes(@PathVariable Long id) {
        log.debug("REST request to delete Etapes : {}", id);
        return etapesRepository
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
