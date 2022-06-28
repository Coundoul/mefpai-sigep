import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrdonnaceurMatiere, OrdonnaceurMatiere } from '../ordonnaceur-matiere.model';
import { OrdonnaceurMatiereService } from '../service/ordonnaceur-matiere.service';

@Injectable({ providedIn: 'root' })
export class OrdonnaceurMatiereRoutingResolveService implements Resolve<IOrdonnaceurMatiere> {
  constructor(protected service: OrdonnaceurMatiereService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrdonnaceurMatiere> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ordonnaceurMatiere: HttpResponse<OrdonnaceurMatiere>) => {
          if (ordonnaceurMatiere.body) {
            return of(ordonnaceurMatiere.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrdonnaceurMatiere());
  }
}
