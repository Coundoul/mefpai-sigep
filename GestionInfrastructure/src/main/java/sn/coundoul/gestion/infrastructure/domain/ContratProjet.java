package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ContratProjet.
 */
@Entity
@Table(name = "contrat_projet")
public class ContratProjet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @JsonIgnoreProperties(value = { "nom", "nomEtablissement", "nomBatiment", "nomIntervenants" }, allowSetters = true)
    @OneToOne(mappedBy = "nom")
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
