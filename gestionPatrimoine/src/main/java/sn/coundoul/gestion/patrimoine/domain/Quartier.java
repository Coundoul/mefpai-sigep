package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Quartier.
 */
@Table("quartier")
public class Quartier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_quartier")
    private String nomQuartier;

    @JsonIgnoreProperties(value = { "nomDepartement" }, allowSetters = true)
    @Transient
    private Commune nomCommune;

    @Column("nom_commune_id")
    private Long nomCommuneId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Quartier id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomQuartier() {
        return this.nomQuartier;
    }

    public Quartier nomQuartier(String nomQuartier) {
        this.nomQuartier = nomQuartier;
        return this;
    }

    public void setNomQuartier(String nomQuartier) {
        this.nomQuartier = nomQuartier;
    }

    public Commune getNomCommune() {
        return this.nomCommune;
    }

    public Quartier nomCommune(Commune commune) {
        this.setNomCommune(commune);
        this.nomCommuneId = commune != null ? commune.getId() : null;
        return this;
    }

    public void setNomCommune(Commune commune) {
        this.nomCommune = commune;
        this.nomCommuneId = commune != null ? commune.getId() : null;
    }

    public Long getNomCommuneId() {
        return this.nomCommuneId;
    }

    public void setNomCommuneId(Long commune) {
        this.nomCommuneId = commune;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quartier)) {
            return false;
        }
        return id != null && id.equals(((Quartier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quartier{" +
            "id=" + getId() +
            ", nomQuartier='" + getNomQuartier() + "'" +
            "}";
    }
}
