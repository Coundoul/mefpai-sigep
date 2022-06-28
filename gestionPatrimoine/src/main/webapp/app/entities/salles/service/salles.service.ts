import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISalles, getSallesIdentifier } from '../salles.model';

export type EntityResponseType = HttpResponse<ISalles>;
export type EntityArrayResponseType = HttpResponse<ISalles[]>;

@Injectable({ providedIn: 'root' })
export class SallesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/salles');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(salles: ISalles): Observable<EntityResponseType> {
    return this.http.post<ISalles>(this.resourceUrl, salles, { observe: 'response' });
  }

  update(salles: ISalles): Observable<EntityResponseType> {
    return this.http.put<ISalles>(`${this.resourceUrl}/${getSallesIdentifier(salles) as number}`, salles, { observe: 'response' });
  }

  partialUpdate(salles: ISalles): Observable<EntityResponseType> {
    return this.http.patch<ISalles>(`${this.resourceUrl}/${getSallesIdentifier(salles) as number}`, salles, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISalles>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISalles[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSallesToCollectionIfMissing(sallesCollection: ISalles[], ...sallesToCheck: (ISalles | null | undefined)[]): ISalles[] {
    const salles: ISalles[] = sallesToCheck.filter(isPresent);
    if (salles.length > 0) {
      const sallesCollectionIdentifiers = sallesCollection.map(sallesItem => getSallesIdentifier(sallesItem)!);
      const sallesToAdd = salles.filter(sallesItem => {
        const sallesIdentifier = getSallesIdentifier(sallesItem);
        if (sallesIdentifier == null || sallesCollectionIdentifiers.includes(sallesIdentifier)) {
          return false;
        }
        sallesCollectionIdentifiers.push(sallesIdentifier);
        return true;
      });
      return [...sallesToAdd, ...sallesCollection];
    }
    return sallesCollection;
  }
}
