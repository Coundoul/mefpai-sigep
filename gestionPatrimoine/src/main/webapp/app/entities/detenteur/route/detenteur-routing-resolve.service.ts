import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetenteur, Detenteur } from '../detenteur.model';
import { DetenteurService } from '../service/detenteur.service';

@Injectable({ providedIn: 'root' })
export class DetenteurRoutingResolveService implements Resolve<IDetenteur> {
  constructor(protected service: DetenteurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDetenteur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((detenteur: HttpResponse<Detenteur>) => {
          if (detenteur.body) {
            return of(detenteur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Detenteur());
  }
}
