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
import sn.coundoul.gestion.infrastructure.domain.Formateurs;
import sn.coundoul.gestion.infrastructure.repository.FormateursRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.Formateurs}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FormateursResource {

    private final Logger log = LoggerFactory.getLogger(FormateursResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureFormateurs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormateursRepository formateursRepository;

    public FormateursResource(FormateursRepository formateursRepository) {
        this.formateursRepository = formateursRepository;
    }

    /**
     * {@code POST  /formateurs} : Create a new formateurs.
     *
     * @param formateurs the formateurs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formateurs, or with status {@code 400 (Bad Request)} if the formateurs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formateurs")
    public ResponseEntity<Formateurs> createFormateurs(@Valid @RequestBody Formateurs formateurs) throws URISyntaxException {
        log.debug("REST request to save Formateurs : {}", formateurs);
        if (formateurs.getId() != null) {
            throw new BadRequestAlertException("A new formateurs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Formateurs result = formateursRepository.save(formateurs);
        return ResponseEntity
            .created(new URI("/api/formateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formateurs/:id} : Updates an existing formateurs.
     *
     * @param id the id of the formateurs to save.
     * @param formateurs the formateurs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formateurs,
     * or with status {@code 400 (Bad Request)} if the formateurs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formateurs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formateurs/{id}")
    public ResponseEntity<Formateurs> updateFormateurs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Formateurs formateurs
    ) throws URISyntaxException {
        log.debug("REST request to update Formateurs : {}, {}", id, formateurs);
        if (formateurs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formateurs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formateursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Formateurs result = formateursRepository.save(formateurs);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formateurs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formateurs/:id} : Partial updates given fields of an existing formateurs, field will ignore if it is null
     *
     * @param id the id of the formateurs to save.
     * @param formateurs the formateurs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formateurs,
     * or with status {@code 400 (Bad Request)} if the formateurs is not valid,
     * or with status {@code 404 (Not Found)} if the formateurs is not found,
     * or with status {@code 500 (Internal Server Error)} if the formateurs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/formateurs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Formateurs> partialUpdateFormateurs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Formateurs formateurs
    ) throws URISyntaxException {
        log.debug("REST request to partial update Formateurs partially : {}, {}", id, formateurs);
        if (formateurs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formateurs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formateursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Formateurs> result = formateursRepository
            .findById(formateurs.getId())
            .map(
                existingFormateurs -> {
                    if (formateurs.getNomFormateur() != null) {
                        existingFormateurs.setNomFormateur(formateurs.getNomFormateur());
                    }
                    if (formateurs.getPrenomFormateur() != null) {
                        existingFormateurs.setPrenomFormateur(formateurs.getPrenomFormateur());
                    }
                    if (formateurs.getEmail() != null) {
                        existingFormateurs.setEmail(formateurs.getEmail());
                    }
                    if (formateurs.getNumb1() != null) {
                        existingFormateurs.setNumb1(formateurs.getNumb1());
                    }
                    if (formateurs.getNumb2() != null) {
                        existingFormateurs.setNumb2(formateurs.getNumb2());
                    }
                    if (formateurs.getAdresse() != null) {
                        existingFormateurs.setAdresse(formateurs.getAdresse());
                    }
                    if (formateurs.getVille() != null) {
                        existingFormateurs.setVille(formateurs.getVille());
                    }
                    if (formateurs.getSpecialite() != null) {
                        existingFormateurs.setSpecialite(formateurs.getSpecialite());
                    }

                    return existingFormateurs;
                }
            )
            .map(formateursRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formateurs.getId().toString())
        );
    }

    /**
     * {@code GET  /formateurs} : get all the formateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formateurs in body.
     */
    @GetMapping("/formateurs")
    public ResponseEntity<List<Formateurs>> getAllFormateurs(Pageable pageable) {
        log.debug("REST request to get a page of Formateurs");
        Page<Formateurs> page = formateursRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formateurs/:id} : get the "id" formateurs.
     *
     * @param id the id of the formateurs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formateurs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formateurs/{id}")
    public ResponseEntity<Formateurs> getFormateurs(@PathVariable Long id) {
        log.debug("REST request to get Formateurs : {}", id);
        Optional<Formateurs> formateurs = formateursRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(formateurs);
    }

    /**
     * {@code DELETE  /formateurs/:id} : delete the "id" formateurs.
     *
     * @param id the id of the formateurs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formateurs/{id}")
    public ResponseEntity<Void> deleteFormateurs(@PathVariable Long id) {
        log.debug("REST request to delete Formateurs : {}", id);
        formateursRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
