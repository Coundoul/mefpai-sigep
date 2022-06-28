import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UtilisateurFinalComponent } from '../list/utilisateur-final.component';
import { UtilisateurFinalDetailComponent } from '../detail/utilisateur-final-detail.component';
import { UtilisateurFinalUpdateComponent } from '../update/utilisateur-final-update.component';
import { UtilisateurFinalRoutingResolveService } from './utilisateur-final-routing-resolve.service';

const utilisateurFinalRoute: Routes = [
  {
    path: '',
    component: UtilisateurFinalComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UtilisateurFinalDetailComponent,
    resolve: {
      utilisateurFinal: UtilisateurFinalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UtilisateurFinalUpdateComponent,
    resolve: {
      utilisateurFinal: UtilisateurFinalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UtilisateurFinalUpdateComponent,
    resolve: {
      utilisateurFinal: UtilisateurFinalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(utilisateurFinalRoute)],
  exports: [RouterModule],
})
export class UtilisateurFinalRoutingModule {}
