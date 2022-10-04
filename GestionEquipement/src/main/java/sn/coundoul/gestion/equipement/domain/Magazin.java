package sn.coundoul.gestion.equipement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Magazin.
 */
@Entity
@Table(name = "magazin")
public class Magazin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_magazin", nullable = false)
    private String nomMagazin;

    @NotNull
    @Column(name = "surface_batie", nullable = false)
    private Double surfaceBatie;

    @NotNull
    @Column(name = "superficie", nullable = false)
    private Double superficie;

    @NotNull
    @Column(name = "id_pers", nullable = false)
    private Integer idPers;

    @ManyToOne
    @JsonIgnoreProperties(allowSetters = true)
    private Quartier nomQuartier;

    @OneToMany(mappedBy = "nomMagazin")
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
        return this;
    }

    public void setNomQuartier(Quartier quartier) {
        this.nomQuartier = quartier;
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
