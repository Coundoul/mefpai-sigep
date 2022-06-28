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
import sn.coundoul.gestion.utilisateurs.domain.ComptablePrincipale;
import sn.coundoul.gestion.utilisateurs.repository.ComptablePrincipaleRepository;
import sn.coundoul.gestion.utilisateurs.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.utilisateurs.domain.ComptablePrincipale}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ComptablePrincipaleResource {

    private final Logger log = LoggerFactory.getLogger(ComptablePrincipaleResource.class);

    private static final String ENTITY_NAME = "gestionUtilisateursComptablePrincipale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComptablePrincipaleRepository comptablePrincipaleRepository;

    public ComptablePrincipaleResource(ComptablePrincipaleRepository comptablePrincipaleRepository) {
        this.comptablePrincipaleRepository = comptablePrincipaleRepository;
    }

    /**
     * {@code POST  /comptable-principales} : Create a new comptablePrincipale.
     *
     * @param comptablePrincipale the comptablePrincipale to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comptablePrincipale, or with status {@code 400 (Bad Request)} if the comptablePrincipale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comptable-principales")
    public ResponseEntity<ComptablePrincipale> createComptablePrincipale(@Valid @RequestBody ComptablePrincipale comptablePrincipale)
        throws URISyntaxException {
        log.debug("REST request to save ComptablePrincipale : {}", comptablePrincipale);
        if (comptablePrincipale.getId() != null) {
            throw new BadRequestAlertException("A new comptablePrincipale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComptablePrincipale result = comptablePrincipaleRepository.save(comptablePrincipale);
        return ResponseEntity
            .created(new URI("/api/comptable-principales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comptable-principales/:id} : Updates an existing comptablePrincipale.
     *
     * @param id the id of the comptablePrincipale to save.
     * @param comptablePrincipale the comptablePrincipale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comptablePrincipale,
     * or with status {@code 400 (Bad Request)} if the comptablePrincipale is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comptablePrincipale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comptable-principales/{id}")
    public ResponseEntity<ComptablePrincipale> updateComptablePrincipale(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComptablePrincipale comptablePrincipale
    ) throws URISyntaxException {
        log.debug("REST request to update ComptablePrincipale : {}, {}", id, comptablePrincipale);
        if (comptablePrincipale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comptablePrincipale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comptablePrincipaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComptablePrincipale result = comptablePrincipaleRepository.save(comptablePrincipale);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comptablePrincipale.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comptable-principales/:id} : Partial updates given fields of an existing comptablePrincipale, field will ignore if it is null
     *
     * @param id the id of the comptablePrincipale to save.
     * @param comptablePrincipale the comptablePrincipale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comptablePrincipale,
     * or with status {@code 400 (Bad Request)} if the comptablePrincipale is not valid,
     * or with status {@code 404 (Not Found)} if the comptablePrincipale is not found,
     * or with status {@code 500 (Internal Server Error)} if the comptablePrincipale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comptable-principales/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ComptablePrincipale> partialUpdateComptablePrincipale(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComptablePrincipale comptablePrincipale
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComptablePrincipale partially : {}, {}", id, comptablePrincipale);
        if (comptablePrincipale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comptablePrincipale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comptablePrincipaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComptablePrincipale> result = comptablePrincipaleRepository
            .findById(comptablePrincipale.getId())
            .map(
                existingComptablePrincipale -> {
                    if (comptablePrincipale.getNomPers() != null) {
                        existingComptablePrincipale.setNomPers(comptablePrincipale.getNomPers());
                    }
                    if (comptablePrincipale.getPrenomPers() != null) {
                        existingComptablePrincipale.setPrenomPers(comptablePrincipale.getPrenomPers());
                    }
                    if (comptablePrincipale.getSexe() != null) {
                        existingComptablePrincipale.setSexe(comptablePrincipale.getSexe());
                    }
                    if (comptablePrincipale.getMobile() != null) {
                        existingComptablePrincipale.setMobile(comptablePrincipale.getMobile());
                    }
                    if (comptablePrincipale.getAdresse() != null) {
                        existingComptablePrincipale.setAdresse(comptablePrincipale.getAdresse());
                    }
                    if (comptablePrincipale.getDirection() != null) {
                        existingComptablePrincipale.setDirection(comptablePrincipale.getDirection());
                    }

                    return existingComptablePrincipale;
                }
            )
            .map(comptablePrincipaleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comptablePrincipale.getId().toString())
        );
    }

    /**
     * {@code GET  /comptable-principales} : get all the comptablePrincipales.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comptablePrincipales in body.
     */
    @GetMapping("/comptable-principales")
    public ResponseEntity<List<ComptablePrincipale>> getAllComptablePrincipales(Pageable pageable) {
        log.debug("REST request to get a page of ComptablePrincipales");
        Page<ComptablePrincipale> page = comptablePrincipaleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comptable-principales/:id} : get the "id" comptablePrincipale.
     *
     * @param id the id of the comptablePrincipale to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comptablePrincipale, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comptable-principales/{id}")
    public ResponseEntity<ComptablePrincipale> getComptablePrincipale(@PathVariable Long id) {
        log.debug("REST request to get ComptablePrincipale : {}", id);
        Optional<ComptablePrincipale> comptablePrincipale = comptablePrincipaleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(comptablePrincipale);
    }

    /**
     * {@code DELETE  /comptable-principales/:id} : delete the "id" comptablePrincipale.
     *
     * @param id the id of the comptablePrincipale to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comptable-principales/{id}")
    public ResponseEntity<Void> deleteComptablePrincipale(@PathVariable Long id) {
        log.debug("REST request to delete ComptablePrincipale : {}", id);
        comptablePrincipaleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
