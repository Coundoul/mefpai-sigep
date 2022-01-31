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
import sn.coundoul.gestion.patrimoine.domain.DetailSortie;
import sn.coundoul.gestion.patrimoine.repository.DetailSortieRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.DetailSortie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DetailSortieResource {

    private final Logger log = LoggerFactory.getLogger(DetailSortieResource.class);

    private static final String ENTITY_NAME = "detailSortie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailSortieRepository detailSortieRepository;

    public DetailSortieResource(DetailSortieRepository detailSortieRepository) {
        this.detailSortieRepository = detailSortieRepository;
    }

    /**
     * {@code POST  /detail-sorties} : Create a new detailSortie.
     *
     * @param detailSortie the detailSortie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailSortie, or with status {@code 400 (Bad Request)} if the detailSortie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detail-sorties")
    public Mono<ResponseEntity<DetailSortie>> createDetailSortie(@Valid @RequestBody DetailSortie detailSortie) throws URISyntaxException {
        log.debug("REST request to save DetailSortie : {}", detailSortie);
        if (detailSortie.getId() != null) {
            throw new BadRequestAlertException("A new detailSortie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return detailSortieRepository
            .save(detailSortie)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/detail-sorties/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /detail-sorties/:id} : Updates an existing detailSortie.
     *
     * @param id the id of the detailSortie to save.
     * @param detailSortie the detailSortie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailSortie,
     * or with status {@code 400 (Bad Request)} if the detailSortie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailSortie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detail-sorties/{id}")
    public Mono<ResponseEntity<DetailSortie>> updateDetailSortie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetailSortie detailSortie
    ) throws URISyntaxException {
        log.debug("REST request to update DetailSortie : {}, {}", id, detailSortie);
        if (detailSortie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailSortie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return detailSortieRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return detailSortieRepository
                        .save(detailSortie)
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
     * {@code PATCH  /detail-sorties/:id} : Partial updates given fields of an existing detailSortie, field will ignore if it is null
     *
     * @param id the id of the detailSortie to save.
     * @param detailSortie the detailSortie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailSortie,
     * or with status {@code 400 (Bad Request)} if the detailSortie is not valid,
     * or with status {@code 404 (Not Found)} if the detailSortie is not found,
     * or with status {@code 500 (Internal Server Error)} if the detailSortie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detail-sorties/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<DetailSortie>> partialUpdateDetailSortie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetailSortie detailSortie
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetailSortie partially : {}, {}", id, detailSortie);
        if (detailSortie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailSortie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return detailSortieRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<DetailSortie> result = detailSortieRepository
                        .findById(detailSortie.getId())
                        .map(
                            existingDetailSortie -> {
                                if (detailSortie.getPieceJointe() != null) {
                                    existingDetailSortie.setPieceJointe(detailSortie.getPieceJointe());
                                }
                                if (detailSortie.getIdPers() != null) {
                                    existingDetailSortie.setIdPers(detailSortie.getIdPers());
                                }

                                return existingDetailSortie;
                            }
                        )
                        .flatMap(detailSortieRepository::save);

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
     * {@code GET  /detail-sorties} : get all the detailSorties.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailSorties in body.
     */
    @GetMapping("/detail-sorties")
    public Mono<ResponseEntity<List<DetailSortie>>> getAllDetailSorties(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of DetailSorties");
        return detailSortieRepository
            .count()
            .zipWith(detailSortieRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /detail-sorties/:id} : get the "id" detailSortie.
     *
     * @param id the id of the detailSortie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailSortie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detail-sorties/{id}")
    public Mono<ResponseEntity<DetailSortie>> getDetailSortie(@PathVariable Long id) {
        log.debug("REST request to get DetailSortie : {}", id);
        Mono<DetailSortie> detailSortie = detailSortieRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(detailSortie);
    }

    /**
     * {@code DELETE  /detail-sorties/:id} : delete the "id" detailSortie.
     *
     * @param id the id of the detailSortie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detail-sorties/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDetailSortie(@PathVariable Long id) {
        log.debug("REST request to delete DetailSortie : {}", id);
        return detailSortieRepository
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
