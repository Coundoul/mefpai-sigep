package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Classe;

/**
 * A Salles.
 */
@Table("salles")
public class Salles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_salle")
    private String nomSalle;

    @NotNull(message = "must not be null")
    @Column("classe")
    private Classe classe;

    @JsonIgnoreProperties(value = { "nomEtablissement", "nomCorps", "typeBas", "nomAteliers" }, allowSetters = true)
    @Transient
    private Batiment nomBatiment;

    @Column("nom_batiment_id")
    private Long nomBatimentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Salles id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomSalle() {
        return this.nomSalle;
    }

    public Salles nomSalle(String nomSalle) {
        this.nomSalle = nomSalle;
        return this;
    }

    public void setNomSalle(String nomSalle) {
        this.nomSalle = nomSalle;
    }

    public Classe getClasse() {
        return this.classe;
    }

    public Salles classe(Classe classe) {
        this.classe = classe;
        return this;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Batiment getNomBatiment() {
        return this.nomBatiment;
    }

    public Salles nomBatiment(Batiment batiment) {
        this.setNomBatiment(batiment);
        this.nomBatimentId = batiment != null ? batiment.getId() : null;
        return this;
    }

    public void setNomBatiment(Batiment batiment) {
        this.nomBatiment = batiment;
        this.nomBatimentId = batiment != null ? batiment.getId() : null;
    }

    public Long getNomBatimentId() {
        return this.nomBatimentId;
    }

    public void setNomBatimentId(Long batiment) {
        this.nomBatimentId = batiment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Salles)) {
            return false;
        }
        return id != null && id.equals(((Salles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Salles{" +
            "id=" + getId() +
            ", nomSalle='" + getNomSalle() + "'" +
            ", classe='" + getClasse() + "'" +
            "}";
    }
}
