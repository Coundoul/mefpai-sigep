import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MangasinierFichisteComponent } from '../list/mangasinier-fichiste.component';
import { MangasinierFichisteDetailComponent } from '../detail/mangasinier-fichiste-detail.component';
import { MangasinierFichisteUpdateComponent } from '../update/mangasinier-fichiste-update.component';
import { MangasinierFichisteRoutingResolveService } from './mangasinier-fichiste-routing-resolve.service';

const mangasinierFichisteRoute: Routes = [
  {
    path: '',
    component: MangasinierFichisteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MangasinierFichisteDetailComponent,
    resolve: {
      mangasinierFichiste: MangasinierFichisteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MangasinierFichisteUpdateComponent,
    resolve: {
      mangasinierFichiste: MangasinierFichisteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MangasinierFichisteUpdateComponent,
    resolve: {
      mangasinierFichiste: MangasinierFichisteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mangasinierFichisteRoute)],
  exports: [RouterModule],
})
export class MangasinierFichisteRoutingModule {}
