import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttribution, getAttributionIdentifier } from '../attribution.model';

export type EntityResponseType = HttpResponse<IAttribution>;
export type EntityArrayResponseType = HttpResponse<IAttribution[]>;

@Injectable({ providedIn: 'root' })
export class AttributionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attributions', 'gestionequipement');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(attribution: IAttribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attribution);
    return this.http
      .post<IAttribution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(attribution: IAttribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attribution);
    return this.http
      .put<IAttribution>(`${this.resourceUrl}/${getAttributionIdentifier(attribution) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(attribution: IAttribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attribution);
    return this.http
      .patch<IAttribution>(`${this.resourceUrl}/${getAttributionIdentifier(attribution) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAttribution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAttribution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAttributionToCollectionIfMissing(
    attributionCollection: IAttribution[],
    ...attributionsToCheck: (IAttribution | null | undefined)[]
  ): IAttribution[] {
    const attributions: IAttribution[] = attributionsToCheck.filter(isPresent);
    if (attributions.length > 0) {
      const attributionCollectionIdentifiers = attributionCollection.map(attributionItem => getAttributionIdentifier(attributionItem)!);
      const attributionsToAdd = attributions.filter(attributionItem => {
        const attributionIdentifier = getAttributionIdentifier(attributionItem);
        if (attributionIdentifier == null || attributionCollectionIdentifiers.includes(attributionIdentifier)) {
          return false;
        }
        attributionCollectionIdentifiers.push(attributionIdentifier);
        return true;
      });
      return [...attributionsToAdd, ...attributionCollection];
    }
    return attributionCollection;
  }

  protected convertDateFromClient(attribution: IAttribution): IAttribution {
    return Object.assign({}, attribution, {
      dateAffectation: attribution.dateAffectation?.isValid() ? attribution.dateAffectation.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateAffectation = res.body.dateAffectation ? dayjs(res.body.dateAffectation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((attribution: IAttribution) => {
        attribution.dateAffectation = attribution.dateAffectation ? dayjs(attribution.dateAffectation) : undefined;
      });
    }
    return res;
  }
}
