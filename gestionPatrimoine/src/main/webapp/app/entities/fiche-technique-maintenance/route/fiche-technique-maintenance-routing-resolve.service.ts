import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFicheTechniqueMaintenance, FicheTechniqueMaintenance } from '../fiche-technique-maintenance.model';
import { FicheTechniqueMaintenanceService } from '../service/fiche-technique-maintenance.service';

@Injectable({ providedIn: 'root' })
export class FicheTechniqueMaintenanceRoutingResolveService implements Resolve<IFicheTechniqueMaintenance> {
  constructor(protected service: FicheTechniqueMaintenanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFicheTechniqueMaintenance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ficheTechniqueMaintenance: HttpResponse<FicheTechniqueMaintenance>) => {
          if (ficheTechniqueMaintenance.body) {
            return of(ficheTechniqueMaintenance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FicheTechniqueMaintenance());
  }
}
