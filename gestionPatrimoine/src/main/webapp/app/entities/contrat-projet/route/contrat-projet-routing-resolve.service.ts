import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContratProjet, ContratProjet } from '../contrat-projet.model';
import { ContratProjetService } from '../service/contrat-projet.service';

@Injectable({ providedIn: 'root' })
export class ContratProjetRoutingResolveService implements Resolve<IContratProjet> {
  constructor(protected service: ContratProjetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContratProjet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contratProjet: HttpResponse<ContratProjet>) => {
          if (contratProjet.body) {
            return of(contratProjet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContratProjet());
  }
}
