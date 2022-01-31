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
import sn.coundoul.gestion.patrimoine.domain.Intervenant;
import sn.coundoul.gestion.patrimoine.repository.IntervenantRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Intervenant}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IntervenantResource {

    private final Logger log = LoggerFactory.getLogger(IntervenantResource.class);

    private static final String ENTITY_NAME = "intervenant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IntervenantRepository intervenantRepository;

    public IntervenantResource(IntervenantRepository intervenantRepository) {
        this.intervenantRepository = intervenantRepository;
    }

    /**
     * {@code POST  /intervenants} : Create a new intervenant.
     *
     * @param intervenant the intervenant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new intervenant, or with status {@code 400 (Bad Request)} if the intervenant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/intervenants")
    public Mono<ResponseEntity<Intervenant>> createIntervenant(@Valid @RequestBody Intervenant intervenant) throws URISyntaxException {
        log.debug("REST request to save Intervenant : {}", intervenant);
        if (intervenant.getId() != null) {
            throw new BadRequestAlertException("A new intervenant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return intervenantRepository
            .save(intervenant)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/intervenants/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /intervenants/:id} : Updates an existing intervenant.
     *
     * @param id the id of the intervenant to save.
     * @param intervenant the intervenant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intervenant,
     * or with status {@code 400 (Bad Request)} if the intervenant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the intervenant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/intervenants/{id}")
    public Mono<ResponseEntity<Intervenant>> updateIntervenant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Intervenant intervenant
    ) throws URISyntaxException {
        log.debug("REST request to update Intervenant : {}, {}", id, intervenant);
        if (intervenant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intervenant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return intervenantRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return intervenantRepository
                        .save(intervenant)
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
     * {@code PATCH  /intervenants/:id} : Partial updates given fields of an existing intervenant, field will ignore if it is null
     *
     * @param id the id of the intervenant to save.
     * @param intervenant the intervenant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intervenant,
     * or with status {@code 400 (Bad Request)} if the intervenant is not valid,
     * or with status {@code 404 (Not Found)} if the intervenant is not found,
     * or with status {@code 500 (Internal Server Error)} if the intervenant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/intervenants/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Intervenant>> partialUpdateIntervenant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Intervenant intervenant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Intervenant partially : {}, {}", id, intervenant);
        if (intervenant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intervenant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return intervenantRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Intervenant> result = intervenantRepository
                        .findById(intervenant.getId())
                        .map(
                            existingIntervenant -> {
                                if (intervenant.getNomIntervenant() != null) {
                                    existingIntervenant.setNomIntervenant(intervenant.getNomIntervenant());
                                }
                                if (intervenant.getPrenomIntervenant() != null) {
                                    existingIntervenant.setPrenomIntervenant(intervenant.getPrenomIntervenant());
                                }
                                if (intervenant.getEmailProfessionnel() != null) {
                                    existingIntervenant.setEmailProfessionnel(intervenant.getEmailProfessionnel());
                                }
                                if (intervenant.getRaisonSocial() != null) {
                                    existingIntervenant.setRaisonSocial(intervenant.getRaisonSocial());
                                }
                                if (intervenant.getMaitre() != null) {
                                    existingIntervenant.setMaitre(intervenant.getMaitre());
                                }
                                if (intervenant.getRole() != null) {
                                    existingIntervenant.setRole(intervenant.getRole());
                                }

                                return existingIntervenant;
                            }
                        )
                        .flatMap(intervenantRepository::save);

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
     * {@code GET  /intervenants} : get all the intervenants.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of intervenants in body.
     */
    @GetMapping("/intervenants")
    public Mono<ResponseEntity<List<Intervenant>>> getAllIntervenants(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Intervenants");
        return intervenantRepository
            .count()
            .zipWith(intervenantRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /intervenants/:id} : get the "id" intervenant.
     *
     * @param id the id of the intervenant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the intervenant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/intervenants/{id}")
    public Mono<ResponseEntity<Intervenant>> getIntervenant(@PathVariable Long id) {
        log.debug("REST request to get Intervenant : {}", id);
        Mono<Intervenant> intervenant = intervenantRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(intervenant);
    }

    /**
     * {@code DELETE  /intervenants/:id} : delete the "id" intervenant.
     *
     * @param id the id of the intervenant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/intervenants/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteIntervenant(@PathVariable Long id) {
        log.debug("REST request to delete Intervenant : {}", id);
        return intervenantRepository
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
