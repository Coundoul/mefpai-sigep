import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RequeteComponent } from '../list/requete.component';
import { RequeteDetailComponent } from '../detail/requete-detail.component';
import { RequeteUpdateComponent } from '../update/requete-update.component';
import { RequeteRoutingResolveService } from './requete-routing-resolve.service';

const requeteRoute: Routes = [
  {
    path: '',
    component: RequeteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RequeteDetailComponent,
    resolve: {
      requete: RequeteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RequeteUpdateComponent,
    resolve: {
      requete: RequeteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RequeteUpdateComponent,
    resolve: {
      requete: RequeteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(requeteRoute)],
  exports: [RouterModule],
})
export class RequeteRoutingModule {}
