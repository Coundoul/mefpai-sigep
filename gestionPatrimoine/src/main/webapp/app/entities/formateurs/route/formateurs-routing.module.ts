import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormateursComponent } from '../list/formateurs.component';
import { FormateursDetailComponent } from '../detail/formateurs-detail.component';
import { FormateursUpdateComponent } from '../update/formateurs-update.component';
import { FormateursRoutingResolveService } from './formateurs-routing-resolve.service';

const formateursRoute: Routes = [
  {
    path: '',
    component: FormateursComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormateursDetailComponent,
    resolve: {
      formateurs: FormateursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormateursUpdateComponent,
    resolve: {
      formateurs: FormateursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormateursUpdateComponent,
    resolve: {
      formateurs: FormateursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formateursRoute)],
  exports: [RouterModule],
})
export class FormateursRoutingModule {}
