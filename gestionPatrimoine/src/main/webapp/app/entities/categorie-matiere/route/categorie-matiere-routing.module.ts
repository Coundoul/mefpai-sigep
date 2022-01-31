import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategorieMatiereComponent } from '../list/categorie-matiere.component';
import { CategorieMatiereDetailComponent } from '../detail/categorie-matiere-detail.component';
import { CategorieMatiereUpdateComponent } from '../update/categorie-matiere-update.component';
import { CategorieMatiereRoutingResolveService } from './categorie-matiere-routing-resolve.service';

const categorieMatiereRoute: Routes = [
  {
    path: '',
    component: CategorieMatiereComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategorieMatiereDetailComponent,
    resolve: {
      categorieMatiere: CategorieMatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategorieMatiereUpdateComponent,
    resolve: {
      categorieMatiere: CategorieMatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategorieMatiereUpdateComponent,
    resolve: {
      categorieMatiere: CategorieMatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categorieMatiereRoute)],
  exports: [RouterModule],
})
export class CategorieMatiereRoutingModule {}
