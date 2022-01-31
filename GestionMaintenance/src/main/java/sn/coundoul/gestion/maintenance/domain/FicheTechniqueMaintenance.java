package sn.coundoul.gestion.maintenance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FicheTechniqueMaintenance.
 */
@Entity
@Table(name = "fiche_technique_maintenance")
public class FicheTechniqueMaintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "piece_jointe", nullable = false)
    private String pieceJointe;

    @NotNull
    @Column(name = "id_pers", nullable = false)
    private Integer idPers;

    @Column(name = "date_depot")
    private Instant dateDepot;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomStructure" }, allowSetters = true)
    private Requete type;

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
        return this;
    }

    public void setType(Requete requete) {
        this.type = requete;
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
