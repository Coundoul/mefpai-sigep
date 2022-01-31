package sn.coundoul.gestion.infrastructure.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Departement.
 */
@Entity
@Table(name = "departement")
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_departement", nullable = false)
    private String nomDepartement;

    @ManyToOne
    private Region nomRegion;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Departement id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomDepartement() {
        return this.nomDepartement;
    }

    public Departement nomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
        return this;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    public Region getNomRegion() {
        return this.nomRegion;
    }

    public Departement nomRegion(Region region) {
        this.setNomRegion(region);
        return this;
    }

    public void setNomRegion(Region region) {
        this.nomRegion = region;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departement)) {
            return false;
        }
        return id != null && id.equals(((Departement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", nomDepartement='" + getNomDepartement() + "'" +
            "}";
    }
}
