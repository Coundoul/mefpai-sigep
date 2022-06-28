import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAtelier, Atelier } from '../atelier.model';
import { AtelierService } from '../service/atelier.service';

@Injectable({ providedIn: 'root' })
export class AtelierRoutingResolveService implements Resolve<IAtelier> {
  constructor(protected service: AtelierService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAtelier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((atelier: HttpResponse<Atelier>) => {
          if (atelier.body) {
            return of(atelier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Atelier());
  }
}
