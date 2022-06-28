import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeBatiment, TypeBatiment } from '../type-batiment.model';
import { TypeBatimentService } from '../service/type-batiment.service';

@Injectable({ providedIn: 'root' })
export class TypeBatimentRoutingResolveService implements Resolve<ITypeBatiment> {
  constructor(protected service: TypeBatimentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeBatiment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeBatiment: HttpResponse<TypeBatiment>) => {
          if (typeBatiment.body) {
            return of(typeBatiment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeBatiment());
  }
}
