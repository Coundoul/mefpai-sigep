import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEtapes, Etapes } from '../etapes.model';
import { EtapesService } from '../service/etapes.service';

@Injectable({ providedIn: 'root' })
export class EtapesRoutingResolveService implements Resolve<IEtapes> {
  constructor(protected service: EtapesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEtapes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((etapes: HttpResponse<Etapes>) => {
          if (etapes.body) {
            return of(etapes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Etapes());
  }
}
