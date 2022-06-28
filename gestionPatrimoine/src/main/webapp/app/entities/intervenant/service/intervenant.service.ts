import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIntervenant, getIntervenantIdentifier } from '../intervenant.model';

export type EntityResponseType = HttpResponse<IIntervenant>;
export type EntityArrayResponseType = HttpResponse<IIntervenant[]>;

@Injectable({ providedIn: 'root' })
export class IntervenantService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/intervenants');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(intervenant: IIntervenant): Observable<EntityResponseType> {
    return this.http.post<IIntervenant>(this.resourceUrl, intervenant, { observe: 'response' });
  }

  update(intervenant: IIntervenant): Observable<EntityResponseType> {
    return this.http.put<IIntervenant>(`${this.resourceUrl}/${getIntervenantIdentifier(intervenant) as number}`, intervenant, {
      observe: 'response',
    });
  }

  partialUpdate(intervenant: IIntervenant): Observable<EntityResponseType> {
    return this.http.patch<IIntervenant>(`${this.resourceUrl}/${getIntervenantIdentifier(intervenant) as number}`, intervenant, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIntervenant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIntervenant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIntervenantToCollectionIfMissing(
    intervenantCollection: IIntervenant[],
    ...intervenantsToCheck: (IIntervenant | null | undefined)[]
  ): IIntervenant[] {
    const intervenants: IIntervenant[] = intervenantsToCheck.filter(isPresent);
    if (intervenants.length > 0) {
      const intervenantCollectionIdentifiers = intervenantCollection.map(intervenantItem => getIntervenantIdentifier(intervenantItem)!);
      const intervenantsToAdd = intervenants.filter(intervenantItem => {
        const intervenantIdentifier = getIntervenantIdentifier(intervenantItem);
        if (intervenantIdentifier == null || intervenantCollectionIdentifiers.includes(intervenantIdentifier)) {
          return false;
        }
        intervenantCollectionIdentifiers.push(intervenantIdentifier);
        return true;
      });
      return [...intervenantsToAdd, ...intervenantCollection];
    }
    return intervenantCollection;
  }
}
