import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuartier, getQuartierIdentifier } from '../quartier.model';

export type EntityResponseType = HttpResponse<IQuartier>;
export type EntityArrayResponseType = HttpResponse<IQuartier[]>;

@Injectable({ providedIn: 'root' })
export class QuartierService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/quartiers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(quartier: IQuartier): Observable<EntityResponseType> {
    return this.http.post<IQuartier>(this.resourceUrl, quartier, { observe: 'response' });
  }

  update(quartier: IQuartier): Observable<EntityResponseType> {
    return this.http.put<IQuartier>(`${this.resourceUrl}/${getQuartierIdentifier(quartier) as number}`, quartier, { observe: 'response' });
  }

  partialUpdate(quartier: IQuartier): Observable<EntityResponseType> {
    return this.http.patch<IQuartier>(`${this.resourceUrl}/${getQuartierIdentifier(quartier) as number}`, quartier, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuartier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuartier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuartierToCollectionIfMissing(quartierCollection: IQuartier[], ...quartiersToCheck: (IQuartier | null | undefined)[]): IQuartier[] {
    const quartiers: IQuartier[] = quartiersToCheck.filter(isPresent);
    if (quartiers.length > 0) {
      const quartierCollectionIdentifiers = quartierCollection.map(quartierItem => getQuartierIdentifier(quartierItem)!);
      const quartiersToAdd = quartiers.filter(quartierItem => {
        const quartierIdentifier = getQuartierIdentifier(quartierItem);
        if (quartierIdentifier == null || quartierCollectionIdentifiers.includes(quartierIdentifier)) {
          return false;
        }
        quartierCollectionIdentifiers.push(quartierIdentifier);
        return true;
      });
      return [...quartiersToAdd, ...quartierCollection];
    }
    return quartierCollection;
  }
}
