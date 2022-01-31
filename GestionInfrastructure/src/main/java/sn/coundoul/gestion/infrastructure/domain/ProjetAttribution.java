package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ProjetAttribution.
 */
@Entity
@Table(name = "projet_attribution")
public class ProjetAttribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_attribution")
    private Instant dateAttribution;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @NotNull
    @Column(name = "id_equipement", nullable = false)
    private Integer idEquipement;

    @NotNull
    @Column(name = "id_pers", nullable = false)
    private Integer idPers;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nom", "nomEtablissement", "nomBatiment", "nomIntervenants" }, allowSetters = true)
    private Projets nomProjet;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjetAttribution id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDateAttribution() {
        return this.dateAttribution;
    }

    public ProjetAttribution dateAttribution(Instant dateAttribution) {
        this.dateAttribution = dateAttribution;
        return this;
    }

    public void setDateAttribution(Instant dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public ProjetAttribution quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getIdEquipement() {
        return this.idEquipement;
    }

    public ProjetAttribution idEquipement(Integer idEquipement) {
        this.idEquipement = idEquipement;
        return this;
    }

    public void setIdEquipement(Integer idEquipement) {
        this.idEquipement = idEquipement;
    }

    public Integer getIdPers() {
        return this.idPers;
    }

    public ProjetAttribution idPers(Integer idPers) {
        this.idPers = idPers;
        return this;
    }

    public void setIdPers(Integer idPers) {
        this.idPers = idPers;
    }

    public Projets getNomProjet() {
        return this.nomProjet;
    }

    public ProjetAttribution nomProjet(Projets projets) {
        this.setNomProjet(projets);
        return this;
    }

    public void setNomProjet(Projets projets) {
        this.nomProjet = projets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjetAttribution)) {
            return false;
        }
        return id != null && id.equals(((ProjetAttribution) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjetAttribution{" +
            "id=" + getId() +
            ", dateAttribution='" + getDateAttribution() + "'" +
            ", quantite=" + getQuantite() +
            ", idEquipement=" + getIdEquipement() +
            ", idPers=" + getIdPers() +
            "}";
    }
}
