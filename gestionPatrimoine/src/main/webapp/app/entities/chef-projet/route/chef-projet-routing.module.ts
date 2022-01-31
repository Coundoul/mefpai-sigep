import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChefProjetComponent } from '../list/chef-projet.component';
import { ChefProjetDetailComponent } from '../detail/chef-projet-detail.component';
import { ChefProjetUpdateComponent } from '../update/chef-projet-update.component';
import { ChefProjetRoutingResolveService } from './chef-projet-routing-resolve.service';

const chefProjetRoute: Routes = [
  {
    path: '',
    component: ChefProjetComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChefProjetDetailComponent,
    resolve: {
      chefProjet: ChefProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChefProjetUpdateComponent,
    resolve: {
      chefProjet: ChefProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChefProjetUpdateComponent,
    resolve: {
      chefProjet: ChefProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chefProjetRoute)],
  exports: [RouterModule],
})
export class ChefProjetRoutingModule {}
