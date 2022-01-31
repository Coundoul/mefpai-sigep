import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIntervenant, Intervenant } from '../intervenant.model';
import { IntervenantService } from '../service/intervenant.service';

@Injectable({ providedIn: 'root' })
export class IntervenantRoutingResolveService implements Resolve<IIntervenant> {
  constructor(protected service: IntervenantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIntervenant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((intervenant: HttpResponse<Intervenant>) => {
          if (intervenant.body) {
            return of(intervenant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Intervenant());
  }
}
