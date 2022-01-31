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
import sn.coundoul.gestion.patrimoine.domain.AttributionInfrastructure;
import sn.coundoul.gestion.patrimoine.repository.AttributionInfrastructureRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.AttributionInfrastructure}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AttributionInfrastructureResource {

    private final Logger log = LoggerFactory.getLogger(AttributionInfrastructureResource.class);

    private static final String ENTITY_NAME = "attributionInfrastructure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributionInfrastructureRepository attributionInfrastructureRepository;

    public AttributionInfrastructureResource(AttributionInfrastructureRepository attributionInfrastructureRepository) {
        this.attributionInfrastructureRepository = attributionInfrastructureRepository;
    }

    /**
     * {@code POST  /attribution-infrastructures} : Create a new attributionInfrastructure.
     *
     * @param attributionInfrastructure the attributionInfrastructure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributionInfrastructure, or with status {@code 400 (Bad Request)} if the attributionInfrastructure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attribution-infrastructures")
    public Mono<ResponseEntity<AttributionInfrastructure>> createAttributionInfrastructure(
        @Valid @RequestBody AttributionInfrastructure attributionInfrastructure
    ) throws URISyntaxException {
        log.debug("REST request to save AttributionInfrastructure : {}", attributionInfrastructure);
        if (attributionInfrastructure.getId() != null) {
            throw new BadRequestAlertException("A new attributionInfrastructure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return attributionInfrastructureRepository
            .save(attributionInfrastructure)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/attribution-infrastructures/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /attribution-infrastructures/:id} : Updates an existing attributionInfrastructure.
     *
     * @param id the id of the attributionInfrastructure to save.
     * @param attributionInfrastructure the attributionInfrastructure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributionInfrastructure,
     * or with status {@code 400 (Bad Request)} if the attributionInfrastructure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributionInfrastructure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attribution-infrastructures/{id}")
    public Mono<ResponseEntity<AttributionInfrastructure>> updateAttributionInfrastructure(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AttributionInfrastructure attributionInfrastructure
    ) throws URISyntaxException {
        log.debug("REST request to update AttributionInfrastructure : {}, {}", id, attributionInfrastructure);
        if (attributionInfrastructure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributionInfrastructure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return attributionInfrastructureRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return attributionInfrastructureRepository
                        .save(attributionInfrastructure)
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
     * {@code PATCH  /attribution-infrastructures/:id} : Partial updates given fields of an existing attributionInfrastructure, field will ignore if it is null
     *
     * @param id the id of the attributionInfrastructure to save.
     * @param attributionInfrastructure the attributionInfrastructure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributionInfrastructure,
     * or with status {@code 400 (Bad Request)} if the attributionInfrastructure is not valid,
     * or with status {@code 404 (Not Found)} if the attributionInfrastructure is not found,
     * or with status {@code 500 (Internal Server Error)} if the attributionInfrastructure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attribution-infrastructures/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<AttributionInfrastructure>> partialUpdateAttributionInfrastructure(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AttributionInfrastructure attributionInfrastructure
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttributionInfrastructure partially : {}, {}", id, attributionInfrastructure);
        if (attributionInfrastructure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributionInfrastructure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return attributionInfrastructureRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<AttributionInfrastructure> result = attributionInfrastructureRepository
                        .findById(attributionInfrastructure.getId())
                        .map(
                            existingAttributionInfrastructure -> {
                                if (attributionInfrastructure.getDateAttribution() != null) {
                                    existingAttributionInfrastructure.setDateAttribution(attributionInfrastructure.getDateAttribution());
                                }
                                if (attributionInfrastructure.getQuantite() != null) {
                                    existingAttributionInfrastructure.setQuantite(attributionInfrastructure.getQuantite());
                                }
                                if (attributionInfrastructure.getIdEquipement() != null) {
                                    existingAttributionInfrastructure.setIdEquipement(attributionInfrastructure.getIdEquipement());
                                }
                                if (attributionInfrastructure.getIdPers() != null) {
                                    existingAttributionInfrastructure.setIdPers(attributionInfrastructure.getIdPers());
                                }

                                return existingAttributionInfrastructure;
                            }
                        )
                        .flatMap(attributionInfrastructureRepository::save);

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
     * {@code GET  /attribution-infrastructures} : get all the attributionInfrastructures.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributionInfrastructures in body.
     */
    @GetMapping("/attribution-infrastructures")
    public Mono<ResponseEntity<List<AttributionInfrastructure>>> getAllAttributionInfrastructures(
        Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AttributionInfrastructures");
        return attributionInfrastructureRepository
            .count()
            .zipWith(attributionInfrastructureRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /attribution-infrastructures/:id} : get the "id" attributionInfrastructure.
     *
     * @param id the id of the attributionInfrastructure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributionInfrastructure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attribution-infrastructures/{id}")
    public Mono<ResponseEntity<AttributionInfrastructure>> getAttributionInfrastructure(@PathVariable Long id) {
        log.debug("REST request to get AttributionInfrastructure : {}", id);
        Mono<AttributionInfrastructure> attributionInfrastructure = attributionInfrastructureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attributionInfrastructure);
    }

    /**
     * {@code DELETE  /attribution-infrastructures/:id} : delete the "id" attributionInfrastructure.
     *
     * @param id the id of the attributionInfrastructure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attribution-infrastructures/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAttributionInfrastructure(@PathVariable Long id) {
        log.debug("REST request to delete AttributionInfrastructure : {}", id);
        return attributionInfrastructureRepository
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
