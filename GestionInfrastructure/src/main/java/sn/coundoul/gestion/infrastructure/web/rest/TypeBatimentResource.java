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
import sn.coundoul.gestion.infrastructure.domain.TypeBatiment;
import sn.coundoul.gestion.infrastructure.repository.TypeBatimentRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.TypeBatiment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypeBatimentResource {

    private final Logger log = LoggerFactory.getLogger(TypeBatimentResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureTypeBatiment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeBatimentRepository typeBatimentRepository;

    public TypeBatimentResource(TypeBatimentRepository typeBatimentRepository) {
        this.typeBatimentRepository = typeBatimentRepository;
    }

    /**
     * {@code POST  /type-batiments} : Create a new typeBatiment.
     *
     * @param typeBatiment the typeBatiment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeBatiment, or with status {@code 400 (Bad Request)} if the typeBatiment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-batiments")
    public ResponseEntity<TypeBatiment> createTypeBatiment(@Valid @RequestBody TypeBatiment typeBatiment) throws URISyntaxException {
        log.debug("REST request to save TypeBatiment : {}", typeBatiment);
        if (typeBatiment.getId() != null) {
            throw new BadRequestAlertException("A new typeBatiment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeBatiment result = typeBatimentRepository.save(typeBatiment);
        return ResponseEntity
            .created(new URI("/api/type-batiments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-batiments/:id} : Updates an existing typeBatiment.
     *
     * @param id the id of the typeBatiment to save.
     * @param typeBatiment the typeBatiment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeBatiment,
     * or with status {@code 400 (Bad Request)} if the typeBatiment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeBatiment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-batiments/{id}")
    public ResponseEntity<TypeBatiment> updateTypeBatiment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeBatiment typeBatiment
    ) throws URISyntaxException {
        log.debug("REST request to update TypeBatiment : {}, {}", id, typeBatiment);
        if (typeBatiment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeBatiment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeBatimentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeBatiment result = typeBatimentRepository.save(typeBatiment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeBatiment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-batiments/:id} : Partial updates given fields of an existing typeBatiment, field will ignore if it is null
     *
     * @param id the id of the typeBatiment to save.
     * @param typeBatiment the typeBatiment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeBatiment,
     * or with status {@code 400 (Bad Request)} if the typeBatiment is not valid,
     * or with status {@code 404 (Not Found)} if the typeBatiment is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeBatiment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-batiments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TypeBatiment> partialUpdateTypeBatiment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeBatiment typeBatiment
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeBatiment partially : {}, {}", id, typeBatiment);
        if (typeBatiment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeBatiment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeBatimentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeBatiment> result = typeBatimentRepository
            .findById(typeBatiment.getId())
            .map(
                existingTypeBatiment -> {
                    if (typeBatiment.getTypeBa() != null) {
                        existingTypeBatiment.setTypeBa(typeBatiment.getTypeBa());
                    }

                    return existingTypeBatiment;
                }
            )
            .map(typeBatimentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeBatiment.getId().toString())
        );
    }

    /**
     * {@code GET  /type-batiments} : get all the typeBatiments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeBatiments in body.
     */
    @GetMapping("/type-batiments")
    public ResponseEntity<List<TypeBatiment>> getAllTypeBatiments(Pageable pageable) {
        log.debug("REST request to get a page of TypeBatiments");
        Page<TypeBatiment> page = typeBatimentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-batiments/:id} : get the "id" typeBatiment.
     *
     * @param id the id of the typeBatiment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeBatiment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-batiments/{id}")
    public ResponseEntity<TypeBatiment> getTypeBatiment(@PathVariable Long id) {
        log.debug("REST request to get TypeBatiment : {}", id);
        Optional<TypeBatiment> typeBatiment = typeBatimentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typeBatiment);
    }

    /**
     * {@code DELETE  /type-batiments/:id} : delete the "id" typeBatiment.
     *
     * @param id the id of the typeBatiment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-batiments/{id}")
    public ResponseEntity<Void> deleteTypeBatiment(@PathVariable Long id) {
        log.debug("REST request to delete TypeBatiment : {}", id);
        typeBatimentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
