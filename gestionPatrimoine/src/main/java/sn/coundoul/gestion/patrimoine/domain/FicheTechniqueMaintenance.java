package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A FicheTechniqueMaintenance.
 */
@Table("fiche_technique_maintenance")
public class FicheTechniqueMaintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("piece_jointe")
    private String pieceJointe;

    @NotNull(message = "must not be null")
    @Column("id_pers")
    private Integer idPers;

    @Column("date_depot")
    private Instant dateDepot;

    @JsonIgnoreProperties(value = { "nomStructure" }, allowSetters = true)
    @Transient
    private Requete type;

    @Column("type_id")
    private Long typeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FicheTechniqueMaintenance id(Long id) {
        this.id = id;
        return this;
    }

    public String getPieceJointe() {
        return this.pieceJointe;
    }

    public FicheTechniqueMaintenance pieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
        return this;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public Integer getIdPers() {
        return this.idPers;
    }

    public FicheTechniqueMaintenance idPers(Integer idPers) {
        this.idPers = idPers;
        return this;
    }

    public void setIdPers(Integer idPers) {
        this.idPers = idPers;
    }

    public Instant getDateDepot() {
        return this.dateDepot;
    }

    public FicheTechniqueMaintenance dateDepot(Instant dateDepot) {
        this.dateDepot = dateDepot;
        return this;
    }

    public void setDateDepot(Instant dateDepot) {
        this.dateDepot = dateDepot;
    }

    public Requete getType() {
        return this.type;
    }

    public FicheTechniqueMaintenance type(Requete requete) {
        this.setType(requete);
        this.typeId = requete != null ? requete.getId() : null;
        return this;
    }

    public void setType(Requete requete) {
        this.type = requete;
        this.typeId = requete != null ? requete.getId() : null;
    }

    public Long getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Long requete) {
        this.typeId = requete;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FicheTechniqueMaintenance)) {
            return false;
        }
        return id != null && id.equals(((FicheTechniqueMaintenance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FicheTechniqueMaintenance{" +
            "id=" + getId() +
            ", pieceJointe='" + getPieceJointe() + "'" +
            ", idPers=" + getIdPers() +
            ", dateDepot='" + getDateDepot() + "'" +
            "}";
    }
}
