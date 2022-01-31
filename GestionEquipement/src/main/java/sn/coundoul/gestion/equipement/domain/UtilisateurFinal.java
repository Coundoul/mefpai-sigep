package sn.coundoul.gestion.equipement.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A UtilisateurFinal.
 */
@Entity
@Table(name = "utilisateur_final")
public class UtilisateurFinal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_utilisateur", nullable = false)
    private String nomUtilisateur;

    @NotNull
    @Column(name = "prenom_utilisateur", nullable = false)
    private String prenomUtilisateur;

    @NotNull
    @Column(name = "email_institutionnel", nullable = false)
    private String emailInstitutionnel;

    @NotNull
    @Column(name = "mobile", nullable = false)
    private String mobile;

    @NotNull
    @Column(name = "sexe", nullable = false)
    private String sexe;

    @Column(name = "departement")
    private String departement;

    @Column(name = "service_dep")
    private String serviceDep;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UtilisateurFinal id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomUtilisateur() {
        return this.nomUtilisateur;
    }

    public UtilisateurFinal nomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
        return this;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getPrenomUtilisateur() {
        return this.prenomUtilisateur;
    }

    public UtilisateurFinal prenomUtilisateur(String prenomUtilisateur) {
        this.prenomUtilisateur = prenomUtilisateur;
        return this;
    }

    public void setPrenomUtilisateur(String prenomUtilisateur) {
        this.prenomUtilisateur = prenomUtilisateur;
    }

    public String getEmailInstitutionnel() {
        return this.emailInstitutionnel;
    }

    public UtilisateurFinal emailInstitutionnel(String emailInstitutionnel) {
        this.emailInstitutionnel = emailInstitutionnel;
        return this;
    }

    public void setEmailInstitutionnel(String emailInstitutionnel) {
        this.emailInstitutionnel = emailInstitutionnel;
    }

    public String getMobile() {
        return this.mobile;
    }

    public UtilisateurFinal mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSexe() {
        return this.sexe;
    }

    public UtilisateurFinal sexe(String sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDepartement() {
        return this.departement;
    }

    public UtilisateurFinal departement(String departement) {
        this.departement = departement;
        return this;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getServiceDep() {
        return this.serviceDep;
    }

    public UtilisateurFinal serviceDep(String serviceDep) {
        this.serviceDep = serviceDep;
        return this;
    }

    public void setServiceDep(String serviceDep) {
        this.serviceDep = serviceDep;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisateurFinal)) {
            return false;
        }
        return id != null && id.equals(((UtilisateurFinal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurFinal{" +
            "id=" + getId() +
            ", nomUtilisateur='" + getNomUtilisateur() + "'" +
            ", prenomUtilisateur='" + getPrenomUtilisateur() + "'" +
            ", emailInstitutionnel='" + getEmailInstitutionnel() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", departement='" + getDepartement() + "'" +
            ", serviceDep='" + getServiceDep() + "'" +
            "}";
    }
}
