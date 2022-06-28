import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BureauComponent } from '../list/bureau.component';
import { BureauDetailComponent } from '../detail/bureau-detail.component';
import { BureauUpdateComponent } from '../update/bureau-update.component';
import { BureauRoutingResolveService } from './bureau-routing-resolve.service';

const bureauRoute: Routes = [
  {
    path: '',
    component: BureauComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BureauDetailComponent,
    resolve: {
      bureau: BureauRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BureauUpdateComponent,
    resolve: {
      bureau: BureauRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BureauUpdateComponent,
    resolve: {
      bureau: BureauRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bureauRoute)],
  exports: [RouterModule],
})
export class BureauRoutingModule {}
