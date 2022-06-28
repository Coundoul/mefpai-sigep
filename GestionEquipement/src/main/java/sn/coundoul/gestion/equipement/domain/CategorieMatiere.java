package sn.coundoul.gestion.equipement.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CategorieMatiere.
 */
@Entity
@Table(name = "categorie_matiere")
public class CategorieMatiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "categorie", nullable = false)
    private String categorie;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategorieMatiere id(Long id) {
        this.id = id;
        return this;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public CategorieMatiere categorie(String categorie) {
        this.categorie = categorie;
        return this;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategorieMatiere)) {
            return false;
        }
        return id != null && id.equals(((CategorieMatiere) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategorieMatiere{" +
            "id=" + getId() +
            ", categorie='" + getCategorie() + "'" +
            "}";
    }
}
