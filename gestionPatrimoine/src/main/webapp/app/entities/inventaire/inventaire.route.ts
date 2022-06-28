import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InventaireComponent } from './inventaire.component';

export const INVENTAIRE_ROUTE: Route = {
  path: '',
  component: InventaireComponent,
  data: {
    pageTitle: 'inventaire.title',
  },
  canActivate: [UserRouteAccessService],
};
