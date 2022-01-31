import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProjetAttribution, ProjetAttribution } from '../projet-attribution.model';
import { ProjetAttributionService } from '../service/projet-attribution.service';

@Injectable({ providedIn: 'root' })
export class ProjetAttributionRoutingResolveService implements Resolve<IProjetAttribution> {
  constructor(protected service: ProjetAttributionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProjetAttribution> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((projetAttribution: HttpResponse<ProjetAttribution>) => {
          if (projetAttribution.body) {
            return of(projetAttribution.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProjetAttribution());
  }
}
