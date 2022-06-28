package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Formateurs.
 */
@Entity
@Table(name = "formateurs")
public class Formateurs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_formateur", nullable = false)
    private String nomFormateur;

    @NotNull
    @Column(name = "prenom_formateur", nullable = false)
    private String prenomFormateur;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "numb_1", nullable = false)
    private String numb1;

    @Column(name = "numb_2")
    private String numb2;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @NotNull
    @Column(name = "specialite", nullable = false)
    private String specialite;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomQuartier", "nomProjets" }, allowSetters = true)
    private Etablissement nomEtablissement;

    @OneToMany(mappedBy = "nomFormateur")
    @JsonIgnoreProperties(value = { "nomFormateur" }, allowSetters = true)
    private Set<FiliereStabilise> nomFilieres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Formateurs id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomFormateur() {
        return this.nomFormateur;
    }

    public Formateurs nomFormateur(String nomFormateur) {
        this.nomFormateur = nomFormateur;
        return this;
    }

    public void setNomFormateur(String nomFormateur) {
        this.nomFormateur = nomFormateur;
    }

    public String getPrenomFormateur() {
        return this.prenomFormateur;
    }

    public Formateurs prenomFormateur(String prenomFormateur) {
        this.prenomFormateur = prenomFormateur;
        return this;
    }

    public void setPrenomFormateur(String prenomFormateur) {
        this.prenomFormateur = prenomFormateur;
    }

    public String getEmail() {
        return this.email;
    }

    public Formateurs email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumb1() {
        return this.numb1;
    }

    public Formateurs numb1(String numb1) {
        this.numb1 = numb1;
        return this;
    }

    public void setNumb1(String numb1) {
        this.numb1 = numb1;
    }

    public String getNumb2() {
        return this.numb2;
    }

    public Formateurs numb2(String numb2) {
        this.numb2 = numb2;
        return this;
    }

    public void setNumb2(String numb2) {
        this.numb2 = numb2;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Formateurs adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return this.ville;
    }

    public Formateurs ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getSpecialite() {
        return this.specialite;
    }

    public Formateurs specialite(String specialite) {
        this.specialite = specialite;
        return this;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public Etablissement getNomEtablissement() {
        return this.nomEtablissement;
    }

    public Formateurs nomEtablissement(Etablissement etablissement) {
        this.setNomEtablissement(etablissement);
        return this;
    }

    public void setNomEtablissement(Etablissement etablissement) {
        this.nomEtablissement = etablissement;
    }

    public Set<FiliereStabilise> getNomFilieres() {
        return this.nomFilieres;
    }

    public Formateurs nomFilieres(Set<FiliereStabilise> filiereStabilises) {
        this.setNomFilieres(filiereStabilises);
        return this;
    }

    public Formateurs addNomFiliere(FiliereStabilise filiereStabilise) {
        this.nomFilieres.add(filiereStabilise);
        filiereStabilise.setNomFormateur(this);
        return this;
    }

    public Formateurs removeNomFiliere(FiliereStabilise filiereStabilise) {
        this.nomFilieres.remove(filiereStabilise);
        filiereStabilise.setNomFormateur(null);
        return this;
    }

    public void setNomFilieres(Set<FiliereStabilise> filiereStabilises) {
        if (this.nomFilieres != null) {
            this.nomFilieres.forEach(i -> i.setNomFormateur(null));
        }
        if (filiereStabilises != null) {
            filiereStabilises.forEach(i -> i.setNomFormateur(this));
        }
        this.nomFilieres = filiereStabilises;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formateurs)) {
            return false;
        }
        return id != null && id.equals(((Formateurs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Formateurs{" +
            "id=" + getId() +
            ", nomFormateur='" + getNomFormateur() + "'" +
            ", prenomFormateur='" + getPrenomFormateur() + "'" +
            ", email='" + getEmail() + "'" +
            ", numb1='" + getNumb1() + "'" +
            ", numb2='" + getNumb2() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", ville='" + getVille() + "'" +
            ", specialite='" + getSpecialite() + "'" +
            "}";
    }
}
