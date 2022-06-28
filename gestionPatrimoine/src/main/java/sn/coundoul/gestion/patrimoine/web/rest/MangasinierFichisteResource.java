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
import sn.coundoul.gestion.patrimoine.domain.MangasinierFichiste;
import sn.coundoul.gestion.patrimoine.repository.MangasinierFichisteRepository;
import sn.coundoul.gestion.patrimoine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.patrimoine.domain.MangasinierFichiste}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MangasinierFichisteResource {

    private final Logger log = LoggerFactory.getLogger(MangasinierFichisteResource.class);

    private static final String ENTITY_NAME = "mangasinierFichiste";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MangasinierFichisteRepository mangasinierFichisteRepository;

    public MangasinierFichisteResource(MangasinierFichisteRepository mangasinierFichisteRepository) {
        this.mangasinierFichisteRepository = mangasinierFichisteRepository;
    }

    /**
     * {@code POST  /mangasinier-fichistes} : Create a new mangasinierFichiste.
     *
     * @param mangasinierFichiste the mangasinierFichiste to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mangasinierFichiste, or with status {@code 400 (Bad Request)} if the mangasinierFichiste has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mangasinier-fichistes")
    public Mono<ResponseEntity<MangasinierFichiste>> createMangasinierFichiste(@Valid @RequestBody MangasinierFichiste mangasinierFichiste)
        throws URISyntaxException {
        log.debug("REST request to save MangasinierFichiste : {}", mangasinierFichiste);
        if (mangasinierFichiste.getId() != null) {
            throw new BadRequestAlertException("A new mangasinierFichiste cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return mangasinierFichisteRepository
            .save(mangasinierFichiste)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/mangasinier-fichistes/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /mangasinier-fichistes/:id} : Updates an existing mangasinierFichiste.
     *
     * @param id the id of the mangasinierFichiste to save.
     * @param mangasinierFichiste the mangasinierFichiste to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mangasinierFichiste,
     * or with status {@code 400 (Bad Request)} if the mangasinierFichiste is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mangasinierFichiste couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mangasinier-fichistes/{id}")
    public Mono<ResponseEntity<MangasinierFichiste>> updateMangasinierFichiste(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MangasinierFichiste mangasinierFichiste
    ) throws URISyntaxException {
        log.debug("REST request to update MangasinierFichiste : {}, {}", id, mangasinierFichiste);
        if (mangasinierFichiste.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mangasinierFichiste.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return mangasinierFichisteRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return mangasinierFichisteRepository
                        .save(mangasinierFichiste)
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
     * {@code PATCH  /mangasinier-fichistes/:id} : Partial updates given fields of an existing mangasinierFichiste, field will ignore if it is null
     *
     * @param id the id of the mangasinierFichiste to save.
     * @param mangasinierFichiste the mangasinierFichiste to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mangasinierFichiste,
     * or with status {@code 400 (Bad Request)} if the mangasinierFichiste is not valid,
     * or with status {@code 404 (Not Found)} if the mangasinierFichiste is not found,
     * or with status {@code 500 (Internal Server Error)} if the mangasinierFichiste couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mangasinier-fichistes/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<MangasinierFichiste>> partialUpdateMangasinierFichiste(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MangasinierFichiste mangasinierFichiste
    ) throws URISyntaxException {
        log.debug("REST request to partial update MangasinierFichiste partially : {}, {}", id, mangasinierFichiste);
        if (mangasinierFichiste.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mangasinierFichiste.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return mangasinierFichisteRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<MangasinierFichiste> result = mangasinierFichisteRepository
                        .findById(mangasinierFichiste.getId())
                        .map(
                            existingMangasinierFichiste -> {
                                if (mangasinierFichiste.getNomPers() != null) {
                                    existingMangasinierFichiste.setNomPers(mangasinierFichiste.getNomPers());
                                }
                                if (mangasinierFichiste.getPrenomPers() != null) {
                                    existingMangasinierFichiste.setPrenomPers(mangasinierFichiste.getPrenomPers());
                                }
                                if (mangasinierFichiste.getSexe() != null) {
                                    existingMangasinierFichiste.setSexe(mangasinierFichiste.getSexe());
                                }
                                if (mangasinierFichiste.getMobile() != null) {
                                    existingMangasinierFichiste.setMobile(mangasinierFichiste.getMobile());
                                }
                                if (mangasinierFichiste.getAdresse() != null) {
                                    existingMangasinierFichiste.setAdresse(mangasinierFichiste.getAdresse());
                                }
                                if (mangasinierFichiste.getDirection() != null) {
                                    existingMangasinierFichiste.setDirection(mangasinierFichiste.getDirection());
                                }

                                return existingMangasinierFichiste;
                            }
                        )
                        .flatMap(mangasinierFichisteRepository::save);

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
     * {@code GET  /mangasinier-fichistes} : get all the mangasinierFichistes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mangasinierFichistes in body.
     */
    @GetMapping("/mangasinier-fichistes")
    public Mono<ResponseEntity<List<MangasinierFichiste>>> getAllMangasinierFichistes(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of MangasinierFichistes");
        return mangasinierFichisteRepository
            .count()
            .zipWith(mangasinierFichisteRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /mangasinier-fichistes/:id} : get the "id" mangasinierFichiste.
     *
     * @param id the id of the mangasinierFichiste to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mangasinierFichiste, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mangasinier-fichistes/{id}")
    public Mono<ResponseEntity<MangasinierFichiste>> getMangasinierFichiste(@PathVariable Long id) {
        log.debug("REST request to get MangasinierFichiste : {}", id);
        Mono<MangasinierFichiste> mangasinierFichiste = mangasinierFichisteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mangasinierFichiste);
    }

    /**
     * {@code DELETE  /mangasinier-fichistes/:id} : delete the "id" mangasinierFichiste.
     *
     * @param id the id of the mangasinierFichiste to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mangasinier-fichistes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteMangasinierFichiste(@PathVariable Long id) {
        log.debug("REST request to delete MangasinierFichiste : {}", id);
        return mangasinierFichisteRepository
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
