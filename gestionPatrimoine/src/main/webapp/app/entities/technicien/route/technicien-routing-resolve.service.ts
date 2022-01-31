import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechnicien, Technicien } from '../technicien.model';
import { TechnicienService } from '../service/technicien.service';

@Injectable({ providedIn: 'root' })
export class TechnicienRoutingResolveService implements Resolve<ITechnicien> {
  constructor(protected service: TechnicienService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITechnicien> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((technicien: HttpResponse<Technicien>) => {
          if (technicien.body) {
            return of(technicien.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Technicien());
  }
}
