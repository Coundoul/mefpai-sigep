import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRequete, getRequeteIdentifier } from '../requete.model';

export type EntityResponseType = HttpResponse<IRequete>;
export type EntityArrayResponseType = HttpResponse<IRequete[]>;

@Injectable({ providedIn: 'root' })
export class RequeteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/requetes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(requete: IRequete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(requete);
    return this.http
      .post<IRequete>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(requete: IRequete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(requete);
    return this.http
      .put<IRequete>(`${this.resourceUrl}/${getRequeteIdentifier(requete) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(requete: IRequete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(requete);
    return this.http
      .patch<IRequete>(`${this.resourceUrl}/${getRequeteIdentifier(requete) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRequete>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRequete[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRequeteToCollectionIfMissing(requeteCollection: IRequete[], ...requetesToCheck: (IRequete | null | undefined)[]): IRequete[] {
    const requetes: IRequete[] = requetesToCheck.filter(isPresent);
    if (requetes.length > 0) {
      const requeteCollectionIdentifiers = requeteCollection.map(requeteItem => getRequeteIdentifier(requeteItem)!);
      const requetesToAdd = requetes.filter(requeteItem => {
        const requeteIdentifier = getRequeteIdentifier(requeteItem);
        if (requeteIdentifier == null || requeteCollectionIdentifiers.includes(requeteIdentifier)) {
          return false;
        }
        requeteCollectionIdentifiers.push(requeteIdentifier);
        return true;
      });
      return [...requetesToAdd, ...requeteCollection];
    }
    return requeteCollection;
  }

  protected convertDateFromClient(requete: IRequete): IRequete {
    return Object.assign({}, requete, {
      dateLancement: requete.dateLancement?.isValid() ? requete.dateLancement.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateLancement = res.body.dateLancement ? dayjs(res.body.dateLancement) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((requete: IRequete) => {
        requete.dateLancement = requete.dateLancement ? dayjs(requete.dateLancement) : undefined;
      });
    }
    return res;
  }
}
