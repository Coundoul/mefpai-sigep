import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategoriserComponent } from './categoriser.component';

export const CATEGORISER_ROUTE: Route = {
  path: '',
  component: CategoriserComponent,
  data: {
    pageTitle: 'categoriser.matiere',
  },
  canActivate: [UserRouteAccessService],
};
