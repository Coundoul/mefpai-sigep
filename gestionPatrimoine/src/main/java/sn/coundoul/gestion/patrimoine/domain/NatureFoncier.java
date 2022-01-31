package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A NatureFoncier.
 */
@Table("nature_foncier")
public class NatureFoncier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("type_foncier")
    private String typeFoncier;

    @NotNull(message = "must not be null")
    @Column("piece_jointe")
    private String pieceJointe;

    @JsonIgnoreProperties(value = { "nomResponsable" }, allowSetters = true)
    @Transient
    private CorpsEtat nomCorps;

    @Column("nom_corps_id")
    private Long nomCorpsId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NatureFoncier id(Long id) {
        this.id = id;
        return this;
    }

    public String getTypeFoncier() {
        return this.typeFoncier;
    }

    public NatureFoncier typeFoncier(String typeFoncier) {
        this.typeFoncier = typeFoncier;
        return this;
    }

    public void setTypeFoncier(String typeFoncier) {
        this.typeFoncier = typeFoncier;
    }

    public String getPieceJointe() {
        return this.pieceJointe;
    }

    public NatureFoncier pieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
        return this;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public CorpsEtat getNomCorps() {
        return this.nomCorps;
    }

    public NatureFoncier nomCorps(CorpsEtat corpsEtat) {
        this.setNomCorps(corpsEtat);
        this.nomCorpsId = corpsEtat != null ? corpsEtat.getId() : null;
        return this;
    }

    public void setNomCorps(CorpsEtat corpsEtat) {
        this.nomCorps = corpsEtat;
        this.nomCorpsId = corpsEtat != null ? corpsEtat.getId() : null;
    }

    public Long getNomCorpsId() {
        return this.nomCorpsId;
    }

    public void setNomCorpsId(Long corpsEtat) {
        this.nomCorpsId = corpsEtat;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NatureFoncier)) {
            return false;
        }
        return id != null && id.equals(((NatureFoncier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NatureFoncier{" +
            "id=" + getId() +
            ", typeFoncier='" + getTypeFoncier() + "'" +
            ", pieceJointe='" + getPieceJointe() + "'" +
            "}";
    }
}
