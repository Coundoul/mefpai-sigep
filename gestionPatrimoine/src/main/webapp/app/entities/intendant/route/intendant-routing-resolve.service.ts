import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIntendant, Intendant } from '../intendant.model';
import { IntendantService } from '../service/intendant.service';

@Injectable({ providedIn: 'root' })
export class IntendantRoutingResolveService implements Resolve<IIntendant> {
  constructor(protected service: IntendantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIntendant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((intendant: HttpResponse<Intendant>) => {
          if (intendant.body) {
            return of(intendant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Intendant());
  }
}
