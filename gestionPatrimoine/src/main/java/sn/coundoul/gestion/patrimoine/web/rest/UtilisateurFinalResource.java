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
import sn.coundoul.gestion.patrimoine.domain.UtilisateurFinal;
import sn.coundoul.gestion.patrimoine.repository.UtilisateurFinalRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.UtilisateurFinal}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UtilisateurFinalResource {

    private final Logger log = LoggerFactory.getLogger(UtilisateurFinalResource.class);

    private static final String ENTITY_NAME = "utilisateurFinal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtilisateurFinalRepository utilisateurFinalRepository;

    public UtilisateurFinalResource(UtilisateurFinalRepository utilisateurFinalRepository) {
        this.utilisateurFinalRepository = utilisateurFinalRepository;
    }

    /**
     * {@code POST  /utilisateur-finals} : Create a new utilisateurFinal.
     *
     * @param utilisateurFinal the utilisateurFinal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utilisateurFinal, or with status {@code 400 (Bad Request)} if the utilisateurFinal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/utilisateur-finals")
    public Mono<ResponseEntity<UtilisateurFinal>> createUtilisateurFinal(@Valid @RequestBody UtilisateurFinal utilisateurFinal)
        throws URISyntaxException {
        log.debug("REST request to save UtilisateurFinal : {}", utilisateurFinal);
        if (utilisateurFinal.getId() != null) {
            throw new BadRequestAlertException("A new utilisateurFinal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return utilisateurFinalRepository
            .save(utilisateurFinal)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/utilisateur-finals/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /utilisateur-finals/:id} : Updates an existing utilisateurFinal.
     *
     * @param id the id of the utilisateurFinal to save.
     * @param utilisateurFinal the utilisateurFinal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisateurFinal,
     * or with status {@code 400 (Bad Request)} if the utilisateurFinal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utilisateurFinal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/utilisateur-finals/{id}")
    public Mono<ResponseEntity<UtilisateurFinal>> updateUtilisateurFinal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UtilisateurFinal utilisateurFinal
    ) throws URISyntaxException {
        log.debug("REST request to update UtilisateurFinal : {}, {}", id, utilisateurFinal);
        if (utilisateurFinal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateurFinal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return utilisateurFinalRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return utilisateurFinalRepository
                        .save(utilisateurFinal)
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
     * {@code PATCH  /utilisateur-finals/:id} : Partial updates given fields of an existing utilisateurFinal, field will ignore if it is null
     *
     * @param id the id of the utilisateurFinal to save.
     * @param utilisateurFinal the utilisateurFinal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisateurFinal,
     * or with status {@code 400 (Bad Request)} if the utilisateurFinal is not valid,
     * or with status {@code 404 (Not Found)} if the utilisateurFinal is not found,
     * or with status {@code 500 (Internal Server Error)} if the utilisateurFinal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/utilisateur-finals/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<UtilisateurFinal>> partialUpdateUtilisateurFinal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UtilisateurFinal utilisateurFinal
    ) throws URISyntaxException {
        log.debug("REST request to partial update UtilisateurFinal partially : {}, {}", id, utilisateurFinal);
        if (utilisateurFinal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateurFinal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return utilisateurFinalRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<UtilisateurFinal> result = utilisateurFinalRepository
                        .findById(utilisateurFinal.getId())
                        .map(
                            existingUtilisateurFinal -> {
                                if (utilisateurFinal.getNomUtilisateur() != null) {
                                    existingUtilisateurFinal.setNomUtilisateur(utilisateurFinal.getNomUtilisateur());
                                }
                                if (utilisateurFinal.getPrenomUtilisateur() != null) {
                                    existingUtilisateurFinal.setPrenomUtilisateur(utilisateurFinal.getPrenomUtilisateur());
                                }
                                if (utilisateurFinal.getEmailInstitutionnel() != null) {
                                    existingUtilisateurFinal.setEmailInstitutionnel(utilisateurFinal.getEmailInstitutionnel());
                                }
                                if (utilisateurFinal.getMobile() != null) {
                                    existingUtilisateurFinal.setMobile(utilisateurFinal.getMobile());
                                }
                                if (utilisateurFinal.getSexe() != null) {
                                    existingUtilisateurFinal.setSexe(utilisateurFinal.getSexe());
                                }
                                if (utilisateurFinal.getDepartement() != null) {
                                    existingUtilisateurFinal.setDepartement(utilisateurFinal.getDepartement());
                                }
                                if (utilisateurFinal.getServiceDep() != null) {
                                    existingUtilisateurFinal.setServiceDep(utilisateurFinal.getServiceDep());
                                }

                                return existingUtilisateurFinal;
                            }
                        )
                        .flatMap(utilisateurFinalRepository::save);

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
     * {@code GET  /utilisateur-finals} : get all the utilisateurFinals.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utilisateurFinals in body.
     */
    @GetMapping("/utilisateur-finals")
    public Mono<ResponseEntity<List<UtilisateurFinal>>> getAllUtilisateurFinals(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of UtilisateurFinals");
        return utilisateurFinalRepository
            .count()
            .zipWith(utilisateurFinalRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /utilisateur-finals/:id} : get the "id" utilisateurFinal.
     *
     * @param id the id of the utilisateurFinal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utilisateurFinal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/utilisateur-finals/{id}")
    public Mono<ResponseEntity<UtilisateurFinal>> getUtilisateurFinal(@PathVariable Long id) {
        log.debug("REST request to get UtilisateurFinal : {}", id);
        Mono<UtilisateurFinal> utilisateurFinal = utilisateurFinalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(utilisateurFinal);
    }

    /**
     * {@code DELETE  /utilisateur-finals/:id} : delete the "id" utilisateurFinal.
     *
     * @param id the id of the utilisateurFinal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/utilisateur-finals/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteUtilisateurFinal(@PathVariable Long id) {
        log.debug("REST request to delete UtilisateurFinal : {}", id);
        return utilisateurFinalRepository
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
