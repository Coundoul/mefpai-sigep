import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChefProjet, ChefProjet } from '../chef-projet.model';
import { ChefProjetService } from '../service/chef-projet.service';

@Injectable({ providedIn: 'root' })
export class ChefProjetRoutingResolveService implements Resolve<IChefProjet> {
  constructor(protected service: ChefProjetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChefProjet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chefProjet: HttpResponse<ChefProjet>) => {
          if (chefProjet.body) {
            return of(chefProjet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChefProjet());
  }
}
