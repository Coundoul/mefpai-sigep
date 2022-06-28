import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AttributionInfrastructureComponent } from '../list/attribution-infrastructure.component';
import { AttributionInfrastructureDetailComponent } from '../detail/attribution-infrastructure-detail.component';
import { AttributionInfrastructureUpdateComponent } from '../update/attribution-infrastructure-update.component';
import { AttributionInfrastructureRoutingResolveService } from './attribution-infrastructure-routing-resolve.service';

const attributionInfrastructureRoute: Routes = [
  {
    path: '',
    component: AttributionInfrastructureComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttributionInfrastructureDetailComponent,
    resolve: {
      attributionInfrastructure: AttributionInfrastructureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttributionInfrastructureUpdateComponent,
    resolve: {
      attributionInfrastructure: AttributionInfrastructureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttributionInfrastructureUpdateComponent,
    resolve: {
      attributionInfrastructure: AttributionInfrastructureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(attributionInfrastructureRoute)],
  exports: [RouterModule],
})
export class AttributionInfrastructureRoutingModule {}
