import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISalles, Salles } from '../salles.model';
import { SallesService } from '../service/salles.service';

@Injectable({ providedIn: 'root' })
export class SallesRoutingResolveService implements Resolve<ISalles> {
  constructor(protected service: SallesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalles> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((salles: HttpResponse<Salles>) => {
          if (salles.body) {
            return of(salles.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Salles());
  }
}
