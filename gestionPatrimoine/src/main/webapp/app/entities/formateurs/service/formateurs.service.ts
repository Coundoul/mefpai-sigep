import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormateurs, getFormateursIdentifier } from '../formateurs.model';

export type EntityResponseType = HttpResponse<IFormateurs>;
export type EntityArrayResponseType = HttpResponse<IFormateurs[]>;

@Injectable({ providedIn: 'root' })
export class FormateursService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/formateurs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(formateurs: IFormateurs): Observable<EntityResponseType> {
    return this.http.post<IFormateurs>(this.resourceUrl, formateurs, { observe: 'response' });
  }

  update(formateurs: IFormateurs): Observable<EntityResponseType> {
    return this.http.put<IFormateurs>(`${this.resourceUrl}/${getFormateursIdentifier(formateurs) as number}`, formateurs, {
      observe: 'response',
    });
  }

  partialUpdate(formateurs: IFormateurs): Observable<EntityResponseType> {
    return this.http.patch<IFormateurs>(`${this.resourceUrl}/${getFormateursIdentifier(formateurs) as number}`, formateurs, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFormateurs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFormateurs[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormateursToCollectionIfMissing(
    formateursCollection: IFormateurs[],
    ...formateursToCheck: (IFormateurs | null | undefined)[]
  ): IFormateurs[] {
    const formateurs: IFormateurs[] = formateursToCheck.filter(isPresent);
    if (formateurs.length > 0) {
      const formateursCollectionIdentifiers = formateursCollection.map(formateursItem => getFormateursIdentifier(formateursItem)!);
      const formateursToAdd = formateurs.filter(formateursItem => {
        const formateursIdentifier = getFormateursIdentifier(formateursItem);
        if (formateursIdentifier == null || formateursCollectionIdentifiers.includes(formateursIdentifier)) {
          return false;
        }
        formateursCollectionIdentifiers.push(formateursIdentifier);
        return true;
      });
      return [...formateursToAdd, ...formateursCollection];
    }
    return formateursCollection;
  }
}
