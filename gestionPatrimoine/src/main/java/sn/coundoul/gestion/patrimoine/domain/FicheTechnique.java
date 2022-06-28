package sn.coundoul.gestion.patrimoine.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A FicheTechnique.
 */
@Table("fiche_technique")
public class FicheTechnique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("piece_jointe")
    private String pieceJointe;

    @Column("date_depot")
    private Instant dateDepot;

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

    public FicheTechnique id(Long id) {
        this.id = id;
        return this;
    }

    public String getPieceJointe() {
        return this.pieceJointe;
    }

    public FicheTechnique pieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
        return this;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public Instant getDateDepot() {
        return this.dateDepot;
    }

    public FicheTechnique dateDepot(Instant dateDepot) {
        this.dateDepot = dateDepot;
        return this;
    }

    public void setDateDepot(Instant dateDepot) {
        this.dateDepot = dateDepot;
    }

    public Responsable getNomResponsable() {
        return this.nomResponsable;
    }

    public FicheTechnique nomResponsable(Responsable responsable) {
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
        if (!(o instanceof FicheTechnique)) {
            return false;
        }
        return id != null && id.equals(((FicheTechnique) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FicheTechnique{" +
            "id=" + getId() +
            ", pieceJointe='" + getPieceJointe() + "'" +
            ", dateDepot='" + getDateDepot() + "'" +
            "}";
    }
}
