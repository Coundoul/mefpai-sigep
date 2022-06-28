package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import sn.coundoul.gestion.patrimoine.domain.enumeration.TypeProjet;

/**
 * A Projets.
 */
@Table("projets")
public class Projets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("type_projet")
    private TypeProjet typeProjet;

    @NotNull(message = "must not be null")
    @Column("nom_projet")
    private String nomProjet;

    @NotNull(message = "must not be null")
    @Column("date_debut")
    private ZonedDateTime dateDebut;

    @NotNull(message = "must not be null")
    @Column("date_fin")
    private ZonedDateTime dateFin;

    @NotNull(message = "must not be null")
    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("extension")
    private Boolean extension;

    private Long nomId;

    @Transient
    private ContratProjet nom;

    @JsonIgnoreProperties(value = { "nomQuartier", "nomProjets" }, allowSetters = true)
    @Transient
    private Etablissement nomEtablissement;

    @Column("nom_etablissement_id")
    private Long nomEtablissementId;

    @JsonIgnoreProperties(value = { "nomQuartier", "nomProjets" }, allowSetters = true)
    @Transient
    private Etablissement nomBatiment;

    @Column("nom_batiment_id")
    private Long nomBatimentId;

    @Transient
    @JsonIgnoreProperties(value = { "nomProjet" }, allowSetters = true)
    private Set<Intervenant> nomIntervenants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Projets id(Long id) {
        this.id = id;
        return this;
    }

    public TypeProjet getTypeProjet() {
        return this.typeProjet;
    }

    public Projets typeProjet(TypeProjet typeProjet) {
        this.typeProjet = typeProjet;
        return this;
    }

    public void setTypeProjet(TypeProjet typeProjet) {
        this.typeProjet = typeProjet;
    }

    public String getNomProjet() {
        return this.nomProjet;
    }

    public Projets nomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
        return this;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public ZonedDateTime getDateDebut() {
        return this.dateDebut;
    }

    public Projets dateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return this.dateFin;
    }

    public Projets dateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return this.description;
    }

    public Projets description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getExtension() {
        return this.extension;
    }

    public Projets extension(Boolean extension) {
        this.extension = extension;
        return this;
    }

    public void setExtension(Boolean extension) {
        this.extension = extension;
    }

    public ContratProjet getNom() {
        return this.nom;
    }

    public Projets nom(ContratProjet contratProjet) {
        this.setNom(contratProjet);
        this.nomId = contratProjet != null ? contratProjet.getId() : null;
        return this;
    }

    public void setNom(ContratProjet contratProjet) {
        this.nom = contratProjet;
        this.nomId = contratProjet != null ? contratProjet.getId() : null;
    }

    public Long getNomId() {
        return this.nomId;
    }

    public void setNomId(Long contratProjet) {
        this.nomId = contratProjet;
    }

    public Etablissement getNomEtablissement() {
        return this.nomEtablissement;
    }

    public Projets nomEtablissement(Etablissement etablissement) {
        this.setNomEtablissement(etablissement);
        this.nomEtablissementId = etablissement != null ? etablissement.getId() : null;
        return this;
    }

    public void setNomEtablissement(Etablissement etablissement) {
        this.nomEtablissement = etablissement;
        this.nomEtablissementId = etablissement != null ? etablissement.getId() : null;
    }

    public Long getNomEtablissementId() {
        return this.nomEtablissementId;
    }

    public void setNomEtablissementId(Long etablissement) {
        this.nomEtablissementId = etablissement;
    }

    public Etablissement getNomBatiment() {
        return this.nomBatiment;
    }

    public Projets nomBatiment(Etablissement etablissement) {
        this.setNomBatiment(etablissement);
        this.nomBatimentId = etablissement != null ? etablissement.getId() : null;
        return this;
    }

    public void setNomBatiment(Etablissement etablissement) {
        this.nomBatiment = etablissement;
        this.nomBatimentId = etablissement != null ? etablissement.getId() : null;
    }

    public Long getNomBatimentId() {
        return this.nomBatimentId;
    }

    public void setNomBatimentId(Long etablissement) {
        this.nomBatimentId = etablissement;
    }

    public Set<Intervenant> getNomIntervenants() {
        return this.nomIntervenants;
    }

    public Projets nomIntervenants(Set<Intervenant> intervenants) {
        this.setNomIntervenants(intervenants);
        return this;
    }

    public Projets addNomIntervenant(Intervenant intervenant) {
        this.nomIntervenants.add(intervenant);
        intervenant.setNomProjet(this);
        return this;
    }

    public Projets removeNomIntervenant(Intervenant intervenant) {
        this.nomIntervenants.remove(intervenant);
        intervenant.setNomProjet(null);
        return this;
    }

    public void setNomIntervenants(Set<Intervenant> intervenants) {
        if (this.nomIntervenants != null) {
            this.nomIntervenants.forEach(i -> i.setNomProjet(null));
        }
        if (intervenants != null) {
            intervenants.forEach(i -> i.setNomProjet(this));
        }
        this.nomIntervenants = intervenants;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Projets)) {
            return false;
        }
        return id != null && id.equals(((Projets) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Projets{" +
            "id=" + getId() +
            ", typeProjet='" + getTypeProjet() + "'" +
            ", nomProjet='" + getNomProjet() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", description='" + getDescription() + "'" +
            ", extension='" + getExtension() + "'" +
            "}";
    }
}
