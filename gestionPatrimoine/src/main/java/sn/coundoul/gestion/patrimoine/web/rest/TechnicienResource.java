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
import sn.coundoul.gestion.patrimoine.domain.Technicien;
import sn.coundoul.gestion.patrimoine.repository.TechnicienRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Technicien}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TechnicienResource {

    private final Logger log = LoggerFactory.getLogger(TechnicienResource.class);

    private static final String ENTITY_NAME = "technicien";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechnicienRepository technicienRepository;

    public TechnicienResource(TechnicienRepository technicienRepository) {
        this.technicienRepository = technicienRepository;
    }

    /**
     * {@code POST  /techniciens} : Create a new technicien.
     *
     * @param technicien the technicien to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new technicien, or with status {@code 400 (Bad Request)} if the technicien has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/techniciens")
    public Mono<ResponseEntity<Technicien>> createTechnicien(@Valid @RequestBody Technicien technicien) throws URISyntaxException {
        log.debug("REST request to save Technicien : {}", technicien);
        if (technicien.getId() != null) {
            throw new BadRequestAlertException("A new technicien cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return technicienRepository
            .save(technicien)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/techniciens/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /techniciens/:id} : Updates an existing technicien.
     *
     * @param id the id of the technicien to save.
     * @param technicien the technicien to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technicien,
     * or with status {@code 400 (Bad Request)} if the technicien is not valid,
     * or with status {@code 500 (Internal Server Error)} if the technicien couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/techniciens/{id}")
    public Mono<ResponseEntity<Technicien>> updateTechnicien(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Technicien technicien
    ) throws URISyntaxException {
        log.debug("REST request to update Technicien : {}, {}", id, technicien);
        if (technicien.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technicien.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return technicienRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return technicienRepository
                        .save(technicien)
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
     * {@code PATCH  /techniciens/:id} : Partial updates given fields of an existing technicien, field will ignore if it is null
     *
     * @param id the id of the technicien to save.
     * @param technicien the technicien to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technicien,
     * or with status {@code 400 (Bad Request)} if the technicien is not valid,
     * or with status {@code 404 (Not Found)} if the technicien is not found,
     * or with status {@code 500 (Internal Server Error)} if the technicien couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/techniciens/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Technicien>> partialUpdateTechnicien(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Technicien technicien
    ) throws URISyntaxException {
        log.debug("REST request to partial update Technicien partially : {}, {}", id, technicien);
        if (technicien.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technicien.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return technicienRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Technicien> result = technicienRepository
                        .findById(technicien.getId())
                        .map(
                            existingTechnicien -> {
                                if (technicien.getNomPers() != null) {
                                    existingTechnicien.setNomPers(technicien.getNomPers());
                                }
                                if (technicien.getPrenomPers() != null) {
                                    existingTechnicien.setPrenomPers(technicien.getPrenomPers());
                                }
                                if (technicien.getSexe() != null) {
                                    existingTechnicien.setSexe(technicien.getSexe());
                                }
                                if (technicien.getMobile() != null) {
                                    existingTechnicien.setMobile(technicien.getMobile());
                                }
                                if (technicien.getAdresse() != null) {
                                    existingTechnicien.setAdresse(technicien.getAdresse());
                                }
                                if (technicien.getDirection() != null) {
                                    existingTechnicien.setDirection(technicien.getDirection());
                                }

                                return existingTechnicien;
                            }
                        )
                        .flatMap(technicienRepository::save);

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
     * {@code GET  /techniciens} : get all the techniciens.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of techniciens in body.
     */
    @GetMapping("/techniciens")
    public Mono<ResponseEntity<List<Technicien>>> getAllTechniciens(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Techniciens");
        return technicienRepository
            .count()
            .zipWith(technicienRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /techniciens/:id} : get the "id" technicien.
     *
     * @param id the id of the technicien to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the technicien, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/techniciens/{id}")
    public Mono<ResponseEntity<Technicien>> getTechnicien(@PathVariable Long id) {
        log.debug("REST request to get Technicien : {}", id);
        Mono<Technicien> technicien = technicienRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(technicien);
    }

    /**
     * {@code DELETE  /techniciens/:id} : delete the "id" technicien.
     *
     * @param id the id of the technicien to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/techniciens/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTechnicien(@PathVariable Long id) {
        log.debug("REST request to delete Technicien : {}", id);
        return technicienRepository
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
