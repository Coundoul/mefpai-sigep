package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import sn.coundoul.gestion.infrastructure.domain.enumeration.Classe;

/**
 * A Salles.
 */
@Entity
@Table(name = "salles")
public class Salles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_salle", nullable = false)
    private String nomSalle;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "classe", nullable = false)
    private Classe classe;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomEtablissement", "nomCorps", "typeBas", "nomAteliers" }, allowSetters = true)
    private Batiment nomBatiment;

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
        return this;
    }

    public void setNomBatiment(Batiment batiment) {
        this.nomBatiment = batiment;
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
