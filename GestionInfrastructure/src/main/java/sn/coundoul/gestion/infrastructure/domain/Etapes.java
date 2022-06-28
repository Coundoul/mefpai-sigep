package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Etapes.
 */
@Entity
@Table(name = "etapes")
public class Etapes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private ZonedDateTime dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private ZonedDateTime dateFin;

    @NotNull
    @Column(name = "nom_tache", nullable = false)
    private String nomTache;

    @Column(name = "duration")
    private Duration duration;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nom", "nomEtablissement", "nomBatiment", "nomIntervenants" }, allowSetters = true)
    private Projets nomProjet;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etapes id(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getDateDebut() {
        return this.dateDebut;
    }

    public Etapes dateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return this.dateFin;
    }

    public Etapes dateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getNomTache() {
        return this.nomTache;
    }

    public Etapes nomTache(String nomTache) {
        this.nomTache = nomTache;
        return this;
    }

    public void setNomTache(String nomTache) {
        this.nomTache = nomTache;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Etapes duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Projets getNomProjet() {
        return this.nomProjet;
    }

    public Etapes nomProjet(Projets projets) {
        this.setNomProjet(projets);
        return this;
    }

    public void setNomProjet(Projets projets) {
        this.nomProjet = projets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etapes)) {
            return false;
        }
        return id != null && id.equals(((Etapes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etapes{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", nomTache='" + getNomTache() + "'" +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
