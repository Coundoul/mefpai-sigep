import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ComptablePrincipaleComponent } from '../list/comptable-principale.component';
import { ComptablePrincipaleDetailComponent } from '../detail/comptable-principale-detail.component';
import { ComptablePrincipaleUpdateComponent } from '../update/comptable-principale-update.component';
import { ComptablePrincipaleRoutingResolveService } from './comptable-principale-routing-resolve.service';

const comptablePrincipaleRoute: Routes = [
  {
    path: '',
    component: ComptablePrincipaleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ComptablePrincipaleDetailComponent,
    resolve: {
      comptablePrincipale: ComptablePrincipaleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ComptablePrincipaleUpdateComponent,
    resolve: {
      comptablePrincipale: ComptablePrincipaleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ComptablePrincipaleUpdateComponent,
    resolve: {
      comptablePrincipale: ComptablePrincipaleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(comptablePrincipaleRoute)],
  exports: [RouterModule],
})
export class ComptablePrincipaleRoutingModule {}
