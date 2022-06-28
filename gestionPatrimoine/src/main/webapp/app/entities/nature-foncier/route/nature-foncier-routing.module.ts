import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NatureFoncierComponent } from '../list/nature-foncier.component';
import { NatureFoncierDetailComponent } from '../detail/nature-foncier-detail.component';
import { NatureFoncierUpdateComponent } from '../update/nature-foncier-update.component';
import { NatureFoncierRoutingResolveService } from './nature-foncier-routing-resolve.service';

const natureFoncierRoute: Routes = [
  {
    path: '',
    component: NatureFoncierComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NatureFoncierDetailComponent,
    resolve: {
      natureFoncier: NatureFoncierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NatureFoncierUpdateComponent,
    resolve: {
      natureFoncier: NatureFoncierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NatureFoncierUpdateComponent,
    resolve: {
      natureFoncier: NatureFoncierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(natureFoncierRoute)],
  exports: [RouterModule],
})
export class NatureFoncierRoutingModule {}
