import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DetailSortieComponent } from '../list/detail-sortie.component';
import { DetailSortieDetailComponent } from '../detail/detail-sortie-detail.component';
import { DetailSortieUpdateComponent } from '../update/detail-sortie-update.component';
import { DetailSortieRoutingResolveService } from './detail-sortie-routing-resolve.service';

const detailSortieRoute: Routes = [
  {
    path: '',
    component: DetailSortieComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetailSortieDetailComponent,
    resolve: {
      detailSortie: DetailSortieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DetailSortieUpdateComponent,
    resolve: {
      detailSortie: DetailSortieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DetailSortieUpdateComponent,
    resolve: {
      detailSortie: DetailSortieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(detailSortieRoute)],
  exports: [RouterModule],
})
export class DetailSortieRoutingModule {}
