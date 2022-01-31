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
import sn.coundoul.gestion.patrimoine.domain.ComptableSecondaire;
import sn.coundoul.gestion.patrimoine.repository.ComptableSecondaireRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.ComptableSecondaire}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ComptableSecondaireResource {

    private final Logger log = LoggerFactory.getLogger(ComptableSecondaireResource.class);

    private static final String ENTITY_NAME = "comptableSecondaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComptableSecondaireRepository comptableSecondaireRepository;

    public ComptableSecondaireResource(ComptableSecondaireRepository comptableSecondaireRepository) {
        this.comptableSecondaireRepository = comptableSecondaireRepository;
    }

    /**
     * {@code POST  /comptable-secondaires} : Create a new comptableSecondaire.
     *
     * @param comptableSecondaire the comptableSecondaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comptableSecondaire, or with status {@code 400 (Bad Request)} if the comptableSecondaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comptable-secondaires")
    public Mono<ResponseEntity<ComptableSecondaire>> createComptableSecondaire(@Valid @RequestBody ComptableSecondaire comptableSecondaire)
        throws URISyntaxException {
        log.debug("REST request to save ComptableSecondaire : {}", comptableSecondaire);
        if (comptableSecondaire.getId() != null) {
            throw new BadRequestAlertException("A new comptableSecondaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return comptableSecondaireRepository
            .save(comptableSecondaire)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/comptable-secondaires/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /comptable-secondaires/:id} : Updates an existing comptableSecondaire.
     *
     * @param id the id of the comptableSecondaire to save.
     * @param comptableSecondaire the comptableSecondaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comptableSecondaire,
     * or with status {@code 400 (Bad Request)} if the comptableSecondaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comptableSecondaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comptable-secondaires/{id}")
    public Mono<ResponseEntity<ComptableSecondaire>> updateComptableSecondaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComptableSecondaire comptableSecondaire
    ) throws URISyntaxException {
        log.debug("REST request to update ComptableSecondaire : {}, {}", id, comptableSecondaire);
        if (comptableSecondaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comptableSecondaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return comptableSecondaireRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return comptableSecondaireRepository
                        .save(comptableSecondaire)
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
     * {@code PATCH  /comptable-secondaires/:id} : Partial updates given fields of an existing comptableSecondaire, field will ignore if it is null
     *
     * @param id the id of the comptableSecondaire to save.
     * @param comptableSecondaire the comptableSecondaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comptableSecondaire,
     * or with status {@code 400 (Bad Request)} if the comptableSecondaire is not valid,
     * or with status {@code 404 (Not Found)} if the comptableSecondaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the comptableSecondaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comptable-secondaires/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<ComptableSecondaire>> partialUpdateComptableSecondaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComptableSecondaire comptableSecondaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComptableSecondaire partially : {}, {}", id, comptableSecondaire);
        if (comptableSecondaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comptableSecondaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return comptableSecondaireRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ComptableSecondaire> result = comptableSecondaireRepository
                        .findById(comptableSecondaire.getId())
                        .map(
                            existingComptableSecondaire -> {
                                if (comptableSecondaire.getNomPers() != null) {
                                    existingComptableSecondaire.setNomPers(comptableSecondaire.getNomPers());
                                }
                                if (comptableSecondaire.getPrenomPers() != null) {
                                    existingComptableSecondaire.setPrenomPers(comptableSecondaire.getPrenomPers());
                                }
                                if (comptableSecondaire.getSexe() != null) {
                                    existingComptableSecondaire.setSexe(comptableSecondaire.getSexe());
                                }
                                if (comptableSecondaire.getMobile() != null) {
                                    existingComptableSecondaire.setMobile(comptableSecondaire.getMobile());
                                }
                                if (comptableSecondaire.getAdresse() != null) {
                                    existingComptableSecondaire.setAdresse(comptableSecondaire.getAdresse());
                                }
                                if (comptableSecondaire.getDirection() != null) {
                                    existingComptableSecondaire.setDirection(comptableSecondaire.getDirection());
                                }

                                return existingComptableSecondaire;
                            }
                        )
                        .flatMap(comptableSecondaireRepository::save);

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
     * {@code GET  /comptable-secondaires} : get all the comptableSecondaires.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comptableSecondaires in body.
     */
    @GetMapping("/comptable-secondaires")
    public Mono<ResponseEntity<List<ComptableSecondaire>>> getAllComptableSecondaires(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of ComptableSecondaires");
        return comptableSecondaireRepository
            .count()
            .zipWith(comptableSecondaireRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /comptable-secondaires/:id} : get the "id" comptableSecondaire.
     *
     * @param id the id of the comptableSecondaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comptableSecondaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comptable-secondaires/{id}")
    public Mono<ResponseEntity<ComptableSecondaire>> getComptableSecondaire(@PathVariable Long id) {
        log.debug("REST request to get ComptableSecondaire : {}", id);
        Mono<ComptableSecondaire> comptableSecondaire = comptableSecondaireRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(comptableSecondaire);
    }

    /**
     * {@code DELETE  /comptable-secondaires/:id} : delete the "id" comptableSecondaire.
     *
     * @param id the id of the comptableSecondaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comptable-secondaires/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteComptableSecondaire(@PathVariable Long id) {
        log.debug("REST request to delete ComptableSecondaire : {}", id);
        return comptableSecondaireRepository
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
