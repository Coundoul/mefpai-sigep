package sn.coundoul.gestion.equipement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DetailSortie.
 */
@Entity
@Table(name = "detail_sortie")
public class DetailSortie implements Serializable {

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

    @JsonIgnoreProperties(value = { "references" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
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
        return this;
    }

    public void setTypeBon(Bon bon) {
        this.typeBon = bon;
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
