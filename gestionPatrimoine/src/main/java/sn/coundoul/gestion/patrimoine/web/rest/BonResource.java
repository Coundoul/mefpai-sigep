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
import sn.coundoul.gestion.patrimoine.domain.Bon;
import sn.coundoul.gestion.patrimoine.repository.BonRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Bon}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BonResource {

    private final Logger log = LoggerFactory.getLogger(BonResource.class);

    private static final String ENTITY_NAME = "bon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BonRepository bonRepository;

    public BonResource(BonRepository bonRepository) {
        this.bonRepository = bonRepository;
    }

    /**
     * {@code POST  /bons} : Create a new bon.
     *
     * @param bon the bon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bon, or with status {@code 400 (Bad Request)} if the bon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bons")
    public Mono<ResponseEntity<Bon>> createBon(@Valid @RequestBody Bon bon) throws URISyntaxException {
        log.debug("REST request to save Bon : {}", bon);
        if (bon.getId() != null) {
            throw new BadRequestAlertException("A new bon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return bonRepository
            .save(bon)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/bons/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /bons/:id} : Updates an existing bon.
     *
     * @param id the id of the bon to save.
     * @param bon the bon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bon,
     * or with status {@code 400 (Bad Request)} if the bon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bons/{id}")
    public Mono<ResponseEntity<Bon>> updateBon(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Bon bon)
        throws URISyntaxException {
        log.debug("REST request to update Bon : {}, {}", id, bon);
        if (bon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bonRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return bonRepository
                        .save(bon)
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
     * {@code PATCH  /bons/:id} : Partial updates given fields of an existing bon, field will ignore if it is null
     *
     * @param id the id of the bon to save.
     * @param bon the bon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bon,
     * or with status {@code 400 (Bad Request)} if the bon is not valid,
     * or with status {@code 404 (Not Found)} if the bon is not found,
     * or with status {@code 500 (Internal Server Error)} if the bon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bons/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Bon>> partialUpdateBon(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Bon bon
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bon partially : {}, {}", id, bon);
        if (bon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bonRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Bon> result = bonRepository
                        .findById(bon.getId())
                        .map(
                            existingBon -> {
                                if (bon.getTypeBon() != null) {
                                    existingBon.setTypeBon(bon.getTypeBon());
                                }
                                if (bon.getQuantiteLivre() != null) {
                                    existingBon.setQuantiteLivre(bon.getQuantiteLivre());
                                }
                                if (bon.getQuantiteCommande() != null) {
                                    existingBon.setQuantiteCommande(bon.getQuantiteCommande());
                                }
                                if (bon.getDateCreation() != null) {
                                    existingBon.setDateCreation(bon.getDateCreation());
                                }
                                if (bon.getEtat() != null) {
                                    existingBon.setEtat(bon.getEtat());
                                }

                                return existingBon;
                            }
                        )
                        .flatMap(bonRepository::save);

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
     * {@code GET  /bons} : get all the bons.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bons in body.
     */
    @GetMapping("/bons")
    public Mono<ResponseEntity<List<Bon>>> getAllBons(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Bons");
        return bonRepository
            .count()
            .zipWith(bonRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /bons/:id} : get the "id" bon.
     *
     * @param id the id of the bon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bons/{id}")
    public Mono<ResponseEntity<Bon>> getBon(@PathVariable Long id) {
        log.debug("REST request to get Bon : {}", id);
        Mono<Bon> bon = bonRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bon);
    }

    /**
     * {@code DELETE  /bons/:id} : delete the "id" bon.
     *
     * @param id the id of the bon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bons/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBon(@PathVariable Long id) {
        log.debug("REST request to delete Bon : {}", id);
        return bonRepository
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
