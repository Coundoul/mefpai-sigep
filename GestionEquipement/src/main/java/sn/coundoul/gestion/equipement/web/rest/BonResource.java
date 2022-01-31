package sn.coundoul.gestion.equipement.web.rest;

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
import sn.coundoul.gestion.equipement.domain.Bon;
import sn.coundoul.gestion.equipement.repository.BonRepository;
import sn.coundoul.gestion.equipement.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.equipement.domain.Bon}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BonResource {

    private final Logger log = LoggerFactory.getLogger(BonResource.class);

    private static final String ENTITY_NAME = "gestionEquipementBon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BonRepository bonRepository;

    public BonResource(BonRepository bonRepository) {
        this.bonRepository = bonRepository;
    }

    /**
     * {@code POST  /bons} : Create a new bon.
     *
     * @param bon the bon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bon, or with status {@code 400 (Bad Request)} if the bon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bons")
    public ResponseEntity<Bon> createBon(@Valid @RequestBody Bon bon) throws URISyntaxException {
        log.debug("REST request to save Bon : {}", bon);
        if (bon.getId() != null) {
            throw new BadRequestAlertException("A new bon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bon result = bonRepository.save(bon);
        return ResponseEntity
            .created(new URI("/api/bons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bons/:id} : Updates an existing bon.
     *
     * @param id the id of the bon to save.
     * @param bon the bon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bon,
     * or with status {@code 400 (Bad Request)} if the bon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bons/{id}")
    public ResponseEntity<Bon> updateBon(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Bon bon)
        throws URISyntaxException {
        log.debug("REST request to update Bon : {}, {}", id, bon);
        if (bon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bon result = bonRepository.save(bon);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bon.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bons/:id} : Partial updates given fields of an existing bon, field will ignore if it is null
     *
     * @param id the id of the bon to save.
     * @param bon the bon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bon,
     * or with status {@code 400 (Bad Request)} if the bon is not valid,
     * or with status {@code 404 (Not Found)} if the bon is not found,
     * or with status {@code 500 (Internal Server Error)} if the bon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bons/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Bon> partialUpdateBon(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Bon bon)
        throws URISyntaxException {
        log.debug("REST request to partial update Bon partially : {}, {}", id, bon);
        if (bon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bon> result = bonRepository
            .findById(bon.getId())
            .map(
                existingBon -> {
                    if (bon.getTypeBon() != null) {
                        existingBon.setTypeBon(bon.getTypeBon());
                    }
                    if (bon.getQuantiteLivre() != null) {
                        existingBon.setQuantiteLivre(bon.getQuantiteLivre());
                    }
                    if (bon.getQuantiteCommande() != null) {
                        existingBon.setQuantiteCommande(bon.getQuantiteCommande());
                    }
                    if (bon.getDateCreation() != null) {
                        existingBon.setDateCreation(bon.getDateCreation());
                    }
                    if (bon.getEtat() != null) {
                        existingBon.setEtat(bon.getEtat());
                    }

                    return existingBon;
                }
            )
            .map(bonRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bon.getId().toString())
        );
    }

    /**
     * {@code GET  /bons} : get all the bons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bons in body.
     */
    @GetMapping("/bons")
    public ResponseEntity<List<Bon>> getAllBons(Pageable pageable) {
        log.debug("REST request to get a page of Bons");
        Page<Bon> page = bonRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bons/:id} : get the "id" bon.
     *
     * @param id the id of the bon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bons/{id}")
    public ResponseEntity<Bon> getBon(@PathVariable Long id) {
        log.debug("REST request to get Bon : {}", id);
        Optional<Bon> bon = bonRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bon);
    }

    /**
     * {@code DELETE  /bons/:id} : delete the "id" bon.
     *
     * @param id the id of the bon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bons/{id}")
    public ResponseEntity<Void> deleteBon(@PathVariable Long id) {
        log.debug("REST request to delete Bon : {}", id);
        bonRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
