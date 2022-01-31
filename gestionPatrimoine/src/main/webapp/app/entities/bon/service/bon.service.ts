import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBon, getBonIdentifier } from '../bon.model';

export type EntityResponseType = HttpResponse<IBon>;
export type EntityArrayResponseType = HttpResponse<IBon[]>;

@Injectable({ providedIn: 'root' })
export class BonService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/bons', 'gestionequipement');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(bon: IBon): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bon);
    return this.http
      .post<IBon>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bon: IBon): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bon);
    return this.http
      .put<IBon>(`${this.resourceUrl}/${getBonIdentifier(bon) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(bon: IBon): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bon);
    return this.http
      .patch<IBon>(`${this.resourceUrl}/${getBonIdentifier(bon) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBon>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBon[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBonToCollectionIfMissing(bonCollection: IBon[], ...bonsToCheck: (IBon | null | undefined)[]): IBon[] {
    const bons: IBon[] = bonsToCheck.filter(isPresent);
    if (bons.length > 0) {
      const bonCollectionIdentifiers = bonCollection.map(bonItem => getBonIdentifier(bonItem)!);
      const bonsToAdd = bons.filter(bonItem => {
        const bonIdentifier = getBonIdentifier(bonItem);
        if (bonIdentifier == null || bonCollectionIdentifiers.includes(bonIdentifier)) {
          return false;
        }
        bonCollectionIdentifiers.push(bonIdentifier);
        return true;
      });
      return [...bonsToAdd, ...bonCollection];
    }
    return bonCollection;
  }

  protected convertDateFromClient(bon: IBon): IBon {
    return Object.assign({}, bon, {
      dateCreation: bon.dateCreation?.isValid() ? bon.dateCreation.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation ? dayjs(res.body.dateCreation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((bon: IBon) => {
        bon.dateCreation = bon.dateCreation ? dayjs(bon.dateCreation) : undefined;
      });
    }
    return res;
  }
}
