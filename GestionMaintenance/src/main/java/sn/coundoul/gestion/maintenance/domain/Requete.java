package sn.coundoul.gestion.maintenance.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Requete.
 */
@Entity
@Table(name = "requete")
public class Requete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "type_panne", nullable = false)
    private Double typePanne;

    @NotNull
    @Column(name = "date_post", nullable = false)
    private Double datePost;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "etat_traite")
    private Boolean etatTraite;

    @Column(name = "date_lancement")
    private Instant dateLancement;

    @NotNull
    @Column(name = "id_pers", nullable = false)
    private Integer idPers;

    @ManyToOne
    private Bureau nomStructure;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Requete id(Long id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Requete type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getTypePanne() {
        return this.typePanne;
    }

    public Requete typePanne(Double typePanne) {
        this.typePanne = typePanne;
        return this;
    }

    public void setTypePanne(Double typePanne) {
        this.typePanne = typePanne;
    }

    public Double getDatePost() {
        return this.datePost;
    }

    public Requete datePost(Double datePost) {
        this.datePost = datePost;
        return this;
    }

    public void setDatePost(Double datePost) {
        this.datePost = datePost;
    }

    public String getDescription() {
        return this.description;
    }

    public Requete description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEtatTraite() {
        return this.etatTraite;
    }

    public Requete etatTraite(Boolean etatTraite) {
        this.etatTraite = etatTraite;
        return this;
    }

    public void setEtatTraite(Boolean etatTraite) {
        this.etatTraite = etatTraite;
    }

    public Instant getDateLancement() {
        return this.dateLancement;
    }

    public Requete dateLancement(Instant dateLancement) {
        this.dateLancement = dateLancement;
        return this;
    }

    public void setDateLancement(Instant dateLancement) {
        this.dateLancement = dateLancement;
    }

    public Integer getIdPers() {
        return this.idPers;
    }

    public Requete idPers(Integer idPers) {
        this.idPers = idPers;
        return this;
    }

    public void setIdPers(Integer idPers) {
        this.idPers = idPers;
    }

    public Bureau getNomStructure() {
        return this.nomStructure;
    }

    public Requete nomStructure(Bureau bureau) {
        this.setNomStructure(bureau);
        return this;
    }

    public void setNomStructure(Bureau bureau) {
        this.nomStructure = bureau;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Requete)) {
            return false;
        }
        return id != null && id.equals(((Requete) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Requete{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", typePanne=" + getTypePanne() +
            ", datePost=" + getDatePost() +
            ", description='" + getDescription() + "'" +
            ", etatTraite='" + getEtatTraite() + "'" +
            ", dateLancement='" + getDateLancement() + "'" +
            ", idPers=" + getIdPers() +
            "}";
    }
}
