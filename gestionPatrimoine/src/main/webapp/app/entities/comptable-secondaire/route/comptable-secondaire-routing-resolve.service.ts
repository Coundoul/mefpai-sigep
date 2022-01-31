import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IComptableSecondaire, ComptableSecondaire } from '../comptable-secondaire.model';
import { ComptableSecondaireService } from '../service/comptable-secondaire.service';

@Injectable({ providedIn: 'root' })
export class ComptableSecondaireRoutingResolveService implements Resolve<IComptableSecondaire> {
  constructor(protected service: ComptableSecondaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IComptableSecondaire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((comptableSecondaire: HttpResponse<ComptableSecondaire>) => {
          if (comptableSecondaire.body) {
            return of(comptableSecondaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ComptableSecondaire());
  }
}
