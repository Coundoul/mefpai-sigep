import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProjetAttribution, getProjetAttributionIdentifier } from '../projet-attribution.model';

export type EntityResponseType = HttpResponse<IProjetAttribution>;
export type EntityArrayResponseType = HttpResponse<IProjetAttribution[]>;

@Injectable({ providedIn: 'root' })
export class ProjetAttributionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/projet-attributions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(projetAttribution: IProjetAttribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projetAttribution);
    return this.http
      .post<IProjetAttribution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(projetAttribution: IProjetAttribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projetAttribution);
    return this.http
      .put<IProjetAttribution>(`${this.resourceUrl}/${getProjetAttributionIdentifier(projetAttribution) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(projetAttribution: IProjetAttribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projetAttribution);
    return this.http
      .patch<IProjetAttribution>(`${this.resourceUrl}/${getProjetAttributionIdentifier(projetAttribution) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProjetAttribution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProjetAttribution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProjetAttributionToCollectionIfMissing(
    projetAttributionCollection: IProjetAttribution[],
    ...projetAttributionsToCheck: (IProjetAttribution | null | undefined)[]
  ): IProjetAttribution[] {
    const projetAttributions: IProjetAttribution[] = projetAttributionsToCheck.filter(isPresent);
    if (projetAttributions.length > 0) {
      const projetAttributionCollectionIdentifiers = projetAttributionCollection.map(
        projetAttributionItem => getProjetAttributionIdentifier(projetAttributionItem)!
      );
      const projetAttributionsToAdd = projetAttributions.filter(projetAttributionItem => {
        const projetAttributionIdentifier = getProjetAttributionIdentifier(projetAttributionItem);
        if (projetAttributionIdentifier == null || projetAttributionCollectionIdentifiers.includes(projetAttributionIdentifier)) {
          return false;
        }
        projetAttributionCollectionIdentifiers.push(projetAttributionIdentifier);
        return true;
      });
      return [...projetAttributionsToAdd, ...projetAttributionCollection];
    }
    return projetAttributionCollection;
  }

  protected convertDateFromClient(projetAttribution: IProjetAttribution): IProjetAttribution {
    return Object.assign({}, projetAttribution, {
      dateAttribution: projetAttribution.dateAttribution?.isValid() ? projetAttribution.dateAttribution.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateAttribution = res.body.dateAttribution ? dayjs(res.body.dateAttribution) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((projetAttribution: IProjetAttribution) => {
        projetAttribution.dateAttribution = projetAttribution.dateAttribution ? dayjs(projetAttribution.dateAttribution) : undefined;
      });
    }
    return res;
  }
}
