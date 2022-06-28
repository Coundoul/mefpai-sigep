import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIntendant, getIntendantIdentifier } from '../intendant.model';

export type EntityResponseType = HttpResponse<IIntendant>;
export type EntityArrayResponseType = HttpResponse<IIntendant[]>;

@Injectable({ providedIn: 'root' })
export class IntendantService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/intendants');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(intendant: IIntendant): Observable<EntityResponseType> {
    return this.http.post<IIntendant>(this.resourceUrl, intendant, { observe: 'response' });
  }

  update(intendant: IIntendant): Observable<EntityResponseType> {
    return this.http.put<IIntendant>(`${this.resourceUrl}/${getIntendantIdentifier(intendant) as number}`, intendant, {
      observe: 'response',
    });
  }

  partialUpdate(intendant: IIntendant): Observable<EntityResponseType> {
    return this.http.patch<IIntendant>(`${this.resourceUrl}/${getIntendantIdentifier(intendant) as number}`, intendant, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIntendant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIntendant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIntendantToCollectionIfMissing(
    intendantCollection: IIntendant[],
    ...intendantsToCheck: (IIntendant | null | undefined)[]
  ): IIntendant[] {
    const intendants: IIntendant[] = intendantsToCheck.filter(isPresent);
    if (intendants.length > 0) {
      const intendantCollectionIdentifiers = intendantCollection.map(intendantItem => getIntendantIdentifier(intendantItem)!);
      const intendantsToAdd = intendants.filter(intendantItem => {
        const intendantIdentifier = getIntendantIdentifier(intendantItem);
        if (intendantIdentifier == null || intendantCollectionIdentifiers.includes(intendantIdentifier)) {
          return false;
        }
        intendantCollectionIdentifiers.push(intendantIdentifier);
        return true;
      });
      return [...intendantsToAdd, ...intendantCollection];
    }
    return intendantCollection;
  }
}
