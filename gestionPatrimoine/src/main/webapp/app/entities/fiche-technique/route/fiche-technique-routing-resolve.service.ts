import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFicheTechnique, FicheTechnique } from '../fiche-technique.model';
import { FicheTechniqueService } from '../service/fiche-technique.service';

@Injectable({ providedIn: 'root' })
export class FicheTechniqueRoutingResolveService implements Resolve<IFicheTechnique> {
  constructor(protected service: FicheTechniqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFicheTechnique> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ficheTechnique: HttpResponse<FicheTechnique>) => {
          if (ficheTechnique.body) {
            return of(ficheTechnique.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FicheTechnique());
  }
}
