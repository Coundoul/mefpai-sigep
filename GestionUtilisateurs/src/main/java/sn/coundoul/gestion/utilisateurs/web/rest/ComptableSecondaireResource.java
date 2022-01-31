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
import sn.coundoul.gestion.utilisateurs.domain.ComptableSecondaire;
import sn.coundoul.gestion.utilisateurs.repository.ComptableSecondaireRepository;
import sn.coundoul.gestion.utilisateurs.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.utilisateurs.domain.ComptableSecondaire}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ComptableSecondaireResource {

    private final Logger log = LoggerFactory.getLogger(ComptableSecondaireResource.class);

    private static final String ENTITY_NAME = "gestionUtilisateursComptableSecondaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComptableSecondaireRepository comptableSecondaireRepository;

    public ComptableSecondaireResource(ComptableSecondaireRepository comptableSecondaireRepository) {
        this.comptableSecondaireRepository = comptableSecondaireRepository;
    }

    /**
     * {@code POST  /comptable-secondaires} : Create a new comptableSecondaire.
     *
     * @param comptableSecondaire the comptableSecondaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comptableSecondaire, or with status {@code 400 (Bad Request)} if the comptableSecondaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comptable-secondaires")
    public ResponseEntity<ComptableSecondaire> createComptableSecondaire(@Valid @RequestBody ComptableSecondaire comptableSecondaire)
        throws URISyntaxException {
        log.debug("REST request to save ComptableSecondaire : {}", comptableSecondaire);
        if (comptableSecondaire.getId() != null) {
            throw new BadRequestAlertException("A new comptableSecondaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComptableSecondaire result = comptableSecondaireRepository.save(comptableSecondaire);
        return ResponseEntity
            .created(new URI("/api/comptable-secondaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comptable-secondaires/:id} : Updates an existing comptableSecondaire.
     *
     * @param id the id of the comptableSecondaire to save.
     * @param comptableSecondaire the comptableSecondaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comptableSecondaire,
     * or with status {@code 400 (Bad Request)} if the comptableSecondaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comptableSecondaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comptable-secondaires/{id}")
    public ResponseEntity<ComptableSecondaire> updateComptableSecondaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComptableSecondaire comptableSecondaire
    ) throws URISyntaxException {
        log.debug("REST request to update ComptableSecondaire : {}, {}", id, comptableSecondaire);
        if (comptableSecondaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comptableSecondaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comptableSecondaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComptableSecondaire result = comptableSecondaireRepository.save(comptableSecondaire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comptableSecondaire.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comptable-secondaires/:id} : Partial updates given fields of an existing comptableSecondaire, field will ignore if it is null
     *
     * @param id the id of the comptableSecondaire to save.
     * @param comptableSecondaire the comptableSecondaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comptableSecondaire,
     * or with status {@code 400 (Bad Request)} if the comptableSecondaire is not valid,
     * or with status {@code 404 (Not Found)} if the comptableSecondaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the comptableSecondaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comptable-secondaires/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ComptableSecondaire> partialUpdateComptableSecondaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComptableSecondaire comptableSecondaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComptableSecondaire partially : {}, {}", id, comptableSecondaire);
        if (comptableSecondaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comptableSecondaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comptableSecondaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComptableSecondaire> result = comptableSecondaireRepository
            .findById(comptableSecondaire.getId())
            .map(
                existingComptableSecondaire -> {
                    if (comptableSecondaire.getNomPers() != null) {
                        existingComptableSecondaire.setNomPers(comptableSecondaire.getNomPers());
                    }
                    if (comptableSecondaire.getPrenomPers() != null) {
                        existingComptableSecondaire.setPrenomPers(comptableSecondaire.getPrenomPers());
                    }
                    if (comptableSecondaire.getSexe() != null) {
                        existingComptableSecondaire.setSexe(comptableSecondaire.getSexe());
                    }
                    if (comptableSecondaire.getMobile() != null) {
                        existingComptableSecondaire.setMobile(comptableSecondaire.getMobile());
                    }
                    if (comptableSecondaire.getAdresse() != null) {
                        existingComptableSecondaire.setAdresse(comptableSecondaire.getAdresse());
                    }
                    if (comptableSecondaire.getDirection() != null) {
                        existingComptableSecondaire.setDirection(comptableSecondaire.getDirection());
                    }

                    return existingComptableSecondaire;
                }
            )
            .map(comptableSecondaireRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comptableSecondaire.getId().toString())
        );
    }

    /**
     * {@code GET  /comptable-secondaires} : get all the comptableSecondaires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comptableSecondaires in body.
     */
    @GetMapping("/comptable-secondaires")
    public ResponseEntity<List<ComptableSecondaire>> getAllComptableSecondaires(Pageable pageable) {
        log.debug("REST request to get a page of ComptableSecondaires");
        Page<ComptableSecondaire> page = comptableSecondaireRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comptable-secondaires/:id} : get the "id" comptableSecondaire.
     *
     * @param id the id of the comptableSecondaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comptableSecondaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comptable-secondaires/{id}")
    public ResponseEntity<ComptableSecondaire> getComptableSecondaire(@PathVariable Long id) {
        log.debug("REST request to get ComptableSecondaire : {}", id);
        Optional<ComptableSecondaire> comptableSecondaire = comptableSecondaireRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(comptableSecondaire);
    }

    /**
     * {@code DELETE  /comptable-secondaires/:id} : delete the "id" comptableSecondaire.
     *
     * @param id the id of the comptableSecondaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comptable-secondaires/{id}")
    public ResponseEntity<Void> deleteComptableSecondaire(@PathVariable Long id) {
        log.debug("REST request to delete ComptableSecondaire : {}", id);
        comptableSecondaireRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
