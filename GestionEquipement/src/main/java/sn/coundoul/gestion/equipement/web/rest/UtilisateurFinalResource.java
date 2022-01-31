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
import sn.coundoul.gestion.equipement.domain.UtilisateurFinal;
import sn.coundoul.gestion.equipement.repository.UtilisateurFinalRepository;
import sn.coundoul.gestion.equipement.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.equipement.domain.UtilisateurFinal}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UtilisateurFinalResource {

    private final Logger log = LoggerFactory.getLogger(UtilisateurFinalResource.class);

    private static final String ENTITY_NAME = "gestionEquipementUtilisateurFinal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtilisateurFinalRepository utilisateurFinalRepository;

    public UtilisateurFinalResource(UtilisateurFinalRepository utilisateurFinalRepository) {
        this.utilisateurFinalRepository = utilisateurFinalRepository;
    }

    /**
     * {@code POST  /utilisateur-finals} : Create a new utilisateurFinal.
     *
     * @param utilisateurFinal the utilisateurFinal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utilisateurFinal, or with status {@code 400 (Bad Request)} if the utilisateurFinal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/utilisateur-finals")
    public ResponseEntity<UtilisateurFinal> createUtilisateurFinal(@Valid @RequestBody UtilisateurFinal utilisateurFinal)
        throws URISyntaxException {
        log.debug("REST request to save UtilisateurFinal : {}", utilisateurFinal);
        if (utilisateurFinal.getId() != null) {
            throw new BadRequestAlertException("A new utilisateurFinal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UtilisateurFinal result = utilisateurFinalRepository.save(utilisateurFinal);
        return ResponseEntity
            .created(new URI("/api/utilisateur-finals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /utilisateur-finals/:id} : Updates an existing utilisateurFinal.
     *
     * @param id the id of the utilisateurFinal to save.
     * @param utilisateurFinal the utilisateurFinal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisateurFinal,
     * or with status {@code 400 (Bad Request)} if the utilisateurFinal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utilisateurFinal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/utilisateur-finals/{id}")
    public ResponseEntity<UtilisateurFinal> updateUtilisateurFinal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UtilisateurFinal utilisateurFinal
    ) throws URISyntaxException {
        log.debug("REST request to update UtilisateurFinal : {}, {}", id, utilisateurFinal);
        if (utilisateurFinal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateurFinal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisateurFinalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UtilisateurFinal result = utilisateurFinalRepository.save(utilisateurFinal);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisateurFinal.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /utilisateur-finals/:id} : Partial updates given fields of an existing utilisateurFinal, field will ignore if it is null
     *
     * @param id the id of the utilisateurFinal to save.
     * @param utilisateurFinal the utilisateurFinal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisateurFinal,
     * or with status {@code 400 (Bad Request)} if the utilisateurFinal is not valid,
     * or with status {@code 404 (Not Found)} if the utilisateurFinal is not found,
     * or with status {@code 500 (Internal Server Error)} if the utilisateurFinal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/utilisateur-finals/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UtilisateurFinal> partialUpdateUtilisateurFinal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UtilisateurFinal utilisateurFinal
    ) throws URISyntaxException {
        log.debug("REST request to partial update UtilisateurFinal partially : {}, {}", id, utilisateurFinal);
        if (utilisateurFinal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateurFinal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisateurFinalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UtilisateurFinal> result = utilisateurFinalRepository
            .findById(utilisateurFinal.getId())
            .map(
                existingUtilisateurFinal -> {
                    if (utilisateurFinal.getNomUtilisateur() != null) {
                        existingUtilisateurFinal.setNomUtilisateur(utilisateurFinal.getNomUtilisateur());
                    }
                    if (utilisateurFinal.getPrenomUtilisateur() != null) {
                        existingUtilisateurFinal.setPrenomUtilisateur(utilisateurFinal.getPrenomUtilisateur());
                    }
                    if (utilisateurFinal.getEmailInstitutionnel() != null) {
                        existingUtilisateurFinal.setEmailInstitutionnel(utilisateurFinal.getEmailInstitutionnel());
                    }
                    if (utilisateurFinal.getMobile() != null) {
                        existingUtilisateurFinal.setMobile(utilisateurFinal.getMobile());
                    }
                    if (utilisateurFinal.getSexe() != null) {
                        existingUtilisateurFinal.setSexe(utilisateurFinal.getSexe());
                    }
                    if (utilisateurFinal.getDepartement() != null) {
                        existingUtilisateurFinal.setDepartement(utilisateurFinal.getDepartement());
                    }
                    if (utilisateurFinal.getServiceDep() != null) {
                        existingUtilisateurFinal.setServiceDep(utilisateurFinal.getServiceDep());
                    }

                    return existingUtilisateurFinal;
                }
            )
            .map(utilisateurFinalRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisateurFinal.getId().toString())
        );
    }

    /**
     * {@code GET  /utilisateur-finals} : get all the utilisateurFinals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utilisateurFinals in body.
     */
    @GetMapping("/utilisateur-finals")
    public ResponseEntity<List<UtilisateurFinal>> getAllUtilisateurFinals(Pageable pageable) {
        log.debug("REST request to get a page of UtilisateurFinals");
        Page<UtilisateurFinal> page = utilisateurFinalRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /utilisateur-finals/:id} : get the "id" utilisateurFinal.
     *
     * @param id the id of the utilisateurFinal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utilisateurFinal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/utilisateur-finals/{id}")
    public ResponseEntity<UtilisateurFinal> getUtilisateurFinal(@PathVariable Long id) {
        log.debug("REST request to get UtilisateurFinal : {}", id);
        Optional<UtilisateurFinal> utilisateurFinal = utilisateurFinalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(utilisateurFinal);
    }

    /**
     * {@code DELETE  /utilisateur-finals/:id} : delete the "id" utilisateurFinal.
     *
     * @param id the id of the utilisateurFinal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/utilisateur-finals/{id}")
    public ResponseEntity<Void> deleteUtilisateurFinal(@PathVariable Long id) {
        log.debug("REST request to delete UtilisateurFinal : {}", id);
        utilisateurFinalRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
