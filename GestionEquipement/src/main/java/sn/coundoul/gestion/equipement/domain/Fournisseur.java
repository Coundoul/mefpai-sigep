package sn.coundoul.gestion.equipement.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import sn.coundoul.gestion.equipement.domain.enumeration.Sexe;

/**
 * A Fournisseur.
 */
@Entity
@Table(name = "fournisseur")
public class Fournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code_fournisseuer", nullable = false)
    private String codeFournisseuer;

    @NotNull
    @Column(name = "nom_fournisseur", nullable = false)
    private String nomFournisseur;

    @NotNull
    @Column(name = "prenom_fournisseur", nullable = false)
    private String prenomFournisseur;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @NotNull
    @Column(name = "raison_social", nullable = false)
    private String raisonSocial;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @NotNull
    @Column(name = "num_1", nullable = false)
    private String num1;

    @Column(name = "num_2")
    private String num2;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fournisseur id(Long id) {
        this.id = id;
        return this;
    }

    public String getCodeFournisseuer() {
        return this.codeFournisseuer;
    }

    public Fournisseur codeFournisseuer(String codeFournisseuer) {
        this.codeFournisseuer = codeFournisseuer;
        return this;
    }

    public void setCodeFournisseuer(String codeFournisseuer) {
        this.codeFournisseuer = codeFournisseuer;
    }

    public String getNomFournisseur() {
        return this.nomFournisseur;
    }

    public Fournisseur nomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
        return this;
    }

    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }

    public String getPrenomFournisseur() {
        return this.prenomFournisseur;
    }

    public Fournisseur prenomFournisseur(String prenomFournisseur) {
        this.prenomFournisseur = prenomFournisseur;
        return this;
    }

    public void setPrenomFournisseur(String prenomFournisseur) {
        this.prenomFournisseur = prenomFournisseur;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Fournisseur sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getRaisonSocial() {
        return this.raisonSocial;
    }

    public Fournisseur raisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
        return this;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Fournisseur adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNum1() {
        return this.num1;
    }

    public Fournisseur num1(String num1) {
        this.num1 = num1;
        return this;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public String getNum2() {
        return this.num2;
    }

    public Fournisseur num2(String num2) {
        this.num2 = num2;
        return this;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public String getVille() {
        return this.ville;
    }

    public Fournisseur ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getEmail() {
        return this.email;
    }

    public Fournisseur email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fournisseur)) {
            return false;
        }
        return id != null && id.equals(((Fournisseur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fournisseur{" +
            "id=" + getId() +
            ", codeFournisseuer='" + getCodeFournisseuer() + "'" +
            ", nomFournisseur='" + getNomFournisseur() + "'" +
            ", prenomFournisseur='" + getPrenomFournisseur() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", raisonSocial='" + getRaisonSocial() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", num1='" + getNum1() + "'" +
            ", num2='" + getNum2() + "'" +
            ", ville='" + getVille() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
