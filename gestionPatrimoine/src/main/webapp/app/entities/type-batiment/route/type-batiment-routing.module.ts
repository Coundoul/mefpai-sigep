import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeBatimentComponent } from '../list/type-batiment.component';
import { TypeBatimentDetailComponent } from '../detail/type-batiment-detail.component';
import { TypeBatimentUpdateComponent } from '../update/type-batiment-update.component';
import { TypeBatimentRoutingResolveService } from './type-batiment-routing-resolve.service';

const typeBatimentRoute: Routes = [
  {
    path: '',
    component: TypeBatimentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeBatimentDetailComponent,
    resolve: {
      typeBatiment: TypeBatimentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeBatimentUpdateComponent,
    resolve: {
      typeBatiment: TypeBatimentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeBatimentUpdateComponent,
    resolve: {
      typeBatiment: TypeBatimentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeBatimentRoute)],
  exports: [RouterModule],
})
export class TypeBatimentRoutingModule {}
