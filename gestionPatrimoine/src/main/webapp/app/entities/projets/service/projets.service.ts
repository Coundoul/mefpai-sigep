import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProjets, getProjetsIdentifier } from '../projets.model';

export type EntityResponseType = HttpResponse<IProjets>;
export type EntityArrayResponseType = HttpResponse<IProjets[]>;

@Injectable({ providedIn: 'root' })
export class ProjetsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/projets', 'gestioninfrastructure');

  public resourceUrlProjets = this.applicationConfigService.getEndpointFor('api/projets/etablissement', 'gestioninfrastructure');


  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(projets: IProjets): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projets);
    return this.http
      .post<IProjets>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(projets: IProjets): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projets);
    return this.http
      .put<IProjets>(`${this.resourceUrl}/${getProjetsIdentifier(projets) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(projets: IProjets): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projets);
    return this.http
      .patch<IProjets>(`${this.resourceUrl}/${getProjetsIdentifier(projets) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProjets>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProjets[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryProjet(id: number, req?:any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProjets[]>(`${this.resourceUrlProjets}/${id}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProjetsToCollectionIfMissing(projetsCollection: IProjets[], ...projetsToCheck: (IProjets | null | undefined)[]): IProjets[] {
    const projets: IProjets[] = projetsToCheck.filter(isPresent);
    if (projets.length > 0) {
      const projetsCollectionIdentifiers = projetsCollection.map(projetsItem => getProjetsIdentifier(projetsItem)!);
      const projetsToAdd = projets.filter(projetsItem => {
        const projetsIdentifier = getProjetsIdentifier(projetsItem);
        if (projetsIdentifier == null || projetsCollectionIdentifiers.includes(projetsIdentifier)) {
          return false;
        }
        projetsCollectionIdentifiers.push(projetsIdentifier);
        return true;
      });
      return [...projetsToAdd, ...projetsCollection];
    }
    return projetsCollection;
  }

  protected convertDateFromClient(projets: IProjets): IProjets {
    return Object.assign({}, projets, {
      dateDebut: projets.dateDebut?.isValid() ? projets.dateDebut.toJSON() : undefined,
      dateFin: projets.dateFin?.isValid() ? projets.dateFin.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebut = res.body.dateDebut ? dayjs(res.body.dateDebut) : undefined;
      res.body.dateFin = res.body.dateFin ? dayjs(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((projets: IProjets) => {
        projets.dateDebut = projets.dateDebut ? dayjs(projets.dateDebut) : undefined;
        projets.dateFin = projets.dateFin ? dayjs(projets.dateFin) : undefined;
      });
    }
    return res;
  }
}
