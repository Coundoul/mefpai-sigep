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
import sn.coundoul.gestion.patrimoine.domain.ComptablePrincipale;
import sn.coundoul.gestion.patrimoine.repository.ComptablePrincipaleRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.ComptablePrincipale}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ComptablePrincipaleResource {

    private final Logger log = LoggerFactory.getLogger(ComptablePrincipaleResource.class);

    private static final String ENTITY_NAME = "comptablePrincipale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComptablePrincipaleRepository comptablePrincipaleRepository;

    public ComptablePrincipaleResource(ComptablePrincipaleRepository comptablePrincipaleRepository) {
        this.comptablePrincipaleRepository = comptablePrincipaleRepository;
    }

    /**
     * {@code POST  /comptable-principales} : Create a new comptablePrincipale.
     *
     * @param comptablePrincipale the comptablePrincipale to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comptablePrincipale, or with status {@code 400 (Bad Request)} if the comptablePrincipale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comptable-principales")
    public Mono<ResponseEntity<ComptablePrincipale>> createComptablePrincipale(@Valid @RequestBody ComptablePrincipale comptablePrincipale)
        throws URISyntaxException {
        log.debug("REST request to save ComptablePrincipale : {}", comptablePrincipale);
        if (comptablePrincipale.getId() != null) {
            throw new BadRequestAlertException("A new comptablePrincipale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return comptablePrincipaleRepository
            .save(comptablePrincipale)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/comptable-principales/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /comptable-principales/:id} : Updates an existing comptablePrincipale.
     *
     * @param id the id of the comptablePrincipale to save.
     * @param comptablePrincipale the comptablePrincipale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comptablePrincipale,
     * or with status {@code 400 (Bad Request)} if the comptablePrincipale is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comptablePrincipale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comptable-principales/{id}")
    public Mono<ResponseEntity<ComptablePrincipale>> updateComptablePrincipale(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComptablePrincipale comptablePrincipale
    ) throws URISyntaxException {
        log.debug("REST request to update ComptablePrincipale : {}, {}", id, comptablePrincipale);
        if (comptablePrincipale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comptablePrincipale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return comptablePrincipaleRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return comptablePrincipaleRepository
                        .save(comptablePrincipale)
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
     * {@code PATCH  /comptable-principales/:id} : Partial updates given fields of an existing comptablePrincipale, field will ignore if it is null
     *
     * @param id the id of the comptablePrincipale to save.
     * @param comptablePrincipale the comptablePrincipale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comptablePrincipale,
     * or with status {@code 400 (Bad Request)} if the comptablePrincipale is not valid,
     * or with status {@code 404 (Not Found)} if the comptablePrincipale is not found,
     * or with status {@code 500 (Internal Server Error)} if the comptablePrincipale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comptable-principales/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<ComptablePrincipale>> partialUpdateComptablePrincipale(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComptablePrincipale comptablePrincipale
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComptablePrincipale partially : {}, {}", id, comptablePrincipale);
        if (comptablePrincipale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comptablePrincipale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return comptablePrincipaleRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ComptablePrincipale> result = comptablePrincipaleRepository
                        .findById(comptablePrincipale.getId())
                        .map(
                            existingComptablePrincipale -> {
                                if (comptablePrincipale.getNomPers() != null) {
                                    existingComptablePrincipale.setNomPers(comptablePrincipale.getNomPers());
                                }
                                if (comptablePrincipale.getPrenomPers() != null) {
                                    existingComptablePrincipale.setPrenomPers(comptablePrincipale.getPrenomPers());
                                }
                                if (comptablePrincipale.getSexe() != null) {
                                    existingComptablePrincipale.setSexe(comptablePrincipale.getSexe());
                                }
                                if (comptablePrincipale.getMobile() != null) {
                                    existingComptablePrincipale.setMobile(comptablePrincipale.getMobile());
                                }
                                if (comptablePrincipale.getAdresse() != null) {
                                    existingComptablePrincipale.setAdresse(comptablePrincipale.getAdresse());
                                }
                                if (comptablePrincipale.getDirection() != null) {
                                    existingComptablePrincipale.setDirection(comptablePrincipale.getDirection());
                                }

                                return existingComptablePrincipale;
                            }
                        )
                        .flatMap(comptablePrincipaleRepository::save);

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
     * {@code GET  /comptable-principales} : get all the comptablePrincipales.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comptablePrincipales in body.
     */
    @GetMapping("/comptable-principales")
    public Mono<ResponseEntity<List<ComptablePrincipale>>> getAllComptablePrincipales(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of ComptablePrincipales");
        return comptablePrincipaleRepository
            .count()
            .zipWith(comptablePrincipaleRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /comptable-principales/:id} : get the "id" comptablePrincipale.
     *
     * @param id the id of the comptablePrincipale to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comptablePrincipale, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comptable-principales/{id}")
    public Mono<ResponseEntity<ComptablePrincipale>> getComptablePrincipale(@PathVariable Long id) {
        log.debug("REST request to get ComptablePrincipale : {}", id);
        Mono<ComptablePrincipale> comptablePrincipale = comptablePrincipaleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(comptablePrincipale);
    }

    /**
     * {@code DELETE  /comptable-principales/:id} : delete the "id" comptablePrincipale.
     *
     * @param id the id of the comptablePrincipale to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comptable-principales/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteComptablePrincipale(@PathVariable Long id) {
        log.debug("REST request to delete ComptablePrincipale : {}", id);
        return comptablePrincipaleRepository
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
