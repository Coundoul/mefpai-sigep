import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChefMaintenance, ChefMaintenance } from '../chef-maintenance.model';
import { ChefMaintenanceService } from '../service/chef-maintenance.service';

@Injectable({ providedIn: 'root' })
export class ChefMaintenanceRoutingResolveService implements Resolve<IChefMaintenance> {
  constructor(protected service: ChefMaintenanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChefMaintenance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chefMaintenance: HttpResponse<ChefMaintenance>) => {
          if (chefMaintenance.body) {
            return of(chefMaintenance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChefMaintenance());
  }
}
