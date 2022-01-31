package sn.coundoul.gestion.utilisateurs.web.rest;

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
import sn.coundoul.gestion.utilisateurs.domain.Technicien;
import sn.coundoul.gestion.utilisateurs.repository.TechnicienRepository;
import sn.coundoul.gestion.utilisateurs.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.utilisateurs.domain.Technicien}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TechnicienResource {

    private final Logger log = LoggerFactory.getLogger(TechnicienResource.class);

    private static final String ENTITY_NAME = "gestionUtilisateursTechnicien";

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
    public ResponseEntity<Technicien> createTechnicien(@Valid @RequestBody Technicien technicien) throws URISyntaxException {
        log.debug("REST request to save Technicien : {}", technicien);
        if (technicien.getId() != null) {
            throw new BadRequestAlertException("A new technicien cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Technicien result = technicienRepository.save(technicien);
        return ResponseEntity
            .created(new URI("/api/techniciens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<Technicien> updateTechnicien(
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

        if (!technicienRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Technicien result = technicienRepository.save(technicien);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, technicien.getId().toString()))
            .body(result);
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
    public ResponseEntity<Technicien> partialUpdateTechnicien(
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

        if (!technicienRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Technicien> result = technicienRepository
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
            .map(technicienRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, technicien.getId().toString())
        );
    }

    /**
     * {@code GET  /techniciens} : get all the techniciens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of techniciens in body.
     */
    @GetMapping("/techniciens")
    public ResponseEntity<List<Technicien>> getAllTechniciens(Pageable pageable) {
        log.debug("REST request to get a page of Techniciens");
        Page<Technicien> page = technicienRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /techniciens/:id} : get the "id" technicien.
     *
     * @param id the id of the technicien to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the technicien, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/techniciens/{id}")
    public ResponseEntity<Technicien> getTechnicien(@PathVariable Long id) {
        log.debug("REST request to get Technicien : {}", id);
        Optional<Technicien> technicien = technicienRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(technicien);
    }

    /**
     * {@code DELETE  /techniciens/:id} : delete the "id" technicien.
     *
     * @param id the id of the technicien to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/techniciens/{id}")
    public ResponseEntity<Void> deleteTechnicien(@PathVariable Long id) {
        log.debug("REST request to delete Technicien : {}", id);
        technicienRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
