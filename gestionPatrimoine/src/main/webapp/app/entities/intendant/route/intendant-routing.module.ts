import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IntendantComponent } from '../list/intendant.component';
import { IntendantDetailComponent } from '../detail/intendant-detail.component';
import { IntendantUpdateComponent } from '../update/intendant-update.component';
import { IntendantRoutingResolveService } from './intendant-routing-resolve.service';

const intendantRoute: Routes = [
  {
    path: '',
    component: IntendantComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IntendantDetailComponent,
    resolve: {
      intendant: IntendantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IntendantUpdateComponent,
    resolve: {
      intendant: IntendantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IntendantUpdateComponent,
    resolve: {
      intendant: IntendantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(intendantRoute)],
  exports: [RouterModule],
})
export class IntendantRoutingModule {}
