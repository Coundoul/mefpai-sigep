package sn.coundoul.gestion.patrimoine.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UtilisateurFinal.
 */
@Table("utilisateur_final")
public class UtilisateurFinal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_utilisateur")
    private String nomUtilisateur;

    @NotNull(message = "must not be null")
    @Column("prenom_utilisateur")
    private String prenomUtilisateur;

    @NotNull(message = "must not be null")
    @Column("email_institutionnel")
    private String emailInstitutionnel;

    @NotNull(message = "must not be null")
    @Column("mobile")
    private String mobile;

    @NotNull(message = "must not be null")
    @Column("sexe")
    private String sexe;

    @Column("departement")
    private String departement;

    @Column("service_dep")
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
