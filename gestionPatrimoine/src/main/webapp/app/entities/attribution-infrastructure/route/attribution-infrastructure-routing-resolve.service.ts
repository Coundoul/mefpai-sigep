import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttributionInfrastructure, AttributionInfrastructure } from '../attribution-infrastructure.model';
import { AttributionInfrastructureService } from '../service/attribution-infrastructure.service';

@Injectable({ providedIn: 'root' })
export class AttributionInfrastructureRoutingResolveService implements Resolve<IAttributionInfrastructure> {
  constructor(protected service: AttributionInfrastructureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttributionInfrastructure> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((attributionInfrastructure: HttpResponse<AttributionInfrastructure>) => {
          if (attributionInfrastructure.body) {
            return of(attributionInfrastructure.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AttributionInfrastructure());
  }
}
