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
import sn.coundoul.gestion.infrastructure.domain.Responsable;
import sn.coundoul.gestion.infrastructure.repository.ResponsableRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.Responsable}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ResponsableResource {

    private final Logger log = LoggerFactory.getLogger(ResponsableResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureResponsable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResponsableRepository responsableRepository;

    public ResponsableResource(ResponsableRepository responsableRepository) {
        this.responsableRepository = responsableRepository;
    }

    /**
     * {@code POST  /responsables} : Create a new responsable.
     *
     * @param responsable the responsable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new responsable, or with status {@code 400 (Bad Request)} if the responsable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/responsables")
    public ResponseEntity<Responsable> createResponsable(@Valid @RequestBody Responsable responsable) throws URISyntaxException {
        log.debug("REST request to save Responsable : {}", responsable);
        if (responsable.getId() != null) {
            throw new BadRequestAlertException("A new responsable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Responsable result = responsableRepository.save(responsable);
        return ResponseEntity
            .created(new URI("/api/responsables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /responsables/:id} : Updates an existing responsable.
     *
     * @param id the id of the responsable to save.
     * @param responsable the responsable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsable,
     * or with status {@code 400 (Bad Request)} if the responsable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the responsable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/responsables/{id}")
    public ResponseEntity<Responsable> updateResponsable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Responsable responsable
    ) throws URISyntaxException {
        log.debug("REST request to update Responsable : {}, {}", id, responsable);
        if (responsable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responsableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Responsable result = responsableRepository.save(responsable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, responsable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /responsables/:id} : Partial updates given fields of an existing responsable, field will ignore if it is null
     *
     * @param id the id of the responsable to save.
     * @param responsable the responsable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsable,
     * or with status {@code 400 (Bad Request)} if the responsable is not valid,
     * or with status {@code 404 (Not Found)} if the responsable is not found,
     * or with status {@code 500 (Internal Server Error)} if the responsable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/responsables/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Responsable> partialUpdateResponsable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Responsable responsable
    ) throws URISyntaxException {
        log.debug("REST request to partial update Responsable partially : {}, {}", id, responsable);
        if (responsable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responsableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Responsable> result = responsableRepository
            .findById(responsable.getId())
            .map(
                existingResponsable -> {
                    if (responsable.getNomResponsable() != null) {
                        existingResponsable.setNomResponsable(responsable.getNomResponsable());
                    }
                    if (responsable.getPrenomResponsable() != null) {
                        existingResponsable.setPrenomResponsable(responsable.getPrenomResponsable());
                    }
                    if (responsable.getEmail() != null) {
                        existingResponsable.setEmail(responsable.getEmail());
                    }
                    if (responsable.getSpecialite() != null) {
                        existingResponsable.setSpecialite(responsable.getSpecialite());
                    }
                    if (responsable.getNumb1() != null) {
                        existingResponsable.setNumb1(responsable.getNumb1());
                    }
                    if (responsable.getNumb2() != null) {
                        existingResponsable.setNumb2(responsable.getNumb2());
                    }
                    if (responsable.getRaisonSocial() != null) {
                        existingResponsable.setRaisonSocial(responsable.getRaisonSocial());
                    }

                    return existingResponsable;
                }
            )
            .map(responsableRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, responsable.getId().toString())
        );
    }

    /**
     * {@code GET  /responsables} : get all the responsables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of responsables in body.
     */
    @GetMapping("/responsables")
    public ResponseEntity<List<Responsable>> getAllResponsables(Pageable pageable) {
        log.debug("REST request to get a page of Responsables");
        Page<Responsable> page = responsableRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /responsables/:id} : get the "id" responsable.
     *
     * @param id the id of the responsable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the responsable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/responsables/{id}")
    public ResponseEntity<Responsable> getResponsable(@PathVariable Long id) {
        log.debug("REST request to get Responsable : {}", id);
        Optional<Responsable> responsable = responsableRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(responsable);
    }

    /**
     * {@code DELETE  /responsables/:id} : delete the "id" responsable.
     *
     * @param id the id of the responsable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/responsables/{id}")
    public ResponseEntity<Void> deleteResponsable(@PathVariable Long id) {
        log.debug("REST request to delete Responsable : {}", id);
        responsableRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
