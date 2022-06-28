package sn.coundoul.gestion.infrastructure.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
import sn.coundoul.gestion.infrastructure.domain.ContratProjet;
import sn.coundoul.gestion.infrastructure.repository.ContratProjetRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.ContratProjet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContratProjetResource {

    private final Logger log = LoggerFactory.getLogger(ContratProjetResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureContratProjet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContratProjetRepository contratProjetRepository;

    public ContratProjetResource(ContratProjetRepository contratProjetRepository) {
        this.contratProjetRepository = contratProjetRepository;
    }

    /**
     * {@code POST  /contrat-projets} : Create a new contratProjet.
     *
     * @param contratProjet the contratProjet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contratProjet, or with status {@code 400 (Bad Request)} if the contratProjet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contrat-projets")
    public ResponseEntity<ContratProjet> createContratProjet(@Valid @RequestBody ContratProjet contratProjet) throws URISyntaxException {
        log.debug("REST request to save ContratProjet : {}", contratProjet);
        if (contratProjet.getId() != null) {
            throw new BadRequestAlertException("A new contratProjet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContratProjet result = contratProjetRepository.save(contratProjet);
        return ResponseEntity
            .created(new URI("/api/contrat-projets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contrat-projets/:id} : Updates an existing contratProjet.
     *
     * @param id the id of the contratProjet to save.
     * @param contratProjet the contratProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contratProjet,
     * or with status {@code 400 (Bad Request)} if the contratProjet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contratProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contrat-projets/{id}")
    public ResponseEntity<ContratProjet> updateContratProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContratProjet contratProjet
    ) throws URISyntaxException {
        log.debug("REST request to update ContratProjet : {}, {}", id, contratProjet);
        if (contratProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contratProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contratProjetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContratProjet result = contratProjetRepository.save(contratProjet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contratProjet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contrat-projets/:id} : Partial updates given fields of an existing contratProjet, field will ignore if it is null
     *
     * @param id the id of the contratProjet to save.
     * @param contratProjet the contratProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contratProjet,
     * or with status {@code 400 (Bad Request)} if the contratProjet is not valid,
     * or with status {@code 404 (Not Found)} if the contratProjet is not found,
     * or with status {@code 500 (Internal Server Error)} if the contratProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contrat-projets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ContratProjet> partialUpdateContratProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContratProjet contratProjet
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContratProjet partially : {}, {}", id, contratProjet);
        if (contratProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contratProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contratProjetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContratProjet> result = contratProjetRepository
            .findById(contratProjet.getId())
            .map(
                existingContratProjet -> {
                    if (contratProjet.getNom() != null) {
                        existingContratProjet.setNom(contratProjet.getNom());
                    }

                    return existingContratProjet;
                }
            )
            .map(contratProjetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contratProjet.getId().toString())
        );
    }

    /**
     * {@code GET  /contrat-projets} : get all the contratProjets.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contratProjets in body.
     */
    @GetMapping("/contrat-projets")
    public ResponseEntity<List<ContratProjet>> getAllContratProjets(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("nomprojet-is-null".equals(filter)) {
            log.debug("REST request to get all ContratProjets where nomProjet is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(contratProjetRepository.findAll().spliterator(), false)
                    .filter(contratProjet -> contratProjet.getNomProjet() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }
        log.debug("REST request to get a page of ContratProjets");
        Page<ContratProjet> page = contratProjetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contrat-projets/:id} : get the "id" contratProjet.
     *
     * @param id the id of the contratProjet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contratProjet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contrat-projets/{id}")
    public ResponseEntity<ContratProjet> getContratProjet(@PathVariable Long id) {
        log.debug("REST request to get ContratProjet : {}", id);
        Optional<ContratProjet> contratProjet = contratProjetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contratProjet);
    }

    /**
     * {@code DELETE  /contrat-projets/:id} : delete the "id" contratProjet.
     *
     * @param id the id of the contratProjet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contrat-projets/{id}")
    public ResponseEntity<Void> deleteContratProjet(@PathVariable Long id) {
        log.debug("REST request to delete ContratProjet : {}", id);
        contratProjetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
