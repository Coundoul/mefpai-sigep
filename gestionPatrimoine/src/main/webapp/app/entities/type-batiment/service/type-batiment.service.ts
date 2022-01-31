import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeBatiment, getTypeBatimentIdentifier } from '../type-batiment.model';

export type EntityResponseType = HttpResponse<ITypeBatiment>;
export type EntityArrayResponseType = HttpResponse<ITypeBatiment[]>;

@Injectable({ providedIn: 'root' })
export class TypeBatimentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/type-batiments');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(typeBatiment: ITypeBatiment): Observable<EntityResponseType> {
    return this.http.post<ITypeBatiment>(this.resourceUrl, typeBatiment, { observe: 'response' });
  }

  update(typeBatiment: ITypeBatiment): Observable<EntityResponseType> {
    return this.http.put<ITypeBatiment>(`${this.resourceUrl}/${getTypeBatimentIdentifier(typeBatiment) as number}`, typeBatiment, {
      observe: 'response',
    });
  }

  partialUpdate(typeBatiment: ITypeBatiment): Observable<EntityResponseType> {
    return this.http.patch<ITypeBatiment>(`${this.resourceUrl}/${getTypeBatimentIdentifier(typeBatiment) as number}`, typeBatiment, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeBatiment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeBatiment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypeBatimentToCollectionIfMissing(
    typeBatimentCollection: ITypeBatiment[],
    ...typeBatimentsToCheck: (ITypeBatiment | null | undefined)[]
  ): ITypeBatiment[] {
    const typeBatiments: ITypeBatiment[] = typeBatimentsToCheck.filter(isPresent);
    if (typeBatiments.length > 0) {
      const typeBatimentCollectionIdentifiers = typeBatimentCollection.map(
        typeBatimentItem => getTypeBatimentIdentifier(typeBatimentItem)!
      );
      const typeBatimentsToAdd = typeBatiments.filter(typeBatimentItem => {
        const typeBatimentIdentifier = getTypeBatimentIdentifier(typeBatimentItem);
        if (typeBatimentIdentifier == null || typeBatimentCollectionIdentifiers.includes(typeBatimentIdentifier)) {
          return false;
        }
        typeBatimentCollectionIdentifiers.push(typeBatimentIdentifier);
        return true;
      });
      return [...typeBatimentsToAdd, ...typeBatimentCollection];
    }
    return typeBatimentCollection;
  }
}
