package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A AttributionInfrastructure.
 */
@Entity
@Table(name = "attribution_infrastructure")
public class AttributionInfrastructure implements Serializable {

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
    @JsonIgnoreProperties(value = { "nomQuartier", "nomProjets" }, allowSetters = true)
    private Etablissement nomEtablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttributionInfrastructure id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDateAttribution() {
        return this.dateAttribution;
    }

    public AttributionInfrastructure dateAttribution(Instant dateAttribution) {
        this.dateAttribution = dateAttribution;
        return this;
    }

    public void setDateAttribution(Instant dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public AttributionInfrastructure quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getIdEquipement() {
        return this.idEquipement;
    }

    public AttributionInfrastructure idEquipement(Integer idEquipement) {
        this.idEquipement = idEquipement;
        return this;
    }

    public void setIdEquipement(Integer idEquipement) {
        this.idEquipement = idEquipement;
    }

    public Integer getIdPers() {
        return this.idPers;
    }

    public AttributionInfrastructure idPers(Integer idPers) {
        this.idPers = idPers;
        return this;
    }

    public void setIdPers(Integer idPers) {
        this.idPers = idPers;
    }

    public Etablissement getNomEtablissement() {
        return this.nomEtablissement;
    }

    public AttributionInfrastructure nomEtablissement(Etablissement etablissement) {
        this.setNomEtablissement(etablissement);
        return this;
    }

    public void setNomEtablissement(Etablissement etablissement) {
        this.nomEtablissement = etablissement;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributionInfrastructure)) {
            return false;
        }
        return id != null && id.equals(((AttributionInfrastructure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttributionInfrastructure{" +
            "id=" + getId() +
            ", dateAttribution='" + getDateAttribution() + "'" +
            ", quantite=" + getQuantite() +
            ", idEquipement=" + getIdEquipement() +
            ", idPers=" + getIdPers() +
            "}";
    }
}
