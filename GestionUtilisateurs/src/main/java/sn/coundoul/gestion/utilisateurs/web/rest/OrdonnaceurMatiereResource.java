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
import sn.coundoul.gestion.utilisateurs.domain.OrdonnaceurMatiere;
import sn.coundoul.gestion.utilisateurs.repository.OrdonnaceurMatiereRepository;
import sn.coundoul.gestion.utilisateurs.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.utilisateurs.domain.OrdonnaceurMatiere}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrdonnaceurMatiereResource {

    private final Logger log = LoggerFactory.getLogger(OrdonnaceurMatiereResource.class);

    private static final String ENTITY_NAME = "gestionUtilisateursOrdonnaceurMatiere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdonnaceurMatiereRepository ordonnaceurMatiereRepository;

    public OrdonnaceurMatiereResource(OrdonnaceurMatiereRepository ordonnaceurMatiereRepository) {
        this.ordonnaceurMatiereRepository = ordonnaceurMatiereRepository;
    }

    /**
     * {@code POST  /ordonnaceur-matieres} : Create a new ordonnaceurMatiere.
     *
     * @param ordonnaceurMatiere the ordonnaceurMatiere to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordonnaceurMatiere, or with status {@code 400 (Bad Request)} if the ordonnaceurMatiere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordonnaceur-matieres")
    public ResponseEntity<OrdonnaceurMatiere> createOrdonnaceurMatiere(@Valid @RequestBody OrdonnaceurMatiere ordonnaceurMatiere)
        throws URISyntaxException {
        log.debug("REST request to save OrdonnaceurMatiere : {}", ordonnaceurMatiere);
        if (ordonnaceurMatiere.getId() != null) {
            throw new BadRequestAlertException("A new ordonnaceurMatiere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdonnaceurMatiere result = ordonnaceurMatiereRepository.save(ordonnaceurMatiere);
        return ResponseEntity
            .created(new URI("/api/ordonnaceur-matieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordonnaceur-matieres/:id} : Updates an existing ordonnaceurMatiere.
     *
     * @param id the id of the ordonnaceurMatiere to save.
     * @param ordonnaceurMatiere the ordonnaceurMatiere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordonnaceurMatiere,
     * or with status {@code 400 (Bad Request)} if the ordonnaceurMatiere is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordonnaceurMatiere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordonnaceur-matieres/{id}")
    public ResponseEntity<OrdonnaceurMatiere> updateOrdonnaceurMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrdonnaceurMatiere ordonnaceurMatiere
    ) throws URISyntaxException {
        log.debug("REST request to update OrdonnaceurMatiere : {}, {}", id, ordonnaceurMatiere);
        if (ordonnaceurMatiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordonnaceurMatiere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordonnaceurMatiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdonnaceurMatiere result = ordonnaceurMatiereRepository.save(ordonnaceurMatiere);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordonnaceurMatiere.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ordonnaceur-matieres/:id} : Partial updates given fields of an existing ordonnaceurMatiere, field will ignore if it is null
     *
     * @param id the id of the ordonnaceurMatiere to save.
     * @param ordonnaceurMatiere the ordonnaceurMatiere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordonnaceurMatiere,
     * or with status {@code 400 (Bad Request)} if the ordonnaceurMatiere is not valid,
     * or with status {@code 404 (Not Found)} if the ordonnaceurMatiere is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordonnaceurMatiere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ordonnaceur-matieres/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdonnaceurMatiere> partialUpdateOrdonnaceurMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrdonnaceurMatiere ordonnaceurMatiere
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdonnaceurMatiere partially : {}, {}", id, ordonnaceurMatiere);
        if (ordonnaceurMatiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordonnaceurMatiere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordonnaceurMatiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdonnaceurMatiere> result = ordonnaceurMatiereRepository
            .findById(ordonnaceurMatiere.getId())
            .map(
                existingOrdonnaceurMatiere -> {
                    if (ordonnaceurMatiere.getNomPers() != null) {
                        existingOrdonnaceurMatiere.setNomPers(ordonnaceurMatiere.getNomPers());
                    }
                    if (ordonnaceurMatiere.getPrenomPers() != null) {
                        existingOrdonnaceurMatiere.setPrenomPers(ordonnaceurMatiere.getPrenomPers());
                    }
                    if (ordonnaceurMatiere.getSexe() != null) {
                        existingOrdonnaceurMatiere.setSexe(ordonnaceurMatiere.getSexe());
                    }
                    if (ordonnaceurMatiere.getMobile() != null) {
                        existingOrdonnaceurMatiere.setMobile(ordonnaceurMatiere.getMobile());
                    }
                    if (ordonnaceurMatiere.getAdresse() != null) {
                        existingOrdonnaceurMatiere.setAdresse(ordonnaceurMatiere.getAdresse());
                    }
                    if (ordonnaceurMatiere.getDirection() != null) {
                        existingOrdonnaceurMatiere.setDirection(ordonnaceurMatiere.getDirection());
                    }

                    return existingOrdonnaceurMatiere;
                }
            )
            .map(ordonnaceurMatiereRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordonnaceurMatiere.getId().toString())
        );
    }

    /**
     * {@code GET  /ordonnaceur-matieres} : get all the ordonnaceurMatieres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordonnaceurMatieres in body.
     */
    @GetMapping("/ordonnaceur-matieres")
    public ResponseEntity<List<OrdonnaceurMatiere>> getAllOrdonnaceurMatieres(Pageable pageable) {
        log.debug("REST request to get a page of OrdonnaceurMatieres");
        Page<OrdonnaceurMatiere> page = ordonnaceurMatiereRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ordonnaceur-matieres/:id} : get the "id" ordonnaceurMatiere.
     *
     * @param id the id of the ordonnaceurMatiere to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordonnaceurMatiere, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordonnaceur-matieres/{id}")
    public ResponseEntity<OrdonnaceurMatiere> getOrdonnaceurMatiere(@PathVariable Long id) {
        log.debug("REST request to get OrdonnaceurMatiere : {}", id);
        Optional<OrdonnaceurMatiere> ordonnaceurMatiere = ordonnaceurMatiereRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ordonnaceurMatiere);
    }

    /**
     * {@code DELETE  /ordonnaceur-matieres/:id} : delete the "id" ordonnaceurMatiere.
     *
     * @param id the id of the ordonnaceurMatiere to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordonnaceur-matieres/{id}")
    public ResponseEntity<Void> deleteOrdonnaceurMatiere(@PathVariable Long id) {
        log.debug("REST request to delete OrdonnaceurMatiere : {}", id);
        ordonnaceurMatiereRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
