package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TypeBatiment.
 */
@Table("type_batiment")
public class TypeBatiment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("type_ba")
    private String typeBa;

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
