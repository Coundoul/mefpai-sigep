package sn.coundoul.gestion.infrastructure.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.coundoul.gestion.infrastructure.domain.Etapes;
import sn.coundoul.gestion.infrastructure.repository.EtapesRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.Etapes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EtapesResource {

    private final Logger log = LoggerFactory.getLogger(EtapesResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureEtapes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtapesRepository etapesRepository;

    public EtapesResource(EtapesRepository etapesRepository) {
        this.etapesRepository = etapesRepository;
    }

    /**
     * {@code POST  /etapes} : Create a new etapes.
     *
     * @param etapes the etapes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etapes, or with status {@code 400 (Bad Request)} if the etapes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etapes")
    public ResponseEntity<Etapes> createEtapes(@Valid @RequestBody Etapes etapes) throws URISyntaxException {
        log.debug("REST request to save Etapes : {}", etapes);
        if (etapes.getId() != null) {
            throw new BadRequestAlertException("A new etapes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Etapes result = etapesRepository.save(etapes);
        return ResponseEntity
            .created(new URI("/api/etapes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etapes/:id} : Updates an existing etapes.
     *
     * @param id the id of the etapes to save.
     * @param etapes the etapes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etapes,
     * or with status {@code 400 (Bad Request)} if the etapes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etapes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etapes/{id}")
    public ResponseEntity<Etapes> updateEtapes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Etapes etapes
    ) throws URISyntaxException {
        log.debug("REST request to update Etapes : {}, {}", id, etapes);
        if (etapes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etapes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etapesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Etapes result = etapesRepository.save(etapes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etapes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /etapes/:id} : Partial updates given fields of an existing etapes, field will ignore if it is null
     *
     * @param id the id of the etapes to save.
     * @param etapes the etapes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etapes,
     * or with status {@code 400 (Bad Request)} if the etapes is not valid,
     * or with status {@code 404 (Not Found)} if the etapes is not found,
     * or with status {@code 500 (Internal Server Error)} if the etapes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/etapes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Etapes> partialUpdateEtapes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Etapes etapes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Etapes partially : {}, {}", id, etapes);
        if (etapes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etapes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etapesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Etapes> result = etapesRepository
            .findById(etapes.getId())
            .map(
                existingEtapes -> {
                    if (etapes.getDateDebut() != null) {
                        existingEtapes.setDateDebut(etapes.getDateDebut());
                    }
                    if (etapes.getDateFin() != null) {
                        existingEtapes.setDateFin(etapes.getDateFin());
                    }
                    if (etapes.getNomTache() != null) {
                        existingEtapes.setNomTache(etapes.getNomTache());
                    }
                    if (etapes.getDuration() != null) {
                        existingEtapes.setDuration(etapes.getDuration());
                    }

                    return existingEtapes;
                }
            )
            .map(etapesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etapes.getId().toString())
        );
    }

    /**
     * {@code GET  /etapes} : get all the etapes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etapes in body.
     */
    @GetMapping("/etapes")
    public ResponseEntity<List<Etapes>> getAllEtapes(Pageable pageable) {
        log.debug("REST request to get a page of Etapes");
        Page<Etapes> page = etapesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/etapes/projets/{id}")
    public ResponseEntity<List<Etapes>> getAllEtapesProjet(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get a page of Batiments");
        Page<Etapes> page = etapesRepository.findAllEtapesProjet(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }   

    /**
     * {@code GET  /etapes/:id} : get the "id" etapes.
     *
     * @param id the id of the etapes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etapes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etapes/{id}")
    public ResponseEntity<Etapes> getEtapes(@PathVariable Long id) {
        log.debug("REST request to get Etapes : {}", id);
        Optional<Etapes> etapes = etapesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(etapes);
    }

    /**
     * {@code DELETE  /etapes/:id} : delete the "id" etapes.
     *
     * @param id the id of the etapes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etapes/{id}")
    public ResponseEntity<Void> deleteEtapes(@PathVariable Long id) {
        log.debug("REST request to delete Etapes : {}", id);
        etapesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
