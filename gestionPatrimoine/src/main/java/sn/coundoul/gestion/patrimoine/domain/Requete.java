package sn.coundoul.gestion.patrimoine.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Requete.
 */
@Table("requete")
public class Requete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("type")
    private String type;

    @NotNull(message = "must not be null")
    @Column("type_panne")
    private Double typePanne;

    @NotNull(message = "must not be null")
    @Column("date_post")
    private Double datePost;

    @NotNull(message = "must not be null")
    @Column("description")
    private String description;

    @Column("etat_traite")
    private Boolean etatTraite;

    @Column("date_lancement")
    private Instant dateLancement;

    @NotNull(message = "must not be null")
    @Column("id_pers")
    private Integer idPers;

    @Transient
    private Bureau nomStructure;

    @Column("nom_structure_id")
    private Long nomStructureId;

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
        this.nomStructureId = bureau != null ? bureau.getId() : null;
        return this;
    }

    public void setNomStructure(Bureau bureau) {
        this.nomStructure = bureau;
        this.nomStructureId = bureau != null ? bureau.getId() : null;
    }

    public Long getNomStructureId() {
        return this.nomStructureId;
    }

    public void setNomStructureId(Long bureau) {
        this.nomStructureId = bureau;
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
