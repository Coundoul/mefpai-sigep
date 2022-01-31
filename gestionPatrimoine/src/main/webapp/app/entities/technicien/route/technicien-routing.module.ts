import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TechnicienComponent } from '../list/technicien.component';
import { TechnicienDetailComponent } from '../detail/technicien-detail.component';
import { TechnicienUpdateComponent } from '../update/technicien-update.component';
import { TechnicienRoutingResolveService } from './technicien-routing-resolve.service';

const technicienRoute: Routes = [
  {
    path: '',
    component: TechnicienComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechnicienDetailComponent,
    resolve: {
      technicien: TechnicienRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechnicienUpdateComponent,
    resolve: {
      technicien: TechnicienRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechnicienUpdateComponent,
    resolve: {
      technicien: TechnicienRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(technicienRoute)],
  exports: [RouterModule],
})
export class TechnicienRoutingModule {}
