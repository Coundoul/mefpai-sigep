import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DetenteurComponent } from '../list/detenteur.component';
import { DetenteurDetailComponent } from '../detail/detenteur-detail.component';
import { DetenteurUpdateComponent } from '../update/detenteur-update.component';
import { DetenteurRoutingResolveService } from './detenteur-routing-resolve.service';

const detenteurRoute: Routes = [
  {
    path: '',
    component: DetenteurComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetenteurDetailComponent,
    resolve: {
      detenteur: DetenteurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DetenteurUpdateComponent,
    resolve: {
      detenteur: DetenteurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DetenteurUpdateComponent,
    resolve: {
      detenteur: DetenteurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(detenteurRoute)],
  exports: [RouterModule],
})
export class DetenteurRoutingModule {}
