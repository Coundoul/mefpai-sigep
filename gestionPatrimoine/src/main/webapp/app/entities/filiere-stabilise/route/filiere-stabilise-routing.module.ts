import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FiliereStabiliseComponent } from '../list/filiere-stabilise.component';
import { FiliereStabiliseDetailComponent } from '../detail/filiere-stabilise-detail.component';
import { FiliereStabiliseUpdateComponent } from '../update/filiere-stabilise-update.component';
import { FiliereStabiliseRoutingResolveService } from './filiere-stabilise-routing-resolve.service';

const filiereStabiliseRoute: Routes = [
  {
    path: '',
    component: FiliereStabiliseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FiliereStabiliseDetailComponent,
    resolve: {
      filiereStabilise: FiliereStabiliseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FiliereStabiliseUpdateComponent,
    resolve: {
      filiereStabilise: FiliereStabiliseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FiliereStabiliseUpdateComponent,
    resolve: {
      filiereStabilise: FiliereStabiliseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(filiereStabiliseRoute)],
  exports: [RouterModule],
})
export class FiliereStabiliseRoutingModule {}
