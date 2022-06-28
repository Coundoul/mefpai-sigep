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
import sn.coundoul.gestion.patrimoine.domain.ContratProjet;
import sn.coundoul.gestion.patrimoine.repository.ContratProjetRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.ContratProjet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContratProjetResource {

    private final Logger log = LoggerFactory.getLogger(ContratProjetResource.class);

    private static final String ENTITY_NAME = "contratProjet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContratProjetRepository contratProjetRepository;

    public ContratProjetResource(ContratProjetRepository contratProjetRepository) {
        this.contratProjetRepository = contratProjetRepository;
    }

    /**
     * {@code POST  /contrat-projets} : Create a new contratProjet.
     *
     * @param contratProjet the contratProjet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contratProjet, or with status {@code 400 (Bad Request)} if the contratProjet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contrat-projets")
    public Mono<ResponseEntity<ContratProjet>> createContratProjet(@Valid @RequestBody ContratProjet contratProjet)
        throws URISyntaxException {
        log.debug("REST request to save ContratProjet : {}", contratProjet);
        if (contratProjet.getId() != null) {
            throw new BadRequestAlertException("A new contratProjet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return contratProjetRepository
            .save(contratProjet)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/contrat-projets/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /contrat-projets/:id} : Updates an existing contratProjet.
     *
     * @param id the id of the contratProjet to save.
     * @param contratProjet the contratProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contratProjet,
     * or with status {@code 400 (Bad Request)} if the contratProjet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contratProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contrat-projets/{id}")
    public Mono<ResponseEntity<ContratProjet>> updateContratProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContratProjet contratProjet
    ) throws URISyntaxException {
        log.debug("REST request to update ContratProjet : {}, {}", id, contratProjet);
        if (contratProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contratProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return contratProjetRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return contratProjetRepository
                        .save(contratProjet)
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
     * {@code PATCH  /contrat-projets/:id} : Partial updates given fields of an existing contratProjet, field will ignore if it is null
     *
     * @param id the id of the contratProjet to save.
     * @param contratProjet the contratProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contratProjet,
     * or with status {@code 400 (Bad Request)} if the contratProjet is not valid,
     * or with status {@code 404 (Not Found)} if the contratProjet is not found,
     * or with status {@code 500 (Internal Server Error)} if the contratProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contrat-projets/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<ContratProjet>> partialUpdateContratProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContratProjet contratProjet
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContratProjet partially : {}, {}", id, contratProjet);
        if (contratProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contratProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return contratProjetRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ContratProjet> result = contratProjetRepository
                        .findById(contratProjet.getId())
                        .map(
                            existingContratProjet -> {
                                if (contratProjet.getNom() != null) {
                                    existingContratProjet.setNom(contratProjet.getNom());
                                }

                                return existingContratProjet;
                            }
                        )
                        .flatMap(contratProjetRepository::save);

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
     * {@code GET  /contrat-projets} : get all the contratProjets.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contratProjets in body.
     */
    @GetMapping("/contrat-projets")
    public Mono<ResponseEntity<List<ContratProjet>>> getAllContratProjets(
        Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {
        if ("nomprojet-is-null".equals(filter)) {
            log.debug("REST request to get all ContratProjets where nomProjet is null");
            return contratProjetRepository.findAllWhereNomProjetIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of ContratProjets");
        return contratProjetRepository
            .count()
            .zipWith(contratProjetRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /contrat-projets/:id} : get the "id" contratProjet.
     *
     * @param id the id of the contratProjet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contratProjet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contrat-projets/{id}")
    public Mono<ResponseEntity<ContratProjet>> getContratProjet(@PathVariable Long id) {
        log.debug("REST request to get ContratProjet : {}", id);
        Mono<ContratProjet> contratProjet = contratProjetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contratProjet);
    }

    /**
     * {@code DELETE  /contrat-projets/:id} : delete the "id" contratProjet.
     *
     * @param id the id of the contratProjet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contrat-projets/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteContratProjet(@PathVariable Long id) {
        log.debug("REST request to delete ContratProjet : {}", id);
        return contratProjetRepository
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
