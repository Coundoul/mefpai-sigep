package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;

/**
 * A ChefMaintenance.
 */
@Table("chef_maintenance")
public class ChefMaintenance implements Serializable {

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

    @Column("direction")
    private Direction direction;

    @Transient
    @JsonIgnoreProperties(value = { "chefMaintenance" }, allowSetters = true)
    private Set<Technicien> techniciens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChefMaintenance id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomPers() {
        return this.nomPers;
    }

    public ChefMaintenance nomPers(String nomPers) {
        this.nomPers = nomPers;
        return this;
    }

    public void setNomPers(String nomPers) {
        this.nomPers = nomPers;
    }

    public String getPrenomPers() {
        return this.prenomPers;
    }

    public ChefMaintenance prenomPers(String prenomPers) {
        this.prenomPers = prenomPers;
        return this;
    }

    public void setPrenomPers(String prenomPers) {
        this.prenomPers = prenomPers;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public ChefMaintenance sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getMobile() {
        return this.mobile;
    }

    public ChefMaintenance mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public ChefMaintenance adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public ChefMaintenance direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Set<Technicien> getTechniciens() {
        return this.techniciens;
    }

    public ChefMaintenance techniciens(Set<Technicien> techniciens) {
        this.setTechniciens(techniciens);
        return this;
    }

    public ChefMaintenance addTechnicien(Technicien technicien) {
        this.techniciens.add(technicien);
        technicien.setChefMaintenance(this);
        return this;
    }

    public ChefMaintenance removeTechnicien(Technicien technicien) {
        this.techniciens.remove(technicien);
        technicien.setChefMaintenance(null);
        return this;
    }

    public void setTechniciens(Set<Technicien> techniciens) {
        if (this.techniciens != null) {
            this.techniciens.forEach(i -> i.setChefMaintenance(null));
        }
        if (techniciens != null) {
            techniciens.forEach(i -> i.setChefMaintenance(this));
        }
        this.techniciens = techniciens;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChefMaintenance)) {
            return false;
        }
        return id != null && id.equals(((ChefMaintenance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChefMaintenance{" +
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
