package sn.coundoul.gestion.equipement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
import sn.coundoul.gestion.equipement.domain.Equipement;
import sn.coundoul.gestion.equipement.repository.EquipementRepository;
import sn.coundoul.gestion.equipement.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.coundoul.gestion.equipement.domain.Equipement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EquipementResource {

    private final Logger log = LoggerFactory.getLogger(EquipementResource.class);

    private static final String ENTITY_NAME = "gestionEquipementEquipement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipementRepository equipementRepository;

    // private final SecurityUtils securityUtils;

    public EquipementResource(EquipementRepository equipementRepository) {
        this.equipementRepository = equipementRepository;
    }

    /**
     * {@code POST  /equipements} : Create a new equipement.
     *
     * @param equipement the equipement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipement, or with status {@code 400 (Bad Request)} if the equipement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipements")
    public ResponseEntity<Equipement> createEquipement(@Valid @RequestBody Equipement equipement) throws URISyntaxException {
        log.debug("REST request to save Equipement : {}", equipement);
        if (equipement.getId() != null) {
            throw new BadRequestAlertException("A new equipement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Equipement result = equipementRepository.save(equipement);
        return ResponseEntity
            .created(new URI("/api/equipements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipements/:id} : Updates an existing equipement.
     *
     * @param id the id of the equipement to save.
     * @param equipement the equipement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipement,
     * or with status {@code 400 (Bad Request)} if the equipement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipements/{id}")
    public ResponseEntity<Equipement> updateEquipement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Equipement equipement
    ) throws URISyntaxException {
        log.debug("REST request to update Equipement : {}, {}", id, equipement);
        if (equipement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Equipement result = equipementRepository.save(equipement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /equipements/:id} : Partial updates given fields of an existing equipement, field will ignore if it is null
     *
     * @param id the id of the equipement to save.
     * @param equipement the equipement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipement,
     * or with status {@code 400 (Bad Request)} if the equipement is not valid,
     * or with status {@code 404 (Not Found)} if the equipement is not found,
     * or with status {@code 500 (Internal Server Error)} if the equipement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/equipements/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Equipement> partialUpdateEquipement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Equipement equipement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Equipement partially : {}, {}", id, equipement);
        if (equipement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Equipement> result = equipementRepository
            .findById(equipement.getId())
            .map(
                existingEquipement -> {
                    if (equipement.getReference() != null) {
                        existingEquipement.setReference(equipement.getReference());
                    }
                    if (equipement.getDescription() != null) {
                        existingEquipement.setDescription(equipement.getDescription());
                    }
                    if (equipement.getTypeMatiere() != null) {
                        existingEquipement.setTypeMatiere(equipement.getTypeMatiere());
                    }
                    if (equipement.getQuantite() != null) {
                        existingEquipement.setQuantite(equipement.getQuantite());
                    }
                    if (equipement.getEtatMatiere() != null) {
                        existingEquipement.setEtatMatiere(equipement.getEtatMatiere());
                    }
                    if (equipement.getGroup() != null) {
                        existingEquipement.setGroup(equipement.getGroup());
                    }
                    if (equipement.getPhoto() != null) {
                        existingEquipement.setPhoto(equipement.getPhoto());
                    }
                    if (equipement.getPhotoContentType() != null) {
                        existingEquipement.setPhotoContentType(equipement.getPhotoContentType());
                    }

                    return existingEquipement;
                }
            )
            .map(equipementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipement.getId().toString())
        );
    }

    /**
     * {@code GET  /equipements} : get all the equipements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipements in body.
     */
    @GetMapping("/equipements")
    public ResponseEntity<List<Equipement>> getAllEquipements(Pageable pageable) {
        log.debug("REST request to get a page of Equipements");
        // if(SecurityUtils.hasCurrentUserThisAuthority("COMPTABLE_PRINCIPALE")){
        //     Page<Equipement> page = equipementRepository.findAllequipementComptable(pageable);
        //     HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        //     return ResponseEntity.ok().headers(headers).body(page.getContent());
        // }

        // else if(SecurityUtils.hasCurrentUserThisAuthority("ROLE_SECONDAIRE")){
        //     // Page<Equipement> page = equipementRepository.findAllE(pageable);
        //     // HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        //     // return ResponseEntity.ok().headers(headers).body(page.getContent());
        // }

        Page<Equipement> page = equipementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/equipements/statics")
    public ResponseEntity<List<Object>> Statistique(Pageable pageable) {
        log.debug("REST request to get a page of statique matiere equipements");
        Page<Object> page = equipementRepository.statiqueMatiere(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equipements/:id} : get the "id" equipement.
     *
     * @param id the id of the equipement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipement, or with status {@code 404 (Not Found)}.
     */

    @GetMapping("/equipements/{id}")
    public ResponseEntity<Equipement> getEquipement(@PathVariable Long id) {
        log.debug("REST request to get Equipement : {}", id);
        Optional<Equipement> equipement = equipementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(equipement);
    }

    @GetMapping("/equipements-categorie/{id}")
    public ResponseEntity<List<Equipement>> getAllEquipementCategorie(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get Equipement Par Categorie : {} id", id);
        Page<Equipement> page = equipementRepository.findAllCategorieEquipement(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/equipements/EtatMatieres")
    public ResponseEntity<List<Equipement>> getAllEquipementEtat(Pageable pageable) {
        log.debug("REST request to get Equipement Etat des Matieres");
        Page<Equipement> page = equipementRepository.findEtatMatiere(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code DELETE  /equipements/:id} : delete the "id" equipement.
     *
     * @param id the id of the equipement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

    @DeleteMapping("/equipements/{id}")
    public ResponseEntity<Void> deleteEquipement(@PathVariable Long id) {
        log.debug("REST request to delete Equipement : {}", id);
        equipementRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/equipements/{reference}/{etatMatiere}/{dateSignalisation}")
    public ResponseEntity<Void> updateEtatMatiere(
        @PathVariable String reference,
        @PathVariable String etatMatiere,
        @PathVariable Instant dateSignalisation
    ) {
        log.debug("REST request to update Equipement reference : {}", reference);
        equipementRepository.updateEtatMatiere(reference, etatMatiere, dateSignalisation);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reference.toString()))
            .build();
    }
}
