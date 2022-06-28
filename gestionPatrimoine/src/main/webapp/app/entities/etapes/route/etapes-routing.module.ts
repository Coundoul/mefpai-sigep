import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EtapesComponent } from '../list/etapes.component';
import { EtapesDetailComponent } from '../detail/etapes-detail.component';
import { EtapesUpdateComponent } from '../update/etapes-update.component';
import { EtapesRoutingResolveService } from './etapes-routing-resolve.service';

const etapesRoute: Routes = [
  {
    path: '',
    component: EtapesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EtapesDetailComponent,
    resolve: {
      etapes: EtapesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EtapesUpdateComponent,
    resolve: {
      etapes: EtapesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EtapesUpdateComponent,
    resolve: {
      etapes: EtapesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(etapesRoute)],
  exports: [RouterModule],
})
export class EtapesRoutingModule {}
