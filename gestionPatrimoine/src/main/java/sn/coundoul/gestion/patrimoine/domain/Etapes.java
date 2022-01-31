package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Etapes.
 */
@Table("etapes")
public class Etapes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("date_debut")
    private ZonedDateTime dateDebut;

    @NotNull(message = "must not be null")
    @Column("date_fin")
    private ZonedDateTime dateFin;

    @NotNull(message = "must not be null")
    @Column("nom_tache")
    private String nomTache;

    @Column("duration")
    private Duration duration;

    @JsonIgnoreProperties(value = { "nom", "nomEtablissement", "nomBatiment", "nomIntervenants" }, allowSetters = true)
    @Transient
    private Projets nomProjet;

    @Column("nom_projet_id")
    private Long nomProjetId;

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
        this.nomProjetId = projets != null ? projets.getId() : null;
        return this;
    }

    public void setNomProjet(Projets projets) {
        this.nomProjet = projets;
        this.nomProjetId = projets != null ? projets.getId() : null;
    }

    public Long getNomProjetId() {
        return this.nomProjetId;
    }

    public void setNomProjetId(Long projets) {
        this.nomProjetId = projets;
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
