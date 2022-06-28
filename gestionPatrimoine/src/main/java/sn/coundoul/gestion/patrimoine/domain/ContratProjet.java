package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ContratProjet.
 */
@Table("contrat_projet")
public class ContratProjet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom")
    private String nom;

    @Transient
    private Projets nomProjet;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContratProjet id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public ContratProjet nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Projets getNomProjet() {
        return this.nomProjet;
    }

    public ContratProjet nomProjet(Projets projets) {
        this.setNomProjet(projets);
        return this;
    }

    public void setNomProjet(Projets projets) {
        if (this.nomProjet != null) {
            this.nomProjet.setNom(null);
        }
        if (nomProjet != null) {
            nomProjet.setNom(this);
        }
        this.nomProjet = projets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContratProjet)) {
            return false;
        }
        return id != null && id.equals(((ContratProjet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContratProjet{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
