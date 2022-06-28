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
import sn.coundoul.gestion.patrimoine.domain.FicheTechnique;
import sn.coundoul.gestion.patrimoine.repository.FicheTechniqueRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.FicheTechnique}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FicheTechniqueResource {

    private final Logger log = LoggerFactory.getLogger(FicheTechniqueResource.class);

    private static final String ENTITY_NAME = "ficheTechnique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FicheTechniqueRepository ficheTechniqueRepository;

    public FicheTechniqueResource(FicheTechniqueRepository ficheTechniqueRepository) {
        this.ficheTechniqueRepository = ficheTechniqueRepository;
    }

    /**
     * {@code POST  /fiche-techniques} : Create a new ficheTechnique.
     *
     * @param ficheTechnique the ficheTechnique to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ficheTechnique, or with status {@code 400 (Bad Request)} if the ficheTechnique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fiche-techniques")
    public Mono<ResponseEntity<FicheTechnique>> createFicheTechnique(@Valid @RequestBody FicheTechnique ficheTechnique)
        throws URISyntaxException {
        log.debug("REST request to save FicheTechnique : {}", ficheTechnique);
        if (ficheTechnique.getId() != null) {
            throw new BadRequestAlertException("A new ficheTechnique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ficheTechniqueRepository
            .save(ficheTechnique)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/fiche-techniques/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /fiche-techniques/:id} : Updates an existing ficheTechnique.
     *
     * @param id the id of the ficheTechnique to save.
     * @param ficheTechnique the ficheTechnique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ficheTechnique,
     * or with status {@code 400 (Bad Request)} if the ficheTechnique is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ficheTechnique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fiche-techniques/{id}")
    public Mono<ResponseEntity<FicheTechnique>> updateFicheTechnique(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FicheTechnique ficheTechnique
    ) throws URISyntaxException {
        log.debug("REST request to update FicheTechnique : {}, {}", id, ficheTechnique);
        if (ficheTechnique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ficheTechnique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ficheTechniqueRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return ficheTechniqueRepository
                        .save(ficheTechnique)
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
     * {@code PATCH  /fiche-techniques/:id} : Partial updates given fields of an existing ficheTechnique, field will ignore if it is null
     *
     * @param id the id of the ficheTechnique to save.
     * @param ficheTechnique the ficheTechnique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ficheTechnique,
     * or with status {@code 400 (Bad Request)} if the ficheTechnique is not valid,
     * or with status {@code 404 (Not Found)} if the ficheTechnique is not found,
     * or with status {@code 500 (Internal Server Error)} if the ficheTechnique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fiche-techniques/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<FicheTechnique>> partialUpdateFicheTechnique(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FicheTechnique ficheTechnique
    ) throws URISyntaxException {
        log.debug("REST request to partial update FicheTechnique partially : {}, {}", id, ficheTechnique);
        if (ficheTechnique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ficheTechnique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ficheTechniqueRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<FicheTechnique> result = ficheTechniqueRepository
                        .findById(ficheTechnique.getId())
                        .map(
                            existingFicheTechnique -> {
                                if (ficheTechnique.getPieceJointe() != null) {
                                    existingFicheTechnique.setPieceJointe(ficheTechnique.getPieceJointe());
                                }
                                if (ficheTechnique.getDateDepot() != null) {
                                    existingFicheTechnique.setDateDepot(ficheTechnique.getDateDepot());
                                }

                                return existingFicheTechnique;
                            }
                        )
                        .flatMap(ficheTechniqueRepository::save);

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
     * {@code GET  /fiche-techniques} : get all the ficheTechniques.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ficheTechniques in body.
     */
    @GetMapping("/fiche-techniques")
    public Mono<ResponseEntity<List<FicheTechnique>>> getAllFicheTechniques(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of FicheTechniques");
        return ficheTechniqueRepository
            .count()
            .zipWith(ficheTechniqueRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /fiche-techniques/:id} : get the "id" ficheTechnique.
     *
     * @param id the id of the ficheTechnique to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ficheTechnique, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fiche-techniques/{id}")
    public Mono<ResponseEntity<FicheTechnique>> getFicheTechnique(@PathVariable Long id) {
        log.debug("REST request to get FicheTechnique : {}", id);
        Mono<FicheTechnique> ficheTechnique = ficheTechniqueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ficheTechnique);
    }

    /**
     * {@code DELETE  /fiche-techniques/:id} : delete the "id" ficheTechnique.
     *
     * @param id the id of the ficheTechnique to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fiche-techniques/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFicheTechnique(@PathVariable Long id) {
        log.debug("REST request to delete FicheTechnique : {}", id);
        return ficheTechniqueRepository
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
