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
import sn.coundoul.gestion.patrimoine.domain.ChefProjet;
import sn.coundoul.gestion.patrimoine.repository.ChefProjetRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.ChefProjet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ChefProjetResource {

    private final Logger log = LoggerFactory.getLogger(ChefProjetResource.class);

    private static final String ENTITY_NAME = "chefProjet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChefProjetRepository chefProjetRepository;

    public ChefProjetResource(ChefProjetRepository chefProjetRepository) {
        this.chefProjetRepository = chefProjetRepository;
    }

    /**
     * {@code POST  /chef-projets} : Create a new chefProjet.
     *
     * @param chefProjet the chefProjet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chefProjet, or with status {@code 400 (Bad Request)} if the chefProjet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chef-projets")
    public Mono<ResponseEntity<ChefProjet>> createChefProjet(@Valid @RequestBody ChefProjet chefProjet) throws URISyntaxException {
        log.debug("REST request to save ChefProjet : {}", chefProjet);
        if (chefProjet.getId() != null) {
            throw new BadRequestAlertException("A new chefProjet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return chefProjetRepository
            .save(chefProjet)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/chef-projets/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /chef-projets/:id} : Updates an existing chefProjet.
     *
     * @param id the id of the chefProjet to save.
     * @param chefProjet the chefProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chefProjet,
     * or with status {@code 400 (Bad Request)} if the chefProjet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chefProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chef-projets/{id}")
    public Mono<ResponseEntity<ChefProjet>> updateChefProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChefProjet chefProjet
    ) throws URISyntaxException {
        log.debug("REST request to update ChefProjet : {}, {}", id, chefProjet);
        if (chefProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chefProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return chefProjetRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return chefProjetRepository
                        .save(chefProjet)
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
     * {@code PATCH  /chef-projets/:id} : Partial updates given fields of an existing chefProjet, field will ignore if it is null
     *
     * @param id the id of the chefProjet to save.
     * @param chefProjet the chefProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chefProjet,
     * or with status {@code 400 (Bad Request)} if the chefProjet is not valid,
     * or with status {@code 404 (Not Found)} if the chefProjet is not found,
     * or with status {@code 500 (Internal Server Error)} if the chefProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chef-projets/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<ChefProjet>> partialUpdateChefProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChefProjet chefProjet
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChefProjet partially : {}, {}", id, chefProjet);
        if (chefProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chefProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return chefProjetRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ChefProjet> result = chefProjetRepository
                        .findById(chefProjet.getId())
                        .map(
                            existingChefProjet -> {
                                if (chefProjet.getNomPers() != null) {
                                    existingChefProjet.setNomPers(chefProjet.getNomPers());
                                }
                                if (chefProjet.getPrenomPers() != null) {
                                    existingChefProjet.setPrenomPers(chefProjet.getPrenomPers());
                                }
                                if (chefProjet.getSexe() != null) {
                                    existingChefProjet.setSexe(chefProjet.getSexe());
                                }
                                if (chefProjet.getMobile() != null) {
                                    existingChefProjet.setMobile(chefProjet.getMobile());
                                }
                                if (chefProjet.getAdresse() != null) {
                                    existingChefProjet.setAdresse(chefProjet.getAdresse());
                                }
                                if (chefProjet.getDirection() != null) {
                                    existingChefProjet.setDirection(chefProjet.getDirection());
                                }

                                return existingChefProjet;
                            }
                        )
                        .flatMap(chefProjetRepository::save);

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
     * {@code GET  /chef-projets} : get all the chefProjets.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chefProjets in body.
     */
    @GetMapping("/chef-projets")
    public Mono<ResponseEntity<List<ChefProjet>>> getAllChefProjets(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of ChefProjets");
        return chefProjetRepository
            .count()
            .zipWith(chefProjetRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /chef-projets/:id} : get the "id" chefProjet.
     *
     * @param id the id of the chefProjet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chefProjet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chef-projets/{id}")
    public Mono<ResponseEntity<ChefProjet>> getChefProjet(@PathVariable Long id) {
        log.debug("REST request to get ChefProjet : {}", id);
        Mono<ChefProjet> chefProjet = chefProjetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(chefProjet);
    }

    /**
     * {@code DELETE  /chef-projets/:id} : delete the "id" chefProjet.
     *
     * @param id the id of the chefProjet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chef-projets/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteChefProjet(@PathVariable Long id) {
        log.debug("REST request to delete ChefProjet : {}", id);
        return chefProjetRepository
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
