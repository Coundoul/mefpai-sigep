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
import sn.coundoul.gestion.patrimoine.domain.Formateurs;
import sn.coundoul.gestion.patrimoine.repository.FormateursRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Formateurs}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FormateursResource {

    private final Logger log = LoggerFactory.getLogger(FormateursResource.class);

    private static final String ENTITY_NAME = "formateurs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormateursRepository formateursRepository;

    public FormateursResource(FormateursRepository formateursRepository) {
        this.formateursRepository = formateursRepository;
    }

    /**
     * {@code POST  /formateurs} : Create a new formateurs.
     *
     * @param formateurs the formateurs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formateurs, or with status {@code 400 (Bad Request)} if the formateurs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formateurs")
    public Mono<ResponseEntity<Formateurs>> createFormateurs(@Valid @RequestBody Formateurs formateurs) throws URISyntaxException {
        log.debug("REST request to save Formateurs : {}", formateurs);
        if (formateurs.getId() != null) {
            throw new BadRequestAlertException("A new formateurs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return formateursRepository
            .save(formateurs)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/formateurs/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /formateurs/:id} : Updates an existing formateurs.
     *
     * @param id the id of the formateurs to save.
     * @param formateurs the formateurs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formateurs,
     * or with status {@code 400 (Bad Request)} if the formateurs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formateurs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formateurs/{id}")
    public Mono<ResponseEntity<Formateurs>> updateFormateurs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Formateurs formateurs
    ) throws URISyntaxException {
        log.debug("REST request to update Formateurs : {}, {}", id, formateurs);
        if (formateurs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formateurs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return formateursRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return formateursRepository
                        .save(formateurs)
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
     * {@code PATCH  /formateurs/:id} : Partial updates given fields of an existing formateurs, field will ignore if it is null
     *
     * @param id the id of the formateurs to save.
     * @param formateurs the formateurs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formateurs,
     * or with status {@code 400 (Bad Request)} if the formateurs is not valid,
     * or with status {@code 404 (Not Found)} if the formateurs is not found,
     * or with status {@code 500 (Internal Server Error)} if the formateurs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/formateurs/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Formateurs>> partialUpdateFormateurs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Formateurs formateurs
    ) throws URISyntaxException {
        log.debug("REST request to partial update Formateurs partially : {}, {}", id, formateurs);
        if (formateurs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formateurs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return formateursRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Formateurs> result = formateursRepository
                        .findById(formateurs.getId())
                        .map(
                            existingFormateurs -> {
                                if (formateurs.getNomFormateur() != null) {
                                    existingFormateurs.setNomFormateur(formateurs.getNomFormateur());
                                }
                                if (formateurs.getPrenomFormateur() != null) {
                                    existingFormateurs.setPrenomFormateur(formateurs.getPrenomFormateur());
                                }
                                if (formateurs.getEmail() != null) {
                                    existingFormateurs.setEmail(formateurs.getEmail());
                                }
                                if (formateurs.getNumb1() != null) {
                                    existingFormateurs.setNumb1(formateurs.getNumb1());
                                }
                                if (formateurs.getNumb2() != null) {
                                    existingFormateurs.setNumb2(formateurs.getNumb2());
                                }
                                if (formateurs.getAdresse() != null) {
                                    existingFormateurs.setAdresse(formateurs.getAdresse());
                                }
                                if (formateurs.getVille() != null) {
                                    existingFormateurs.setVille(formateurs.getVille());
                                }
                                if (formateurs.getSpecialite() != null) {
                                    existingFormateurs.setSpecialite(formateurs.getSpecialite());
                                }

                                return existingFormateurs;
                            }
                        )
                        .flatMap(formateursRepository::save);

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
     * {@code GET  /formateurs} : get all the formateurs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formateurs in body.
     */
    @GetMapping("/formateurs")
    public Mono<ResponseEntity<List<Formateurs>>> getAllFormateurs(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Formateurs");
        return formateursRepository
            .count()
            .zipWith(formateursRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /formateurs/:id} : get the "id" formateurs.
     *
     * @param id the id of the formateurs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formateurs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formateurs/{id}")
    public Mono<ResponseEntity<Formateurs>> getFormateurs(@PathVariable Long id) {
        log.debug("REST request to get Formateurs : {}", id);
        Mono<Formateurs> formateurs = formateursRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(formateurs);
    }

    /**
     * {@code DELETE  /formateurs/:id} : delete the "id" formateurs.
     *
     * @param id the id of the formateurs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formateurs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFormateurs(@PathVariable Long id) {
        log.debug("REST request to delete Formateurs : {}", id);
        return formateursRepository
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
