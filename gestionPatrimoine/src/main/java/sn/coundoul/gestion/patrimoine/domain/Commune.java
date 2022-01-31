package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Commune.
 */
@Table("commune")
public class Commune implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_commune")
    private String nomCommune;

    @JsonIgnoreProperties(value = { "nomRegion" }, allowSetters = true)
    @Transient
    private Departement nomDepartement;

    @Column("nom_departement_id")
    private Long nomDepartementId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commune id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomCommune() {
        return this.nomCommune;
    }

    public Commune nomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
        return this;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public Departement getNomDepartement() {
        return this.nomDepartement;
    }

    public Commune nomDepartement(Departement departement) {
        this.setNomDepartement(departement);
        this.nomDepartementId = departement != null ? departement.getId() : null;
        return this;
    }

    public void setNomDepartement(Departement departement) {
        this.nomDepartement = departement;
        this.nomDepartementId = departement != null ? departement.getId() : null;
    }

    public Long getNomDepartementId() {
        return this.nomDepartementId;
    }

    public void setNomDepartementId(Long departement) {
        this.nomDepartementId = departement;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commune)) {
            return false;
        }
        return id != null && id.equals(((Commune) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commune{" +
            "id=" + getId() +
            ", nomCommune='" + getNomCommune() + "'" +
            "}";
    }
}
