package sn.coundoul.gestion.utilisateurs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Direction;
import sn.coundoul.gestion.utilisateurs.domain.enumeration.Sexe;

/**
 * A ComptablePrincipale.
 */
@Entity
@Table(name = "comptable_principale")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ComptablePrincipale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_pers", nullable = false)
    private String nomPers;

    @NotNull
    @Column(name = "prenom_pers", nullable = false)
    private String prenomPers;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @NotNull
    @Column(name = "mobile", nullable = false)
    private String mobile;

    @Column(name = "adresse")
    private String adresse;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private Direction direction;

    @OneToMany(mappedBy = "comptablePrincipale")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comptablePrincipale" }, allowSetters = true)
    private Set<MangasinierFichiste> mangasinierFichistes = new HashSet<>();

    @OneToMany(mappedBy = "comptablePrincipale")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
