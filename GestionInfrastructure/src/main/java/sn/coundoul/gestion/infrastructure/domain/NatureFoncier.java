package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A NatureFoncier.
 */
@Entity
@Table(name = "nature_foncier")
public class NatureFoncier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "type_foncier", nullable = false)
    private String typeFoncier;

    @NotNull
    @Column(name = "piece_jointe", nullable = false)
    private String pieceJointe;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomResponsable" }, allowSetters = true)
    private CorpsEtat nomCorps;

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
        return this;
    }

    public void setNomCorps(CorpsEtat corpsEtat) {
        this.nomCorps = corpsEtat;
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
