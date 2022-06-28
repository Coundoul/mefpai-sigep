import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FicheTechniqueMaintenanceComponent } from '../list/fiche-technique-maintenance.component';
import { FicheTechniqueMaintenanceDetailComponent } from '../detail/fiche-technique-maintenance-detail.component';
import { FicheTechniqueMaintenanceUpdateComponent } from '../update/fiche-technique-maintenance-update.component';
import { FicheTechniqueMaintenanceRoutingResolveService } from './fiche-technique-maintenance-routing-resolve.service';

const ficheTechniqueMaintenanceRoute: Routes = [
  {
    path: '',
    component: FicheTechniqueMaintenanceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FicheTechniqueMaintenanceDetailComponent,
    resolve: {
      ficheTechniqueMaintenance: FicheTechniqueMaintenanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FicheTechniqueMaintenanceUpdateComponent,
    resolve: {
      ficheTechniqueMaintenance: FicheTechniqueMaintenanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FicheTechniqueMaintenanceUpdateComponent,
    resolve: {
      ficheTechniqueMaintenance: FicheTechniqueMaintenanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ficheTechniqueMaintenanceRoute)],
  exports: [RouterModule],
})
export class FicheTechniqueMaintenanceRoutingModule {}
