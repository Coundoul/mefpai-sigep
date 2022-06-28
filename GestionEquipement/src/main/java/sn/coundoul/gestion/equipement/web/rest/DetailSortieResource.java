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
import sn.coundoul.gestion.equipement.domain.DetailSortie;
import sn.coundoul.gestion.equipement.repository.DetailSortieRepository;
import sn.coundoul.gestion.equipement.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.equipement.domain.DetailSortie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DetailSortieResource {

    private final Logger log = LoggerFactory.getLogger(DetailSortieResource.class);

    private static final String ENTITY_NAME = "gestionEquipementDetailSortie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailSortieRepository detailSortieRepository;

    public DetailSortieResource(DetailSortieRepository detailSortieRepository) {
        this.detailSortieRepository = detailSortieRepository;
    }

    /**
     * {@code POST  /detail-sorties} : Create a new detailSortie.
     *
     * @param detailSortie the detailSortie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailSortie, or with status {@code 400 (Bad Request)} if the detailSortie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detail-sorties")
    public ResponseEntity<DetailSortie> createDetailSortie(@Valid @RequestBody DetailSortie detailSortie) throws URISyntaxException {
        log.debug("REST request to save DetailSortie : {}", detailSortie);
        if (detailSortie.getId() != null) {
            throw new BadRequestAlertException("A new detailSortie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailSortie result = detailSortieRepository.save(detailSortie);
        return ResponseEntity
            .created(new URI("/api/detail-sorties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detail-sorties/:id} : Updates an existing detailSortie.
     *
     * @param id the id of the detailSortie to save.
     * @param detailSortie the detailSortie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailSortie,
     * or with status {@code 400 (Bad Request)} if the detailSortie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailSortie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detail-sorties/{id}")
    public ResponseEntity<DetailSortie> updateDetailSortie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetailSortie detailSortie
    ) throws URISyntaxException {
        log.debug("REST request to update DetailSortie : {}, {}", id, detailSortie);
        if (detailSortie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailSortie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailSortieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetailSortie result = detailSortieRepository.save(detailSortie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailSortie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detail-sorties/:id} : Partial updates given fields of an existing detailSortie, field will ignore if it is null
     *
     * @param id the id of the detailSortie to save.
     * @param detailSortie the detailSortie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailSortie,
     * or with status {@code 400 (Bad Request)} if the detailSortie is not valid,
     * or with status {@code 404 (Not Found)} if the detailSortie is not found,
     * or with status {@code 500 (Internal Server Error)} if the detailSortie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detail-sorties/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DetailSortie> partialUpdateDetailSortie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetailSortie detailSortie
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetailSortie partially : {}, {}", id, detailSortie);
        if (detailSortie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailSortie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailSortieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailSortie> result = detailSortieRepository
            .findById(detailSortie.getId())
            .map(
                existingDetailSortie -> {
                    if (detailSortie.getPieceJointe() != null) {
                        existingDetailSortie.setPieceJointe(detailSortie.getPieceJointe());
                    }
                    if (detailSortie.getIdPers() != null) {
                        existingDetailSortie.setIdPers(detailSortie.getIdPers());
                    }

                    return existingDetailSortie;
                }
            )
            .map(detailSortieRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailSortie.getId().toString())
        );
    }

    /**
     * {@code GET  /detail-sorties} : get all the detailSorties.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailSorties in body.
     */
    @GetMapping("/detail-sorties")
    public ResponseEntity<List<DetailSortie>> getAllDetailSorties(Pageable pageable) {
        log.debug("REST request to get a page of DetailSorties");
        Page<DetailSortie> page = detailSortieRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detail-sorties/:id} : get the "id" detailSortie.
     *
     * @param id the id of the detailSortie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailSortie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detail-sorties/{id}")
    public ResponseEntity<DetailSortie> getDetailSortie(@PathVariable Long id) {
        log.debug("REST request to get DetailSortie : {}", id);
        Optional<DetailSortie> detailSortie = detailSortieRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(detailSortie);
    }

    /**
     * {@code DELETE  /detail-sorties/:id} : delete the "id" detailSortie.
     *
     * @param id the id of the detailSortie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detail-sorties/{id}")
    public ResponseEntity<Void> deleteDetailSortie(@PathVariable Long id) {
        log.debug("REST request to delete DetailSortie : {}", id);
        detailSortieRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
