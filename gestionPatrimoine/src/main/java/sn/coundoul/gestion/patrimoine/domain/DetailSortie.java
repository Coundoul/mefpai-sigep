package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DetailSortie.
 */
@Table("detail_sortie")
public class DetailSortie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("piece_jointe")
    private String pieceJointe;

    @NotNull(message = "must not be null")
    @Column("id_pers")
    private Integer idPers;

    private Long typeBonId;

    @Transient
    private Bon typeBon;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DetailSortie id(Long id) {
        this.id = id;
        return this;
    }

    public String getPieceJointe() {
        return this.pieceJointe;
    }

    public DetailSortie pieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
        return this;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public Integer getIdPers() {
        return this.idPers;
    }

    public DetailSortie idPers(Integer idPers) {
        this.idPers = idPers;
        return this;
    }

    public void setIdPers(Integer idPers) {
        this.idPers = idPers;
    }

    public Bon getTypeBon() {
        return this.typeBon;
    }

    public DetailSortie typeBon(Bon bon) {
        this.setTypeBon(bon);
        this.typeBonId = bon != null ? bon.getId() : null;
        return this;
    }

    public void setTypeBon(Bon bon) {
        this.typeBon = bon;
        this.typeBonId = bon != null ? bon.getId() : null;
    }

    public Long getTypeBonId() {
        return this.typeBonId;
    }

    public void setTypeBonId(Long bon) {
        this.typeBonId = bon;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailSortie)) {
            return false;
        }
        return id != null && id.equals(((DetailSortie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailSortie{" +
            "id=" + getId() +
            ", pieceJointe='" + getPieceJointe() + "'" +
            ", idPers=" + getIdPers() +
            "}";
    }
}
