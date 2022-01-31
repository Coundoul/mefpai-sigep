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
import sn.coundoul.gestion.patrimoine.domain.Fournisseur;
import sn.coundoul.gestion.patrimoine.repository.FournisseurRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.Fournisseur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FournisseurResource {

    private final Logger log = LoggerFactory.getLogger(FournisseurResource.class);

    private static final String ENTITY_NAME = "fournisseur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FournisseurRepository fournisseurRepository;

    public FournisseurResource(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    /**
     * {@code POST  /fournisseurs} : Create a new fournisseur.
     *
     * @param fournisseur the fournisseur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fournisseur, or with status {@code 400 (Bad Request)} if the fournisseur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fournisseurs")
    public Mono<ResponseEntity<Fournisseur>> createFournisseur(@Valid @RequestBody Fournisseur fournisseur) throws URISyntaxException {
        log.debug("REST request to save Fournisseur : {}", fournisseur);
        if (fournisseur.getId() != null) {
            throw new BadRequestAlertException("A new fournisseur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return fournisseurRepository
            .save(fournisseur)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/fournisseurs/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /fournisseurs/:id} : Updates an existing fournisseur.
     *
     * @param id the id of the fournisseur to save.
     * @param fournisseur the fournisseur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fournisseur,
     * or with status {@code 400 (Bad Request)} if the fournisseur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fournisseur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fournisseurs/{id}")
    public Mono<ResponseEntity<Fournisseur>> updateFournisseur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Fournisseur fournisseur
    ) throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}, {}", id, fournisseur);
        if (fournisseur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fournisseur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fournisseurRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return fournisseurRepository
                        .save(fournisseur)
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
     * {@code PATCH  /fournisseurs/:id} : Partial updates given fields of an existing fournisseur, field will ignore if it is null
     *
     * @param id the id of the fournisseur to save.
     * @param fournisseur the fournisseur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fournisseur,
     * or with status {@code 400 (Bad Request)} if the fournisseur is not valid,
     * or with status {@code 404 (Not Found)} if the fournisseur is not found,
     * or with status {@code 500 (Internal Server Error)} if the fournisseur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fournisseurs/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Fournisseur>> partialUpdateFournisseur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Fournisseur fournisseur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fournisseur partially : {}, {}", id, fournisseur);
        if (fournisseur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fournisseur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fournisseurRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Fournisseur> result = fournisseurRepository
                        .findById(fournisseur.getId())
                        .map(
                            existingFournisseur -> {
                                if (fournisseur.getCodeFournisseuer() != null) {
                                    existingFournisseur.setCodeFournisseuer(fournisseur.getCodeFournisseuer());
                                }
                                if (fournisseur.getNomFournisseur() != null) {
                                    existingFournisseur.setNomFournisseur(fournisseur.getNomFournisseur());
                                }
                                if (fournisseur.getPrenomFournisseur() != null) {
                                    existingFournisseur.setPrenomFournisseur(fournisseur.getPrenomFournisseur());
                                }
                                if (fournisseur.getSexe() != null) {
                                    existingFournisseur.setSexe(fournisseur.getSexe());
                                }
                                if (fournisseur.getRaisonSocial() != null) {
                                    existingFournisseur.setRaisonSocial(fournisseur.getRaisonSocial());
                                }
                                if (fournisseur.getAdresse() != null) {
                                    existingFournisseur.setAdresse(fournisseur.getAdresse());
                                }
                                if (fournisseur.getNum1() != null) {
                                    existingFournisseur.setNum1(fournisseur.getNum1());
                                }
                                if (fournisseur.getNum2() != null) {
                                    existingFournisseur.setNum2(fournisseur.getNum2());
                                }
                                if (fournisseur.getVille() != null) {
                                    existingFournisseur.setVille(fournisseur.getVille());
                                }
                                if (fournisseur.getEmail() != null) {
                                    existingFournisseur.setEmail(fournisseur.getEmail());
                                }

                                return existingFournisseur;
                            }
                        )
                        .flatMap(fournisseurRepository::save);

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
     * {@code GET  /fournisseurs} : get all the fournisseurs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fournisseurs in body.
     */
    @GetMapping("/fournisseurs")
    public Mono<ResponseEntity<List<Fournisseur>>> getAllFournisseurs(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Fournisseurs");
        return fournisseurRepository
            .count()
            .zipWith(fournisseurRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /fournisseurs/:id} : get the "id" fournisseur.
     *
     * @param id the id of the fournisseur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fournisseur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fournisseurs/{id}")
    public Mono<ResponseEntity<Fournisseur>> getFournisseur(@PathVariable Long id) {
        log.debug("REST request to get Fournisseur : {}", id);
        Mono<Fournisseur> fournisseur = fournisseurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fournisseur);
    }

    /**
     * {@code DELETE  /fournisseurs/:id} : delete the "id" fournisseur.
     *
     * @param id the id of the fournisseur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fournisseurs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFournisseur(@PathVariable Long id) {
        log.debug("REST request to delete Fournisseur : {}", id);
        return fournisseurRepository
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
