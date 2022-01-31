import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAtelier, getAtelierIdentifier } from '../atelier.model';

export type EntityResponseType = HttpResponse<IAtelier>;
export type EntityArrayResponseType = HttpResponse<IAtelier[]>;

@Injectable({ providedIn: 'root' })
export class AtelierService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/ateliers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(atelier: IAtelier): Observable<EntityResponseType> {
    return this.http.post<IAtelier>(this.resourceUrl, atelier, { observe: 'response' });
  }

  update(atelier: IAtelier): Observable<EntityResponseType> {
    return this.http.put<IAtelier>(`${this.resourceUrl}/${getAtelierIdentifier(atelier) as number}`, atelier, { observe: 'response' });
  }

  partialUpdate(atelier: IAtelier): Observable<EntityResponseType> {
    return this.http.patch<IAtelier>(`${this.resourceUrl}/${getAtelierIdentifier(atelier) as number}`, atelier, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAtelier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAtelier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAtelierToCollectionIfMissing(atelierCollection: IAtelier[], ...ateliersToCheck: (IAtelier | null | undefined)[]): IAtelier[] {
    const ateliers: IAtelier[] = ateliersToCheck.filter(isPresent);
    if (ateliers.length > 0) {
      const atelierCollectionIdentifiers = atelierCollection.map(atelierItem => getAtelierIdentifier(atelierItem)!);
      const ateliersToAdd = ateliers.filter(atelierItem => {
        const atelierIdentifier = getAtelierIdentifier(atelierItem);
        if (atelierIdentifier == null || atelierCollectionIdentifiers.includes(atelierIdentifier)) {
          return false;
        }
        atelierCollectionIdentifiers.push(atelierIdentifier);
        return true;
      });
      return [...ateliersToAdd, ...atelierCollection];
    }
    return atelierCollection;
  }
}
