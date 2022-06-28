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
 * A ProjetAttribution.
 */
@Table("projet_attribution")
public class ProjetAttribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("date_attribution")
    private Instant dateAttribution;

    @NotNull(message = "must not be null")
    @Column("quantite")
    private Integer quantite;

    @NotNull(message = "must not be null")
    @Column("id_equipement")
    private Integer idEquipement;

    @NotNull(message = "must not be null")
    @Column("id_pers")
    private Integer idPers;

    @JsonIgnoreProperties(value = { "nom", "nomEtablissement", "nomBatiment", "nomIntervenants" }, allowSetters = true)
    @Transient
    private Projets nomProjet;

    @Column("nom_projet_id")
    private Long nomProjetId;

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
        this.nomProjetId = projets != null ? projets.getId() : null;
        return this;
    }

    public void setNomProjet(Projets projets) {
        this.nomProjet = projets;
        this.nomProjetId = projets != null ? projets.getId() : null;
    }

    public Long getNomProjetId() {
        return this.nomProjetId;
    }

    public void setNomProjetId(Long projets) {
        this.nomProjetId = projets;
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
