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
import sn.coundoul.gestion.utilisateurs.domain.ChefEtablissement;
import sn.coundoul.gestion.utilisateurs.repository.ChefEtablissementRepository;
import sn.coundoul.gestion.utilisateurs.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.utilisateurs.domain.ChefEtablissement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ChefEtablissementResource {

    private final Logger log = LoggerFactory.getLogger(ChefEtablissementResource.class);

    private static final String ENTITY_NAME = "gestionUtilisateursChefEtablissement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChefEtablissementRepository chefEtablissementRepository;

    public ChefEtablissementResource(ChefEtablissementRepository chefEtablissementRepository) {
        this.chefEtablissementRepository = chefEtablissementRepository;
    }

    /**
     * {@code POST  /chef-etablissements} : Create a new chefEtablissement.
     *
     * @param chefEtablissement the chefEtablissement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chefEtablissement, or with status {@code 400 (Bad Request)} if the chefEtablissement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chef-etablissements")
    public ResponseEntity<ChefEtablissement> createChefEtablissement(@Valid @RequestBody ChefEtablissement chefEtablissement)
        throws URISyntaxException {
        log.debug("REST request to save ChefEtablissement : {}", chefEtablissement);
        if (chefEtablissement.getId() != null) {
            throw new BadRequestAlertException("A new chefEtablissement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChefEtablissement result = chefEtablissementRepository.save(chefEtablissement);
        return ResponseEntity
            .created(new URI("/api/chef-etablissements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chef-etablissements/:id} : Updates an existing chefEtablissement.
     *
     * @param id the id of the chefEtablissement to save.
     * @param chefEtablissement the chefEtablissement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chefEtablissement,
     * or with status {@code 400 (Bad Request)} if the chefEtablissement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chefEtablissement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chef-etablissements/{id}")
    public ResponseEntity<ChefEtablissement> updateChefEtablissement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChefEtablissement chefEtablissement
    ) throws URISyntaxException {
        log.debug("REST request to update ChefEtablissement : {}, {}", id, chefEtablissement);
        if (chefEtablissement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chefEtablissement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chefEtablissementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChefEtablissement result = chefEtablissementRepository.save(chefEtablissement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chefEtablissement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chef-etablissements/:id} : Partial updates given fields of an existing chefEtablissement, field will ignore if it is null
     *
     * @param id the id of the chefEtablissement to save.
     * @param chefEtablissement the chefEtablissement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chefEtablissement,
     * or with status {@code 400 (Bad Request)} if the chefEtablissement is not valid,
     * or with status {@code 404 (Not Found)} if the chefEtablissement is not found,
     * or with status {@code 500 (Internal Server Error)} if the chefEtablissement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chef-etablissements/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ChefEtablissement> partialUpdateChefEtablissement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChefEtablissement chefEtablissement
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChefEtablissement partially : {}, {}", id, chefEtablissement);
        if (chefEtablissement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chefEtablissement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chefEtablissementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChefEtablissement> result = chefEtablissementRepository
            .findById(chefEtablissement.getId())
            .map(
                existingChefEtablissement -> {
                    if (chefEtablissement.getNomPers() != null) {
                        existingChefEtablissement.setNomPers(chefEtablissement.getNomPers());
                    }
                    if (chefEtablissement.getPrenomPers() != null) {
                        existingChefEtablissement.setPrenomPers(chefEtablissement.getPrenomPers());
                    }
                    if (chefEtablissement.getSexe() != null) {
                        existingChefEtablissement.setSexe(chefEtablissement.getSexe());
                    }
                    if (chefEtablissement.getMobile() != null) {
                        existingChefEtablissement.setMobile(chefEtablissement.getMobile());
                    }
                    if (chefEtablissement.getAdresse() != null) {
                        existingChefEtablissement.setAdresse(chefEtablissement.getAdresse());
                    }

                    return existingChefEtablissement;
                }
            )
            .map(chefEtablissementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chefEtablissement.getId().toString())
        );
    }

    /**
     * {@code GET  /chef-etablissements} : get all the chefEtablissements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chefEtablissements in body.
     */
    @GetMapping("/chef-etablissements")
    public ResponseEntity<List<ChefEtablissement>> getAllChefEtablissements(Pageable pageable) {
        log.debug("REST request to get a page of ChefEtablissements");
        Page<ChefEtablissement> page = chefEtablissementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chef-etablissements/:id} : get the "id" chefEtablissement.
     *
     * @param id the id of the chefEtablissement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chefEtablissement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chef-etablissements/{id}")
    public ResponseEntity<ChefEtablissement> getChefEtablissement(@PathVariable Long id) {
        log.debug("REST request to get ChefEtablissement : {}", id);
        Optional<ChefEtablissement> chefEtablissement = chefEtablissementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(chefEtablissement);
    }

    /**
     * {@code DELETE  /chef-etablissements/:id} : delete the "id" chefEtablissement.
     *
     * @param id the id of the chefEtablissement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chef-etablissements/{id}")
    public ResponseEntity<Void> deleteChefEtablissement(@PathVariable Long id) {
        log.debug("REST request to delete ChefEtablissement : {}", id);
        chefEtablissementRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
