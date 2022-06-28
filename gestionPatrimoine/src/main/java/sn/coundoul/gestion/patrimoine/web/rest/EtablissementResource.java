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
import sn.coundoul.gestion.patrimoine.domain.Etablissement;
import sn.coundoul.gestion.patrimoine.repository.EtablissementRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Etablissement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EtablissementResource {

    private final Logger log = LoggerFactory.getLogger(EtablissementResource.class);

    private static final String ENTITY_NAME = "etablissement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtablissementRepository etablissementRepository;

    public EtablissementResource(EtablissementRepository etablissementRepository) {
        this.etablissementRepository = etablissementRepository;
    }

    /**
     * {@code POST  /etablissements} : Create a new etablissement.
     *
     * @param etablissement the etablissement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etablissement, or with status {@code 400 (Bad Request)} if the etablissement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etablissements")
    public Mono<ResponseEntity<Etablissement>> createEtablissement(@Valid @RequestBody Etablissement etablissement)
        throws URISyntaxException {
        log.debug("REST request to save Etablissement : {}", etablissement);
        if (etablissement.getId() != null) {
            throw new BadRequestAlertException("A new etablissement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return etablissementRepository
            .save(etablissement)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/etablissements/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /etablissements/:id} : Updates an existing etablissement.
     *
     * @param id the id of the etablissement to save.
     * @param etablissement the etablissement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etablissement,
     * or with status {@code 400 (Bad Request)} if the etablissement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etablissement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etablissements/{id}")
    public Mono<ResponseEntity<Etablissement>> updateEtablissement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Etablissement etablissement
    ) throws URISyntaxException {
        log.debug("REST request to update Etablissement : {}, {}", id, etablissement);
        if (etablissement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etablissement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return etablissementRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return etablissementRepository
                        .save(etablissement)
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
     * {@code PATCH  /etablissements/:id} : Partial updates given fields of an existing etablissement, field will ignore if it is null
     *
     * @param id the id of the etablissement to save.
     * @param etablissement the etablissement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etablissement,
     * or with status {@code 400 (Bad Request)} if the etablissement is not valid,
     * or with status {@code 404 (Not Found)} if the etablissement is not found,
     * or with status {@code 500 (Internal Server Error)} if the etablissement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/etablissements/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Etablissement>> partialUpdateEtablissement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Etablissement etablissement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Etablissement partially : {}, {}", id, etablissement);
        if (etablissement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etablissement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return etablissementRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Etablissement> result = etablissementRepository
                        .findById(etablissement.getId())
                        .map(
                            existingEtablissement -> {
                                if (etablissement.getNomEtablissement() != null) {
                                    existingEtablissement.setNomEtablissement(etablissement.getNomEtablissement());
                                }
                                if (etablissement.getSurfaceBatie() != null) {
                                    existingEtablissement.setSurfaceBatie(etablissement.getSurfaceBatie());
                                }
                                if (etablissement.getSuperficie() != null) {
                                    existingEtablissement.setSuperficie(etablissement.getSuperficie());
                                }
                                if (etablissement.getIdPers() != null) {
                                    existingEtablissement.setIdPers(etablissement.getIdPers());
                                }

                                return existingEtablissement;
                            }
                        )
                        .flatMap(etablissementRepository::save);

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
     * {@code GET  /etablissements} : get all the etablissements.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etablissements in body.
     */
    @GetMapping("/etablissements")
    public Mono<ResponseEntity<List<Etablissement>>> getAllEtablissements(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Etablissements");
        return etablissementRepository
            .count()
            .zipWith(etablissementRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /etablissements/:id} : get the "id" etablissement.
     *
     * @param id the id of the etablissement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etablissement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etablissements/{id}")
    public Mono<ResponseEntity<Etablissement>> getEtablissement(@PathVariable Long id) {
        log.debug("REST request to get Etablissement : {}", id);
        Mono<Etablissement> etablissement = etablissementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(etablissement);
    }

    /**
     * {@code DELETE  /etablissements/:id} : delete the "id" etablissement.
     *
     * @param id the id of the etablissement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etablissements/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteEtablissement(@PathVariable Long id) {
        log.debug("REST request to delete Etablissement : {}", id);
        return etablissementRepository
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
