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
import sn.coundoul.gestion.patrimoine.domain.Magazin;
import sn.coundoul.gestion.patrimoine.repository.MagazinRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Magazin}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MagazinResource {

    private final Logger log = LoggerFactory.getLogger(MagazinResource.class);

    private static final String ENTITY_NAME = "magazin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MagazinRepository magazinRepository;

    public MagazinResource(MagazinRepository magazinRepository) {
        this.magazinRepository = magazinRepository;
    }

    /**
     * {@code POST  /magazins} : Create a new magazin.
     *
     * @param magazin the magazin to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new magazin, or with status {@code 400 (Bad Request)} if the magazin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/magazins")
    public Mono<ResponseEntity<Magazin>> createMagazin(@Valid @RequestBody Magazin magazin) throws URISyntaxException {
        log.debug("REST request to save Magazin : {}", magazin);
        if (magazin.getId() != null) {
            throw new BadRequestAlertException("A new magazin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return magazinRepository
            .save(magazin)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/magazins/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /magazins/:id} : Updates an existing magazin.
     *
     * @param id the id of the magazin to save.
     * @param magazin the magazin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magazin,
     * or with status {@code 400 (Bad Request)} if the magazin is not valid,
     * or with status {@code 500 (Internal Server Error)} if the magazin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/magazins/{id}")
    public Mono<ResponseEntity<Magazin>> updateMagazin(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Magazin magazin
    ) throws URISyntaxException {
        log.debug("REST request to update Magazin : {}, {}", id, magazin);
        if (magazin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magazin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return magazinRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return magazinRepository
                        .save(magazin)
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
     * {@code PATCH  /magazins/:id} : Partial updates given fields of an existing magazin, field will ignore if it is null
     *
     * @param id the id of the magazin to save.
     * @param magazin the magazin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magazin,
     * or with status {@code 400 (Bad Request)} if the magazin is not valid,
     * or with status {@code 404 (Not Found)} if the magazin is not found,
     * or with status {@code 500 (Internal Server Error)} if the magazin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/magazins/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Magazin>> partialUpdateMagazin(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Magazin magazin
    ) throws URISyntaxException {
        log.debug("REST request to partial update Magazin partially : {}, {}", id, magazin);
        if (magazin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magazin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return magazinRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Magazin> result = magazinRepository
                        .findById(magazin.getId())
                        .map(
                            existingMagazin -> {
                                if (magazin.getNomMagazin() != null) {
                                    existingMagazin.setNomMagazin(magazin.getNomMagazin());
                                }
                                if (magazin.getSurfaceBatie() != null) {
                                    existingMagazin.setSurfaceBatie(magazin.getSurfaceBatie());
                                }
                                if (magazin.getSuperficie() != null) {
                                    existingMagazin.setSuperficie(magazin.getSuperficie());
                                }
                                if (magazin.getIdPers() != null) {
                                    existingMagazin.setIdPers(magazin.getIdPers());
                                }

                                return existingMagazin;
                            }
                        )
                        .flatMap(magazinRepository::save);

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
     * {@code GET  /magazins} : get all the magazins.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of magazins in body.
     */
    @GetMapping("/magazins")
    public Mono<ResponseEntity<List<Magazin>>> getAllMagazins(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Magazins");
        return magazinRepository
            .count()
            .zipWith(magazinRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /magazins/:id} : get the "id" magazin.
     *
     * @param id the id of the magazin to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the magazin, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/magazins/{id}")
    public Mono<ResponseEntity<Magazin>> getMagazin(@PathVariable Long id) {
        log.debug("REST request to get Magazin : {}", id);
        Mono<Magazin> magazin = magazinRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(magazin);
    }

    /**
     * {@code DELETE  /magazins/:id} : delete the "id" magazin.
     *
     * @param id the id of the magazin to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/magazins/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteMagazin(@PathVariable Long id) {
        log.debug("REST request to delete Magazin : {}", id);
        return magazinRepository
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
