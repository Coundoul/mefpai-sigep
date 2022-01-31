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
import sn.coundoul.gestion.utilisateurs.domain.Detenteur;
import sn.coundoul.gestion.utilisateurs.repository.DetenteurRepository;
import sn.coundoul.gestion.utilisateurs.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.utilisateurs.domain.Detenteur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DetenteurResource {

    private final Logger log = LoggerFactory.getLogger(DetenteurResource.class);

    private static final String ENTITY_NAME = "gestionUtilisateursDetenteur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetenteurRepository detenteurRepository;

    public DetenteurResource(DetenteurRepository detenteurRepository) {
        this.detenteurRepository = detenteurRepository;
    }

    /**
     * {@code POST  /detenteurs} : Create a new detenteur.
     *
     * @param detenteur the detenteur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detenteur, or with status {@code 400 (Bad Request)} if the detenteur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detenteurs")
    public ResponseEntity<Detenteur> createDetenteur(@Valid @RequestBody Detenteur detenteur) throws URISyntaxException {
        log.debug("REST request to save Detenteur : {}", detenteur);
        if (detenteur.getId() != null) {
            throw new BadRequestAlertException("A new detenteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Detenteur result = detenteurRepository.save(detenteur);
        return ResponseEntity
            .created(new URI("/api/detenteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detenteurs/:id} : Updates an existing detenteur.
     *
     * @param id the id of the detenteur to save.
     * @param detenteur the detenteur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detenteur,
     * or with status {@code 400 (Bad Request)} if the detenteur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detenteur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detenteurs/{id}")
    public ResponseEntity<Detenteur> updateDetenteur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Detenteur detenteur
    ) throws URISyntaxException {
        log.debug("REST request to update Detenteur : {}, {}", id, detenteur);
        if (detenteur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detenteur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detenteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Detenteur result = detenteurRepository.save(detenteur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detenteur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detenteurs/:id} : Partial updates given fields of an existing detenteur, field will ignore if it is null
     *
     * @param id the id of the detenteur to save.
     * @param detenteur the detenteur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detenteur,
     * or with status {@code 400 (Bad Request)} if the detenteur is not valid,
     * or with status {@code 404 (Not Found)} if the detenteur is not found,
     * or with status {@code 500 (Internal Server Error)} if the detenteur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detenteurs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Detenteur> partialUpdateDetenteur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Detenteur detenteur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Detenteur partially : {}, {}", id, detenteur);
        if (detenteur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detenteur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detenteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Detenteur> result = detenteurRepository
            .findById(detenteur.getId())
            .map(
                existingDetenteur -> {
                    if (detenteur.getNomPers() != null) {
                        existingDetenteur.setNomPers(detenteur.getNomPers());
                    }
                    if (detenteur.getPrenomPers() != null) {
                        existingDetenteur.setPrenomPers(detenteur.getPrenomPers());
                    }
                    if (detenteur.getSexe() != null) {
                        existingDetenteur.setSexe(detenteur.getSexe());
                    }
                    if (detenteur.getMobile() != null) {
                        existingDetenteur.setMobile(detenteur.getMobile());
                    }
                    if (detenteur.getAdresse() != null) {
                        existingDetenteur.setAdresse(detenteur.getAdresse());
                    }
                    if (detenteur.getDirection() != null) {
                        existingDetenteur.setDirection(detenteur.getDirection());
                    }

                    return existingDetenteur;
                }
            )
            .map(detenteurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detenteur.getId().toString())
        );
    }

    /**
     * {@code GET  /detenteurs} : get all the detenteurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detenteurs in body.
     */
    @GetMapping("/detenteurs")
    public ResponseEntity<List<Detenteur>> getAllDetenteurs(Pageable pageable) {
        log.debug("REST request to get a page of Detenteurs");
        Page<Detenteur> page = detenteurRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detenteurs/:id} : get the "id" detenteur.
     *
     * @param id the id of the detenteur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detenteur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detenteurs/{id}")
    public ResponseEntity<Detenteur> getDetenteur(@PathVariable Long id) {
        log.debug("REST request to get Detenteur : {}", id);
        Optional<Detenteur> detenteur = detenteurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(detenteur);
    }

    /**
     * {@code DELETE  /detenteurs/:id} : delete the "id" detenteur.
     *
     * @param id the id of the detenteur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detenteurs/{id}")
    public ResponseEntity<Void> deleteDetenteur(@PathVariable Long id) {
        log.debug("REST request to delete Detenteur : {}", id);
        detenteurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
