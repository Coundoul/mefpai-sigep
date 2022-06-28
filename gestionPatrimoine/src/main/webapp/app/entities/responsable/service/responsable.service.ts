import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResponsable, getResponsableIdentifier } from '../responsable.model';

export type EntityResponseType = HttpResponse<IResponsable>;
export type EntityArrayResponseType = HttpResponse<IResponsable[]>;

@Injectable({ providedIn: 'root' })
export class ResponsableService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/responsables');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(responsable: IResponsable): Observable<EntityResponseType> {
    return this.http.post<IResponsable>(this.resourceUrl, responsable, { observe: 'response' });
  }

  update(responsable: IResponsable): Observable<EntityResponseType> {
    return this.http.put<IResponsable>(`${this.resourceUrl}/${getResponsableIdentifier(responsable) as number}`, responsable, {
      observe: 'response',
    });
  }

  partialUpdate(responsable: IResponsable): Observable<EntityResponseType> {
    return this.http.patch<IResponsable>(`${this.resourceUrl}/${getResponsableIdentifier(responsable) as number}`, responsable, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResponsable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResponsable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addResponsableToCollectionIfMissing(
    responsableCollection: IResponsable[],
    ...responsablesToCheck: (IResponsable | null | undefined)[]
  ): IResponsable[] {
    const responsables: IResponsable[] = responsablesToCheck.filter(isPresent);
    if (responsables.length > 0) {
      const responsableCollectionIdentifiers = responsableCollection.map(responsableItem => getResponsableIdentifier(responsableItem)!);
      const responsablesToAdd = responsables.filter(responsableItem => {
        const responsableIdentifier = getResponsableIdentifier(responsableItem);
        if (responsableIdentifier == null || responsableCollectionIdentifiers.includes(responsableIdentifier)) {
          return false;
        }
        responsableCollectionIdentifiers.push(responsableIdentifier);
        return true;
      });
      return [...responsablesToAdd, ...responsableCollection];
    }
    return responsableCollection;
  }
}
