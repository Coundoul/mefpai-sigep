package sn.coundoul.gestion.equipement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import sn.coundoul.gestion.equipement.domain.enumeration.TypeBon;

/**
 * A Bon.
 */
@Entity
@Table(name = "bon")
public class Bon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_bon", nullable = false)
    private TypeBon typeBon;

    @Column(name = "quantite_livre")
    private Integer quantiteLivre;

    @Column(name = "quantite_commande")
    private Integer quantiteCommande;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @Column(name = "etat")
    private Boolean etat;

    @OneToMany(mappedBy = "bon")
    @JsonIgnoreProperties(value = { "nomMagazin", "nomFournisseur", "bon", "categorie" }, allowSetters = true)
    private Set<Equipement> references = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bon id(Long id) {
        this.id = id;
        return this;
    }

    public TypeBon getTypeBon() {
        return this.typeBon;
    }

    public Bon typeBon(TypeBon typeBon) {
        this.typeBon = typeBon;
        return this;
    }

    public void setTypeBon(TypeBon typeBon) {
        this.typeBon = typeBon;
    }

    public Integer getQuantiteLivre() {
        return this.quantiteLivre;
    }

    public Bon quantiteLivre(Integer quantiteLivre) {
        this.quantiteLivre = quantiteLivre;
        return this;
    }

    public void setQuantiteLivre(Integer quantiteLivre) {
        this.quantiteLivre = quantiteLivre;
    }

    public Integer getQuantiteCommande() {
        return this.quantiteCommande;
    }

    public Bon quantiteCommande(Integer quantiteCommande) {
        this.quantiteCommande = quantiteCommande;
        return this;
    }

    public void setQuantiteCommande(Integer quantiteCommande) {
        this.quantiteCommande = quantiteCommande;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Bon dateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getEtat() {
        return this.etat;
    }

    public Bon etat(Boolean etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    public Set<Equipement> getReferences() {
        return this.references;
    }

    public Bon references(Set<Equipement> equipements) {
        this.setReferences(equipements);
        return this;
    }

    public Bon addReference(Equipement equipement) {
        this.references.add(equipement);
        equipement.setBon(this);
        return this;
    }

    public Bon removeReference(Equipement equipement) {
        this.references.remove(equipement);
        equipement.setBon(null);
        return this;
    }

    public void setReferences(Set<Equipement> equipements) {
        if (this.references != null) {
            this.references.forEach(i -> i.setBon(null));
        }
        if (equipements != null) {
            equipements.forEach(i -> i.setBon(this));
        }
        this.references = equipements;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bon)) {
            return false;
        }
        return id != null && id.equals(((Bon) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bon{" +
            "id=" + getId() +
            ", typeBon='" + getTypeBon() + "'" +
            ", quantiteLivre=" + getQuantiteLivre() +
            ", quantiteCommande=" + getQuantiteCommande() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
