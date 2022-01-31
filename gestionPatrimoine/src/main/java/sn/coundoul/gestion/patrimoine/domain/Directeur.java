package sn.coundoul.gestion.patrimoine.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;

/**
 * A Directeur.
 */
@Table("directeur")
public class Directeur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_pers")
    private String nomPers;

    @NotNull(message = "must not be null")
    @Column("prenom_pers")
    private String prenomPers;

    @NotNull(message = "must not be null")
    @Column("sexe")
    private Sexe sexe;

    @NotNull(message = "must not be null")
    @Column("mobile")
    private String mobile;

    @Column("adresse")
    private String adresse;

    @NotNull(message = "must not be null")
    @Column("direction")
    private Direction direction;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Directeur id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomPers() {
        return this.nomPers;
    }

    public Directeur nomPers(String nomPers) {
        this.nomPers = nomPers;
        return this;
    }

    public void setNomPers(String nomPers) {
        this.nomPers = nomPers;
    }

    public String getPrenomPers() {
        return this.prenomPers;
    }

    public Directeur prenomPers(String prenomPers) {
        this.prenomPers = prenomPers;
        return this;
    }

    public void setPrenomPers(String prenomPers) {
        this.prenomPers = prenomPers;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Directeur sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Directeur mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Directeur adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Directeur direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Directeur)) {
            return false;
        }
        return id != null && id.equals(((Directeur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Directeur{" +
            "id=" + getId() +
            ", nomPers='" + getNomPers() + "'" +
            ", prenomPers='" + getPrenomPers() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", direction='" + getDirection() + "'" +
            "}";
    }
}
