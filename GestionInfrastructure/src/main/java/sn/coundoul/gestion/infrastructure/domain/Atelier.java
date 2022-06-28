package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Atelier.
 */
@Entity
@Table(name = "atelier")
public class Atelier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_atelier", nullable = false)
    private String nomAtelier;

    @NotNull
    @Column(name = "surface", nullable = false)
    private Double surface;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomFormateur" }, allowSetters = true)
    private FiliereStabilise nomFiliere;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomEtablissement", "nomCorps", "typeBas", "nomAteliers" }, allowSetters = true)
    private Batiment nomBatiment;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Atelier id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomAtelier() {
        return this.nomAtelier;
    }

    public Atelier nomAtelier(String nomAtelier) {
        this.nomAtelier = nomAtelier;
        return this;
    }

    public void setNomAtelier(String nomAtelier) {
        this.nomAtelier = nomAtelier;
    }

    public Double getSurface() {
        return this.surface;
    }

    public Atelier surface(Double surface) {
        this.surface = surface;
        return this;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public String getDescription() {
        return this.description;
    }

    public Atelier description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FiliereStabilise getNomFiliere() {
        return this.nomFiliere;
    }

    public Atelier nomFiliere(FiliereStabilise filiereStabilise) {
        this.setNomFiliere(filiereStabilise);
        return this;
    }

    public void setNomFiliere(FiliereStabilise filiereStabilise) {
        this.nomFiliere = filiereStabilise;
    }

    public Batiment getNomBatiment() {
        return this.nomBatiment;
    }

    public Atelier nomBatiment(Batiment batiment) {
        this.setNomBatiment(batiment);
        return this;
    }

    public void setNomBatiment(Batiment batiment) {
        this.nomBatiment = batiment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Atelier)) {
            return false;
        }
        return id != null && id.equals(((Atelier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Atelier{" +
            "id=" + getId() +
            ", nomAtelier='" + getNomAtelier() + "'" +
            ", surface=" + getSurface() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
