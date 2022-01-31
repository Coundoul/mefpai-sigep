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
import sn.coundoul.gestion.patrimoine.domain.OrdonnaceurMatiere;
import sn.coundoul.gestion.patrimoine.repository.OrdonnaceurMatiereRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.OrdonnaceurMatiere}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrdonnaceurMatiereResource {

    private final Logger log = LoggerFactory.getLogger(OrdonnaceurMatiereResource.class);

    private static final String ENTITY_NAME = "ordonnaceurMatiere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdonnaceurMatiereRepository ordonnaceurMatiereRepository;

    public OrdonnaceurMatiereResource(OrdonnaceurMatiereRepository ordonnaceurMatiereRepository) {
        this.ordonnaceurMatiereRepository = ordonnaceurMatiereRepository;
    }

    /**
     * {@code POST  /ordonnaceur-matieres} : Create a new ordonnaceurMatiere.
     *
     * @param ordonnaceurMatiere the ordonnaceurMatiere to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordonnaceurMatiere, or with status {@code 400 (Bad Request)} if the ordonnaceurMatiere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordonnaceur-matieres")
    public Mono<ResponseEntity<OrdonnaceurMatiere>> createOrdonnaceurMatiere(@Valid @RequestBody OrdonnaceurMatiere ordonnaceurMatiere)
        throws URISyntaxException {
        log.debug("REST request to save OrdonnaceurMatiere : {}", ordonnaceurMatiere);
        if (ordonnaceurMatiere.getId() != null) {
            throw new BadRequestAlertException("A new ordonnaceurMatiere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ordonnaceurMatiereRepository
            .save(ordonnaceurMatiere)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/ordonnaceur-matieres/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /ordonnaceur-matieres/:id} : Updates an existing ordonnaceurMatiere.
     *
     * @param id the id of the ordonnaceurMatiere to save.
     * @param ordonnaceurMatiere the ordonnaceurMatiere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordonnaceurMatiere,
     * or with status {@code 400 (Bad Request)} if the ordonnaceurMatiere is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordonnaceurMatiere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordonnaceur-matieres/{id}")
    public Mono<ResponseEntity<OrdonnaceurMatiere>> updateOrdonnaceurMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrdonnaceurMatiere ordonnaceurMatiere
    ) throws URISyntaxException {
        log.debug("REST request to update OrdonnaceurMatiere : {}, {}", id, ordonnaceurMatiere);
        if (ordonnaceurMatiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordonnaceurMatiere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ordonnaceurMatiereRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return ordonnaceurMatiereRepository
                        .save(ordonnaceurMatiere)
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
     * {@code PATCH  /ordonnaceur-matieres/:id} : Partial updates given fields of an existing ordonnaceurMatiere, field will ignore if it is null
     *
     * @param id the id of the ordonnaceurMatiere to save.
     * @param ordonnaceurMatiere the ordonnaceurMatiere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordonnaceurMatiere,
     * or with status {@code 400 (Bad Request)} if the ordonnaceurMatiere is not valid,
     * or with status {@code 404 (Not Found)} if the ordonnaceurMatiere is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordonnaceurMatiere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ordonnaceur-matieres/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<OrdonnaceurMatiere>> partialUpdateOrdonnaceurMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrdonnaceurMatiere ordonnaceurMatiere
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdonnaceurMatiere partially : {}, {}", id, ordonnaceurMatiere);
        if (ordonnaceurMatiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordonnaceurMatiere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ordonnaceurMatiereRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<OrdonnaceurMatiere> result = ordonnaceurMatiereRepository
                        .findById(ordonnaceurMatiere.getId())
                        .map(
                            existingOrdonnaceurMatiere -> {
                                if (ordonnaceurMatiere.getNomPers() != null) {
                                    existingOrdonnaceurMatiere.setNomPers(ordonnaceurMatiere.getNomPers());
                                }
                                if (ordonnaceurMatiere.getPrenomPers() != null) {
                                    existingOrdonnaceurMatiere.setPrenomPers(ordonnaceurMatiere.getPrenomPers());
                                }
                                if (ordonnaceurMatiere.getSexe() != null) {
                                    existingOrdonnaceurMatiere.setSexe(ordonnaceurMatiere.getSexe());
                                }
                                if (ordonnaceurMatiere.getMobile() != null) {
                                    existingOrdonnaceurMatiere.setMobile(ordonnaceurMatiere.getMobile());
                                }
                                if (ordonnaceurMatiere.getAdresse() != null) {
                                    existingOrdonnaceurMatiere.setAdresse(ordonnaceurMatiere.getAdresse());
                                }
                                if (ordonnaceurMatiere.getDirection() != null) {
                                    existingOrdonnaceurMatiere.setDirection(ordonnaceurMatiere.getDirection());
                                }

                                return existingOrdonnaceurMatiere;
                            }
                        )
                        .flatMap(ordonnaceurMatiereRepository::save);

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
     * {@code GET  /ordonnaceur-matieres} : get all the ordonnaceurMatieres.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordonnaceurMatieres in body.
     */
    @GetMapping("/ordonnaceur-matieres")
    public Mono<ResponseEntity<List<OrdonnaceurMatiere>>> getAllOrdonnaceurMatieres(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of OrdonnaceurMatieres");
        return ordonnaceurMatiereRepository
            .count()
            .zipWith(ordonnaceurMatiereRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /ordonnaceur-matieres/:id} : get the "id" ordonnaceurMatiere.
     *
     * @param id the id of the ordonnaceurMatiere to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordonnaceurMatiere, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordonnaceur-matieres/{id}")
    public Mono<ResponseEntity<OrdonnaceurMatiere>> getOrdonnaceurMatiere(@PathVariable Long id) {
        log.debug("REST request to get OrdonnaceurMatiere : {}", id);
        Mono<OrdonnaceurMatiere> ordonnaceurMatiere = ordonnaceurMatiereRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ordonnaceurMatiere);
    }

    /**
     * {@code DELETE  /ordonnaceur-matieres/:id} : delete the "id" ordonnaceurMatiere.
     *
     * @param id the id of the ordonnaceurMatiere to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordonnaceur-matieres/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteOrdonnaceurMatiere(@PathVariable Long id) {
        log.debug("REST request to delete OrdonnaceurMatiere : {}", id);
        return ordonnaceurMatiereRepository
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
