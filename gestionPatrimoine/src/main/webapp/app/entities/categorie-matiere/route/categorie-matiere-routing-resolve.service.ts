import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategorieMatiere, CategorieMatiere } from '../categorie-matiere.model';
import { CategorieMatiereService } from '../service/categorie-matiere.service';

@Injectable({ providedIn: 'root' })
export class CategorieMatiereRoutingResolveService implements Resolve<ICategorieMatiere> {
  constructor(protected service: CategorieMatiereService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategorieMatiere> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categorieMatiere: HttpResponse<CategorieMatiere>) => {
          if (categorieMatiere.body) {
            return of(categorieMatiere.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CategorieMatiere());
  }
}
