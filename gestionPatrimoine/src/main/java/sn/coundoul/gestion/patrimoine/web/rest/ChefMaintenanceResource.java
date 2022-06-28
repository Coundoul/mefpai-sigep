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
import sn.coundoul.gestion.patrimoine.domain.ChefMaintenance;
import sn.coundoul.gestion.patrimoine.repository.ChefMaintenanceRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.ChefMaintenance}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ChefMaintenanceResource {

    private final Logger log = LoggerFactory.getLogger(ChefMaintenanceResource.class);

    private static final String ENTITY_NAME = "chefMaintenance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChefMaintenanceRepository chefMaintenanceRepository;

    public ChefMaintenanceResource(ChefMaintenanceRepository chefMaintenanceRepository) {
        this.chefMaintenanceRepository = chefMaintenanceRepository;
    }

    /**
     * {@code POST  /chef-maintenances} : Create a new chefMaintenance.
     *
     * @param chefMaintenance the chefMaintenance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chefMaintenance, or with status {@code 400 (Bad Request)} if the chefMaintenance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chef-maintenances")
    public Mono<ResponseEntity<ChefMaintenance>> createChefMaintenance(@Valid @RequestBody ChefMaintenance chefMaintenance)
        throws URISyntaxException {
        log.debug("REST request to save ChefMaintenance : {}", chefMaintenance);
        if (chefMaintenance.getId() != null) {
            throw new BadRequestAlertException("A new chefMaintenance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return chefMaintenanceRepository
            .save(chefMaintenance)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/chef-maintenances/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /chef-maintenances/:id} : Updates an existing chefMaintenance.
     *
     * @param id the id of the chefMaintenance to save.
     * @param chefMaintenance the chefMaintenance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chefMaintenance,
     * or with status {@code 400 (Bad Request)} if the chefMaintenance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chefMaintenance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chef-maintenances/{id}")
    public Mono<ResponseEntity<ChefMaintenance>> updateChefMaintenance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChefMaintenance chefMaintenance
    ) throws URISyntaxException {
        log.debug("REST request to update ChefMaintenance : {}, {}", id, chefMaintenance);
        if (chefMaintenance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chefMaintenance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return chefMaintenanceRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return chefMaintenanceRepository
                        .save(chefMaintenance)
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
     * {@code PATCH  /chef-maintenances/:id} : Partial updates given fields of an existing chefMaintenance, field will ignore if it is null
     *
     * @param id the id of the chefMaintenance to save.
     * @param chefMaintenance the chefMaintenance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chefMaintenance,
     * or with status {@code 400 (Bad Request)} if the chefMaintenance is not valid,
     * or with status {@code 404 (Not Found)} if the chefMaintenance is not found,
     * or with status {@code 500 (Internal Server Error)} if the chefMaintenance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chef-maintenances/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<ChefMaintenance>> partialUpdateChefMaintenance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChefMaintenance chefMaintenance
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChefMaintenance partially : {}, {}", id, chefMaintenance);
        if (chefMaintenance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chefMaintenance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return chefMaintenanceRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ChefMaintenance> result = chefMaintenanceRepository
                        .findById(chefMaintenance.getId())
                        .map(
                            existingChefMaintenance -> {
                                if (chefMaintenance.getNomPers() != null) {
                                    existingChefMaintenance.setNomPers(chefMaintenance.getNomPers());
                                }
                                if (chefMaintenance.getPrenomPers() != null) {
                                    existingChefMaintenance.setPrenomPers(chefMaintenance.getPrenomPers());
                                }
                                if (chefMaintenance.getSexe() != null) {
                                    existingChefMaintenance.setSexe(chefMaintenance.getSexe());
                                }
                                if (chefMaintenance.getMobile() != null) {
                                    existingChefMaintenance.setMobile(chefMaintenance.getMobile());
                                }
                                if (chefMaintenance.getAdresse() != null) {
                                    existingChefMaintenance.setAdresse(chefMaintenance.getAdresse());
                                }
                                if (chefMaintenance.getDirection() != null) {
                                    existingChefMaintenance.setDirection(chefMaintenance.getDirection());
                                }

                                return existingChefMaintenance;
                            }
                        )
                        .flatMap(chefMaintenanceRepository::save);

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
     * {@code GET  /chef-maintenances} : get all the chefMaintenances.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chefMaintenances in body.
     */
    @GetMapping("/chef-maintenances")
    public Mono<ResponseEntity<List<ChefMaintenance>>> getAllChefMaintenances(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of ChefMaintenances");
        return chefMaintenanceRepository
            .count()
            .zipWith(chefMaintenanceRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /chef-maintenances/:id} : get the "id" chefMaintenance.
     *
     * @param id the id of the chefMaintenance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chefMaintenance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chef-maintenances/{id}")
    public Mono<ResponseEntity<ChefMaintenance>> getChefMaintenance(@PathVariable Long id) {
        log.debug("REST request to get ChefMaintenance : {}", id);
        Mono<ChefMaintenance> chefMaintenance = chefMaintenanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(chefMaintenance);
    }

    /**
     * {@code DELETE  /chef-maintenances/:id} : delete the "id" chefMaintenance.
     *
     * @param id the id of the chefMaintenance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chef-maintenances/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteChefMaintenance(@PathVariable Long id) {
        log.debug("REST request to delete ChefMaintenance : {}", id);
        return chefMaintenanceRepository
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
