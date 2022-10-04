package sn.coundoul.gestion.equipement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Commune.
 */
@Entity
@Table(name = "commune")
public class Commune implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_commune", nullable = false)
    private String nomCommune;

    @ManyToOne
    @JsonIgnoreProperties(allowSetters = true)
    private Departement nomDepartement;

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
        return this;
    }

    public void setNomDepartement(Departement departement) {
        this.nomDepartement = departement;
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
