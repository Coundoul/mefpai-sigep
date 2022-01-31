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
import sn.coundoul.gestion.patrimoine.domain.Batiment;
import sn.coundoul.gestion.patrimoine.repository.BatimentRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Batiment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BatimentResource {

    private final Logger log = LoggerFactory.getLogger(BatimentResource.class);

    private static final String ENTITY_NAME = "batiment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BatimentRepository batimentRepository;

    public BatimentResource(BatimentRepository batimentRepository) {
        this.batimentRepository = batimentRepository;
    }

    /**
     * {@code POST  /batiments} : Create a new batiment.
     *
     * @param batiment the batiment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new batiment, or with status {@code 400 (Bad Request)} if the batiment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/batiments")
    public Mono<ResponseEntity<Batiment>> createBatiment(@Valid @RequestBody Batiment batiment) throws URISyntaxException {
        log.debug("REST request to save Batiment : {}", batiment);
        if (batiment.getId() != null) {
            throw new BadRequestAlertException("A new batiment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return batimentRepository
            .save(batiment)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/batiments/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /batiments/:id} : Updates an existing batiment.
     *
     * @param id the id of the batiment to save.
     * @param batiment the batiment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batiment,
     * or with status {@code 400 (Bad Request)} if the batiment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the batiment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/batiments/{id}")
    public Mono<ResponseEntity<Batiment>> updateBatiment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Batiment batiment
    ) throws URISyntaxException {
        log.debug("REST request to update Batiment : {}, {}", id, batiment);
        if (batiment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, batiment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return batimentRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return batimentRepository
                        .save(batiment)
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
     * {@code PATCH  /batiments/:id} : Partial updates given fields of an existing batiment, field will ignore if it is null
     *
     * @param id the id of the batiment to save.
     * @param batiment the batiment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batiment,
     * or with status {@code 400 (Bad Request)} if the batiment is not valid,
     * or with status {@code 404 (Not Found)} if the batiment is not found,
     * or with status {@code 500 (Internal Server Error)} if the batiment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/batiments/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Batiment>> partialUpdateBatiment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Batiment batiment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Batiment partially : {}, {}", id, batiment);
        if (batiment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, batiment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return batimentRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Batiment> result = batimentRepository
                        .findById(batiment.getId())
                        .map(
                            existingBatiment -> {
                                if (batiment.getNomBatiment() != null) {
                                    existingBatiment.setNomBatiment(batiment.getNomBatiment());
                                }
                                if (batiment.getNbrPiece() != null) {
                                    existingBatiment.setNbrPiece(batiment.getNbrPiece());
                                }
                                if (batiment.getDesignation() != null) {
                                    existingBatiment.setDesignation(batiment.getDesignation());
                                }
                                if (batiment.getSurface() != null) {
                                    existingBatiment.setSurface(batiment.getSurface());
                                }
                                if (batiment.getEtatGeneral() != null) {
                                    existingBatiment.setEtatGeneral(batiment.getEtatGeneral());
                                }
                                if (batiment.getDescription() != null) {
                                    existingBatiment.setDescription(batiment.getDescription());
                                }
                                if (batiment.getNombreSalle() != null) {
                                    existingBatiment.setNombreSalle(batiment.getNombreSalle());
                                }

                                return existingBatiment;
                            }
                        )
                        .flatMap(batimentRepository::save);

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
     * {@code GET  /batiments} : get all the batiments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of batiments in body.
     */
    @GetMapping("/batiments")
    public Mono<ResponseEntity<List<Batiment>>> getAllBatiments(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Batiments");
        return batimentRepository
            .count()
            .zipWith(batimentRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /batiments/:id} : get the "id" batiment.
     *
     * @param id the id of the batiment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the batiment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/batiments/{id}")
    public Mono<ResponseEntity<Batiment>> getBatiment(@PathVariable Long id) {
        log.debug("REST request to get Batiment : {}", id);
        Mono<Batiment> batiment = batimentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(batiment);
    }

    /**
     * {@code DELETE  /batiments/:id} : delete the "id" batiment.
     *
     * @param id the id of the batiment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/batiments/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBatiment(@PathVariable Long id) {
        log.debug("REST request to delete Batiment : {}", id);
        return batimentRepository
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
