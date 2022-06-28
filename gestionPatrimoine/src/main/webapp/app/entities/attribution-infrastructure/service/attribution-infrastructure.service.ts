import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttributionInfrastructure, getAttributionInfrastructureIdentifier } from '../attribution-infrastructure.model';

export type EntityResponseType = HttpResponse<IAttributionInfrastructure>;
export type EntityArrayResponseType = HttpResponse<IAttributionInfrastructure[]>;

@Injectable({ providedIn: 'root' })
export class AttributionInfrastructureService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attribution-infrastructures');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(attributionInfrastructure: IAttributionInfrastructure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attributionInfrastructure);
    return this.http
      .post<IAttributionInfrastructure>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(attributionInfrastructure: IAttributionInfrastructure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attributionInfrastructure);
    return this.http
      .put<IAttributionInfrastructure>(
        `${this.resourceUrl}/${getAttributionInfrastructureIdentifier(attributionInfrastructure) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(attributionInfrastructure: IAttributionInfrastructure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attributionInfrastructure);
    return this.http
      .patch<IAttributionInfrastructure>(
        `${this.resourceUrl}/${getAttributionInfrastructureIdentifier(attributionInfrastructure) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAttributionInfrastructure>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAttributionInfrastructure[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAttributionInfrastructureToCollectionIfMissing(
    attributionInfrastructureCollection: IAttributionInfrastructure[],
    ...attributionInfrastructuresToCheck: (IAttributionInfrastructure | null | undefined)[]
  ): IAttributionInfrastructure[] {
    const attributionInfrastructures: IAttributionInfrastructure[] = attributionInfrastructuresToCheck.filter(isPresent);
    if (attributionInfrastructures.length > 0) {
      const attributionInfrastructureCollectionIdentifiers = attributionInfrastructureCollection.map(
        attributionInfrastructureItem => getAttributionInfrastructureIdentifier(attributionInfrastructureItem)!
      );
      const attributionInfrastructuresToAdd = attributionInfrastructures.filter(attributionInfrastructureItem => {
        const attributionInfrastructureIdentifier = getAttributionInfrastructureIdentifier(attributionInfrastructureItem);
        if (
          attributionInfrastructureIdentifier == null ||
          attributionInfrastructureCollectionIdentifiers.includes(attributionInfrastructureIdentifier)
        ) {
          return false;
        }
        attributionInfrastructureCollectionIdentifiers.push(attributionInfrastructureIdentifier);
        return true;
      });
      return [...attributionInfrastructuresToAdd, ...attributionInfrastructureCollection];
    }
    return attributionInfrastructureCollection;
  }

  protected convertDateFromClient(attributionInfrastructure: IAttributionInfrastructure): IAttributionInfrastructure {
    return Object.assign({}, attributionInfrastructure, {
      dateAttribution: attributionInfrastructure.dateAttribution?.isValid()
        ? attributionInfrastructure.dateAttribution.toJSON()
        : undefined,
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
      res.body.forEach((attributionInfrastructure: IAttributionInfrastructure) => {
        attributionInfrastructure.dateAttribution = attributionInfrastructure.dateAttribution
          ? dayjs(attributionInfrastructure.dateAttribution)
          : undefined;
      });
    }
    return res;
  }
}
