import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetailSortie, DetailSortie } from '../detail-sortie.model';
import { DetailSortieService } from '../service/detail-sortie.service';

@Injectable({ providedIn: 'root' })
export class DetailSortieRoutingResolveService implements Resolve<IDetailSortie> {
  constructor(protected service: DetailSortieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDetailSortie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((detailSortie: HttpResponse<DetailSortie>) => {
          if (detailSortie.body) {
            return of(detailSortie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DetailSortie());
  }
}
