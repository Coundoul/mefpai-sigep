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
 * A Magazin.
 */
@Table("magazin")
public class Magazin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_magazin")
    private String nomMagazin;

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
    @JsonIgnoreProperties(value = { "nomMagazin", "nomFournisseur", "bon", "categorie" }, allowSetters = true)
    private Set<Equipement> references = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Magazin id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomMagazin() {
        return this.nomMagazin;
    }

    public Magazin nomMagazin(String nomMagazin) {
        this.nomMagazin = nomMagazin;
        return this;
    }

    public void setNomMagazin(String nomMagazin) {
        this.nomMagazin = nomMagazin;
    }

    public Double getSurfaceBatie() {
        return this.surfaceBatie;
    }

    public Magazin surfaceBatie(Double surfaceBatie) {
        this.surfaceBatie = surfaceBatie;
        return this;
    }

    public void setSurfaceBatie(Double surfaceBatie) {
        this.surfaceBatie = surfaceBatie;
    }

    public Double getSuperficie() {
        return this.superficie;
    }

    public Magazin superficie(Double superficie) {
        this.superficie = superficie;
        return this;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public Integer getIdPers() {
        return this.idPers;
    }

    public Magazin idPers(Integer idPers) {
        this.idPers = idPers;
        return this;
    }

    public void setIdPers(Integer idPers) {
        this.idPers = idPers;
    }

    public Quartier getNomQuartier() {
        return this.nomQuartier;
    }

    public Magazin nomQuartier(Quartier quartier) {
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

    public Set<Equipement> getReferences() {
        return this.references;
    }

    public Magazin references(Set<Equipement> equipements) {
        this.setReferences(equipements);
        return this;
    }

    public Magazin addReference(Equipement equipement) {
        this.references.add(equipement);
        equipement.setNomMagazin(this);
        return this;
    }

    public Magazin removeReference(Equipement equipement) {
        this.references.remove(equipement);
        equipement.setNomMagazin(null);
        return this;
    }

    public void setReferences(Set<Equipement> equipements) {
        if (this.references != null) {
            this.references.forEach(i -> i.setNomMagazin(null));
        }
        if (equipements != null) {
            equipements.forEach(i -> i.setNomMagazin(this));
        }
        this.references = equipements;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Magazin)) {
            return false;
        }
        return id != null && id.equals(((Magazin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Magazin{" +
            "id=" + getId() +
            ", nomMagazin='" + getNomMagazin() + "'" +
            ", surfaceBatie=" + getSurfaceBatie() +
            ", superficie=" + getSuperficie() +
            ", idPers=" + getIdPers() +
            "}";
    }
}
