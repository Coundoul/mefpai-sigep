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
import sn.coundoul.gestion.patrimoine.domain.TypeBatiment;
import sn.coundoul.gestion.patrimoine.repository.TypeBatimentRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.TypeBatiment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypeBatimentResource {

    private final Logger log = LoggerFactory.getLogger(TypeBatimentResource.class);

    private static final String ENTITY_NAME = "typeBatiment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeBatimentRepository typeBatimentRepository;

    public TypeBatimentResource(TypeBatimentRepository typeBatimentRepository) {
        this.typeBatimentRepository = typeBatimentRepository;
    }

    /**
     * {@code POST  /type-batiments} : Create a new typeBatiment.
     *
     * @param typeBatiment the typeBatiment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeBatiment, or with status {@code 400 (Bad Request)} if the typeBatiment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-batiments")
    public Mono<ResponseEntity<TypeBatiment>> createTypeBatiment(@Valid @RequestBody TypeBatiment typeBatiment) throws URISyntaxException {
        log.debug("REST request to save TypeBatiment : {}", typeBatiment);
        if (typeBatiment.getId() != null) {
            throw new BadRequestAlertException("A new typeBatiment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return typeBatimentRepository
            .save(typeBatiment)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/type-batiments/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /type-batiments/:id} : Updates an existing typeBatiment.
     *
     * @param id the id of the typeBatiment to save.
     * @param typeBatiment the typeBatiment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeBatiment,
     * or with status {@code 400 (Bad Request)} if the typeBatiment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeBatiment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-batiments/{id}")
    public Mono<ResponseEntity<TypeBatiment>> updateTypeBatiment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeBatiment typeBatiment
    ) throws URISyntaxException {
        log.debug("REST request to update TypeBatiment : {}, {}", id, typeBatiment);
        if (typeBatiment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeBatiment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typeBatimentRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return typeBatimentRepository
                        .save(typeBatiment)
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
     * {@code PATCH  /type-batiments/:id} : Partial updates given fields of an existing typeBatiment, field will ignore if it is null
     *
     * @param id the id of the typeBatiment to save.
     * @param typeBatiment the typeBatiment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeBatiment,
     * or with status {@code 400 (Bad Request)} if the typeBatiment is not valid,
     * or with status {@code 404 (Not Found)} if the typeBatiment is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeBatiment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-batiments/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<TypeBatiment>> partialUpdateTypeBatiment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeBatiment typeBatiment
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeBatiment partially : {}, {}", id, typeBatiment);
        if (typeBatiment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeBatiment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typeBatimentRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<TypeBatiment> result = typeBatimentRepository
                        .findById(typeBatiment.getId())
                        .map(
                            existingTypeBatiment -> {
                                if (typeBatiment.getTypeBa() != null) {
                                    existingTypeBatiment.setTypeBa(typeBatiment.getTypeBa());
                                }

                                return existingTypeBatiment;
                            }
                        )
                        .flatMap(typeBatimentRepository::save);

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
     * {@code GET  /type-batiments} : get all the typeBatiments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeBatiments in body.
     */
    @GetMapping("/type-batiments")
    public Mono<ResponseEntity<List<TypeBatiment>>> getAllTypeBatiments(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of TypeBatiments");
        return typeBatimentRepository
            .count()
            .zipWith(typeBatimentRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /type-batiments/:id} : get the "id" typeBatiment.
     *
     * @param id the id of the typeBatiment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeBatiment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-batiments/{id}")
    public Mono<ResponseEntity<TypeBatiment>> getTypeBatiment(@PathVariable Long id) {
        log.debug("REST request to get TypeBatiment : {}", id);
        Mono<TypeBatiment> typeBatiment = typeBatimentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typeBatiment);
    }

    /**
     * {@code DELETE  /type-batiments/:id} : delete the "id" typeBatiment.
     *
     * @param id the id of the typeBatiment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-batiments/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTypeBatiment(@PathVariable Long id) {
        log.debug("REST request to delete TypeBatiment : {}", id);
        return typeBatimentRepository
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
