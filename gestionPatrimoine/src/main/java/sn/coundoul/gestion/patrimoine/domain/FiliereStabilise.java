package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A FiliereStabilise.
 */
@Table("filiere_stabilise")
public class FiliereStabilise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_filiere")
    private String nomFiliere;

    @JsonIgnoreProperties(value = { "nomEtablissement", "nomFilieres" }, allowSetters = true)
    @Transient
    private Formateurs nomFormateur;

    @Column("nom_formateur_id")
    private Long nomFormateurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FiliereStabilise id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomFiliere() {
        return this.nomFiliere;
    }

    public FiliereStabilise nomFiliere(String nomFiliere) {
        this.nomFiliere = nomFiliere;
        return this;
    }

    public void setNomFiliere(String nomFiliere) {
        this.nomFiliere = nomFiliere;
    }

    public Formateurs getNomFormateur() {
        return this.nomFormateur;
    }

    public FiliereStabilise nomFormateur(Formateurs formateurs) {
        this.setNomFormateur(formateurs);
        this.nomFormateurId = formateurs != null ? formateurs.getId() : null;
        return this;
    }

    public void setNomFormateur(Formateurs formateurs) {
        this.nomFormateur = formateurs;
        this.nomFormateurId = formateurs != null ? formateurs.getId() : null;
    }

    public Long getNomFormateurId() {
        return this.nomFormateurId;
    }

    public void setNomFormateurId(Long formateurs) {
        this.nomFormateurId = formateurs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiliereStabilise)) {
            return false;
        }
        return id != null && id.equals(((FiliereStabilise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiliereStabilise{" +
            "id=" + getId() +
            ", nomFiliere='" + getNomFiliere() + "'" +
            "}";
    }
}
