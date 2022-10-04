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
import sn.coundoul.gestion.infrastructure.domain.Batiment;
import sn.coundoul.gestion.infrastructure.repository.BatimentRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.Batiment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BatimentResource {

    private final Logger log = LoggerFactory.getLogger(BatimentResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureBatiment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BatimentRepository batimentRepository;

    public BatimentResource(BatimentRepository batimentRepository) {
        this.batimentRepository = batimentRepository;
    }

    /**
     * {@code POST  /batiments} : Create a new batiment.
     *
     * @param batiment the batiment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new batiment, or with status {@code 400 (Bad Request)} if the batiment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/batiments")
    public ResponseEntity<Batiment> createBatiment(@Valid @RequestBody Batiment batiment) throws URISyntaxException {
        log.debug("REST request to save Batiment : {}", batiment);
        if (batiment.getId() != null) {
            throw new BadRequestAlertException("A new batiment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Batiment result = batimentRepository.save(batiment);
        return ResponseEntity
            .created(new URI("/api/batiments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /batiments/:id} : Updates an existing batiment.
     *
     * @param id the id of the batiment to save.
     * @param batiment the batiment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batiment,
     * or with status {@code 400 (Bad Request)} if the batiment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the batiment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/batiments/{id}")
    public ResponseEntity<Batiment> updateBatiment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Batiment batiment
    ) throws URISyntaxException {
        log.debug("REST request to update Batiment : {}, {}", id, batiment);
        if (batiment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, batiment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!batimentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Batiment result = batimentRepository.save(batiment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, batiment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /batiments/:id} : Partial updates given fields of an existing batiment, field will ignore if it is null
     *
     * @param id the id of the batiment to save.
     * @param batiment the batiment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batiment,
     * or with status {@code 400 (Bad Request)} if the batiment is not valid,
     * or with status {@code 404 (Not Found)} if the batiment is not found,
     * or with status {@code 500 (Internal Server Error)} if the batiment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/batiments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Batiment> partialUpdateBatiment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Batiment batiment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Batiment partially : {}, {}", id, batiment);
        if (batiment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, batiment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!batimentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Batiment> result = batimentRepository
            .findById(batiment.getId())
            .map(
                existingBatiment -> {
                    if (batiment.getDesignation() != null) {
                        existingBatiment.setDesignation(batiment.getDesignation());
                    }
                    if (batiment.getNbrPiece() != null) {
                        existingBatiment.setNbrPiece(batiment.getNbrPiece());
                    }
                    if (batiment.getSurface() != null) {
                        existingBatiment.setSurface(batiment.getSurface());
                    }
                    if (batiment.getSourceFinancement() != null) {
                        existingBatiment.setSourceFinancement(batiment.getSourceFinancement());
                    }
                    if (batiment.getPhoto() != null) {
                        existingBatiment.setPhoto(batiment.getPhoto());
                    }
                    if (batiment.getEtatGrosOeuvre() != null) {
                        existingBatiment.setEtatGrosOeuvre(batiment.getEtatGrosOeuvre());
                    }
                    if (batiment.getEtatSecondOeuvre() != null) {
                        existingBatiment.setEtatSecondOeuvre(batiment.getEtatSecondOeuvre());
                    }
                    if (batiment.getObservation() != null) {
                        existingBatiment.setObservation(batiment.getObservation());
                    }

                    return existingBatiment;
                }
            )
            .map(batimentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, batiment.getId().toString())
        );
    }

    /**
     * {@code GET  /batiments} : get all the batiments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of batiments in body.
     */
    @GetMapping("/batiments")
    public ResponseEntity<List<Batiment>> getAllBatiments(Pageable pageable) {
        log.debug("REST request to get a page of Batiments");
        Page<Batiment> page = batimentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /batiments/:id} : get the "id" batiment.
     *
     * @param id the id of the batiment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the batiment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/batiments/{id}")
    public ResponseEntity<Batiment> getBatiment(@PathVariable Long id) {
        log.debug("REST request to get Batiment : {}", id);
        Optional<Batiment> batiment = batimentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(batiment);
    }

    /**
     * {@code DELETE  /batiments/:id} : delete the "id" batiment.
     *
     * @param id the id of the batiment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/batiments/{id}")
    public ResponseEntity<Void> deleteBatiment(@PathVariable Long id) {
        log.debug("REST request to delete Batiment : {}", id);
        batimentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
