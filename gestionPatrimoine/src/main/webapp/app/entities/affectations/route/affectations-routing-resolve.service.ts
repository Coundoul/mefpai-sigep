import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAffectations, Affectations } from '../affectations.model';
import { AffectationsService } from '../service/affectations.service';

@Injectable({ providedIn: 'root' })
export class AffectationsRoutingResolveService implements Resolve<IAffectations> {
  constructor(protected service: AffectationsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAffectations> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((affectations: HttpResponse<Affectations>) => {
          if (affectations.body) {
            return of(affectations.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Affectations());
  }
}
