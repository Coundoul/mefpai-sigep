import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AtelierComponent } from '../list/atelier.component';
import { AtelierDetailComponent } from '../detail/atelier-detail.component';
import { AtelierUpdateComponent } from '../update/atelier-update.component';
import { AtelierRoutingResolveService } from './atelier-routing-resolve.service';

const atelierRoute: Routes = [
  {
    path: '',
    component: AtelierComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AtelierDetailComponent,
    resolve: {
      atelier: AtelierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AtelierUpdateComponent,
    resolve: {
      atelier: AtelierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AtelierUpdateComponent,
    resolve: {
      atelier: AtelierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(atelierRoute)],
  exports: [RouterModule],
})
export class AtelierRoutingModule {}
