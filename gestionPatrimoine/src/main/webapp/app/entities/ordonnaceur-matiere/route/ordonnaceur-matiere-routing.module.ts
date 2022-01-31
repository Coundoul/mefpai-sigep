import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrdonnaceurMatiereComponent } from '../list/ordonnaceur-matiere.component';
import { OrdonnaceurMatiereDetailComponent } from '../detail/ordonnaceur-matiere-detail.component';
import { OrdonnaceurMatiereUpdateComponent } from '../update/ordonnaceur-matiere-update.component';
import { OrdonnaceurMatiereRoutingResolveService } from './ordonnaceur-matiere-routing-resolve.service';

const ordonnaceurMatiereRoute: Routes = [
  {
    path: '',
    component: OrdonnaceurMatiereComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrdonnaceurMatiereDetailComponent,
    resolve: {
      ordonnaceurMatiere: OrdonnaceurMatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrdonnaceurMatiereUpdateComponent,
    resolve: {
      ordonnaceurMatiere: OrdonnaceurMatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrdonnaceurMatiereUpdateComponent,
    resolve: {
      ordonnaceurMatiere: OrdonnaceurMatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ordonnaceurMatiereRoute)],
  exports: [RouterModule],
})
export class OrdonnaceurMatiereRoutingModule {}
