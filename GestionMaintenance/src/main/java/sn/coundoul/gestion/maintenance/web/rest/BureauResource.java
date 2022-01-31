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
import sn.coundoul.gestion.maintenance.domain.Bureau;
import sn.coundoul.gestion.maintenance.repository.BureauRepository;
import sn.coundoul.gestion.maintenance.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.maintenance.domain.Bureau}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BureauResource {

    private final Logger log = LoggerFactory.getLogger(BureauResource.class);

    private static final String ENTITY_NAME = "gestionMaintenanceBureau";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BureauRepository bureauRepository;

    public BureauResource(BureauRepository bureauRepository) {
        this.bureauRepository = bureauRepository;
    }

    /**
     * {@code POST  /bureaus} : Create a new bureau.
     *
     * @param bureau the bureau to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bureau, or with status {@code 400 (Bad Request)} if the bureau has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bureaus")
    public ResponseEntity<Bureau> createBureau(@Valid @RequestBody Bureau bureau) throws URISyntaxException {
        log.debug("REST request to save Bureau : {}", bureau);
        if (bureau.getId() != null) {
            throw new BadRequestAlertException("A new bureau cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bureau result = bureauRepository.save(bureau);
        return ResponseEntity
            .created(new URI("/api/bureaus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bureaus/:id} : Updates an existing bureau.
     *
     * @param id the id of the bureau to save.
     * @param bureau the bureau to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bureau,
     * or with status {@code 400 (Bad Request)} if the bureau is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bureau couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bureaus/{id}")
    public ResponseEntity<Bureau> updateBureau(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Bureau bureau
    ) throws URISyntaxException {
        log.debug("REST request to update Bureau : {}, {}", id, bureau);
        if (bureau.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bureau.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bureauRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bureau result = bureauRepository.save(bureau);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bureau.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bureaus/:id} : Partial updates given fields of an existing bureau, field will ignore if it is null
     *
     * @param id the id of the bureau to save.
     * @param bureau the bureau to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bureau,
     * or with status {@code 400 (Bad Request)} if the bureau is not valid,
     * or with status {@code 404 (Not Found)} if the bureau is not found,
     * or with status {@code 500 (Internal Server Error)} if the bureau couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bureaus/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Bureau> partialUpdateBureau(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Bureau bureau
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bureau partially : {}, {}", id, bureau);
        if (bureau.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bureau.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bureauRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bureau> result = bureauRepository
            .findById(bureau.getId())
            .map(
                existingBureau -> {
                    if (bureau.getNomStructure() != null) {
                        existingBureau.setNomStructure(bureau.getNomStructure());
                    }
                    if (bureau.getDirection() != null) {
                        existingBureau.setDirection(bureau.getDirection());
                    }
                    if (bureau.getNomEtablissement() != null) {
                        existingBureau.setNomEtablissement(bureau.getNomEtablissement());
                    }

                    return existingBureau;
                }
            )
            .map(bureauRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bureau.getId().toString())
        );
    }

    /**
     * {@code GET  /bureaus} : get all the bureaus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bureaus in body.
     */
    @GetMapping("/bureaus")
    public ResponseEntity<List<Bureau>> getAllBureaus(Pageable pageable) {
        log.debug("REST request to get a page of Bureaus");
        Page<Bureau> page = bureauRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bureaus/:id} : get the "id" bureau.
     *
     * @param id the id of the bureau to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bureau, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bureaus/{id}")
    public ResponseEntity<Bureau> getBureau(@PathVariable Long id) {
        log.debug("REST request to get Bureau : {}", id);
        Optional<Bureau> bureau = bureauRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bureau);
    }

    /**
     * {@code DELETE  /bureaus/:id} : delete the "id" bureau.
     *
     * @param id the id of the bureau to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bureaus/{id}")
    public ResponseEntity<Void> deleteBureau(@PathVariable Long id) {
        log.debug("REST request to delete Bureau : {}", id);
        bureauRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
