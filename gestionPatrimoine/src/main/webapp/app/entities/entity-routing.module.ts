import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'affectations',
        data: { pageTitle: 'gestionPatrimoineApp.affectations.home.title' },
        loadChildren: () => import('./affectations/affectations.module').then(m => m.AffectationsModule),
      },
      {
        path: 'atelier',
        data: { pageTitle: 'gestionPatrimoineApp.atelier.home.title' },
        loadChildren: () => import('./atelier/atelier.module').then(m => m.AtelierModule),
      },
      {
        path: 'attribution',
        data: { pageTitle: 'gestionPatrimoineApp.attribution.home.title' },
        loadChildren: () => import('./attribution/attribution.module').then(m => m.AttributionModule),
      },
      {
        path: 'attribution-infrastructure',
        data: { pageTitle: 'gestionPatrimoineApp.attributionInfrastructure.home.title' },
        loadChildren: () =>
          import('./attribution-infrastructure/attribution-infrastructure.module').then(m => m.AttributionInfrastructureModule),
      },
      {
        path: 'batiment',
        data: { pageTitle: 'gestionPatrimoineApp.batiment.home.title' },
        loadChildren: () => import('./batiment/batiment.module').then(m => m.BatimentModule),
      },
      {
        path: 'bon',
        data: { pageTitle: 'gestionPatrimoineApp.bon.home.title' },
        loadChildren: () => import('./bon/bon.module').then(m => m.BonModule),
      },
      {
        path: 'bureau',
        data: { pageTitle: 'gestionPatrimoineApp.bureau.home.title' },
        loadChildren: () => import('./bureau/bureau.module').then(m => m.BureauModule),
      },
      {
        path: 'categorie-matiere',
        data: { pageTitle: 'gestionPatrimoineApp.categorieMatiere.home.title' },
        loadChildren: () => import('./categorie-matiere/categorie-matiere.module').then(m => m.CategorieMatiereModule),
      },
      {
        path: 'categoriser',
        data: { pageTitle: 'gestionPatrimoineApp.categoriser.home.title' },
        loadChildren: () => import('./categoriser/categoriser.module').then(m => m.CategoriserModule),
      },
      {
        path: 'chef-etablissement',
        data: { pageTitle: 'gestionPatrimoineApp.chefEtablissement.home.title' },
        loadChildren: () => import('./chef-etablissement/chef-etablissement.module').then(m => m.ChefEtablissementModule),
      },
      {
        path: 'chef-maintenance',
        data: { pageTitle: 'gestionPatrimoineApp.chefMaintenance.home.title' },
        loadChildren: () => import('./chef-maintenance/chef-maintenance.module').then(m => m.ChefMaintenanceModule),
      },
      {
        path: 'chef-projet',
        data: { pageTitle: 'gestionPatrimoineApp.chefProjet.home.title' },
        loadChildren: () => import('./chef-projet/chef-projet.module').then(m => m.ChefProjetModule),
      },
      {
        path: 'commune',
        data: { pageTitle: 'gestionPatrimoineApp.commune.home.title' },
        loadChildren: () => import('./commune/commune.module').then(m => m.CommuneModule),
      },
      {
        path: 'comptable-principale',
        data: { pageTitle: 'gestionPatrimoineApp.comptablePrincipale.home.title' },
        loadChildren: () => import('./comptable-principale/comptable-principale.module').then(m => m.ComptablePrincipaleModule),
      },
      {
        path: 'comptable-secondaire',
        data: { pageTitle: 'gestionPatrimoineApp.comptableSecondaire.home.title' },
        loadChildren: () => import('./comptable-secondaire/comptable-secondaire.module').then(m => m.ComptableSecondaireModule),
      },
      {
        path: 'contrat-projet',
        data: { pageTitle: 'gestionPatrimoineApp.contratProjet.home.title' },
        loadChildren: () => import('./contrat-projet/contrat-projet.module').then(m => m.ContratProjetModule),
      },
      {
        path: 'corps-etat',
        data: { pageTitle: 'gestionPatrimoineApp.corpsEtat.home.title' },
        loadChildren: () => import('./corps-etat/corps-etat.module').then(m => m.CorpsEtatModule),
      },
      {
        path: 'departement',
        data: { pageTitle: 'gestionPatrimoineApp.departement.home.title' },
        loadChildren: () => import('./departement/departement.module').then(m => m.DepartementModule),
      },
      {
        path: 'detail-sortie',
        data: { pageTitle: 'gestionPatrimoineApp.detailSortie.home.title' },
        loadChildren: () => import('./detail-sortie/detail-sortie.module').then(m => m.DetailSortieModule),
      },
      {
        path: 'detenteur',
        data: { pageTitle: 'gestionPatrimoineApp.detenteur.home.title' },
        loadChildren: () => import('./detenteur/detenteur.module').then(m => m.DetenteurModule),
      },
      {
        path: 'directeur',
        data: { pageTitle: 'gestionPatrimoineApp.directeur.home.title' },
        loadChildren: () => import('./directeur/directeur.module').then(m => m.DirecteurModule),
      },
      {
        path: 'equipement',
        data: { pageTitle: 'gestionPatrimoineApp.equipement.home.title' },
        loadChildren: () => import('./equipement/equipement.module').then(m => m.EquipementModule),
      },
      {
        path: 'inventaire',
        data: { pageTitle: 'gestionPatrimoineApp.inventaire.home.title' },
        loadChildren: () => import('./inventaire/inventaire.module').then(m => m.InventaireModule),
      },
      {
        path: 'etablissement',
        data: { pageTitle: 'gestionPatrimoineApp.etablissement.home.title' },
        loadChildren: () => import('./etablissement/etablissement.module').then(m => m.EtablissementModule),
      },
      {
        path: 'etapes',
        data: { pageTitle: 'gestionPatrimoineApp.etapes.home.title' },
        loadChildren: () => import('./etapes/etapes.module').then(m => m.EtapesModule),
      },
      {
        path: 'fiche-technique',
        data: { pageTitle: 'gestionPatrimoineApp.ficheTechnique.home.title' },
        loadChildren: () => import('./fiche-technique/fiche-technique.module').then(m => m.FicheTechniqueModule),
      },
      {
        path: 'fiche-technique-maintenance',
        data: { pageTitle: 'gestionPatrimoineApp.ficheTechniqueMaintenance.home.title' },
        loadChildren: () =>
          import('./fiche-technique-maintenance/fiche-technique-maintenance.module').then(m => m.FicheTechniqueMaintenanceModule),
      },
      {
        path: 'filiere-stabilise',
        data: { pageTitle: 'gestionPatrimoineApp.filiereStabilise.home.title' },
        loadChildren: () => import('./filiere-stabilise/filiere-stabilise.module').then(m => m.FiliereStabiliseModule),
      },
      {
        path: 'formateurs',
        data: { pageTitle: 'gestionPatrimoineApp.formateurs.home.title' },
        loadChildren: () => import('./formateurs/formateurs.module').then(m => m.FormateursModule),
      },
      {
        path: 'fournisseur',
        data: { pageTitle: 'gestionPatrimoineApp.fournisseur.home.title' },
        loadChildren: () => import('./fournisseur/fournisseur.module').then(m => m.FournisseurModule),
      },
      {
        path: 'intendant',
        data: { pageTitle: 'gestionPatrimoineApp.intendant.home.title' },
        loadChildren: () => import('./intendant/intendant.module').then(m => m.IntendantModule),
      },
      {
        path: 'intervenant',
        data: { pageTitle: 'gestionPatrimoineApp.intervenant.home.title' },
        loadChildren: () => import('./intervenant/intervenant.module').then(m => m.IntervenantModule),
      },
      {
        path: 'magazin',
        data: { pageTitle: 'gestionPatrimoineApp.magazin.home.title' },
        loadChildren: () => import('./magazin/magazin.module').then(m => m.MagazinModule),
      },
      {
        path: 'mangasinier-fichiste',
        data: { pageTitle: 'gestionPatrimoineApp.mangasinierFichiste.home.title' },
        loadChildren: () => import('./mangasinier-fichiste/mangasinier-fichiste.module').then(m => m.MangasinierFichisteModule),
      },
      {
        path: 'nature-foncier',
        data: { pageTitle: 'gestionPatrimoineApp.natureFoncier.home.title' },
        loadChildren: () => import('./nature-foncier/nature-foncier.module').then(m => m.NatureFoncierModule),
      },
      {
        path: 'ordonnaceur-matiere',
        data: { pageTitle: 'gestionPatrimoineApp.ordonnaceurMatiere.home.title' },
        loadChildren: () => import('./ordonnaceur-matiere/ordonnaceur-matiere.module').then(m => m.OrdonnaceurMatiereModule),
      },
      {
        path: 'projet-attribution',
        data: { pageTitle: 'gestionPatrimoineApp.projetAttribution.home.title' },
        loadChildren: () => import('./projet-attribution/projet-attribution.module').then(m => m.ProjetAttributionModule),
      },
      {
        path: 'projets',
        data: { pageTitle: 'gestionPatrimoineApp.projets.home.title' },
        loadChildren: () => import('./projets/projets.module').then(m => m.ProjetsModule),
      },
      {
        path: 'quartier',
        data: { pageTitle: 'gestionPatrimoineApp.quartier.home.title' },
        loadChildren: () => import('./quartier/quartier.module').then(m => m.QuartierModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'gestionPatrimoineApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'requete',
        data: { pageTitle: 'gestionPatrimoineApp.requete.home.title' },
        loadChildren: () => import('./requete/requete.module').then(m => m.RequeteModule),
      },
      {
        path: 'responsable',
        data: { pageTitle: 'gestionPatrimoineApp.responsable.home.title' },
        loadChildren: () => import('./responsable/responsable.module').then(m => m.ResponsableModule),
      },
      {
        path: 'salles',
        data: { pageTitle: 'gestionPatrimoineApp.salles.home.title' },
        loadChildren: () => import('./salles/salles.module').then(m => m.SallesModule),
      },
      {
        path: 'technicien',
        data: { pageTitle: 'gestionPatrimoineApp.technicien.home.title' },
        loadChildren: () => import('./technicien/technicien.module').then(m => m.TechnicienModule),
      },
      {
        path: 'type-batiment',
        data: { pageTitle: 'gestionPatrimoineApp.typeBatiment.home.title' },
        loadChildren: () => import('./type-batiment/type-batiment.module').then(m => m.TypeBatimentModule),
      },
      {
        path: 'utilisateur-final',
        data: { pageTitle: 'gestionPatrimoineApp.utilisateurFinal.home.title' },
        loadChildren: () => import('./utilisateur-final/utilisateur-final.module').then(m => m.UtilisateurFinalModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
