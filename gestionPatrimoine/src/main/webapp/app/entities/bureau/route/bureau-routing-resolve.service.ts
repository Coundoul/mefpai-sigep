import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBureau, Bureau } from '../bureau.model';
import { BureauService } from '../service/bureau.service';

@Injectable({ providedIn: 'root' })
export class BureauRoutingResolveService implements Resolve<IBureau> {
  constructor(protected service: BureauService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBureau> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bureau: HttpResponse<Bureau>) => {
          if (bureau.body) {
            return of(bureau.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bureau());
  }
}
