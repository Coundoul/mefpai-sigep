import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CorpsEtatComponent } from '../list/corps-etat.component';
import { CorpsEtatDetailComponent } from '../detail/corps-etat-detail.component';
import { CorpsEtatUpdateComponent } from '../update/corps-etat-update.component';
import { CorpsEtatRoutingResolveService } from './corps-etat-routing-resolve.service';

const corpsEtatRoute: Routes = [
  {
    path: '',
    component: CorpsEtatComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CorpsEtatDetailComponent,
    resolve: {
      corpsEtat: CorpsEtatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CorpsEtatUpdateComponent,
    resolve: {
      corpsEtat: CorpsEtatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CorpsEtatUpdateComponent,
    resolve: {
      corpsEtat: CorpsEtatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(corpsEtatRoute)],
  exports: [RouterModule],
})
export class CorpsEtatRoutingModule {}
