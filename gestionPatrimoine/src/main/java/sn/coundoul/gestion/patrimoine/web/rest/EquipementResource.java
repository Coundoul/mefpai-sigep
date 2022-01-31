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
import sn.coundoul.gestion.patrimoine.domain.Equipement;
import sn.coundoul.gestion.patrimoine.repository.EquipementRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Equipement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EquipementResource {

    private final Logger log = LoggerFactory.getLogger(EquipementResource.class);

    private static final String ENTITY_NAME = "equipement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipementRepository equipementRepository;

    public EquipementResource(EquipementRepository equipementRepository) {
        this.equipementRepository = equipementRepository;
    }

    /**
     * {@code POST  /equipements} : Create a new equipement.
     *
     * @param equipement the equipement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipement, or with status {@code 400 (Bad Request)} if the equipement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipements")
    public Mono<ResponseEntity<Equipement>> createEquipement(@Valid @RequestBody Equipement equipement) throws URISyntaxException {
        log.debug("REST request to save Equipement : {}", equipement);
        if (equipement.getId() != null) {
            throw new BadRequestAlertException("A new equipement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return equipementRepository
            .save(equipement)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/equipements/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /equipements/:id} : Updates an existing equipement.
     *
     * @param id the id of the equipement to save.
     * @param equipement the equipement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipement,
     * or with status {@code 400 (Bad Request)} if the equipement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipements/{id}")
    public Mono<ResponseEntity<Equipement>> updateEquipement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Equipement equipement
    ) throws URISyntaxException {
        log.debug("REST request to update Equipement : {}, {}", id, equipement);
        if (equipement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return equipementRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return equipementRepository
                        .save(equipement)
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
     * {@code PATCH  /equipements/:id} : Partial updates given fields of an existing equipement, field will ignore if it is null
     *
     * @param id the id of the equipement to save.
     * @param equipement the equipement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipement,
     * or with status {@code 400 (Bad Request)} if the equipement is not valid,
     * or with status {@code 404 (Not Found)} if the equipement is not found,
     * or with status {@code 500 (Internal Server Error)} if the equipement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/equipements/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Equipement>> partialUpdateEquipement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Equipement equipement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Equipement partially : {}, {}", id, equipement);
        if (equipement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return equipementRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Equipement> result = equipementRepository
                        .findById(equipement.getId())
                        .map(
                            existingEquipement -> {
                                if (equipement.getReference() != null) {
                                    existingEquipement.setReference(equipement.getReference());
                                }
                                if (equipement.getDescription() != null) {
                                    existingEquipement.setDescription(equipement.getDescription());
                                }
                                if (equipement.getPrixUnitaire() != null) {
                                    existingEquipement.setPrixUnitaire(equipement.getPrixUnitaire());
                                }
                                if (equipement.getTypeMatiere() != null) {
                                    existingEquipement.setTypeMatiere(equipement.getTypeMatiere());
                                }
                                if (equipement.getQuantite() != null) {
                                    existingEquipement.setQuantite(equipement.getQuantite());
                                }
                                if (equipement.getEtatMatiere() != null) {
                                    existingEquipement.setEtatMatiere(equipement.getEtatMatiere());
                                }
                                if (equipement.getGroup() != null) {
                                    existingEquipement.setGroup(equipement.getGroup());
                                }
                                if (equipement.getPhoto() != null) {
                                    existingEquipement.setPhoto(equipement.getPhoto());
                                }
                                if (equipement.getPhotoContentType() != null) {
                                    existingEquipement.setPhotoContentType(equipement.getPhotoContentType());
                                }

                                return existingEquipement;
                            }
                        )
                        .flatMap(equipementRepository::save);

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
     * {@code GET  /equipements} : get all the equipements.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipements in body.
     */
    @GetMapping("/equipements")
    public Mono<ResponseEntity<List<Equipement>>> getAllEquipements(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Equipements");
        return equipementRepository
            .count()
            .zipWith(equipementRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /equipements/:id} : get the "id" equipement.
     *
     * @param id the id of the equipement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipements/{id}")
    public Mono<ResponseEntity<Equipement>> getEquipement(@PathVariable Long id) {
        log.debug("REST request to get Equipement : {}", id);
        Mono<Equipement> equipement = equipementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(equipement);
    }

    /**
     * {@code DELETE  /equipements/:id} : delete the "id" equipement.
     *
     * @param id the id of the equipement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipements/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteEquipement(@PathVariable Long id) {
        log.debug("REST request to delete Equipement : {}", id);
        return equipementRepository
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
