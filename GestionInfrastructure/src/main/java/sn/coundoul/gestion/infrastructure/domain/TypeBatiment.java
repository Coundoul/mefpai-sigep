package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TypeBatiment.
 */
@Entity
@Table(name = "type_batiment")
public class TypeBatiment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "type_ba", nullable = false)
    private String typeBa;

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

    public TypeBatiment id(Long id) {
        this.id = id;
        return this;
    }

    public String getTypeBa() {
        return this.typeBa;
    }

    public TypeBatiment typeBa(String typeBa) {
        this.typeBa = typeBa;
        return this;
    }

    public void setTypeBa(String typeBa) {
        this.typeBa = typeBa;
    }

    public Batiment getNomBatiment() {
        return this.nomBatiment;
    }

    public TypeBatiment nomBatiment(Batiment batiment) {
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
        if (!(o instanceof TypeBatiment)) {
            return false;
        }
        return id != null && id.equals(((TypeBatiment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeBatiment{" +
            "id=" + getId() +
            ", typeBa='" + getTypeBa() + "'" +
            "}";
    }
}
