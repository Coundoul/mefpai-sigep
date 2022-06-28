import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IComptablePrincipale, ComptablePrincipale } from '../comptable-principale.model';
import { ComptablePrincipaleService } from '../service/comptable-principale.service';

@Injectable({ providedIn: 'root' })
export class ComptablePrincipaleRoutingResolveService implements Resolve<IComptablePrincipale> {
  constructor(protected service: ComptablePrincipaleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IComptablePrincipale> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((comptablePrincipale: HttpResponse<ComptablePrincipale>) => {
          if (comptablePrincipale.body) {
            return of(comptablePrincipale.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ComptablePrincipale());
  }
}
