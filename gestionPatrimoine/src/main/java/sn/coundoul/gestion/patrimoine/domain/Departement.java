package sn.coundoul.gestion.patrimoine.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Departement.
 */
@Table("departement")
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_departement")
    private String nomDepartement;

    @Transient
    private Region nomRegion;

    @Column("nom_region_id")
    private Long nomRegionId;

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
        this.nomRegionId = region != null ? region.getId() : null;
        return this;
    }

    public void setNomRegion(Region region) {
        this.nomRegion = region;
        this.nomRegionId = region != null ? region.getId() : null;
    }

    public Long getNomRegionId() {
        return this.nomRegionId;
    }

    public void setNomRegionId(Long region) {
        this.nomRegionId = region;
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
