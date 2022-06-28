import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IntervenantComponent } from '../list/intervenant.component';
import { IntervenantDetailComponent } from '../detail/intervenant-detail.component';
import { IntervenantUpdateComponent } from '../update/intervenant-update.component';
import { IntervenantRoutingResolveService } from './intervenant-routing-resolve.service';

const intervenantRoute: Routes = [
  {
    path: '',
    component: IntervenantComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IntervenantDetailComponent,
    resolve: {
      intervenant: IntervenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IntervenantUpdateComponent,
    resolve: {
      intervenant: IntervenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IntervenantUpdateComponent,
    resolve: {
      intervenant: IntervenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(intervenantRoute)],
  exports: [RouterModule],
})
export class IntervenantRoutingModule {}
