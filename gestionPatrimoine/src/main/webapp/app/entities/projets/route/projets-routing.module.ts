import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProjetsComponent } from '../list/projets.component';
import { ProjetsDetailComponent } from '../detail/projets-detail.component';
import { ProjetsUpdateComponent } from '../update/projets-update.component';
import { ProjetsRoutingResolveService } from './projets-routing-resolve.service';
import { ListProjetsAllComponent } from '../list-projets-all/list-projets-all.component';

const projetsRoute: Routes = [
  {
    path: '',
    component: ListProjetsAllComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/projetEtablissement',
    component: ProjetsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProjetsDetailComponent,
    resolve: {
      projets: ProjetsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProjetsUpdateComponent,
    resolve: {
      projets: ProjetsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProjetsUpdateComponent,
    resolve: {
      projets: ProjetsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(projetsRoute)],
  exports: [RouterModule],
})
export class ProjetsRoutingModule {}
