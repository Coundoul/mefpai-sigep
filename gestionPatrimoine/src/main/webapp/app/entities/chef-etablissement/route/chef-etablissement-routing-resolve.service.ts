import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChefEtablissement, ChefEtablissement } from '../chef-etablissement.model';
import { ChefEtablissementService } from '../service/chef-etablissement.service';

@Injectable({ providedIn: 'root' })
export class ChefEtablissementRoutingResolveService implements Resolve<IChefEtablissement> {
  constructor(protected service: ChefEtablissementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChefEtablissement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chefEtablissement: HttpResponse<ChefEtablissement>) => {
          if (chefEtablissement.body) {
            return of(chefEtablissement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChefEtablissement());
  }
}
