package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import sn.coundoul.gestion.patrimoine.domain.enumeration.TypeMaitre;

/**
 * A Intervenant.
 */
@Table("intervenant")
public class Intervenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_intervenant")
    private String nomIntervenant;

    @NotNull(message = "must not be null")
    @Column("prenom_intervenant")
    private String prenomIntervenant;

    @NotNull(message = "must not be null")
    @Column("email_professionnel")
    private String emailProfessionnel;

    @NotNull(message = "must not be null")
    @Column("raison_social")
    private String raisonSocial;

    @NotNull(message = "must not be null")
    @Column("maitre")
    private TypeMaitre maitre;

    @NotNull(message = "must not be null")
    @Column("role")
    private String role;

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
