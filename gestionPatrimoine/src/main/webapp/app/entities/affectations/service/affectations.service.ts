import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAffectations, getAffectationsIdentifier } from '../affectations.model';

export type EntityResponseType = HttpResponse<IAffectations>;
export type EntityArrayResponseType = HttpResponse<IAffectations[]>;

@Injectable({ providedIn: 'root' })
export class AffectationsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/affectations', 'gestionequipement');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(affectations: IAffectations): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(affectations);
    return this.http
      .post<IAffectations>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(affectations: IAffectations): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(affectations);
    return this.http
      .put<IAffectations>(`${this.resourceUrl}/${getAffectationsIdentifier(affectations) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(affectations: IAffectations): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(affectations);
    return this.http
      .patch<IAffectations>(`${this.resourceUrl}/${getAffectationsIdentifier(affectations) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAffectations>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAffectations[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAffectationsToCollectionIfMissing(
    affectationsCollection: IAffectations[],
    ...affectationsToCheck: (IAffectations | null | undefined)[]
  ): IAffectations[] {
    const affectations: IAffectations[] = affectationsToCheck.filter(isPresent);
    if (affectations.length > 0) {
      const affectationsCollectionIdentifiers = affectationsCollection.map(
        affectationsItem => getAffectationsIdentifier(affectationsItem)!
      );
      const affectationsToAdd = affectations.filter(affectationsItem => {
        const affectationsIdentifier = getAffectationsIdentifier(affectationsItem);
        if (affectationsIdentifier == null || affectationsCollectionIdentifiers.includes(affectationsIdentifier)) {
          return false;
        }
        affectationsCollectionIdentifiers.push(affectationsIdentifier);
        return true;
      });
      return [...affectationsToAdd, ...affectationsCollection];
    }
    return affectationsCollection;
  }

  protected convertDateFromClient(affectations: IAffectations): IAffectations {
    return Object.assign({}, affectations, {
      dateAttribution: affectations.dateAttribution?.isValid() ? affectations.dateAttribution.toJSON() : undefined,
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
      res.body.forEach((affectations: IAffectations) => {
        affectations.dateAttribution = affectations.dateAttribution ? dayjs(affectations.dateAttribution) : undefined;
      });
    }
    return res;
  }
}
