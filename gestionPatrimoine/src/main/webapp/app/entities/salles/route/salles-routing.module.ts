import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SallesComponent } from '../list/salles.component';
import { SallesDetailComponent } from '../detail/salles-detail.component';
import { SallesUpdateComponent } from '../update/salles-update.component';
import { SallesRoutingResolveService } from './salles-routing-resolve.service';

const sallesRoute: Routes = [
  {
    path: '',
    component: SallesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SallesDetailComponent,
    resolve: {
      salles: SallesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SallesUpdateComponent,
    resolve: {
      salles: SallesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SallesUpdateComponent,
    resolve: {
      salles: SallesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sallesRoute)],
  exports: [RouterModule],
})
export class SallesRoutingModule {}
