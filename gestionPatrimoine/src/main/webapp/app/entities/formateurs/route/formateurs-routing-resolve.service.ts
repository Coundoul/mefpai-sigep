import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormateurs, Formateurs } from '../formateurs.model';
import { FormateursService } from '../service/formateurs.service';

@Injectable({ providedIn: 'root' })
export class FormateursRoutingResolveService implements Resolve<IFormateurs> {
  constructor(protected service: FormateursService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormateurs> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((formateurs: HttpResponse<Formateurs>) => {
          if (formateurs.body) {
            return of(formateurs.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Formateurs());
  }
}
