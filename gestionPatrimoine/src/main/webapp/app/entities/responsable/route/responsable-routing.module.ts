import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResponsableComponent } from '../list/responsable.component';
import { ResponsableDetailComponent } from '../detail/responsable-detail.component';
import { ResponsableUpdateComponent } from '../update/responsable-update.component';
import { ResponsableRoutingResolveService } from './responsable-routing-resolve.service';

const responsableRoute: Routes = [
  {
    path: '',
    component: ResponsableComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResponsableDetailComponent,
    resolve: {
      responsable: ResponsableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResponsableUpdateComponent,
    resolve: {
      responsable: ResponsableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResponsableUpdateComponent,
    resolve: {
      responsable: ResponsableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(responsableRoute)],
  exports: [RouterModule],
})
export class ResponsableRoutingModule {}
