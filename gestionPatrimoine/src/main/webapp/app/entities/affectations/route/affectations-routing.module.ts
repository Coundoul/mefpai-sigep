import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AffectationsComponent } from '../list/affectations.component';
import { AffectationsDetailComponent } from '../detail/affectations-detail.component';
import { AffectationsUpdateComponent } from '../update/affectations-update.component';
import { AffectationsRoutingResolveService } from './affectations-routing-resolve.service';

const affectationsRoute: Routes = [
  {
    path: '',
    component: AffectationsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AffectationsDetailComponent,
    resolve: {
      affectations: AffectationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AffectationsUpdateComponent,
    resolve: {
      affectations: AffectationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AffectationsUpdateComponent,
    resolve: {
      affectations: AffectationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(affectationsRoute)],
  exports: [RouterModule],
})
export class AffectationsRoutingModule {}
