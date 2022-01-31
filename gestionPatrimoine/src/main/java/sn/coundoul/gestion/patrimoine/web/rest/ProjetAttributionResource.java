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
import sn.coundoul.gestion.patrimoine.domain.ProjetAttribution;
import sn.coundoul.gestion.patrimoine.repository.ProjetAttributionRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.ProjetAttribution}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProjetAttributionResource {

    private final Logger log = LoggerFactory.getLogger(ProjetAttributionResource.class);

    private static final String ENTITY_NAME = "projetAttribution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjetAttributionRepository projetAttributionRepository;

    public ProjetAttributionResource(ProjetAttributionRepository projetAttributionRepository) {
        this.projetAttributionRepository = projetAttributionRepository;
    }

    /**
     * {@code POST  /projet-attributions} : Create a new projetAttribution.
     *
     * @param projetAttribution the projetAttribution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projetAttribution, or with status {@code 400 (Bad Request)} if the projetAttribution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/projet-attributions")
    public Mono<ResponseEntity<ProjetAttribution>> createProjetAttribution(@Valid @RequestBody ProjetAttribution projetAttribution)
        throws URISyntaxException {
        log.debug("REST request to save ProjetAttribution : {}", projetAttribution);
        if (projetAttribution.getId() != null) {
            throw new BadRequestAlertException("A new projetAttribution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return projetAttributionRepository
            .save(projetAttribution)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/projet-attributions/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /projet-attributions/:id} : Updates an existing projetAttribution.
     *
     * @param id the id of the projetAttribution to save.
     * @param projetAttribution the projetAttribution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projetAttribution,
     * or with status {@code 400 (Bad Request)} if the projetAttribution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projetAttribution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/projet-attributions/{id}")
    public Mono<ResponseEntity<ProjetAttribution>> updateProjetAttribution(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProjetAttribution projetAttribution
    ) throws URISyntaxException {
        log.debug("REST request to update ProjetAttribution : {}, {}", id, projetAttribution);
        if (projetAttribution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projetAttribution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return projetAttributionRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return projetAttributionRepository
                        .save(projetAttribution)
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
     * {@code PATCH  /projet-attributions/:id} : Partial updates given fields of an existing projetAttribution, field will ignore if it is null
     *
     * @param id the id of the projetAttribution to save.
     * @param projetAttribution the projetAttribution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projetAttribution,
     * or with status {@code 400 (Bad Request)} if the projetAttribution is not valid,
     * or with status {@code 404 (Not Found)} if the projetAttribution is not found,
     * or with status {@code 500 (Internal Server Error)} if the projetAttribution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/projet-attributions/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<ProjetAttribution>> partialUpdateProjetAttribution(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProjetAttribution projetAttribution
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProjetAttribution partially : {}, {}", id, projetAttribution);
        if (projetAttribution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projetAttribution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return projetAttributionRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ProjetAttribution> result = projetAttributionRepository
                        .findById(projetAttribution.getId())
                        .map(
                            existingProjetAttribution -> {
                                if (projetAttribution.getDateAttribution() != null) {
                                    existingProjetAttribution.setDateAttribution(projetAttribution.getDateAttribution());
                                }
                                if (projetAttribution.getQuantite() != null) {
                                    existingProjetAttribution.setQuantite(projetAttribution.getQuantite());
                                }
                                if (projetAttribution.getIdEquipement() != null) {
                                    existingProjetAttribution.setIdEquipement(projetAttribution.getIdEquipement());
                                }
                                if (projetAttribution.getIdPers() != null) {
                                    existingProjetAttribution.setIdPers(projetAttribution.getIdPers());
                                }

                                return existingProjetAttribution;
                            }
                        )
                        .flatMap(projetAttributionRepository::save);

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
     * {@code GET  /projet-attributions} : get all the projetAttributions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projetAttributions in body.
     */
    @GetMapping("/projet-attributions")
    public Mono<ResponseEntity<List<ProjetAttribution>>> getAllProjetAttributions(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of ProjetAttributions");
        return projetAttributionRepository
            .count()
            .zipWith(projetAttributionRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /projet-attributions/:id} : get the "id" projetAttribution.
     *
     * @param id the id of the projetAttribution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projetAttribution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/projet-attributions/{id}")
    public Mono<ResponseEntity<ProjetAttribution>> getProjetAttribution(@PathVariable Long id) {
        log.debug("REST request to get ProjetAttribution : {}", id);
        Mono<ProjetAttribution> projetAttribution = projetAttributionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(projetAttribution);
    }

    /**
     * {@code DELETE  /projet-attributions/:id} : delete the "id" projetAttribution.
     *
     * @param id the id of the projetAttribution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/projet-attributions/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteProjetAttribution(@PathVariable Long id) {
        log.debug("REST request to delete ProjetAttribution : {}", id);
        return projetAttributionRepository
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
