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
import sn.coundoul.gestion.patrimoine.domain.Commune;
import sn.coundoul.gestion.patrimoine.repository.CommuneRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Commune}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommuneResource {

    private final Logger log = LoggerFactory.getLogger(CommuneResource.class);

    private static final String ENTITY_NAME = "commune";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommuneRepository communeRepository;

    public CommuneResource(CommuneRepository communeRepository) {
        this.communeRepository = communeRepository;
    }

    /**
     * {@code POST  /communes} : Create a new commune.
     *
     * @param commune the commune to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commune, or with status {@code 400 (Bad Request)} if the commune has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/communes")
    public Mono<ResponseEntity<Commune>> createCommune(@Valid @RequestBody Commune commune) throws URISyntaxException {
        log.debug("REST request to save Commune : {}", commune);
        if (commune.getId() != null) {
            throw new BadRequestAlertException("A new commune cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return communeRepository
            .save(commune)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/communes/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /communes/:id} : Updates an existing commune.
     *
     * @param id the id of the commune to save.
     * @param commune the commune to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commune,
     * or with status {@code 400 (Bad Request)} if the commune is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commune couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/communes/{id}")
    public Mono<ResponseEntity<Commune>> updateCommune(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Commune commune
    ) throws URISyntaxException {
        log.debug("REST request to update Commune : {}, {}", id, commune);
        if (commune.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commune.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return communeRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return communeRepository
                        .save(commune)
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
     * {@code PATCH  /communes/:id} : Partial updates given fields of an existing commune, field will ignore if it is null
     *
     * @param id the id of the commune to save.
     * @param commune the commune to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commune,
     * or with status {@code 400 (Bad Request)} if the commune is not valid,
     * or with status {@code 404 (Not Found)} if the commune is not found,
     * or with status {@code 500 (Internal Server Error)} if the commune couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/communes/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Commune>> partialUpdateCommune(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Commune commune
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commune partially : {}, {}", id, commune);
        if (commune.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commune.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return communeRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Commune> result = communeRepository
                        .findById(commune.getId())
                        .map(
                            existingCommune -> {
                                if (commune.getNomCommune() != null) {
                                    existingCommune.setNomCommune(commune.getNomCommune());
                                }

                                return existingCommune;
                            }
                        )
                        .flatMap(communeRepository::save);

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
     * {@code GET  /communes} : get all the communes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communes in body.
     */
    @GetMapping("/communes")
    public Mono<ResponseEntity<List<Commune>>> getAllCommunes(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Communes");
        return communeRepository
            .count()
            .zipWith(communeRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /communes/:id} : get the "id" commune.
     *
     * @param id the id of the commune to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commune, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/communes/{id}")
    public Mono<ResponseEntity<Commune>> getCommune(@PathVariable Long id) {
        log.debug("REST request to get Commune : {}", id);
        Mono<Commune> commune = communeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commune);
    }

    /**
     * {@code DELETE  /communes/:id} : delete the "id" commune.
     *
     * @param id the id of the commune to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/communes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCommune(@PathVariable Long id) {
        log.debug("REST request to delete Commune : {}", id);
        return communeRepository
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
