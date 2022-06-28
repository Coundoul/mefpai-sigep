import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBatiment, getBatimentIdentifier } from '../batiment.model';

export type EntityResponseType = HttpResponse<IBatiment>;
export type EntityArrayResponseType = HttpResponse<IBatiment[]>;

@Injectable({ providedIn: 'root' })
export class BatimentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/batiments');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(batiment: IBatiment): Observable<EntityResponseType> {
    return this.http.post<IBatiment>(this.resourceUrl, batiment, { observe: 'response' });
  }

  update(batiment: IBatiment): Observable<EntityResponseType> {
    return this.http.put<IBatiment>(`${this.resourceUrl}/${getBatimentIdentifier(batiment) as number}`, batiment, { observe: 'response' });
  }

  partialUpdate(batiment: IBatiment): Observable<EntityResponseType> {
    return this.http.patch<IBatiment>(`${this.resourceUrl}/${getBatimentIdentifier(batiment) as number}`, batiment, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBatiment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBatiment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBatimentToCollectionIfMissing(batimentCollection: IBatiment[], ...batimentsToCheck: (IBatiment | null | undefined)[]): IBatiment[] {
    const batiments: IBatiment[] = batimentsToCheck.filter(isPresent);
    if (batiments.length > 0) {
      const batimentCollectionIdentifiers = batimentCollection.map(batimentItem => getBatimentIdentifier(batimentItem)!);
      const batimentsToAdd = batiments.filter(batimentItem => {
        const batimentIdentifier = getBatimentIdentifier(batimentItem);
        if (batimentIdentifier == null || batimentCollectionIdentifiers.includes(batimentIdentifier)) {
          return false;
        }
        batimentCollectionIdentifiers.push(batimentIdentifier);
        return true;
      });
      return [...batimentsToAdd, ...batimentCollection];
    }
    return batimentCollection;
  }
}
