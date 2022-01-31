import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRequete, Requete } from '../requete.model';
import { RequeteService } from '../service/requete.service';

@Injectable({ providedIn: 'root' })
export class RequeteRoutingResolveService implements Resolve<IRequete> {
  constructor(protected service: RequeteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRequete> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((requete: HttpResponse<Requete>) => {
          if (requete.body) {
            return of(requete.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Requete());
  }
}
