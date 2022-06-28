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
 * A Attribution.
 */
@Table("attribution")
public class Attribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("quantite_affecter")
    private Integer quantiteAffecter;

    @NotNull(message = "must not be null")
    @Column("id_pers")
    private Integer idPers;

    @Column("date_affectation")
    private Instant dateAffectation;

    @Transient
    private UtilisateurFinal nomUtilisateur;

    @Column("nom_utilisateur_id")
    private Long nomUtilisateurId;

    @JsonIgnoreProperties(value = { "equipement", "attributions" }, allowSetters = true)
    @Transient
    private Affectations affectations;

    @Column("affectations_id")
    private Long affectationsId;

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
        this.nomUtilisateurId = utilisateurFinal != null ? utilisateurFinal.getId() : null;
        return this;
    }

    public void setNomUtilisateur(UtilisateurFinal utilisateurFinal) {
        this.nomUtilisateur = utilisateurFinal;
        this.nomUtilisateurId = utilisateurFinal != null ? utilisateurFinal.getId() : null;
    }

    public Long getNomUtilisateurId() {
        return this.nomUtilisateurId;
    }

    public void setNomUtilisateurId(Long utilisateurFinal) {
        this.nomUtilisateurId = utilisateurFinal;
    }

    public Affectations getAffectations() {
        return this.affectations;
    }

    public Attribution affectations(Affectations affectations) {
        this.setAffectations(affectations);
        this.affectationsId = affectations != null ? affectations.getId() : null;
        return this;
    }

    public void setAffectations(Affectations affectations) {
        this.affectations = affectations;
        this.affectationsId = affectations != null ? affectations.getId() : null;
    }

    public Long getAffectationsId() {
        return this.affectationsId;
    }

    public void setAffectationsId(Long affectations) {
        this.affectationsId = affectations;
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
