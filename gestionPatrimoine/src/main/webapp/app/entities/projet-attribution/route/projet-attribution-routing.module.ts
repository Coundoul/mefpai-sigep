import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProjetAttributionComponent } from '../list/projet-attribution.component';
import { ProjetAttributionDetailComponent } from '../detail/projet-attribution-detail.component';
import { ProjetAttributionUpdateComponent } from '../update/projet-attribution-update.component';
import { ProjetAttributionRoutingResolveService } from './projet-attribution-routing-resolve.service';

const projetAttributionRoute: Routes = [
  {
    path: '',
    component: ProjetAttributionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProjetAttributionDetailComponent,
    resolve: {
      projetAttribution: ProjetAttributionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProjetAttributionUpdateComponent,
    resolve: {
      projetAttribution: ProjetAttributionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProjetAttributionUpdateComponent,
    resolve: {
      projetAttribution: ProjetAttributionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(projetAttributionRoute)],
  exports: [RouterModule],
})
export class ProjetAttributionRoutingModule {}
