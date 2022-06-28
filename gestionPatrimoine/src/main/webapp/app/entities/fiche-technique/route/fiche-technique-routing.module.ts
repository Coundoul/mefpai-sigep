import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FicheTechniqueComponent } from '../list/fiche-technique.component';
import { FicheTechniqueDetailComponent } from '../detail/fiche-technique-detail.component';
import { FicheTechniqueUpdateComponent } from '../update/fiche-technique-update.component';
import { FicheTechniqueRoutingResolveService } from './fiche-technique-routing-resolve.service';

const ficheTechniqueRoute: Routes = [
  {
    path: '',
    component: FicheTechniqueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FicheTechniqueDetailComponent,
    resolve: {
      ficheTechnique: FicheTechniqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FicheTechniqueUpdateComponent,
    resolve: {
      ficheTechnique: FicheTechniqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FicheTechniqueUpdateComponent,
    resolve: {
      ficheTechnique: FicheTechniqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ficheTechniqueRoute)],
  exports: [RouterModule],
})
export class FicheTechniqueRoutingModule {}
