package sn.coundoul.gestion.patrimoine.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.NomS;

/**
 * A Bureau.
 */
@Table("bureau")
public class Bureau implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_structure")
    private NomS nomStructure;

    @Column("direction")
    private Direction direction;

    @Column("nom_etablissement")
    private String nomEtablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bureau id(Long id) {
        this.id = id;
        return this;
    }

    public NomS getNomStructure() {
        return this.nomStructure;
    }

    public Bureau nomStructure(NomS nomStructure) {
        this.nomStructure = nomStructure;
        return this;
    }

    public void setNomStructure(NomS nomStructure) {
        this.nomStructure = nomStructure;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Bureau direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getNomEtablissement() {
        return this.nomEtablissement;
    }

    public Bureau nomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
        return this;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bureau)) {
            return false;
        }
        return id != null && id.equals(((Bureau) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bureau{" +
            "id=" + getId() +
            ", nomStructure='" + getNomStructure() + "'" +
            ", direction='" + getDirection() + "'" +
            ", nomEtablissement='" + getNomEtablissement() + "'" +
            "}";
    }
}
