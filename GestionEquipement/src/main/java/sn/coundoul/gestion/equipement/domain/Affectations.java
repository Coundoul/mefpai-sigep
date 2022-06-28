package sn.coundoul.gestion.equipement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import sn.coundoul.gestion.equipement.domain.enumeration.Type;

/**
 * A Affectations.
 */
@Entity
@Table(name = "affectations")
public class Affectations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantite_affecter", nullable = false)
    private Integer quantiteAffecter;

    @Column(name = "beneficiaire")
    private String beneficiaire;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_attribution", nullable = false)
    private Type typeAttribution;

    @NotNull
    @Column(name = "id_pers", nullable = false)
    private Integer idPers;

    @Column(name = "date_attribution")
    private Instant dateAttribution;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomMagazin", "nomFournisseur", "bon", "categorie" }, allowSetters = true)
    private Equipement equipement;

    @OneToMany(mappedBy = "affectations")
    @JsonIgnoreProperties(value = { "nomUtilisateur", "affectations" }, allowSetters = true)
    private Set<Attribution> attributions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public Affectations beneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Affectations id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getQuantiteAffecter() {
        return this.quantiteAffecter;
    }

    public Affectations quantiteAffecter(Integer quantiteAffecter) {
        this.quantiteAffecter = quantiteAffecter;
        return this;
    }

    public void setQuantiteAffecter(Integer quantiteAffecter) {
        this.quantiteAffecter = quantiteAffecter;
    }

    public Type getTypeAttribution() {
        return this.typeAttribution;
    }

    public Affectations typeAttribution(Type typeAttribution) {
        this.typeAttribution = typeAttribution;
        return this;
    }

    public void setTypeAttribution(Type typeAttribution) {
        this.typeAttribution = typeAttribution;
    }

    public Integer getIdPers() {
        return this.idPers;
    }

    public Affectations idPers(Integer idPers) {
        this.idPers = idPers;
        return this;
    }

    public void setIdPers(Integer idPers) {
        this.idPers = idPers;
    }

    public Instant getDateAttribution() {
        return this.dateAttribution;
    }

    public Affectations dateAttribution(Instant dateAttribution) {
        this.dateAttribution = dateAttribution;
        return this;
    }

    public void setDateAttribution(Instant dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    public Equipement getEquipement() {
        return this.equipement;
    }

    public Affectations equipement(Equipement equipement) {
        this.setEquipement(equipement);
        return this;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }

    public Set<Attribution> getAttributions() {
        return this.attributions;
    }

    public Affectations attributions(Set<Attribution> attributions) {
        this.setAttributions(attributions);
        return this;
    }

    public Affectations addAttribution(Attribution attribution) {
        this.attributions.add(attribution);
        attribution.setAffectations(this);
        return this;
    }

    public Affectations removeAttribution(Attribution attribution) {
        this.attributions.remove(attribution);
        attribution.setAffectations(null);
        return this;
    }

    public void setAttributions(Set<Attribution> attributions) {
        if (this.attributions != null) {
            this.attributions.forEach(i -> i.setAffectations(null));
        }
        if (attributions != null) {
            attributions.forEach(i -> i.setAffectations(this));
        }
        this.attributions = attributions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Affectations)) {
            return false;
        }
        return id != null && id.equals(((Affectations) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Affectations{" +
            "id=" + getId() +
            ", quantiteAffecter=" + getQuantiteAffecter() +
            ", beneficiaire='" + getBeneficiaire() + "'" +
            ", typeAttribution='" + getTypeAttribution() + "'" +
            ", idPers=" + getIdPers() +
            ", dateAttribution='" + getDateAttribution() + "'" +
            "}";
    }
}
