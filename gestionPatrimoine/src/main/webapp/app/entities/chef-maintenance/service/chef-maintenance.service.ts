import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChefMaintenance, getChefMaintenanceIdentifier } from '../chef-maintenance.model';

export type EntityResponseType = HttpResponse<IChefMaintenance>;
export type EntityArrayResponseType = HttpResponse<IChefMaintenance[]>;

@Injectable({ providedIn: 'root' })
export class ChefMaintenanceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/chef-maintenances');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(chefMaintenance: IChefMaintenance): Observable<EntityResponseType> {
    return this.http.post<IChefMaintenance>(this.resourceUrl, chefMaintenance, { observe: 'response' });
  }

  update(chefMaintenance: IChefMaintenance): Observable<EntityResponseType> {
    return this.http.put<IChefMaintenance>(
      `${this.resourceUrl}/${getChefMaintenanceIdentifier(chefMaintenance) as number}`,
      chefMaintenance,
      { observe: 'response' }
    );
  }

  partialUpdate(chefMaintenance: IChefMaintenance): Observable<EntityResponseType> {
    return this.http.patch<IChefMaintenance>(
      `${this.resourceUrl}/${getChefMaintenanceIdentifier(chefMaintenance) as number}`,
      chefMaintenance,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChefMaintenance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChefMaintenance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChefMaintenanceToCollectionIfMissing(
    chefMaintenanceCollection: IChefMaintenance[],
    ...chefMaintenancesToCheck: (IChefMaintenance | null | undefined)[]
  ): IChefMaintenance[] {
    const chefMaintenances: IChefMaintenance[] = chefMaintenancesToCheck.filter(isPresent);
    if (chefMaintenances.length > 0) {
      const chefMaintenanceCollectionIdentifiers = chefMaintenanceCollection.map(
        chefMaintenanceItem => getChefMaintenanceIdentifier(chefMaintenanceItem)!
      );
      const chefMaintenancesToAdd = chefMaintenances.filter(chefMaintenanceItem => {
        const chefMaintenanceIdentifier = getChefMaintenanceIdentifier(chefMaintenanceItem);
        if (chefMaintenanceIdentifier == null || chefMaintenanceCollectionIdentifiers.includes(chefMaintenanceIdentifier)) {
          return false;
        }
        chefMaintenanceCollectionIdentifiers.push(chefMaintenanceIdentifier);
        return true;
      });
      return [...chefMaintenancesToAdd, ...chefMaintenanceCollection];
    }
    return chefMaintenanceCollection;
  }
}
