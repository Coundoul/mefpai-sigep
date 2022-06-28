import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContratProjetComponent } from '../list/contrat-projet.component';
import { ContratProjetDetailComponent } from '../detail/contrat-projet-detail.component';
import { ContratProjetUpdateComponent } from '../update/contrat-projet-update.component';
import { ContratProjetRoutingResolveService } from './contrat-projet-routing-resolve.service';

const contratProjetRoute: Routes = [
  {
    path: '',
    component: ContratProjetComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContratProjetDetailComponent,
    resolve: {
      contratProjet: ContratProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContratProjetUpdateComponent,
    resolve: {
      contratProjet: ContratProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContratProjetUpdateComponent,
    resolve: {
      contratProjet: ContratProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contratProjetRoute)],
  exports: [RouterModule],
})
export class ContratProjetRoutingModule {}
