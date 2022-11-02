package sn.coundoul.gestion.equipement.repository;

import java.lang.String;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.equipement.domain.Equipement;

/**
 * Spring Data SQL repository for the Equipement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipementRepository extends JpaRepository<Equipement, Long> {
    @Query(
        value = "select e.reference, sum(e.quantite) as entreeTotal, sum(a.quantite_affecter) as sortieTotal, count(e.reference) as compter from equipement e, affectations a where e.id=a.equipement_id group by e.reference",
        nativeQuery = true
    )
    Page<Object> tableauInventaire(Pageable pageable);

    @Query(
        value = "select e.reference, e.quantite, e.etat_matiere, a.type_attribution, a.quantite_affecter, a.date_attribution, a.beneficiaire, a.id_pers from equipement e, affectations a where e.id=a.equipement_id AND e.reference =:reference",
        nativeQuery = true
    )
    Page<Object> detailInventaire(@Param("reference") String reference, Pageable pageable);

    @Query(
        value = "select e.id, reference, nom_matiere, quantite, jhi_group, c.categorie from equipement e, categorie_matiere c where e.categorie_id=c.id AND e.reference =:reference",
        nativeQuery = true
    )
    Page<Object> rechercherEquipement(@Param("reference") String reference, Pageable pageable);

    @Query(
        value = "select DISTINCT e.id, reference, nom_matiere, quantite, caracteristique, beneficiaire, type_matiere, etat_matiere, jhi_group, photo, photo_content_type, photo1, photo1_content_type, photo2, photo2_content_type, categorie, nom_magazin, prenom_fournisseur, nom_fournisseur, b.id  from equipement e, magazin m, fournisseur f, bon b, categorie_matiere c where e.categorie_id=c.id AND m.id=e.nom_magazin_id AND f.id=e.nom_fournisseur_id AND b.id=e.bon_id AND e.reference =:reference",
        nativeQuery = true
    )
    Page<Object> detailRechercherEquipement(@Param("reference") String reference, Pageable pageable);

    @Query(
        value = "select DISTINCT e.reference, e.nom_matiere, c.categorie, e.caracteristique, a.beneficiaire, e.etat_matiere, e.date_signalisation, e.photo, e.photo_content_type from equipement e, affectations a, categorie_matiere c where e.categorie_id=c.id and e.id=a.equipement_id and e.etat_matiere='Mauvais Etat' or e.categorie_id=c.id and e.id=a.equipement_id and e.etat_matiere='Hors Service'",
        nativeQuery = true
    )
    Page<Object> categoriseEquipement(Pageable pageable);

    @Modifying
    @Query(
        value = "update equipement e SET e.etat_matiere=:etatMatiere, e.date_signalisation=:dateSignalisation WHERE e.reference=:reference",
        nativeQuery = true
    )
    void updateEtatMatiere(
        @Param("reference") String reference,
        @Param("etatMatiere") String etatMatiere,
        @Param("dateSignalisation") Instant dateSignalisation
    );
}
