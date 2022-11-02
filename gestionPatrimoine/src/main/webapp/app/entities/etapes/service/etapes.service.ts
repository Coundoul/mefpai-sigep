import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEtapes, getEtapesIdentifier } from '../etapes.model';

export type EntityResponseType = HttpResponse<IEtapes>;
export type EntityArrayResponseType = HttpResponse<IEtapes[]>;

@Injectable({ providedIn: 'root' })
export class EtapesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/etapes', 'gestioninfrastructure');

  public resourceUrlProjets = this.applicationConfigService.getEndpointFor('api/etapes/projets', 'gestioninfrastructure');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(etapes: IEtapes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(etapes);
    return this.http
      .post<IEtapes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(etapes: IEtapes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(etapes);
    return this.http
      .put<IEtapes>(`${this.resourceUrl}/${getEtapesIdentifier(etapes) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(etapes: IEtapes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(etapes);
    return this.http
      .patch<IEtapes>(`${this.resourceUrl}/${getEtapesIdentifier(etapes) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEtapes>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEtapes[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryEtapesProjet(id: number, req?:any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEtapes[]>(`${this.resourceUrlProjets}/${id}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEtapesToCollectionIfMissing(etapesCollection: IEtapes[], ...etapesToCheck: (IEtapes | null | undefined)[]): IEtapes[] {
    const etapes: IEtapes[] = etapesToCheck.filter(isPresent);
    if (etapes.length > 0) {
      const etapesCollectionIdentifiers = etapesCollection.map(etapesItem => getEtapesIdentifier(etapesItem)!);
      const etapesToAdd = etapes.filter(etapesItem => {
        const etapesIdentifier = getEtapesIdentifier(etapesItem);
        if (etapesIdentifier == null || etapesCollectionIdentifiers.includes(etapesIdentifier)) {
          return false;
        }
        etapesCollectionIdentifiers.push(etapesIdentifier);
        return true;
      });
      return [...etapesToAdd, ...etapesCollection];
    }
    return etapesCollection;
  }

  protected convertDateFromClient(etapes: IEtapes): IEtapes {
    return Object.assign({}, etapes, {
      dateDebut: etapes.dateDebut?.isValid() ? etapes.dateDebut.toJSON() : undefined,
      dateFin: etapes.dateFin?.isValid() ? etapes.dateFin.toJSON() : undefined,
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
      res.body.forEach((etapes: IEtapes) => {
        etapes.dateDebut = etapes.dateDebut ? dayjs(etapes.dateDebut) : undefined;
        etapes.dateFin = etapes.dateFin ? dayjs(etapes.dateFin) : undefined;
      });
    }
    return res;
  }
}
