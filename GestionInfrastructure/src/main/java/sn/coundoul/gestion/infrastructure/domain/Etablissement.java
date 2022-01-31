package sn.coundoul.gestion.infrastructure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Etablissement.
 */
@Entity
@Table(name = "etablissement")
public class Etablissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_etablissement", nullable = false)
    private String nomEtablissement;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Email
    @Size(min = 5, max = 254)
    @NotNull
    @Column(name = "email", length = 254, nullable = false)
    private String email;

    @NotNull
    @Column(name = "surface_batie", nullable = false)
    private Double surfaceBatie;

    @NotNull
    @Column(name = "superficie", nullable = false)
    private Double superficie;

    @NotNull
    @Column(name = "status_foncier", nullable = false)
    private String statusFoncier;

    @NotNull
    @Column(name = "nombre_apprenants", nullable = false)
    private Integer nombreApprenants;

    @NotNull
    @Column(name = "proprietaire", nullable = false)
    private String proprietaire;

    @NotNull
    @Column(name = "possibilite_extension", nullable = false)
    private String possibiliteExtension;

    @Lob
    @Column(name = "description_extension")
    private String descriptionExtension;

    @NotNull
    @Column(name = "branchement_eau", nullable = false)
    private String branchementEau;

    @NotNull
    @Column(name = "branchement_electricite", nullable = false)
    private String branchementElectricite;

    @NotNull
    @Column(name = "puissance_souscrite", nullable = false)
    private String puissanceSouscrite;

    @Column(name = "type_reseau")
    private String typeReseau;

    @NotNull
    @Column(name = "circuit_terre", nullable = false)
    private String circuitTerre;

    @NotNull
    @Column(name = "protection_arret", nullable = false)
    private String protectionArret;

    @NotNull
    @Column(name = "protection_foudre", nullable = false)
    private String protectionFoudre;

    @NotNull
    @Column(name = "connexion_tel", nullable = false)
    private String connexionTel;

    @NotNull
    @Column(name = "connexion_internet", nullable = false)
    private String connexionInternet;

    @Lob
    @Column(name = "environnement")
    private String environnement;

    @Lob
    @Column(name = "dispositif")
    private String dispositif;

    @NotNull
    @Column(name = "id_pers", nullable = false)
    private Integer idPers;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomCommune" }, allowSetters = true)
    private Quartier nomQuartier;

    @OneToMany(mappedBy = "nomEtablissement")
    @JsonIgnoreProperties(value = { "nom", "nomEtablissement", "nomBatiment", "nomIntervenants" }, allowSetters = true)
    private Set<Projets> nomProjets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etablissement id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomEtablissement() {
        return this.nomEtablissement;
    }

    public Etablissement nomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
        return this;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }


    public Integer getNombreApprenants() {
        return this.nombreApprenants;
    }

    public Etablissement nombreApprenants(Integer nombreApprenants) {
        this.nombreApprenants = nombreApprenants;
        return this;
    }

    public void setNombreApprenants(Integer nombreApprenants) {
        this.nombreApprenants = nombreApprenants;
    }


    public String getStatusFoncier() {
        return this.statusFoncier;
    }

    public Etablissement statusFoncier(String statusFoncier) {
        this.statusFoncier = statusFoncier;
        return this;
    }

    public void setStatusFoncier(String statusFoncier) {
        this.statusFoncier = statusFoncier;
    }

    public String getProprietaire() {
        return this.proprietaire;
    }

    public Etablissement proprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
        return this;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public String getPossibiliteExtension() {
        return this.possibiliteExtension;
    }

    public Etablissement possibiliteExtension(String possibiliteExtension) {
        this.possibiliteExtension = possibiliteExtension;
        return this;
    }

    public void setPossibiliteExtension(String possibiliteExtension) {
        this.possibiliteExtension = possibiliteExtension;
    }

    public String getDescriptionExtension() {
        return this.descriptionExtension;
    }

    public Etablissement descriptionExtension(String descriptionExtension) {
        this.descriptionExtension = descriptionExtension;
        return this;
    }

    public void setDescriptionExtension(String descriptionExtension) {
        this.descriptionExtension = descriptionExtension;
    }

    public String getBranchementEau() {
        return this.branchementEau;
    }

    public Etablissement branchementEau(String branchementEau) {
        this.branchementEau = branchementEau;
        return this;
    }

    public void setBranchementEau(String branchementEau) {
        this.branchementEau = branchementEau;
    }

    public String getBranchementElectricite() {
        return this.branchementElectricite;
    }

    public Etablissement branchementElectricite(String branchementElectricite) {
        this.branchementElectricite = branchementElectricite;
        return this;
    }

    public void setBranchementElectricite(String branchementElectricite) {
        this.branchementElectricite = branchementElectricite;
    }

    public String getTypeReseau() {
        return this.typeReseau;
    }

    public Etablissement typeReseau(String typeReseau) {
        this.typeReseau = typeReseau;
        return this;
    }

    public void setTypeReseau(String typeReseau) {
        this.typeReseau = typeReseau;
    }

    public String getCircuitTerre() {
        return this.circuitTerre;
    }

    public Etablissement circuitTerre(String circuitTerre) {
        this.circuitTerre = circuitTerre;
        return this;
    }

    public void setCircuitTerre(String circuitTerre) {
        this.circuitTerre = circuitTerre;
    }

    public String getProtectionArret() {
        return this.protectionArret;
    }

    public Etablissement protectionArret(String protectionArret) {
        this.protectionArret = protectionArret;
        return this;
    }

    public void setProtectionArret(String protectionArret) {
        this.protectionArret = protectionArret;
    }

    public String getProtectionFoudre() {
        return this.protectionFoudre;
    }

    public Etablissement protectionFoudre(String protectionFoudre) {
        this.protectionFoudre = protectionFoudre;
        return this;
    }

    public void setProtectionFoudre(String protectionFoudre) {
        this.protectionFoudre = protectionFoudre;
    }

    public String getConnexionTel() {
        return this.connexionTel;
    }

    public Etablissement connexionTel(String connexionTel) {
        this.connexionTel = connexionTel;
        return this;
    }

    public void setConnexionTel(String connexionTel) {
        this.connexionTel = connexionTel;
    }

    public String getConnexionInternet() {
        return this.connexionInternet;
    }

    public Etablissement connexionInternet(String connexionInternet) {
        this.connexionInternet = connexionInternet;
        return this;
    }

    public void setConnexionInternet(String connexionInternet) {
        this.connexionInternet = connexionInternet;
    }

    public String getPuissanceSouscrite() {
        return this.puissanceSouscrite;
    }

    public Etablissement puissanceSouscrite(String puissanceSouscrite) {
        this.puissanceSouscrite = puissanceSouscrite;
        return this;
    }

    public void setPuissanceSouscrite(String puissanceSouscrite) {
        this.puissanceSouscrite = puissanceSouscrite;
    }

    public String getEnvironnement() {
        return this.environnement;
    }

    public Etablissement environnement(String environnement) {
        this.environnement = environnement;
        return this;
    }

    public void setEnvironnement(String environnement) {
        this.environnement = environnement;
    }

    public String getDispositif() {
        return this.dispositif;
    }

    public Etablissement dispositif(String dispositif) {
        this.dispositif = dispositif;
        return this;
    }

    public void setDispositif(String dispositif) {
        this.dispositif = dispositif;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Etablissement telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    
    public String getEmail() {
        return this.email;
    }

    public Etablissement email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Etablissement adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Double getSurfaceBatie() {
        return this.surfaceBatie;
    }

    public Etablissement surfaceBatie(Double surfaceBatie) {
        this.surfaceBatie = surfaceBatie;
        return this;
    }

    public void setSurfaceBatie(Double surfaceBatie) {
        this.surfaceBatie = surfaceBatie;
    }

    public Double getSuperficie() {
        return this.superficie;
    }

    public Etablissement superficie(Double superficie) {
        this.superficie = superficie;
        return this;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public Integer getIdPers() {
        return this.idPers;
    }

    public Etablissement idPers(Integer idPers) {
        this.idPers = idPers;
        return this;
    }

    public void setIdPers(Integer idPers) {
        this.idPers = idPers;
    }
    
    
    public Quartier getNomQuartier() {
        return this.nomQuartier;
    }

    public Etablissement nomQuartier(Quartier quartier) {
        this.setNomQuartier(quartier);
        return this;
    }

    public void setNomQuartier(Quartier quartier) {
        this.nomQuartier = quartier;
    }

    public Set<Projets> getNomProjets() {
        return this.nomProjets;
    }

    public Etablissement nomProjets(Set<Projets> projets) {
        this.setNomProjets(projets);
        return this;
    }

    public Etablissement addNomProjet(Projets projets) {
        this.nomProjets.add(projets);
        projets.setNomEtablissement(this);
        return this;
    }

    public Etablissement removeNomProjet(Projets projets) {
        this.nomProjets.remove(projets);
        projets.setNomEtablissement(null);
        return this;
    }

    public void setNomProjets(Set<Projets> projets) {
        if (this.nomProjets != null) {
            this.nomProjets.forEach(i -> i.setNomEtablissement(null));
        }
        if (projets != null) {
            projets.forEach(i -> i.setNomEtablissement(this));
        }
        this.nomProjets = projets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etablissement)) {
            return false;
        }
        return id != null && id.equals(((Etablissement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etablissement{" +
            "id=" + getId() +
            ", nomEtablissement='" + getNomEtablissement() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", Email='" + getEmail() + "'" +
            ", surfaceBatie=" + getSurfaceBatie() +
            ", superficie=" + getSuperficie() +
            ", status_foncier='" + getStatusFoncier() + "'" +
            ", nombre_apprenants='" + getNombreApprenants() + "'" +
            ", proprietaire='" + getProprietaire() + "'" +
            ", possibilite_extension='" + getPossibiliteExtension() + "'" +
            ", description_extension='" + getDescriptionExtension() + "'" +
            ", branchement_eau='" + getBranchementEau() + "'" +
            ", branchement_electricite='" + getBranchementElectricite() + "'" +
            ", puissance_souscrite='" + getPuissanceSouscrite() + "'" +
            ", type_reseau='" + getTypeReseau() + "'" +
            ", circuit_terre='" + getCircuitTerre() + "'" +
            ", protection_arret='" + getProtectionArret() + "'" +
            ", protection_foudre='" + getProtectionFoudre() + "'" +
            ", connexion_tel='" + getConnexionTel() + "'" +
            ", connexion_internet='" + getConnexionInternet() + "'" +
            ", environnement='" + getEnvironnement() + "'" +
            ", dispositif='" + getDispositif() + "'" +            
            ", idPers=" + getIdPers() +
            "}";
    }
}
