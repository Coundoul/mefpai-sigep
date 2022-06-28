package sn.coundoul.gestion.patrimoine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Etablissement.
 */
@Table("etablissement")
public class Etablissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_etablissement")
    private String nomEtablissement;

    @NotNull(message = "must not be null")
    @Column("surface_batie")
    private Double surfaceBatie;

    @NotNull(message = "must not be null")
    @Column("superficie")
    private Double superficie;

    @NotNull(message = "must not be null")
    @Column("id_pers")
    private Integer idPers;

    @JsonIgnoreProperties(value = { "nomCommune" }, allowSetters = true)
    @Transient
    private Quartier nomQuartier;

    @Column("nom_quartier_id")
    private Long nomQuartierId;

    @Transient
    @JsonIgnoreProperties(value = { "nom", "nomEtablissement", "nomBatiment", "nomIntervenants" }, allowSetters = true)
    private Set<Projets> nomProjets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etablissement id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomEtablissement() {
        return this.nomEtablissement;
    }

    public Etablissement nomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
        return this;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    public Double getSurfaceBatie() {
        return this.surfaceBatie;
    }

    public Etablissement surfaceBatie(Double surfaceBatie) {
        this.surfaceBatie = surfaceBatie;
        return this;
    }

    public void setSurfaceBatie(Double surfaceBatie) {
        this.surfaceBatie = surfaceBatie;
    }

    public Double getSuperficie() {
        return this.superficie;
    }

    public Etablissement superficie(Double superficie) {
        this.superficie = superficie;
        return this;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public Integer getIdPers() {
        return this.idPers;
    }

    public Etablissement idPers(Integer idPers) {
        this.idPers = idPers;
        return this;
    }

    public void setIdPers(Integer idPers) {
        this.idPers = idPers;
    }

    public Quartier getNomQuartier() {
        return this.nomQuartier;
    }

    public Etablissement nomQuartier(Quartier quartier) {
        this.setNomQuartier(quartier);
        this.nomQuartierId = quartier != null ? quartier.getId() : null;
        return this;
    }

    public void setNomQuartier(Quartier quartier) {
        this.nomQuartier = quartier;
        this.nomQuartierId = quartier != null ? quartier.getId() : null;
    }

    public Long getNomQuartierId() {
        return this.nomQuartierId;
    }

    public void setNomQuartierId(Long quartier) {
        this.nomQuartierId = quartier;
    }

    public Set<Projets> getNomProjets() {
        return this.nomProjets;
    }

    public Etablissement nomProjets(Set<Projets> projets) {
        this.setNomProjets(projets);
        return this;
    }

    public Etablissement addNomProjet(Projets projets) {
        this.nomProjets.add(projets);
        projets.setNomEtablissement(this);
        return this;
    }

    public Etablissement removeNomProjet(Projets projets) {
        this.nomProjets.remove(projets);
        projets.setNomEtablissement(null);
        return this;
    }

    public void setNomProjets(Set<Projets> projets) {
        if (this.nomProjets != null) {
            this.nomProjets.forEach(i -> i.setNomEtablissement(null));
        }
        if (projets != null) {
            projets.forEach(i -> i.setNomEtablissement(this));
        }
        this.nomProjets = projets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etablissement)) {
            return false;
        }
        return id != null && id.equals(((Etablissement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etablissement{" +
            "id=" + getId() +
            ", nomEtablissement='" + getNomEtablissement() + "'" +
            ", surfaceBatie=" + getSurfaceBatie() +
            ", superficie=" + getSuperficie() +
            ", idPers=" + getIdPers() +
            "}";
    }
}
