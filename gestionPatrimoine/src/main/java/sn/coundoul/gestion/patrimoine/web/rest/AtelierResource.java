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
import sn.coundoul.gestion.patrimoine.domain.Atelier;
import sn.coundoul.gestion.patrimoine.repository.AtelierRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Atelier}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AtelierResource {

    private final Logger log = LoggerFactory.getLogger(AtelierResource.class);

    private static final String ENTITY_NAME = "atelier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AtelierRepository atelierRepository;

    public AtelierResource(AtelierRepository atelierRepository) {
        this.atelierRepository = atelierRepository;
    }

    /**
     * {@code POST  /ateliers} : Create a new atelier.
     *
     * @param atelier the atelier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new atelier, or with status {@code 400 (Bad Request)} if the atelier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ateliers")
    public Mono<ResponseEntity<Atelier>> createAtelier(@Valid @RequestBody Atelier atelier) throws URISyntaxException {
        log.debug("REST request to save Atelier : {}", atelier);
        if (atelier.getId() != null) {
            throw new BadRequestAlertException("A new atelier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return atelierRepository
            .save(atelier)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/ateliers/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /ateliers/:id} : Updates an existing atelier.
     *
     * @param id the id of the atelier to save.
     * @param atelier the atelier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atelier,
     * or with status {@code 400 (Bad Request)} if the atelier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the atelier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ateliers/{id}")
    public Mono<ResponseEntity<Atelier>> updateAtelier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Atelier atelier
    ) throws URISyntaxException {
        log.debug("REST request to update Atelier : {}, {}", id, atelier);
        if (atelier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, atelier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return atelierRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return atelierRepository
                        .save(atelier)
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
     * {@code PATCH  /ateliers/:id} : Partial updates given fields of an existing atelier, field will ignore if it is null
     *
     * @param id the id of the atelier to save.
     * @param atelier the atelier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atelier,
     * or with status {@code 400 (Bad Request)} if the atelier is not valid,
     * or with status {@code 404 (Not Found)} if the atelier is not found,
     * or with status {@code 500 (Internal Server Error)} if the atelier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ateliers/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Atelier>> partialUpdateAtelier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Atelier atelier
    ) throws URISyntaxException {
        log.debug("REST request to partial update Atelier partially : {}, {}", id, atelier);
        if (atelier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, atelier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return atelierRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Atelier> result = atelierRepository
                        .findById(atelier.getId())
                        .map(
                            existingAtelier -> {
                                if (atelier.getNomAtelier() != null) {
                                    existingAtelier.setNomAtelier(atelier.getNomAtelier());
                                }
                                if (atelier.getSurface() != null) {
                                    existingAtelier.setSurface(atelier.getSurface());
                                }
                                if (atelier.getDescription() != null) {
                                    existingAtelier.setDescription(atelier.getDescription());
                                }

                                return existingAtelier;
                            }
                        )
                        .flatMap(atelierRepository::save);

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
     * {@code GET  /ateliers} : get all the ateliers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ateliers in body.
     */
    @GetMapping("/ateliers")
    public Mono<ResponseEntity<List<Atelier>>> getAllAteliers(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Ateliers");
        return atelierRepository
            .count()
            .zipWith(atelierRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /ateliers/:id} : get the "id" atelier.
     *
     * @param id the id of the atelier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the atelier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ateliers/{id}")
    public Mono<ResponseEntity<Atelier>> getAtelier(@PathVariable Long id) {
        log.debug("REST request to get Atelier : {}", id);
        Mono<Atelier> atelier = atelierRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(atelier);
    }

    /**
     * {@code DELETE  /ateliers/:id} : delete the "id" atelier.
     *
     * @param id the id of the atelier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ateliers/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAtelier(@PathVariable Long id) {
        log.debug("REST request to delete Atelier : {}", id);
        return atelierRepository
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
