package sn.coundoul.gestion.patrimoine.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CorpsEtat.
 */
@Table("corps_etat")
public class CorpsEtat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_corps")
    private String nomCorps;

    @NotNull(message = "must not be null")
    @Column("gros_oeuvre")
    private String grosOeuvre;

    @NotNull(message = "must not be null")
    @Column("description_gros_oeuvre")
    private String descriptionGrosOeuvre;

    @NotNull(message = "must not be null")
    @Column("second_oeuvre")
    private String secondOeuvre;

    @NotNull(message = "must not be null")
    @Column("description_second_oeuvre")
    private String descriptionSecondOeuvre;

    @NotNull(message = "must not be null")
    @Column("oservation")
    private Boolean oservation;

    @Column("etat_corps")
    private Boolean etatCorps;

    @Transient
    private Responsable nomResponsable;

    @Column("nom_responsable_id")
    private Long nomResponsableId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CorpsEtat id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomCorps() {
        return this.nomCorps;
    }

    public CorpsEtat nomCorps(String nomCorps) {
        this.nomCorps = nomCorps;
        return this;
    }

    public void setNomCorps(String nomCorps) {
        this.nomCorps = nomCorps;
    }

    public String getGrosOeuvre() {
        return this.grosOeuvre;
    }

    public CorpsEtat grosOeuvre(String grosOeuvre) {
        this.grosOeuvre = grosOeuvre;
        return this;
    }

    public void setGrosOeuvre(String grosOeuvre) {
        this.grosOeuvre = grosOeuvre;
    }

    public String getDescriptionGrosOeuvre() {
        return this.descriptionGrosOeuvre;
    }

    public CorpsEtat descriptionGrosOeuvre(String descriptionGrosOeuvre) {
        this.descriptionGrosOeuvre = descriptionGrosOeuvre;
        return this;
    }

    public void setDescriptionGrosOeuvre(String descriptionGrosOeuvre) {
        this.descriptionGrosOeuvre = descriptionGrosOeuvre;
    }

    public String getSecondOeuvre() {
        return this.secondOeuvre;
    }

    public CorpsEtat secondOeuvre(String secondOeuvre) {
        this.secondOeuvre = secondOeuvre;
        return this;
    }

    public void setSecondOeuvre(String secondOeuvre) {
        this.secondOeuvre = secondOeuvre;
    }

    public String getDescriptionSecondOeuvre() {
        return this.descriptionSecondOeuvre;
    }

    public CorpsEtat descriptionSecondOeuvre(String descriptionSecondOeuvre) {
        this.descriptionSecondOeuvre = descriptionSecondOeuvre;
        return this;
    }

    public void setDescriptionSecondOeuvre(String descriptionSecondOeuvre) {
        this.descriptionSecondOeuvre = descriptionSecondOeuvre;
    }

    public Boolean getOservation() {
        return this.oservation;
    }

    public CorpsEtat oservation(Boolean oservation) {
        this.oservation = oservation;
        return this;
    }

    public void setOservation(Boolean oservation) {
        this.oservation = oservation;
    }

    public Boolean getEtatCorps() {
        return this.etatCorps;
    }

    public CorpsEtat etatCorps(Boolean etatCorps) {
        this.etatCorps = etatCorps;
        return this;
    }

    public void setEtatCorps(Boolean etatCorps) {
        this.etatCorps = etatCorps;
    }

    public Responsable getNomResponsable() {
        return this.nomResponsable;
    }

    public CorpsEtat nomResponsable(Responsable responsable) {
        this.setNomResponsable(responsable);
        this.nomResponsableId = responsable != null ? responsable.getId() : null;
        return this;
    }

    public void setNomResponsable(Responsable responsable) {
        this.nomResponsable = responsable;
        this.nomResponsableId = responsable != null ? responsable.getId() : null;
    }

    public Long getNomResponsableId() {
        return this.nomResponsableId;
    }

    public void setNomResponsableId(Long responsable) {
        this.nomResponsableId = responsable;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CorpsEtat)) {
            return false;
        }
        return id != null && id.equals(((CorpsEtat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorpsEtat{" +
            "id=" + getId() +
            ", nomCorps='" + getNomCorps() + "'" +
            ", grosOeuvre='" + getGrosOeuvre() + "'" +
            ", descriptionGrosOeuvre='" + getDescriptionGrosOeuvre() + "'" +
            ", secondOeuvre='" + getSecondOeuvre() + "'" +
            ", descriptionSecondOeuvre='" + getDescriptionSecondOeuvre() + "'" +
            ", oservation='" + getOservation() + "'" +
            ", etatCorps='" + getEtatCorps() + "'" +
            "}";
    }
}
