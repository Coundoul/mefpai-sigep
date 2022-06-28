import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICorpsEtat, getCorpsEtatIdentifier } from '../corps-etat.model';

export type EntityResponseType = HttpResponse<ICorpsEtat>;
export type EntityArrayResponseType = HttpResponse<ICorpsEtat[]>;

@Injectable({ providedIn: 'root' })
export class CorpsEtatService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/corps-etats');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(corpsEtat: ICorpsEtat): Observable<EntityResponseType> {
    return this.http.post<ICorpsEtat>(this.resourceUrl, corpsEtat, { observe: 'response' });
  }

  update(corpsEtat: ICorpsEtat): Observable<EntityResponseType> {
    return this.http.put<ICorpsEtat>(`${this.resourceUrl}/${getCorpsEtatIdentifier(corpsEtat) as number}`, corpsEtat, {
      observe: 'response',
    });
  }

  partialUpdate(corpsEtat: ICorpsEtat): Observable<EntityResponseType> {
    return this.http.patch<ICorpsEtat>(`${this.resourceUrl}/${getCorpsEtatIdentifier(corpsEtat) as number}`, corpsEtat, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICorpsEtat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICorpsEtat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCorpsEtatToCollectionIfMissing(
    corpsEtatCollection: ICorpsEtat[],
    ...corpsEtatsToCheck: (ICorpsEtat | null | undefined)[]
  ): ICorpsEtat[] {
    const corpsEtats: ICorpsEtat[] = corpsEtatsToCheck.filter(isPresent);
    if (corpsEtats.length > 0) {
      const corpsEtatCollectionIdentifiers = corpsEtatCollection.map(corpsEtatItem => getCorpsEtatIdentifier(corpsEtatItem)!);
      const corpsEtatsToAdd = corpsEtats.filter(corpsEtatItem => {
        const corpsEtatIdentifier = getCorpsEtatIdentifier(corpsEtatItem);
        if (corpsEtatIdentifier == null || corpsEtatCollectionIdentifiers.includes(corpsEtatIdentifier)) {
          return false;
        }
        corpsEtatCollectionIdentifiers.push(corpsEtatIdentifier);
        return true;
      });
      return [...corpsEtatsToAdd, ...corpsEtatCollection];
    }
    return corpsEtatCollection;
  }
}
