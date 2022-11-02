package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.Instant;

/**
 * A Batiment.
 */
@Entity
@Table(name = "batiment")
public class Batiment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;
    
   
    @Column(name = "nbr_piece")
    private Double nbrPiece;

    @Column(name = "surface")
    private Double surface;

    @Column(name = "source_de_financement")
    private String sourceFinancement; 

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @NotNull
    @Column(name = "etat_gros_oeuvre", nullable = false)
    private String etatGrosOeuvre;

    @NotNull
    @Column(name = "etat_second_oeuvre", nullable = false)
    private String etatSecondOeuvre;

    @Column(name = "observation")
    private String observation;

    @Column(name = "date_signalisation")
    private Instant dateSignalisation;

    @Lob
    @Column(name = "photo_signalisation_gros_oeuvre")
    private byte[] photoSignalisationGrosOeuvre;

    @Column(name = "photo_signalisation_content_type_gros_oeuvre")
    private String photoSignalisationGrosOeuvreContentType;

    @Lob
    @Column(name = "photo_signalisation_second_oeuvre")
    private byte[] photoSignalisationSecondOeuvre;

    @Column(name = "photo_signalisation_content_type_second_oeuvre")
    private String photoSignalisationSecondOeuvreContentType;

    @Column(name = "description_signalisation")
    private String descriptionSignalisation;

    @ManyToOne
    @JsonIgnoreProperties(allowSetters = true)
    private Etablissement nomEtablissement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomResponsable" }, allowSetters = true)
    private CorpsEtat nomCorps;

    @OneToMany(mappedBy = "nomBatiment")
    @JsonIgnoreProperties(value = { "nomBatiment" }, allowSetters = true)
    private Set<TypeBatiment> typeBas = new HashSet<>();

    @OneToMany(mappedBy = "nomBatiment")
    @JsonIgnoreProperties(value = { "nomFiliere", "nomBatiment" }, allowSetters = true)
    private Set<Atelier> nomAteliers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Batiment id(Long id) {
        this.id = id;
        return this;
    }

    public String getSourceFinancement() {
        return this.sourceFinancement;
    }

    public Batiment sourceFinancement(String sourceFinancement) {
        this.sourceFinancement = sourceFinancement;
        return this;
    }

    public void setSourceFinancement(String sourceFinancement) {
        this.sourceFinancement = sourceFinancement;
    }

    public Double getNbrPiece() {
        return this.nbrPiece;
    }

    public Batiment nbrPiece(Double nbrPiece) {
        this.nbrPiece = nbrPiece;
        return this;
    }

    public void setNbrPiece(Double nbrPiece) {
        this.nbrPiece = nbrPiece;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Batiment designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getSurface() {
        return this.surface;
    }

    public Batiment surface(Double surface) {
        this.surface = surface;
        return this;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Batiment photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Batiment photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getEtatGrosOeuvre() {
        return this.etatGrosOeuvre;
    }

    public Batiment EtatGrosOeuvre(String etatGrosOeuvre) {
        this.etatGrosOeuvre = etatGrosOeuvre;
        return this;
    }

    public void setEtatGrosOeuvre(String etatGrosOeuvre) {
        this.etatGrosOeuvre = etatGrosOeuvre;
    }

    public String getEtatSecondOeuvre() {
        return this.etatSecondOeuvre;
    }

    public Batiment EtatSecondOeuvre(String etatSecondOeuvre) {
        this.etatSecondOeuvre = etatSecondOeuvre;
        return this;
    }

    public void setEtatSecondOeuvre(String etatSecondOeuvre) {
        this.etatSecondOeuvre = etatSecondOeuvre;
    }

    public String getObservation() {
        return this.observation;
    }

    public Batiment observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Etablissement getNomEtablissement() {
        return this.nomEtablissement;
    }

    public Batiment nomEtablissement(Etablissement nomEtablissement) {
        this.setNomEtablissement(nomEtablissement);
        return this;
    }

    public void setNomEtablissement(Etablissement nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }


    public CorpsEtat getNomCorps() {
        return this.nomCorps;
    }

    public Batiment nomCorps(CorpsEtat corpsEtat) {
        this.setNomCorps(corpsEtat);
        return this;
    }

    public void setNomCorps(CorpsEtat corpsEtat) {
        this.nomCorps = corpsEtat;
    }

    public Set<TypeBatiment> getTypeBas() {
        return this.typeBas;
    }

    public Batiment typeBas(Set<TypeBatiment> typeBatiments) {
        this.setTypeBas(typeBatiments);
        return this;
    }

    public Batiment addTypeBa(TypeBatiment typeBatiment) {
        this.typeBas.add(typeBatiment);
        typeBatiment.setNomBatiment(this);
        return this;
    }

    public Batiment removeTypeBa(TypeBatiment typeBatiment) {
        this.typeBas.remove(typeBatiment);
        typeBatiment.setNomBatiment(null);
        return this;
    }

    public void setTypeBas(Set<TypeBatiment> typeBatiments) {
        if (this.typeBas != null) {
            this.typeBas.forEach(i -> i.setNomBatiment(null));
        }
        if (typeBatiments != null) {
            typeBatiments.forEach(i -> i.setNomBatiment(this));
        }
        this.typeBas = typeBatiments;
    }

    public Set<Atelier> getNomAteliers() {
        return this.nomAteliers;
    }

    public Batiment nomAteliers(Set<Atelier> ateliers) {
        this.setNomAteliers(ateliers);
        return this;
    }

    public Batiment addNomAtelier(Atelier atelier) {
        this.nomAteliers.add(atelier);
        atelier.setNomBatiment(this);
        return this;
    }

    public Batiment removeNomAtelier(Atelier atelier) {
        this.nomAteliers.remove(atelier);
        atelier.setNomBatiment(null);
        return this;
    }

    public void setNomAteliers(Set<Atelier> ateliers) {
        if (this.nomAteliers != null) {
            this.nomAteliers.forEach(i -> i.setNomBatiment(null));
        }
        if (ateliers != null) {
            ateliers.forEach(i -> i.setNomBatiment(this));
        }
        this.nomAteliers = ateliers;
    }


    public byte[] getPhotoSignalisationGrosOeuvre() {
        return this.photoSignalisationGrosOeuvre;
    }

    public Batiment photoSignalisationGrosOeuvre(byte[] photoSignalisationGrosOeuvre) {
        this.photoSignalisationGrosOeuvre = photoSignalisationGrosOeuvre;
        return this;
    }

    public void setPhotoSignalisationGrosOeuvre(byte[] photoSignalisationGrosOeuvre) {
        this.photoSignalisationGrosOeuvre = photoSignalisationGrosOeuvre;
    }

    public String getPhotoSignalisationGrosOeuvreContentType() {
        return this.photoSignalisationGrosOeuvreContentType;
    }

    public Batiment photoSignalisationGrosOeuvreContentType(String photoSignalisationGrosOeuvreContentType) {
        this.photoSignalisationGrosOeuvreContentType = photoSignalisationGrosOeuvreContentType;
        return this;
    }

    public void setPhotoSignalisationGrosOeuvreContentType(String photoSignalisationGrosOeuvreContentType) {
        this.photoSignalisationGrosOeuvreContentType = photoSignalisationGrosOeuvreContentType;
    }

    public byte[] getPhotoSignalisationSecondOeuvre() {
        return this.photoSignalisationSecondOeuvre;
    }

    public Batiment photoSignalisationSecondOeuvre(byte[] photoSignalisationSecondOeuvre) {
        this.photoSignalisationSecondOeuvre = photoSignalisationSecondOeuvre;
        return this;
    }

    public void setPhotoSignalisationSecondOeuvre(byte[] photoSignalisationSecondOeuvre) {
        this.photoSignalisationSecondOeuvre = photoSignalisationSecondOeuvre;
    }

    public String getPhotoSignalisationSecondOeuvreContentType() {
        return this.photoSignalisationSecondOeuvreContentType;
    }

    public Batiment photoSignalisationSecondOeuvreContentType(String photoSignalisationSecondOeuvreContentType) {
        this.photoSignalisationSecondOeuvreContentType = photoSignalisationSecondOeuvreContentType;
        return this;
    }

    public void setPhotoSignalisationSecondOeuvreContentType(String photoSignalisationSecondOeuvreContentType) {
        this.photoSignalisationSecondOeuvreContentType = photoSignalisationSecondOeuvreContentType;
    }

    public String getDescriptionSignalisation() {
        return this.descriptionSignalisation;
    }

    public Batiment descriptionSignalisation(String descriptionSignalisation) {
        this.descriptionSignalisation = descriptionSignalisation;
        return this;
    }

    public void setDescriptionSignalisation(String descriptionSignalisation) {
        this.descriptionSignalisation = descriptionSignalisation;
    }

    public Instant getDateSignalisation() {
        return this.dateSignalisation;
    }

    public Batiment dateAttribution(Instant dateSignalisation) {
        this.dateSignalisation = dateSignalisation;
        return this;
    }

    public void setDateSignalisation(Instant dateSignalisation) {
        this.dateSignalisation = dateSignalisation;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Batiment)) {
            return false;
        }
        return id != null && id.equals(((Batiment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Batiment{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", nbrPiece='" + getNbrPiece() + "'" +
            ", surface=" + getSurface() +
            ", sourceFinancement='" + getSourceFinancement() + "'" +
            ", etatGrosOeuvre='" + getEtatGrosOeuvre() + "'" +
            ", etatSecondOeuvre='" + getEtatSecondOeuvre() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
