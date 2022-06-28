import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFiliereStabilise, FiliereStabilise } from '../filiere-stabilise.model';
import { FiliereStabiliseService } from '../service/filiere-stabilise.service';

@Injectable({ providedIn: 'root' })
export class FiliereStabiliseRoutingResolveService implements Resolve<IFiliereStabilise> {
  constructor(protected service: FiliereStabiliseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFiliereStabilise> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((filiereStabilise: HttpResponse<FiliereStabilise>) => {
          if (filiereStabilise.body) {
            return of(filiereStabilise.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FiliereStabilise());
  }
}
