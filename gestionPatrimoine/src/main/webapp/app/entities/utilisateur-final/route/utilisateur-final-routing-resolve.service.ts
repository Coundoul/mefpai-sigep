import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUtilisateurFinal, UtilisateurFinal } from '../utilisateur-final.model';
import { UtilisateurFinalService } from '../service/utilisateur-final.service';

@Injectable({ providedIn: 'root' })
export class UtilisateurFinalRoutingResolveService implements Resolve<IUtilisateurFinal> {
  constructor(protected service: UtilisateurFinalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUtilisateurFinal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((utilisateurFinal: HttpResponse<UtilisateurFinal>) => {
          if (utilisateurFinal.body) {
            return of(utilisateurFinal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UtilisateurFinal());
  }
}
