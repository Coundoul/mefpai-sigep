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
import sn.coundoul.gestion.patrimoine.domain.Requete;
import sn.coundoul.gestion.patrimoine.repository.RequeteRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Requete}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RequeteResource {

    private final Logger log = LoggerFactory.getLogger(RequeteResource.class);

    private static final String ENTITY_NAME = "requete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequeteRepository requeteRepository;

    public RequeteResource(RequeteRepository requeteRepository) {
        this.requeteRepository = requeteRepository;
    }

    /**
     * {@code POST  /requetes} : Create a new requete.
     *
     * @param requete the requete to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requete, or with status {@code 400 (Bad Request)} if the requete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requetes")
    public Mono<ResponseEntity<Requete>> createRequete(@Valid @RequestBody Requete requete) throws URISyntaxException {
        log.debug("REST request to save Requete : {}", requete);
        if (requete.getId() != null) {
            throw new BadRequestAlertException("A new requete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return requeteRepository
            .save(requete)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/requetes/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /requetes/:id} : Updates an existing requete.
     *
     * @param id the id of the requete to save.
     * @param requete the requete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requete,
     * or with status {@code 400 (Bad Request)} if the requete is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requetes/{id}")
    public Mono<ResponseEntity<Requete>> updateRequete(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Requete requete
    ) throws URISyntaxException {
        log.debug("REST request to update Requete : {}, {}", id, requete);
        if (requete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return requeteRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return requeteRepository
                        .save(requete)
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
     * {@code PATCH  /requetes/:id} : Partial updates given fields of an existing requete, field will ignore if it is null
     *
     * @param id the id of the requete to save.
     * @param requete the requete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requete,
     * or with status {@code 400 (Bad Request)} if the requete is not valid,
     * or with status {@code 404 (Not Found)} if the requete is not found,
     * or with status {@code 500 (Internal Server Error)} if the requete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/requetes/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Requete>> partialUpdateRequete(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Requete requete
    ) throws URISyntaxException {
        log.debug("REST request to partial update Requete partially : {}, {}", id, requete);
        if (requete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return requeteRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Requete> result = requeteRepository
                        .findById(requete.getId())
                        .map(
                            existingRequete -> {
                                if (requete.getType() != null) {
                                    existingRequete.setType(requete.getType());
                                }
                                if (requete.getTypePanne() != null) {
                                    existingRequete.setTypePanne(requete.getTypePanne());
                                }
                                if (requete.getDatePost() != null) {
                                    existingRequete.setDatePost(requete.getDatePost());
                                }
                                if (requete.getDescription() != null) {
                                    existingRequete.setDescription(requete.getDescription());
                                }
                                if (requete.getEtatTraite() != null) {
                                    existingRequete.setEtatTraite(requete.getEtatTraite());
                                }
                                if (requete.getDateLancement() != null) {
                                    existingRequete.setDateLancement(requete.getDateLancement());
                                }
                                if (requete.getIdPers() != null) {
                                    existingRequete.setIdPers(requete.getIdPers());
                                }

                                return existingRequete;
                            }
                        )
                        .flatMap(requeteRepository::save);

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
     * {@code GET  /requetes} : get all the requetes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requetes in body.
     */
    @GetMapping("/requetes")
    public Mono<ResponseEntity<List<Requete>>> getAllRequetes(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Requetes");
        return requeteRepository
            .count()
            .zipWith(requeteRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /requetes/:id} : get the "id" requete.
     *
     * @param id the id of the requete to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requete, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requetes/{id}")
    public Mono<ResponseEntity<Requete>> getRequete(@PathVariable Long id) {
        log.debug("REST request to get Requete : {}", id);
        Mono<Requete> requete = requeteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(requete);
    }

    /**
     * {@code DELETE  /requetes/:id} : delete the "id" requete.
     *
     * @param id the id of the requete to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requetes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteRequete(@PathVariable Long id) {
        log.debug("REST request to delete Requete : {}", id);
        return requeteRepository
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
