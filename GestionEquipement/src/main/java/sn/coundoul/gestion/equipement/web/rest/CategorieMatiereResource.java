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
import sn.coundoul.gestion.equipement.domain.CategorieMatiere;
import sn.coundoul.gestion.equipement.repository.CategorieMatiereRepository;
import sn.coundoul.gestion.equipement.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.equipement.domain.CategorieMatiere}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CategorieMatiereResource {

    private final Logger log = LoggerFactory.getLogger(CategorieMatiereResource.class);

    private static final String ENTITY_NAME = "gestionEquipementCategorieMatiere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategorieMatiereRepository categorieMatiereRepository;

    public CategorieMatiereResource(CategorieMatiereRepository categorieMatiereRepository) {
        this.categorieMatiereRepository = categorieMatiereRepository;
    }

    /**
     * {@code POST  /categorie-matieres} : Create a new categorieMatiere.
     *
     * @param categorieMatiere the categorieMatiere to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categorieMatiere, or with status {@code 400 (Bad Request)} if the categorieMatiere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categorie-matieres")
    public ResponseEntity<CategorieMatiere> createCategorieMatiere(@Valid @RequestBody CategorieMatiere categorieMatiere)
        throws URISyntaxException {
        log.debug("REST request to save CategorieMatiere : {}", categorieMatiere);
        if (categorieMatiere.getId() != null) {
            throw new BadRequestAlertException("A new categorieMatiere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategorieMatiere result = categorieMatiereRepository.save(categorieMatiere);
        return ResponseEntity
            .created(new URI("/api/categorie-matieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categorie-matieres/:id} : Updates an existing categorieMatiere.
     *
     * @param id the id of the categorieMatiere to save.
     * @param categorieMatiere the categorieMatiere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieMatiere,
     * or with status {@code 400 (Bad Request)} if the categorieMatiere is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categorieMatiere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categorie-matieres/{id}")
    public ResponseEntity<CategorieMatiere> updateCategorieMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategorieMatiere categorieMatiere
    ) throws URISyntaxException {
        log.debug("REST request to update CategorieMatiere : {}, {}", id, categorieMatiere);
        if (categorieMatiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorieMatiere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorieMatiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategorieMatiere result = categorieMatiereRepository.save(categorieMatiere);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorieMatiere.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categorie-matieres/:id} : Partial updates given fields of an existing categorieMatiere, field will ignore if it is null
     *
     * @param id the id of the categorieMatiere to save.
     * @param categorieMatiere the categorieMatiere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieMatiere,
     * or with status {@code 400 (Bad Request)} if the categorieMatiere is not valid,
     * or with status {@code 404 (Not Found)} if the categorieMatiere is not found,
     * or with status {@code 500 (Internal Server Error)} if the categorieMatiere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categorie-matieres/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CategorieMatiere> partialUpdateCategorieMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategorieMatiere categorieMatiere
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategorieMatiere partially : {}, {}", id, categorieMatiere);
        if (categorieMatiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorieMatiere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorieMatiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategorieMatiere> result = categorieMatiereRepository
            .findById(categorieMatiere.getId())
            .map(
                existingCategorieMatiere -> {
                    if (categorieMatiere.getCategorie() != null) {
                        existingCategorieMatiere.setCategorie(categorieMatiere.getCategorie());
                    }

                    return existingCategorieMatiere;
                }
            )
            .map(categorieMatiereRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorieMatiere.getId().toString())
        );
    }

    /**
     * {@code GET  /categorie-matieres} : get all the categorieMatieres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categorieMatieres in body.
     */
    @GetMapping("/categorie-matieres")
    public ResponseEntity<List<CategorieMatiere>> getAllCategorieMatieres(Pageable pageable) {
        log.debug("REST request to get a page of CategorieMatieres");
        Page<CategorieMatiere> page = categorieMatiereRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /categorie-matieres/:id} : get the "id" categorieMatiere.
     *
     * @param id the id of the categorieMatiere to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categorieMatiere, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categorie-matieres/{id}")
    public ResponseEntity<CategorieMatiere> getCategorieMatiere(@PathVariable Long id) {
        log.debug("REST request to get CategorieMatiere : {}", id);
        Optional<CategorieMatiere> categorieMatiere = categorieMatiereRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(categorieMatiere);
    }

    /**
     * {@code DELETE  /categorie-matieres/:id} : delete the "id" categorieMatiere.
     *
     * @param id the id of the categorieMatiere to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categorie-matieres/{id}")
    public ResponseEntity<Void> deleteCategorieMatiere(@PathVariable Long id) {
        log.debug("REST request to delete CategorieMatiere : {}", id);
        categorieMatiereRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
