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
 * A AttributionInfrastructure.
 */
@Table("attribution_infrastructure")
public class AttributionInfrastructure implements Serializable {

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

    @JsonIgnoreProperties(value = { "nomQuartier", "nomProjets" }, allowSetters = true)
    @Transient
    private Etablissement nomEtablissement;

    @Column("nom_etablissement_id")
    private Long nomEtablissementId;

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
        this.nomEtablissementId = etablissement != null ? etablissement.getId() : null;
        return this;
    }

    public void setNomEtablissement(Etablissement etablissement) {
        this.nomEtablissement = etablissement;
        this.nomEtablissementId = etablissement != null ? etablissement.getId() : null;
    }

    public Long getNomEtablissementId() {
        return this.nomEtablissementId;
    }

    public void setNomEtablissementId(Long etablissement) {
        this.nomEtablissementId = etablissement;
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
