import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INatureFoncier, NatureFoncier } from '../nature-foncier.model';
import { NatureFoncierService } from '../service/nature-foncier.service';

@Injectable({ providedIn: 'root' })
export class NatureFoncierRoutingResolveService implements Resolve<INatureFoncier> {
  constructor(protected service: NatureFoncierService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INatureFoncier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((natureFoncier: HttpResponse<NatureFoncier>) => {
          if (natureFoncier.body) {
            return of(natureFoncier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NatureFoncier());
  }
}
