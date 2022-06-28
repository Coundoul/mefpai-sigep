package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import sn.coundoul.gestion.infrastructure.domain.enumeration.TypeMaitre;

/**
 * A Intervenant.
 */
@Entity
@Table(name = "intervenant")
public class Intervenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_intervenant", nullable = false)
    private String nomIntervenant;

    @NotNull
    @Column(name = "prenom_intervenant", nullable = false)
    private String prenomIntervenant;

    @NotNull
    @Column(name = "email_professionnel", nullable = false)
    private String emailProfessionnel;

    @NotNull
    @Column(name = "raison_social", nullable = false)
    private String raisonSocial;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "maitre", nullable = false)
    private TypeMaitre maitre;

    @NotNull
    @Column(name = "role", nullable = false)
    private String role;

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

    public Intervenant id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomIntervenant() {
        return this.nomIntervenant;
    }

    public Intervenant nomIntervenant(String nomIntervenant) {
        this.nomIntervenant = nomIntervenant;
        return this;
    }

    public void setNomIntervenant(String nomIntervenant) {
        this.nomIntervenant = nomIntervenant;
    }

    public String getPrenomIntervenant() {
        return this.prenomIntervenant;
    }

    public Intervenant prenomIntervenant(String prenomIntervenant) {
        this.prenomIntervenant = prenomIntervenant;
        return this;
    }

    public void setPrenomIntervenant(String prenomIntervenant) {
        this.prenomIntervenant = prenomIntervenant;
    }

    public String getEmailProfessionnel() {
        return this.emailProfessionnel;
    }

    public Intervenant emailProfessionnel(String emailProfessionnel) {
        this.emailProfessionnel = emailProfessionnel;
        return this;
    }

    public void setEmailProfessionnel(String emailProfessionnel) {
        this.emailProfessionnel = emailProfessionnel;
    }

    public String getRaisonSocial() {
        return this.raisonSocial;
    }

    public Intervenant raisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
        return this;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public TypeMaitre getMaitre() {
        return this.maitre;
    }

    public Intervenant maitre(TypeMaitre maitre) {
        this.maitre = maitre;
        return this;
    }

    public void setMaitre(TypeMaitre maitre) {
        this.maitre = maitre;
    }

    public String getRole() {
        return this.role;
    }

    public Intervenant role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Projets getNomProjet() {
        return this.nomProjet;
    }

    public Intervenant nomProjet(Projets projets) {
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
        if (!(o instanceof Intervenant)) {
            return false;
        }
        return id != null && id.equals(((Intervenant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Intervenant{" +
            "id=" + getId() +
            ", nomIntervenant='" + getNomIntervenant() + "'" +
            ", prenomIntervenant='" + getPrenomIntervenant() + "'" +
            ", emailProfessionnel='" + getEmailProfessionnel() + "'" +
            ", raisonSocial='" + getRaisonSocial() + "'" +
            ", maitre='" + getMaitre() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
