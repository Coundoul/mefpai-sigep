package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Equipement.
 */
@Table("equipement")
public class Equipement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("reference")
    private String reference;

    @Column("beneficiaire")
    private String description;

    @Column("prix_unitaire")
    private Integer prixUnitaire;

    @NotNull(message = "must not be null")
    @Column("type_matiere")
    private String typeMatiere;

    @NotNull(message = "must not be null")
    @Column("quantite")
    private Integer quantite;

    @NotNull(message = "must not be null")
    @Column("etat_matiere")
    private String etatMatiere;

    @NotNull(message = "must not be null")
    @Column("jhi_group")
    private Boolean group;

    @Column("photo")
    private byte[] photo;

    @Column("photo_content_type")
    private String photoContentType;

    @JsonIgnoreProperties(value = { "nomQuartier", "references" }, allowSetters = true)
    @Transient
    private Magazin nomMagazin;

    @Column("nom_magazin_id")
    private Long nomMagazinId;

    @Transient
    private Fournisseur nomFournisseur;

    @Column("nom_fournisseur_id")
    private Long nomFournisseurId;

    @JsonIgnoreProperties(value = { "references" }, allowSetters = true)
    @Transient
    private Bon bon;

    @Column("bon_id")
    private Long bonId;

    @Transient
    private CategorieMatiere categorie;

    @Column("categorie_id")
    private Long categorieId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Equipement id(Long id) {
        this.id = id;
        return this;
    }

    public String getReference() {
        return this.reference;
    }

    public Equipement reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return this.description;
    }

    public Equipement description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrixUnitaire() {
        return this.prixUnitaire;
    }

    public Equipement prixUnitaire(Integer prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        return this;
    }

    public void setPrixUnitaire(Integer prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getTypeMatiere() {
        return this.typeMatiere;
    }

    public Equipement typeMatiere(String typeMatiere) {
        this.typeMatiere = typeMatiere;
        return this;
    }

    public void setTypeMatiere(String typeMatiere) {
        this.typeMatiere = typeMatiere;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public Equipement quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public String getEtatMatiere() {
        return this.etatMatiere;
    }

    public Equipement etatMatiere(String etatMatiere) {
        this.etatMatiere = etatMatiere;
        return this;
    }

    public void setEtatMatiere(String etatMatiere) {
        this.etatMatiere = etatMatiere;
    }

    public Boolean getGroup() {
        return this.group;
    }

    public Equipement group(Boolean group) {
        this.group = group;
        return this;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Equipement photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Equipement photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Magazin getNomMagazin() {
        return this.nomMagazin;
    }

    public Equipement nomMagazin(Magazin magazin) {
        this.setNomMagazin(magazin);
        this.nomMagazinId = magazin != null ? magazin.getId() : null;
        return this;
    }

    public void setNomMagazin(Magazin magazin) {
        this.nomMagazin = magazin;
        this.nomMagazinId = magazin != null ? magazin.getId() : null;
    }

    public Long getNomMagazinId() {
        return this.nomMagazinId;
    }

    public void setNomMagazinId(Long magazin) {
        this.nomMagazinId = magazin;
    }

    public Fournisseur getNomFournisseur() {
        return this.nomFournisseur;
    }

    public Equipement nomFournisseur(Fournisseur fournisseur) {
        this.setNomFournisseur(fournisseur);
        this.nomFournisseurId = fournisseur != null ? fournisseur.getId() : null;
        return this;
    }

    public void setNomFournisseur(Fournisseur fournisseur) {
        this.nomFournisseur = fournisseur;
        this.nomFournisseurId = fournisseur != null ? fournisseur.getId() : null;
    }

    public Long getNomFournisseurId() {
        return this.nomFournisseurId;
    }

    public void setNomFournisseurId(Long fournisseur) {
        this.nomFournisseurId = fournisseur;
    }

    public Bon getBon() {
        return this.bon;
    }

    public Equipement bon(Bon bon) {
        this.setBon(bon);
        this.bonId = bon != null ? bon.getId() : null;
        return this;
    }

    public void setBon(Bon bon) {
        this.bon = bon;
        this.bonId = bon != null ? bon.getId() : null;
    }

    public Long getBonId() {
        return this.bonId;
    }

    public void setBonId(Long bon) {
        this.bonId = bon;
    }

    public CategorieMatiere getCategorie() {
        return this.categorie;
    }

    public Equipement categorie(CategorieMatiere categorieMatiere) {
        this.setCategorie(categorieMatiere);
        this.categorieId = categorieMatiere != null ? categorieMatiere.getId() : null;
        return this;
    }

    public void setCategorie(CategorieMatiere categorieMatiere) {
        this.categorie = categorieMatiere;
        this.categorieId = categorieMatiere != null ? categorieMatiere.getId() : null;
    }

    public Long getCategorieId() {
        return this.categorieId;
    }

    public void setCategorieId(Long categorieMatiere) {
        this.categorieId = categorieMatiere;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Equipement)) {
            return false;
        }
        return id != null && id.equals(((Equipement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Equipement{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", description='" + getDescription() + "'" +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", typeMatiere='" + getTypeMatiere() + "'" +
            ", quantite=" + getQuantite() +
            ", etatMatiere='" + getEtatMatiere() + "'" +
            ", group='" + getGroup() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            "}";
    }
}
