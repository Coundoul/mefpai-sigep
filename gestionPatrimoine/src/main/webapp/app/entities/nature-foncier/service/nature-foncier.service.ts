import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INatureFoncier, getNatureFoncierIdentifier } from '../nature-foncier.model';

export type EntityResponseType = HttpResponse<INatureFoncier>;
export type EntityArrayResponseType = HttpResponse<INatureFoncier[]>;

@Injectable({ providedIn: 'root' })
export class NatureFoncierService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/nature-fonciers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(natureFoncier: INatureFoncier): Observable<EntityResponseType> {
    return this.http.post<INatureFoncier>(this.resourceUrl, natureFoncier, { observe: 'response' });
  }

  update(natureFoncier: INatureFoncier): Observable<EntityResponseType> {
    return this.http.put<INatureFoncier>(`${this.resourceUrl}/${getNatureFoncierIdentifier(natureFoncier) as number}`, natureFoncier, {
      observe: 'response',
    });
  }

  partialUpdate(natureFoncier: INatureFoncier): Observable<EntityResponseType> {
    return this.http.patch<INatureFoncier>(`${this.resourceUrl}/${getNatureFoncierIdentifier(natureFoncier) as number}`, natureFoncier, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INatureFoncier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INatureFoncier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNatureFoncierToCollectionIfMissing(
    natureFoncierCollection: INatureFoncier[],
    ...natureFonciersToCheck: (INatureFoncier | null | undefined)[]
  ): INatureFoncier[] {
    const natureFonciers: INatureFoncier[] = natureFonciersToCheck.filter(isPresent);
    if (natureFonciers.length > 0) {
      const natureFoncierCollectionIdentifiers = natureFoncierCollection.map(
        natureFoncierItem => getNatureFoncierIdentifier(natureFoncierItem)!
      );
      const natureFonciersToAdd = natureFonciers.filter(natureFoncierItem => {
        const natureFoncierIdentifier = getNatureFoncierIdentifier(natureFoncierItem);
        if (natureFoncierIdentifier == null || natureFoncierCollectionIdentifiers.includes(natureFoncierIdentifier)) {
          return false;
        }
        natureFoncierCollectionIdentifiers.push(natureFoncierIdentifier);
        return true;
      });
      return [...natureFonciersToAdd, ...natureFoncierCollection];
    }
    return natureFoncierCollection;
  }
}
