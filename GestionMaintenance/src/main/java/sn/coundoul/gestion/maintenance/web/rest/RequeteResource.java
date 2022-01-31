package sn.coundoul.gestion.maintenance.web.rest;

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
import sn.coundoul.gestion.maintenance.domain.Requete;
import sn.coundoul.gestion.maintenance.repository.RequeteRepository;
import sn.coundoul.gestion.maintenance.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.maintenance.domain.Requete}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RequeteResource {

    private final Logger log = LoggerFactory.getLogger(RequeteResource.class);

    private static final String ENTITY_NAME = "gestionMaintenanceRequete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequeteRepository requeteRepository;

    public RequeteResource(RequeteRepository requeteRepository) {
        this.requeteRepository = requeteRepository;
    }

    /**
     * {@code POST  /requetes} : Create a new requete.
     *
     * @param requete the requete to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requete, or with status {@code 400 (Bad Request)} if the requete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requetes")
    public ResponseEntity<Requete> createRequete(@Valid @RequestBody Requete requete) throws URISyntaxException {
        log.debug("REST request to save Requete : {}", requete);
        if (requete.getId() != null) {
            throw new BadRequestAlertException("A new requete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Requete result = requeteRepository.save(requete);
        return ResponseEntity
            .created(new URI("/api/requetes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /requetes/:id} : Updates an existing requete.
     *
     * @param id the id of the requete to save.
     * @param requete the requete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requete,
     * or with status {@code 400 (Bad Request)} if the requete is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requetes/{id}")
    public ResponseEntity<Requete> updateRequete(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Requete requete
    ) throws URISyntaxException {
        log.debug("REST request to update Requete : {}, {}", id, requete);
        if (requete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requeteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Requete result = requeteRepository.save(requete);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requete.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /requetes/:id} : Partial updates given fields of an existing requete, field will ignore if it is null
     *
     * @param id the id of the requete to save.
     * @param requete the requete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requete,
     * or with status {@code 400 (Bad Request)} if the requete is not valid,
     * or with status {@code 404 (Not Found)} if the requete is not found,
     * or with status {@code 500 (Internal Server Error)} if the requete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/requetes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Requete> partialUpdateRequete(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Requete requete
    ) throws URISyntaxException {
        log.debug("REST request to partial update Requete partially : {}, {}", id, requete);
        if (requete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requeteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Requete> result = requeteRepository
            .findById(requete.getId())
            .map(
                existingRequete -> {
                    if (requete.getType() != null) {
                        existingRequete.setType(requete.getType());
                    }
                    if (requete.getTypePanne() != null) {
                        existingRequete.setTypePanne(requete.getTypePanne());
                    }
                    if (requete.getDatePost() != null) {
                        existingRequete.setDatePost(requete.getDatePost());
                    }
                    if (requete.getDescription() != null) {
                        existingRequete.setDescription(requete.getDescription());
                    }
                    if (requete.getEtatTraite() != null) {
                        existingRequete.setEtatTraite(requete.getEtatTraite());
                    }
                    if (requete.getDateLancement() != null) {
                        existingRequete.setDateLancement(requete.getDateLancement());
                    }
                    if (requete.getIdPers() != null) {
                        existingRequete.setIdPers(requete.getIdPers());
                    }

                    return existingRequete;
                }
            )
            .map(requeteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requete.getId().toString())
        );
    }

    /**
     * {@code GET  /requetes} : get all the requetes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requetes in body.
     */
    @GetMapping("/requetes")
    public ResponseEntity<List<Requete>> getAllRequetes(Pageable pageable) {
        log.debug("REST request to get a page of Requetes");
        Page<Requete> page = requeteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /requetes/:id} : get the "id" requete.
     *
     * @param id the id of the requete to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requete, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requetes/{id}")
    public ResponseEntity<Requete> getRequete(@PathVariable Long id) {
        log.debug("REST request to get Requete : {}", id);
        Optional<Requete> requete = requeteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(requete);
    }

    /**
     * {@code DELETE  /requetes/:id} : delete the "id" requete.
     *
     * @param id the id of the requete to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requetes/{id}")
    public ResponseEntity<Void> deleteRequete(@PathVariable Long id) {
        log.debug("REST request to delete Requete : {}", id);
        requeteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
