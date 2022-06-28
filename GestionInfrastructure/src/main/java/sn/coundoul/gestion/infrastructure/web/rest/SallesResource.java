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
import sn.coundoul.gestion.infrastructure.domain.Salles;
import sn.coundoul.gestion.infrastructure.repository.SallesRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.Salles}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SallesResource {

    private final Logger log = LoggerFactory.getLogger(SallesResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureSalles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SallesRepository sallesRepository;

    public SallesResource(SallesRepository sallesRepository) {
        this.sallesRepository = sallesRepository;
    }

    /**
     * {@code POST  /salles} : Create a new salles.
     *
     * @param salles the salles to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salles, or with status {@code 400 (Bad Request)} if the salles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/salles")
    public ResponseEntity<Salles> createSalles(@Valid @RequestBody Salles salles) throws URISyntaxException {
        log.debug("REST request to save Salles : {}", salles);
        if (salles.getId() != null) {
            throw new BadRequestAlertException("A new salles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Salles result = sallesRepository.save(salles);
        return ResponseEntity
            .created(new URI("/api/salles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /salles/:id} : Updates an existing salles.
     *
     * @param id the id of the salles to save.
     * @param salles the salles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salles,
     * or with status {@code 400 (Bad Request)} if the salles is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salles/{id}")
    public ResponseEntity<Salles> updateSalles(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Salles salles
    ) throws URISyntaxException {
        log.debug("REST request to update Salles : {}, {}", id, salles);
        if (salles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sallesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Salles result = sallesRepository.save(salles);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salles.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /salles/:id} : Partial updates given fields of an existing salles, field will ignore if it is null
     *
     * @param id the id of the salles to save.
     * @param salles the salles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salles,
     * or with status {@code 400 (Bad Request)} if the salles is not valid,
     * or with status {@code 404 (Not Found)} if the salles is not found,
     * or with status {@code 500 (Internal Server Error)} if the salles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/salles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Salles> partialUpdateSalles(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Salles salles
    ) throws URISyntaxException {
        log.debug("REST request to partial update Salles partially : {}, {}", id, salles);
        if (salles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sallesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Salles> result = sallesRepository
            .findById(salles.getId())
            .map(
                existingSalles -> {
                    if (salles.getNomSalle() != null) {
                        existingSalles.setNomSalle(salles.getNomSalle());
                    }
                    if (salles.getClasse() != null) {
                        existingSalles.setClasse(salles.getClasse());
                    }

                    return existingSalles;
                }
            )
            .map(sallesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salles.getId().toString())
        );
    }

    /**
     * {@code GET  /salles} : get all the salles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salles in body.
     */
    @GetMapping("/salles")
    public ResponseEntity<List<Salles>> getAllSalles(Pageable pageable) {
        log.debug("REST request to get a page of Salles");
        Page<Salles> page = sallesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /salles/:id} : get the "id" salles.
     *
     * @param id the id of the salles to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salles, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salles/{id}")
    public ResponseEntity<Salles> getSalles(@PathVariable Long id) {
        log.debug("REST request to get Salles : {}", id);
        Optional<Salles> salles = sallesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(salles);
    }

    /**
     * {@code DELETE  /salles/:id} : delete the "id" salles.
     *
     * @param id the id of the salles to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salles/{id}")
    public ResponseEntity<Void> deleteSalles(@PathVariable Long id) {
        log.debug("REST request to delete Salles : {}", id);
        sallesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
