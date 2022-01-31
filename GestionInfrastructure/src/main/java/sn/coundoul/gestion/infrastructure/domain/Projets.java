package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import sn.coundoul.gestion.infrastructure.domain.enumeration.TypeProjet;

/**
 * A Projets.
 */
@Entity
@Table(name = "projets")
public class Projets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_projet", nullable = false)
    private TypeProjet typeProjet;

    @NotNull
    @Column(name = "nom_projet", nullable = false)
    private String nomProjet;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private ZonedDateTime dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private ZonedDateTime dateFin;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "extension", nullable = false)
    private Boolean extension;

    @JsonIgnoreProperties(value = { "nomProjet" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ContratProjet nom;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomQuartier", "nomProjets" }, allowSetters = true)
    private Etablissement nomEtablissement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomQuartier", "nomProjets" }, allowSetters = true)
    private Etablissement nomBatiment;

    @OneToMany(mappedBy = "nomProjet")
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
        return this;
    }

    public void setNom(ContratProjet contratProjet) {
        this.nom = contratProjet;
    }

    public Etablissement getNomEtablissement() {
        return this.nomEtablissement;
    }

    public Projets nomEtablissement(Etablissement etablissement) {
        this.setNomEtablissement(etablissement);
        return this;
    }

    public void setNomEtablissement(Etablissement etablissement) {
        this.nomEtablissement = etablissement;
    }

    public Etablissement getNomBatiment() {
        return this.nomBatiment;
    }

    public Projets nomBatiment(Etablissement etablissement) {
        this.setNomBatiment(etablissement);
        return this;
    }

    public void setNomBatiment(Etablissement etablissement) {
        this.nomBatiment = etablissement;
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
