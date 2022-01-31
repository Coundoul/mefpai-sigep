package sn.coundoul.gestion.equipement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.Instant;

/**
 * A Equipement.
 */
@Entity
@Table(name = "equipement")
public class Equipement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;

    @Column(name = "nom_matiere")
    private String nomMatiere;

    @Column(name = "caracteristique")
    private String caracteristique;

    @Column(name = "beneficiaire")
    private String description;

    @NotNull
    @Column(name = "type_matiere", nullable = false)
    private String typeMatiere;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    
    @Column(name = "etat_matiere")
    private String etatMatiere;

    @Column(name = "date_signalisation")
    private Instant dateSignalisation;

    @NotNull
    @Column(name = "jhi_group", nullable = false)
    private Boolean group;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Lob
    @Column(name = "photo1")
    private byte[] photo1;

    @Lob
    @Column(name = "photo2")
    private byte[] photo2;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "photo1_content_type")
    private String photo1ContentType;

    @Column(name = "photo2_content_type")
    private String photo2ContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomQuartier", "references" }, allowSetters = true)
    private Magazin nomMagazin;

    @ManyToOne
    private Fournisseur nomFournisseur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "references" }, allowSetters = true)
    private Bon bon;

    @ManyToOne
    private CategorieMatiere categorie;

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

    public Instant getDateSignalisation() {
        return this.dateSignalisation;
    }

    public Equipement dateSignalisation(Instant dateSignalisation) {
        this.dateSignalisation = dateSignalisation;
        return this;
    }

    public void setDateSignalisation(Instant dateSignalisation) {
        this.dateSignalisation = dateSignalisation;
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

    public byte[] getPhoto1() {
        return this.photo1;
    }

    public Equipement photo1(byte[] photo1) {
        this.photo1 = photo1;
        return this;
    }

    public void setPhoto1(byte[] photo1) {
        this.photo1 = photo1;
    }

    public byte[] getPhoto2() {
        return this.photo2;
    }

    public Equipement photo2(byte[] photo2) {
        this.photo2 = photo2;
        return this;
    }

    public void setPhoto2(byte[] photo2) {
        this.photo2 = photo2;
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


    public String getPhoto1ContentType() {
        return this.photo1ContentType;
    }

    public Equipement photo1ContentType(String photo1ContentType) {
        this.photo1ContentType = photo1ContentType;
        return this;
    }

    public void setPhoto1ContentType(String photo1ContentType) {
        this.photo1ContentType = photo1ContentType;
    }

    public String getPhoto2ContentType() {
        return this.photo2ContentType;
    }

    public Equipement photo2ContentType(String photo2ContentType) {
        this.photo2ContentType = photo2ContentType;
        return this;
    }

    public void setPhoto2ContentType(String photo2ContentType) {
        this.photo2ContentType = photo2ContentType;
    }


    public Magazin getNomMagazin() {
        return this.nomMagazin;
    }

    public Equipement nomMagazin(Magazin magazin) {
        this.setNomMagazin(magazin);
        return this;
    }

    public void setNomMagazin(Magazin magazin) {
        this.nomMagazin = magazin;
    }

    public Fournisseur getNomFournisseur() {
        return this.nomFournisseur;
    }

    public Equipement nomFournisseur(Fournisseur fournisseur) {
        this.setNomFournisseur(fournisseur);
        return this;
    }

    public void setNomFournisseur(Fournisseur fournisseur) {
        this.nomFournisseur = fournisseur;
    }

    public Bon getBon() {
        return this.bon;
    }

    public Equipement bon(Bon bon) {
        this.setBon(bon);
        return this;
    }

    public void setBon(Bon bon) {
        this.bon = bon;
    }

    public CategorieMatiere getCategorie() {
        return this.categorie;
    }

    public Equipement categorie(CategorieMatiere categorieMatiere) {
        this.setCategorie(categorieMatiere);
        return this;
    }

    public void setCategorie(CategorieMatiere categorieMatiere) {
        this.categorie = categorieMatiere;
    }

    public String getNomMatiere() {
        return this.nomMatiere;
    }

    public Equipement nomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
        return this;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public String getCaracteristique() {
        return this.caracteristique;
    }

    public Equipement caracteristique(String caracteristique) {
        this.caracteristique = caracteristique;
        return this;
    }

    public void setCaracteristique(String caracteristique) {
        this.caracteristique = caracteristique;
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
            ", nomMatiere='" + getNomMatiere() + "'" +
            ", caracteristique='" + getCaracteristique() + "'" +
            ", beneficiaire='" + getDescription() + "'" +
            ", typeMatiere='" + getTypeMatiere() + "'" +
            ", quantite=" + getQuantite() +
            ", etatMatiere='" + getEtatMatiere() + "'" +
            ", dateSignalisation='" + getDateSignalisation() + "'" +
            ", group='" + getGroup() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", photo1ContentType='" + getPhoto1ContentType() + "'" +
            ", photo2ContentType='" + getPhoto2ContentType() + "'" +
            "}";
    }
}
