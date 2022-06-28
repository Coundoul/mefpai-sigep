package sn.coundoul.gestion.equipement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Attribution.
 */
@Entity
@Table(name = "attribution")
public class Attribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantite_affecter", nullable = false)
    private Integer quantiteAffecter;

    @NotNull
    @Column(name = "id_pers", nullable = false)
    private Integer idPers;

    @Column(name = "date_affectation")
    private Instant dateAffectation;

    @ManyToOne
    private UtilisateurFinal nomUtilisateur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "equipement", "attributions" }, allowSetters = true)
    private Affectations affectations;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attribution id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getQuantiteAffecter() {
        return this.quantiteAffecter;
    }

    public Attribution quantiteAffecter(Integer quantiteAffecter) {
        this.quantiteAffecter = quantiteAffecter;
        return this;
    }

    public void setQuantiteAffecter(Integer quantiteAffecter) {
        this.quantiteAffecter = quantiteAffecter;
    }

    public Integer getIdPers() {
        return this.idPers;
    }

    public Attribution idPers(Integer idPers) {
        this.idPers = idPers;
        return this;
    }

    public void setIdPers(Integer idPers) {
        this.idPers = idPers;
    }

    public Instant getDateAffectation() {
        return this.dateAffectation;
    }

    public Attribution dateAffectation(Instant dateAffectation) {
        this.dateAffectation = dateAffectation;
        return this;
    }

    public void setDateAffectation(Instant dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public UtilisateurFinal getNomUtilisateur() {
        return this.nomUtilisateur;
    }

    public Attribution nomUtilisateur(UtilisateurFinal utilisateurFinal) {
        this.setNomUtilisateur(utilisateurFinal);
        return this;
    }

    public void setNomUtilisateur(UtilisateurFinal utilisateurFinal) {
        this.nomUtilisateur = utilisateurFinal;
    }

    public Affectations getAffectations() {
        return this.affectations;
    }

    public Attribution affectations(Affectations affectations) {
        this.setAffectations(affectations);
        return this;
    }

    public void setAffectations(Affectations affectations) {
        this.affectations = affectations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attribution)) {
            return false;
        }
        return id != null && id.equals(((Attribution) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attribution{" +
            "id=" + getId() +
            ", quantiteAffecter=" + getQuantiteAffecter() +
            ", idPers=" + getIdPers() +
            ", dateAffectation='" + getDateAffectation() + "'" +
            "}";
    }
}
