import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProjets, Projets } from '../projets.model';
import { ProjetsService } from '../service/projets.service';

@Injectable({ providedIn: 'root' })
export class ProjetsRoutingResolveService implements Resolve<IProjets> {
  constructor(protected service: ProjetsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProjets> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((projets: HttpResponse<Projets>) => {
          if (projets.body) {
            return of(projets.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Projets());
  }
}
