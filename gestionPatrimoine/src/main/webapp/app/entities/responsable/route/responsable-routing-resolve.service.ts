import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResponsable, Responsable } from '../responsable.model';
import { ResponsableService } from '../service/responsable.service';

@Injectable({ providedIn: 'root' })
export class ResponsableRoutingResolveService implements Resolve<IResponsable> {
  constructor(protected service: ResponsableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResponsable> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((responsable: HttpResponse<Responsable>) => {
          if (responsable.body) {
            return of(responsable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Responsable());
  }
}
