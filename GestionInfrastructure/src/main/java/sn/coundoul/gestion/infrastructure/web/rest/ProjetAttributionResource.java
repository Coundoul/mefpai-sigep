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
import sn.coundoul.gestion.infrastructure.domain.ProjetAttribution;
import sn.coundoul.gestion.infrastructure.repository.ProjetAttributionRepository;
import sn.coundoul.gestion.infrastructure.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.infrastructure.domain.ProjetAttribution}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProjetAttributionResource {

    private final Logger log = LoggerFactory.getLogger(ProjetAttributionResource.class);

    private static final String ENTITY_NAME = "gestionInfrastructureProjetAttribution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjetAttributionRepository projetAttributionRepository;

    public ProjetAttributionResource(ProjetAttributionRepository projetAttributionRepository) {
        this.projetAttributionRepository = projetAttributionRepository;
    }

    /**
     * {@code POST  /projet-attributions} : Create a new projetAttribution.
     *
     * @param projetAttribution the projetAttribution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projetAttribution, or with status {@code 400 (Bad Request)} if the projetAttribution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/projet-attributions")
    public ResponseEntity<ProjetAttribution> createProjetAttribution(@Valid @RequestBody ProjetAttribution projetAttribution)
        throws URISyntaxException {
        log.debug("REST request to save ProjetAttribution : {}", projetAttribution);
        if (projetAttribution.getId() != null) {
            throw new BadRequestAlertException("A new projetAttribution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjetAttribution result = projetAttributionRepository.save(projetAttribution);
        return ResponseEntity
            .created(new URI("/api/projet-attributions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /projet-attributions/:id} : Updates an existing projetAttribution.
     *
     * @param id the id of the projetAttribution to save.
     * @param projetAttribution the projetAttribution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projetAttribution,
     * or with status {@code 400 (Bad Request)} if the projetAttribution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projetAttribution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/projet-attributions/{id}")
    public ResponseEntity<ProjetAttribution> updateProjetAttribution(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProjetAttribution projetAttribution
    ) throws URISyntaxException {
        log.debug("REST request to update ProjetAttribution : {}, {}", id, projetAttribution);
        if (projetAttribution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projetAttribution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projetAttributionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProjetAttribution result = projetAttributionRepository.save(projetAttribution);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projetAttribution.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /projet-attributions/:id} : Partial updates given fields of an existing projetAttribution, field will ignore if it is null
     *
     * @param id the id of the projetAttribution to save.
     * @param projetAttribution the projetAttribution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projetAttribution,
     * or with status {@code 400 (Bad Request)} if the projetAttribution is not valid,
     * or with status {@code 404 (Not Found)} if the projetAttribution is not found,
     * or with status {@code 500 (Internal Server Error)} if the projetAttribution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/projet-attributions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProjetAttribution> partialUpdateProjetAttribution(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProjetAttribution projetAttribution
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProjetAttribution partially : {}, {}", id, projetAttribution);
        if (projetAttribution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projetAttribution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projetAttributionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProjetAttribution> result = projetAttributionRepository
            .findById(projetAttribution.getId())
            .map(
                existingProjetAttribution -> {
                    if (projetAttribution.getDateAttribution() != null) {
                        existingProjetAttribution.setDateAttribution(projetAttribution.getDateAttribution());
                    }
                    if (projetAttribution.getQuantite() != null) {
                        existingProjetAttribution.setQuantite(projetAttribution.getQuantite());
                    }
                    if (projetAttribution.getIdEquipement() != null) {
                        existingProjetAttribution.setIdEquipement(projetAttribution.getIdEquipement());
                    }
                    if (projetAttribution.getIdPers() != null) {
                        existingProjetAttribution.setIdPers(projetAttribution.getIdPers());
                    }

                    return existingProjetAttribution;
                }
            )
            .map(projetAttributionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projetAttribution.getId().toString())
        );
    }

    /**
     * {@code GET  /projet-attributions} : get all the projetAttributions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projetAttributions in body.
     */
    @GetMapping("/projet-attributions")
    public ResponseEntity<List<ProjetAttribution>> getAllProjetAttributions(Pageable pageable) {
        log.debug("REST request to get a page of ProjetAttributions");
        Page<ProjetAttribution> page = projetAttributionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /projet-attributions/:id} : get the "id" projetAttribution.
     *
     * @param id the id of the projetAttribution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projetAttribution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/projet-attributions/{id}")
    public ResponseEntity<ProjetAttribution> getProjetAttribution(@PathVariable Long id) {
        log.debug("REST request to get ProjetAttribution : {}", id);
        Optional<ProjetAttribution> projetAttribution = projetAttributionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(projetAttribution);
    }

    /**
     * {@code DELETE  /projet-attributions/:id} : delete the "id" projetAttribution.
     *
     * @param id the id of the projetAttribution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/projet-attributions/{id}")
    public ResponseEntity<Void> deleteProjetAttribution(@PathVariable Long id) {
        log.debug("REST request to delete ProjetAttribution : {}", id);
        projetAttributionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
