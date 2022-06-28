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
import sn.coundoul.gestion.patrimoine.domain.Directeur;
import sn.coundoul.gestion.patrimoine.repository.DirecteurRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Directeur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DirecteurResource {

    private final Logger log = LoggerFactory.getLogger(DirecteurResource.class);

    private static final String ENTITY_NAME = "directeur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DirecteurRepository directeurRepository;

    public DirecteurResource(DirecteurRepository directeurRepository) {
        this.directeurRepository = directeurRepository;
    }

    /**
     * {@code POST  /directeurs} : Create a new directeur.
     *
     * @param directeur the directeur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new directeur, or with status {@code 400 (Bad Request)} if the directeur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/directeurs")
    public Mono<ResponseEntity<Directeur>> createDirecteur(@Valid @RequestBody Directeur directeur) throws URISyntaxException {
        log.debug("REST request to save Directeur : {}", directeur);
        if (directeur.getId() != null) {
            throw new BadRequestAlertException("A new directeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return directeurRepository
            .save(directeur)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/directeurs/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /directeurs/:id} : Updates an existing directeur.
     *
     * @param id the id of the directeur to save.
     * @param directeur the directeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directeur,
     * or with status {@code 400 (Bad Request)} if the directeur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the directeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/directeurs/{id}")
    public Mono<ResponseEntity<Directeur>> updateDirecteur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Directeur directeur
    ) throws URISyntaxException {
        log.debug("REST request to update Directeur : {}, {}", id, directeur);
        if (directeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return directeurRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return directeurRepository
                        .save(directeur)
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
     * {@code PATCH  /directeurs/:id} : Partial updates given fields of an existing directeur, field will ignore if it is null
     *
     * @param id the id of the directeur to save.
     * @param directeur the directeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directeur,
     * or with status {@code 400 (Bad Request)} if the directeur is not valid,
     * or with status {@code 404 (Not Found)} if the directeur is not found,
     * or with status {@code 500 (Internal Server Error)} if the directeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/directeurs/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Directeur>> partialUpdateDirecteur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Directeur directeur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Directeur partially : {}, {}", id, directeur);
        if (directeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return directeurRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Directeur> result = directeurRepository
                        .findById(directeur.getId())
                        .map(
                            existingDirecteur -> {
                                if (directeur.getNomPers() != null) {
                                    existingDirecteur.setNomPers(directeur.getNomPers());
                                }
                                if (directeur.getPrenomPers() != null) {
                                    existingDirecteur.setPrenomPers(directeur.getPrenomPers());
                                }
                                if (directeur.getSexe() != null) {
                                    existingDirecteur.setSexe(directeur.getSexe());
                                }
                                if (directeur.getMobile() != null) {
                                    existingDirecteur.setMobile(directeur.getMobile());
                                }
                                if (directeur.getAdresse() != null) {
                                    existingDirecteur.setAdresse(directeur.getAdresse());
                                }
                                if (directeur.getDirection() != null) {
                                    existingDirecteur.setDirection(directeur.getDirection());
                                }

                                return existingDirecteur;
                            }
                        )
                        .flatMap(directeurRepository::save);

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
     * {@code GET  /directeurs} : get all the directeurs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of directeurs in body.
     */
    @GetMapping("/directeurs")
    public Mono<ResponseEntity<List<Directeur>>> getAllDirecteurs(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Directeurs");
        return directeurRepository
            .count()
            .zipWith(directeurRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /directeurs/:id} : get the "id" directeur.
     *
     * @param id the id of the directeur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the directeur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/directeurs/{id}")
    public Mono<ResponseEntity<Directeur>> getDirecteur(@PathVariable Long id) {
        log.debug("REST request to get Directeur : {}", id);
        Mono<Directeur> directeur = directeurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(directeur);
    }

    /**
     * {@code DELETE  /directeurs/:id} : delete the "id" directeur.
     *
     * @param id the id of the directeur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/directeurs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDirecteur(@PathVariable Long id) {
        log.debug("REST request to delete Directeur : {}", id);
        return directeurRepository
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
