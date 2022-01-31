import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetenteur, getDetenteurIdentifier } from '../detenteur.model';

export type EntityResponseType = HttpResponse<IDetenteur>;
export type EntityArrayResponseType = HttpResponse<IDetenteur[]>;

@Injectable({ providedIn: 'root' })
export class DetenteurService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/detenteurs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(detenteur: IDetenteur): Observable<EntityResponseType> {
    return this.http.post<IDetenteur>(this.resourceUrl, detenteur, { observe: 'response' });
  }

  update(detenteur: IDetenteur): Observable<EntityResponseType> {
    return this.http.put<IDetenteur>(`${this.resourceUrl}/${getDetenteurIdentifier(detenteur) as number}`, detenteur, {
      observe: 'response',
    });
  }

  partialUpdate(detenteur: IDetenteur): Observable<EntityResponseType> {
    return this.http.patch<IDetenteur>(`${this.resourceUrl}/${getDetenteurIdentifier(detenteur) as number}`, detenteur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetenteur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetenteur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDetenteurToCollectionIfMissing(
    detenteurCollection: IDetenteur[],
    ...detenteursToCheck: (IDetenteur | null | undefined)[]
  ): IDetenteur[] {
    const detenteurs: IDetenteur[] = detenteursToCheck.filter(isPresent);
    if (detenteurs.length > 0) {
      const detenteurCollectionIdentifiers = detenteurCollection.map(detenteurItem => getDetenteurIdentifier(detenteurItem)!);
      const detenteursToAdd = detenteurs.filter(detenteurItem => {
        const detenteurIdentifier = getDetenteurIdentifier(detenteurItem);
        if (detenteurIdentifier == null || detenteurCollectionIdentifiers.includes(detenteurIdentifier)) {
          return false;
        }
        detenteurCollectionIdentifiers.push(detenteurIdentifier);
        return true;
      });
      return [...detenteursToAdd, ...detenteurCollection];
    }
    return detenteurCollection;
  }
}
