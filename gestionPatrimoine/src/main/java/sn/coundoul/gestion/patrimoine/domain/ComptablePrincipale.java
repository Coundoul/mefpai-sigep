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
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;

/**
 * A ComptablePrincipale.
 */
@Table("comptable_principale")
public class ComptablePrincipale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom_pers")
    private String nomPers;

    @NotNull(message = "must not be null")
    @Column("prenom_pers")
    private String prenomPers;

    @NotNull(message = "must not be null")
    @Column("sexe")
    private Sexe sexe;

    @NotNull(message = "must not be null")
    @Column("mobile")
    private String mobile;

    @Column("adresse")
    private String adresse;

    @NotNull(message = "must not be null")
    @Column("direction")
    private Direction direction;

    @Transient
    @JsonIgnoreProperties(value = { "comptablePrincipale" }, allowSetters = true)
    private Set<MangasinierFichiste> mangasinierFichistes = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "comptablePrincipale" }, allowSetters = true)
    private Set<ComptableSecondaire> comptableSecondaires = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComptablePrincipale id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomPers() {
        return this.nomPers;
    }

    public ComptablePrincipale nomPers(String nomPers) {
        this.nomPers = nomPers;
        return this;
    }

    public void setNomPers(String nomPers) {
        this.nomPers = nomPers;
    }

    public String getPrenomPers() {
        return this.prenomPers;
    }

    public ComptablePrincipale prenomPers(String prenomPers) {
        this.prenomPers = prenomPers;
        return this;
    }

    public void setPrenomPers(String prenomPers) {
        this.prenomPers = prenomPers;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public ComptablePrincipale sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getMobile() {
        return this.mobile;
    }

    public ComptablePrincipale mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public ComptablePrincipale adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public ComptablePrincipale direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Set<MangasinierFichiste> getMangasinierFichistes() {
        return this.mangasinierFichistes;
    }

    public ComptablePrincipale mangasinierFichistes(Set<MangasinierFichiste> mangasinierFichistes) {
        this.setMangasinierFichistes(mangasinierFichistes);
        return this;
    }

    public ComptablePrincipale addMangasinierFichiste(MangasinierFichiste mangasinierFichiste) {
        this.mangasinierFichistes.add(mangasinierFichiste);
        mangasinierFichiste.setComptablePrincipale(this);
        return this;
    }

    public ComptablePrincipale removeMangasinierFichiste(MangasinierFichiste mangasinierFichiste) {
        this.mangasinierFichistes.remove(mangasinierFichiste);
        mangasinierFichiste.setComptablePrincipale(null);
        return this;
    }

    public void setMangasinierFichistes(Set<MangasinierFichiste> mangasinierFichistes) {
        if (this.mangasinierFichistes != null) {
            this.mangasinierFichistes.forEach(i -> i.setComptablePrincipale(null));
        }
        if (mangasinierFichistes != null) {
            mangasinierFichistes.forEach(i -> i.setComptablePrincipale(this));
        }
        this.mangasinierFichistes = mangasinierFichistes;
    }

    public Set<ComptableSecondaire> getComptableSecondaires() {
        return this.comptableSecondaires;
    }

    public ComptablePrincipale comptableSecondaires(Set<ComptableSecondaire> comptableSecondaires) {
        this.setComptableSecondaires(comptableSecondaires);
        return this;
    }

    public ComptablePrincipale addComptableSecondaire(ComptableSecondaire comptableSecondaire) {
        this.comptableSecondaires.add(comptableSecondaire);
        comptableSecondaire.setComptablePrincipale(this);
        return this;
    }

    public ComptablePrincipale removeComptableSecondaire(ComptableSecondaire comptableSecondaire) {
        this.comptableSecondaires.remove(comptableSecondaire);
        comptableSecondaire.setComptablePrincipale(null);
        return this;
    }

    public void setComptableSecondaires(Set<ComptableSecondaire> comptableSecondaires) {
        if (this.comptableSecondaires != null) {
            this.comptableSecondaires.forEach(i -> i.setComptablePrincipale(null));
        }
        if (comptableSecondaires != null) {
            comptableSecondaires.forEach(i -> i.setComptablePrincipale(this));
        }
        this.comptableSecondaires = comptableSecondaires;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComptablePrincipale)) {
            return false;
        }
        return id != null && id.equals(((ComptablePrincipale) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComptablePrincipale{" +
            "id=" + getId() +
            ", nomPers='" + getNomPers() + "'" +
            ", prenomPers='" + getPrenomPers() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", direction='" + getDirection() + "'" +
            "}";
    }
}
