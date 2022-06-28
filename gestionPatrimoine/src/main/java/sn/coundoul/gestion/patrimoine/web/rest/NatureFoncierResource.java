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
import sn.coundoul.gestion.patrimoine.domain.NatureFoncier;
import sn.coundoul.gestion.patrimoine.repository.NatureFoncierRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.NatureFoncier}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NatureFoncierResource {

    private final Logger log = LoggerFactory.getLogger(NatureFoncierResource.class);

    private static final String ENTITY_NAME = "natureFoncier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NatureFoncierRepository natureFoncierRepository;

    public NatureFoncierResource(NatureFoncierRepository natureFoncierRepository) {
        this.natureFoncierRepository = natureFoncierRepository;
    }

    /**
     * {@code POST  /nature-fonciers} : Create a new natureFoncier.
     *
     * @param natureFoncier the natureFoncier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new natureFoncier, or with status {@code 400 (Bad Request)} if the natureFoncier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nature-fonciers")
    public Mono<ResponseEntity<NatureFoncier>> createNatureFoncier(@Valid @RequestBody NatureFoncier natureFoncier)
        throws URISyntaxException {
        log.debug("REST request to save NatureFoncier : {}", natureFoncier);
        if (natureFoncier.getId() != null) {
            throw new BadRequestAlertException("A new natureFoncier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return natureFoncierRepository
            .save(natureFoncier)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/nature-fonciers/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /nature-fonciers/:id} : Updates an existing natureFoncier.
     *
     * @param id the id of the natureFoncier to save.
     * @param natureFoncier the natureFoncier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated natureFoncier,
     * or with status {@code 400 (Bad Request)} if the natureFoncier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the natureFoncier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nature-fonciers/{id}")
    public Mono<ResponseEntity<NatureFoncier>> updateNatureFoncier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NatureFoncier natureFoncier
    ) throws URISyntaxException {
        log.debug("REST request to update NatureFoncier : {}, {}", id, natureFoncier);
        if (natureFoncier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, natureFoncier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return natureFoncierRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return natureFoncierRepository
                        .save(natureFoncier)
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
     * {@code PATCH  /nature-fonciers/:id} : Partial updates given fields of an existing natureFoncier, field will ignore if it is null
     *
     * @param id the id of the natureFoncier to save.
     * @param natureFoncier the natureFoncier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated natureFoncier,
     * or with status {@code 400 (Bad Request)} if the natureFoncier is not valid,
     * or with status {@code 404 (Not Found)} if the natureFoncier is not found,
     * or with status {@code 500 (Internal Server Error)} if the natureFoncier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nature-fonciers/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<NatureFoncier>> partialUpdateNatureFoncier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NatureFoncier natureFoncier
    ) throws URISyntaxException {
        log.debug("REST request to partial update NatureFoncier partially : {}, {}", id, natureFoncier);
        if (natureFoncier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, natureFoncier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return natureFoncierRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<NatureFoncier> result = natureFoncierRepository
                        .findById(natureFoncier.getId())
                        .map(
                            existingNatureFoncier -> {
                                if (natureFoncier.getTypeFoncier() != null) {
                                    existingNatureFoncier.setTypeFoncier(natureFoncier.getTypeFoncier());
                                }
                                if (natureFoncier.getPieceJointe() != null) {
                                    existingNatureFoncier.setPieceJointe(natureFoncier.getPieceJointe());
                                }

                                return existingNatureFoncier;
                            }
                        )
                        .flatMap(natureFoncierRepository::save);

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
     * {@code GET  /nature-fonciers} : get all the natureFonciers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of natureFonciers in body.
     */
    @GetMapping("/nature-fonciers")
    public Mono<ResponseEntity<List<NatureFoncier>>> getAllNatureFonciers(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of NatureFonciers");
        return natureFoncierRepository
            .count()
            .zipWith(natureFoncierRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /nature-fonciers/:id} : get the "id" natureFoncier.
     *
     * @param id the id of the natureFoncier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the natureFoncier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nature-fonciers/{id}")
    public Mono<ResponseEntity<NatureFoncier>> getNatureFoncier(@PathVariable Long id) {
        log.debug("REST request to get NatureFoncier : {}", id);
        Mono<NatureFoncier> natureFoncier = natureFoncierRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(natureFoncier);
    }

    /**
     * {@code DELETE  /nature-fonciers/:id} : delete the "id" natureFoncier.
     *
     * @param id the id of the natureFoncier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nature-fonciers/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteNatureFoncier(@PathVariable Long id) {
        log.debug("REST request to delete NatureFoncier : {}", id);
        return natureFoncierRepository
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
