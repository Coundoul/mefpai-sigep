import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MatieresMaintenancesComponent } from '../list/matieres-maintenances.component';

const matieresMaintenancesRoutes: Routes = [
  {
    path: '',
    component: MatieresMaintenancesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(matieresMaintenancesRoutes)],
  exports: [RouterModule]
})
export class MatieresMaintenancesRoutingModule { }
