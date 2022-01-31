package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Atelier.
 */
@Table("atelier")
public class Atelier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_atelier")
    private String nomAtelier;

    @NotNull(message = "must not be null")
    @Column("surface")
    private Double surface;

    @NotNull(message = "must not be null")
    @Column("description")
    private String description;

    @JsonIgnoreProperties(value = { "nomFormateur" }, allowSetters = true)
    @Transient
    private FiliereStabilise nomFiliere;

    @Column("nom_filiere_id")
    private Long nomFiliereId;

    @JsonIgnoreProperties(value = { "nomEtablissement", "nomCorps", "typeBas", "nomAteliers" }, allowSetters = true)
    @Transient
    private Batiment nomBatiment;

    @Column("nom_batiment_id")
    private Long nomBatimentId;

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
        this.nomFiliereId = filiereStabilise != null ? filiereStabilise.getId() : null;
        return this;
    }

    public void setNomFiliere(FiliereStabilise filiereStabilise) {
        this.nomFiliere = filiereStabilise;
        this.nomFiliereId = filiereStabilise != null ? filiereStabilise.getId() : null;
    }

    public Long getNomFiliereId() {
        return this.nomFiliereId;
    }

    public void setNomFiliereId(Long filiereStabilise) {
        this.nomFiliereId = filiereStabilise;
    }

    public Batiment getNomBatiment() {
        return this.nomBatiment;
    }

    public Atelier nomBatiment(Batiment batiment) {
        this.setNomBatiment(batiment);
        this.nomBatimentId = batiment != null ? batiment.getId() : null;
        return this;
    }

    public void setNomBatiment(Batiment batiment) {
        this.nomBatiment = batiment;
        this.nomBatimentId = batiment != null ? batiment.getId() : null;
    }

    public Long getNomBatimentId() {
        return this.nomBatimentId;
    }

    public void setNomBatimentId(Long batiment) {
        this.nomBatimentId = batiment;
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
