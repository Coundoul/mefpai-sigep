package sn.coundoul.gestion.infrastructure.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Responsable.
 */
@Entity
@Table(name = "responsable")
public class Responsable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_responsable", nullable = false)
    private String nomResponsable;

    @NotNull
    @Column(name = "prenom_responsable", nullable = false)
    private String prenomResponsable;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "specialite", nullable = false)
    private String specialite;

    @NotNull
    @Column(name = "numb_1", nullable = false)
    private String numb1;

    @Column(name = "numb_2")
    private String numb2;

    @NotNull
    @Column(name = "raison_social", nullable = false)
    private String raisonSocial;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Responsable id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomResponsable() {
        return this.nomResponsable;
    }

    public Responsable nomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
        return this;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public String getPrenomResponsable() {
        return this.prenomResponsable;
    }

    public Responsable prenomResponsable(String prenomResponsable) {
        this.prenomResponsable = prenomResponsable;
        return this;
    }

    public void setPrenomResponsable(String prenomResponsable) {
        this.prenomResponsable = prenomResponsable;
    }

    public String getEmail() {
        return this.email;
    }

    public Responsable email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialite() {
        return this.specialite;
    }

    public Responsable specialite(String specialite) {
        this.specialite = specialite;
        return this;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getNumb1() {
        return this.numb1;
    }

    public Responsable numb1(String numb1) {
        this.numb1 = numb1;
        return this;
    }

    public void setNumb1(String numb1) {
        this.numb1 = numb1;
    }

    public String getNumb2() {
        return this.numb2;
    }

    public Responsable numb2(String numb2) {
        this.numb2 = numb2;
        return this;
    }

    public void setNumb2(String numb2) {
        this.numb2 = numb2;
    }

    public String getRaisonSocial() {
        return this.raisonSocial;
    }

    public Responsable raisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
        return this;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Responsable)) {
            return false;
        }
        return id != null && id.equals(((Responsable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Responsable{" +
            "id=" + getId() +
            ", nomResponsable='" + getNomResponsable() + "'" +
            ", prenomResponsable='" + getPrenomResponsable() + "'" +
            ", email='" + getEmail() + "'" +
            ", specialite='" + getSpecialite() + "'" +
            ", numb1='" + getNumb1() + "'" +
            ", numb2='" + getNumb2() + "'" +
            ", raisonSocial='" + getRaisonSocial() + "'" +
            "}";
    }
}
