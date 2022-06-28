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
import sn.coundoul.gestion.utilisateurs.domain.Intendant;
import sn.coundoul.gestion.utilisateurs.repository.IntendantRepository;
import sn.coundoul.gestion.utilisateurs.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.utilisateurs.domain.Intendant}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IntendantResource {

    private final Logger log = LoggerFactory.getLogger(IntendantResource.class);

    private static final String ENTITY_NAME = "gestionUtilisateursIntendant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IntendantRepository intendantRepository;

    public IntendantResource(IntendantRepository intendantRepository) {
        this.intendantRepository = intendantRepository;
    }

    /**
     * {@code POST  /intendants} : Create a new intendant.
     *
     * @param intendant the intendant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new intendant, or with status {@code 400 (Bad Request)} if the intendant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/intendants")
    public ResponseEntity<Intendant> createIntendant(@Valid @RequestBody Intendant intendant) throws URISyntaxException {
        log.debug("REST request to save Intendant : {}", intendant);
        if (intendant.getId() != null) {
            throw new BadRequestAlertException("A new intendant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Intendant result = intendantRepository.save(intendant);
        return ResponseEntity
            .created(new URI("/api/intendants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /intendants/:id} : Updates an existing intendant.
     *
     * @param id the id of the intendant to save.
     * @param intendant the intendant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intendant,
     * or with status {@code 400 (Bad Request)} if the intendant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the intendant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/intendants/{id}")
    public ResponseEntity<Intendant> updateIntendant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Intendant intendant
    ) throws URISyntaxException {
        log.debug("REST request to update Intendant : {}, {}", id, intendant);
        if (intendant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intendant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!intendantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Intendant result = intendantRepository.save(intendant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intendant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /intendants/:id} : Partial updates given fields of an existing intendant, field will ignore if it is null
     *
     * @param id the id of the intendant to save.
     * @param intendant the intendant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intendant,
     * or with status {@code 400 (Bad Request)} if the intendant is not valid,
     * or with status {@code 404 (Not Found)} if the intendant is not found,
     * or with status {@code 500 (Internal Server Error)} if the intendant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/intendants/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Intendant> partialUpdateIntendant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Intendant intendant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Intendant partially : {}, {}", id, intendant);
        if (intendant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intendant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!intendantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Intendant> result = intendantRepository
            .findById(intendant.getId())
            .map(
                existingIntendant -> {
                    if (intendant.getNomPers() != null) {
                        existingIntendant.setNomPers(intendant.getNomPers());
                    }
                    if (intendant.getPrenomPers() != null) {
                        existingIntendant.setPrenomPers(intendant.getPrenomPers());
                    }
                    if (intendant.getSexe() != null) {
                        existingIntendant.setSexe(intendant.getSexe());
                    }
                    if (intendant.getMobile() != null) {
                        existingIntendant.setMobile(intendant.getMobile());
                    }
                    if (intendant.getAdresse() != null) {
                        existingIntendant.setAdresse(intendant.getAdresse());
                    }

                    return existingIntendant;
                }
            )
            .map(intendantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intendant.getId().toString())
        );
    }

    /**
     * {@code GET  /intendants} : get all the intendants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of intendants in body.
     */
    @GetMapping("/intendants")
    public ResponseEntity<List<Intendant>> getAllIntendants(Pageable pageable) {
        log.debug("REST request to get a page of Intendants");
        Page<Intendant> page = intendantRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /intendants/:id} : get the "id" intendant.
     *
     * @param id the id of the intendant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the intendant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/intendants/{id}")
    public ResponseEntity<Intendant> getIntendant(@PathVariable Long id) {
        log.debug("REST request to get Intendant : {}", id);
        Optional<Intendant> intendant = intendantRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(intendant);
    }

    /**
     * {@code DELETE  /intendants/:id} : delete the "id" intendant.
     *
     * @param id the id of the intendant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/intendants/{id}")
    public ResponseEntity<Void> deleteIntendant(@PathVariable Long id) {
        log.debug("REST request to delete Intendant : {}", id);
        intendantRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
