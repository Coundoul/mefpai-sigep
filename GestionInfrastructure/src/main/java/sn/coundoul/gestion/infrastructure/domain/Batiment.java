package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

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
    @Column(name = "nom_batiment", nullable = false)
    private String nomBatiment;

    @NotNull
    @Column(name = "nbr_piece", nullable = false)
    private String nbrPiece;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @NotNull
    @Column(name = "surface", nullable = false)
    private Double surface;

    @NotNull
    @Column(name = "etat_general", nullable = false)
    private Boolean etatGeneral;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "nombre_salle", nullable = false)
    private Integer nombreSalle;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomQuartier", "nomProjets" }, allowSetters = true)
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

    public String getNomBatiment() {
        return this.nomBatiment;
    }

    public Batiment nomBatiment(String nomBatiment) {
        this.nomBatiment = nomBatiment;
        return this;
    }

    public void setNomBatiment(String nomBatiment) {
        this.nomBatiment = nomBatiment;
    }

    public String getNbrPiece() {
        return this.nbrPiece;
    }

    public Batiment nbrPiece(String nbrPiece) {
        this.nbrPiece = nbrPiece;
        return this;
    }

    public void setNbrPiece(String nbrPiece) {
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

    public Boolean getEtatGeneral() {
        return this.etatGeneral;
    }

    public Batiment etatGeneral(Boolean etatGeneral) {
        this.etatGeneral = etatGeneral;
        return this;
    }

    public void setEtatGeneral(Boolean etatGeneral) {
        this.etatGeneral = etatGeneral;
    }

    public String getDescription() {
        return this.description;
    }

    public Batiment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNombreSalle() {
        return this.nombreSalle;
    }

    public Batiment nombreSalle(Integer nombreSalle) {
        this.nombreSalle = nombreSalle;
        return this;
    }

    public void setNombreSalle(Integer nombreSalle) {
        this.nombreSalle = nombreSalle;
    }

    public Etablissement getNomEtablissement() {
        return this.nomEtablissement;
    }

    public Batiment nomEtablissement(Etablissement etablissement) {
        this.setNomEtablissement(etablissement);
        return this;
    }

    public void setNomEtablissement(Etablissement etablissement) {
        this.nomEtablissement = etablissement;
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
            ", nomBatiment='" + getNomBatiment() + "'" +
            ", nbrPiece='" + getNbrPiece() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", surface=" + getSurface() +
            ", etatGeneral='" + getEtatGeneral() + "'" +
            ", description='" + getDescription() + "'" +
            ", nombreSalle=" + getNombreSalle() +
            "}";
    }
}
