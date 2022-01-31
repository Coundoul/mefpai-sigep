package sn.coundoul.gestion.patrimoine.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Responsable.
 */
@Table("responsable")
public class Responsable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_responsable")
    private String nomResponsable;

    @NotNull(message = "must not be null")
    @Column("prenom_responsable")
    private String prenomResponsable;

    @NotNull(message = "must not be null")
    @Column("email")
    private String email;

    @NotNull(message = "must not be null")
    @Column("specialite")
    private String specialite;

    @NotNull(message = "must not be null")
    @Column("numb_1")
    private String numb1;

    @Column("numb_2")
    private String numb2;

    @NotNull(message = "must not be null")
    @Column("raison_social")
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
