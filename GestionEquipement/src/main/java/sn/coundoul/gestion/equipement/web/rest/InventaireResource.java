package sn.coundoul.gestion.equipement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.lang.System.*;
import java.util.*;
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
import sn.coundoul.gestion.equipement.repository.EquipementRepository;
import sn.coundoul.gestion.equipement.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import java.lang.String;

/**
 * REST controller for managing {@link sn.coundoul.gestion.equipement.domain.Equipement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InventaireResource {

    private final Logger log = LoggerFactory.getLogger(InventaireResource.class);

    private static final String ENTITY_NAME = "gestionEquipementEquipement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipementRepository inventaireRepository;


    public InventaireResource(EquipementRepository inventaireRepository) {
        this.inventaireRepository = inventaireRepository;
    }

    @GetMapping("/inventaires")
    public ResponseEntity<List<Object>> listtableauInventaire(Pageable pageable) {
        log.debug("REST request to get a page of  Inventaire Equipements");
        Page<Object> page = inventaireRepository.tableauInventaire(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/inventaires/categorie")
    public ResponseEntity<List<Object>> listtableauEtatInventaire(Pageable pageable) {
        log.debug("REST request to get a page of Equipements Classe Par Categorie");
        Page<Object> page = inventaireRepository.categoriseEquipement(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/inventaires/{reference}")
    public ResponseEntity<List<Object>> getDetailEquipement(@PathVariable String reference, Pageable pageable) {
        log.debug("REST request to get detail equipement inventaire : {}", reference);
        Page<Object> detail = inventaireRepository.detailInventaire(reference, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), detail);
        return ResponseEntity.ok().headers(headers).body(detail.getContent());
    }
}
