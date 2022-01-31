import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChefMaintenanceComponent } from '../list/chef-maintenance.component';
import { ChefMaintenanceDetailComponent } from '../detail/chef-maintenance-detail.component';
import { ChefMaintenanceUpdateComponent } from '../update/chef-maintenance-update.component';
import { ChefMaintenanceRoutingResolveService } from './chef-maintenance-routing-resolve.service';

const chefMaintenanceRoute: Routes = [
  {
    path: '',
    component: ChefMaintenanceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChefMaintenanceDetailComponent,
    resolve: {
      chefMaintenance: ChefMaintenanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChefMaintenanceUpdateComponent,
    resolve: {
      chefMaintenance: ChefMaintenanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChefMaintenanceUpdateComponent,
    resolve: {
      chefMaintenance: ChefMaintenanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chefMaintenanceRoute)],
  exports: [RouterModule],
})
export class ChefMaintenanceRoutingModule {}
