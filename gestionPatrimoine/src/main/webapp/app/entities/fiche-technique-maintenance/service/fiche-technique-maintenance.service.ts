import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFicheTechniqueMaintenance, getFicheTechniqueMaintenanceIdentifier } from '../fiche-technique-maintenance.model';

export type EntityResponseType = HttpResponse<IFicheTechniqueMaintenance>;
export type EntityArrayResponseType = HttpResponse<IFicheTechniqueMaintenance[]>;

@Injectable({ providedIn: 'root' })
export class FicheTechniqueMaintenanceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/fiche-technique-maintenances', 'gestionmaintenance');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(ficheTechniqueMaintenance: IFicheTechniqueMaintenance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ficheTechniqueMaintenance);
    return this.http
      .post<IFicheTechniqueMaintenance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ficheTechniqueMaintenance: IFicheTechniqueMaintenance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ficheTechniqueMaintenance);
    return this.http
      .put<IFicheTechniqueMaintenance>(
        `${this.resourceUrl}/${getFicheTechniqueMaintenanceIdentifier(ficheTechniqueMaintenance) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(ficheTechniqueMaintenance: IFicheTechniqueMaintenance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ficheTechniqueMaintenance);
    return this.http
      .patch<IFicheTechniqueMaintenance>(
        `${this.resourceUrl}/${getFicheTechniqueMaintenanceIdentifier(ficheTechniqueMaintenance) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFicheTechniqueMaintenance>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFicheTechniqueMaintenance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFicheTechniqueMaintenanceToCollectionIfMissing(
    ficheTechniqueMaintenanceCollection: IFicheTechniqueMaintenance[],
    ...ficheTechniqueMaintenancesToCheck: (IFicheTechniqueMaintenance | null | undefined)[]
  ): IFicheTechniqueMaintenance[] {
    const ficheTechniqueMaintenances: IFicheTechniqueMaintenance[] = ficheTechniqueMaintenancesToCheck.filter(isPresent);
    if (ficheTechniqueMaintenances.length > 0) {
      const ficheTechniqueMaintenanceCollectionIdentifiers = ficheTechniqueMaintenanceCollection.map(
        ficheTechniqueMaintenanceItem => getFicheTechniqueMaintenanceIdentifier(ficheTechniqueMaintenanceItem)!
      );
      const ficheTechniqueMaintenancesToAdd = ficheTechniqueMaintenances.filter(ficheTechniqueMaintenanceItem => {
        const ficheTechniqueMaintenanceIdentifier = getFicheTechniqueMaintenanceIdentifier(ficheTechniqueMaintenanceItem);
        if (
          ficheTechniqueMaintenanceIdentifier == null ||
          ficheTechniqueMaintenanceCollectionIdentifiers.includes(ficheTechniqueMaintenanceIdentifier)
        ) {
          return false;
        }
        ficheTechniqueMaintenanceCollectionIdentifiers.push(ficheTechniqueMaintenanceIdentifier);
        return true;
      });
      return [...ficheTechniqueMaintenancesToAdd, ...ficheTechniqueMaintenanceCollection];
    }
    return ficheTechniqueMaintenanceCollection;
  }

  protected convertDateFromClient(ficheTechniqueMaintenance: IFicheTechniqueMaintenance): IFicheTechniqueMaintenance {
    return Object.assign({}, ficheTechniqueMaintenance, {
      dateDepot: ficheTechniqueMaintenance.dateDepot?.isValid() ? ficheTechniqueMaintenance.dateDepot.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDepot = res.body.dateDepot ? dayjs(res.body.dateDepot) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ficheTechniqueMaintenance: IFicheTechniqueMaintenance) => {
        ficheTechniqueMaintenance.dateDepot = ficheTechniqueMaintenance.dateDepot ? dayjs(ficheTechniqueMaintenance.dateDepot) : undefined;
      });
    }
    return res;
  }
}
