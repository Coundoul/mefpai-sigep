import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChefEtablissementComponent } from '../list/chef-etablissement.component';
import { ChefEtablissementDetailComponent } from '../detail/chef-etablissement-detail.component';
import { ChefEtablissementUpdateComponent } from '../update/chef-etablissement-update.component';
import { ChefEtablissementRoutingResolveService } from './chef-etablissement-routing-resolve.service';

const chefEtablissementRoute: Routes = [
  {
    path: '',
    component: ChefEtablissementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChefEtablissementDetailComponent,
    resolve: {
      chefEtablissement: ChefEtablissementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChefEtablissementUpdateComponent,
    resolve: {
      chefEtablissement: ChefEtablissementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChefEtablissementUpdateComponent,
    resolve: {
      chefEtablissement: ChefEtablissementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chefEtablissementRoute)],
  exports: [RouterModule],
})
export class ChefEtablissementRoutingModule {}
