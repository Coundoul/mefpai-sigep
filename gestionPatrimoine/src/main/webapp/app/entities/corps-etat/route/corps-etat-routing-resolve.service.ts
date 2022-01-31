import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICorpsEtat, CorpsEtat } from '../corps-etat.model';
import { CorpsEtatService } from '../service/corps-etat.service';

@Injectable({ providedIn: 'root' })
export class CorpsEtatRoutingResolveService implements Resolve<ICorpsEtat> {
  constructor(protected service: CorpsEtatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICorpsEtat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((corpsEtat: HttpResponse<CorpsEtat>) => {
          if (corpsEtat.body) {
            return of(corpsEtat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CorpsEtat());
  }
}
