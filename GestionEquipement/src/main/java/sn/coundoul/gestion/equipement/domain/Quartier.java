package sn.coundoul.gestion.equipement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Quartier.
 */
@Entity
@Table(name = "quartier")
public class Quartier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_quartier", nullable = false)
    private String nomQuartier;

    @ManyToOne
    @JsonIgnoreProperties(allowSetters = true)
    private Commune nomCommune;

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
        return this;
    }

    public void setNomCommune(Commune commune) {
        this.nomCommune = commune;
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
