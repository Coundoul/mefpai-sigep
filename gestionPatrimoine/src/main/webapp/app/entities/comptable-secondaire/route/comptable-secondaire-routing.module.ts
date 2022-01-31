import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ComptableSecondaireComponent } from '../list/comptable-secondaire.component';
import { ComptableSecondaireDetailComponent } from '../detail/comptable-secondaire-detail.component';
import { ComptableSecondaireUpdateComponent } from '../update/comptable-secondaire-update.component';
import { ComptableSecondaireRoutingResolveService } from './comptable-secondaire-routing-resolve.service';

const comptableSecondaireRoute: Routes = [
  {
    path: '',
    component: ComptableSecondaireComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ComptableSecondaireDetailComponent,
    resolve: {
      comptableSecondaire: ComptableSecondaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ComptableSecondaireUpdateComponent,
    resolve: {
      comptableSecondaire: ComptableSecondaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ComptableSecondaireUpdateComponent,
    resolve: {
      comptableSecondaire: ComptableSecondaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(comptableSecondaireRoute)],
  exports: [RouterModule],
})
export class ComptableSecondaireRoutingModule {}
