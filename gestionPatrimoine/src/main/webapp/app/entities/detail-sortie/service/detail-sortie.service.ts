import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetailSortie, getDetailSortieIdentifier } from '../detail-sortie.model';

export type EntityResponseType = HttpResponse<IDetailSortie>;
export type EntityArrayResponseType = HttpResponse<IDetailSortie[]>;

@Injectable({ providedIn: 'root' })
export class DetailSortieService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/detail-sorties', 'gestionequipement');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(detailSortie: IDetailSortie): Observable<EntityResponseType> {
    return this.http.post<IDetailSortie>(this.resourceUrl, detailSortie, { observe: 'response' });
  }

  update(detailSortie: IDetailSortie): Observable<EntityResponseType> {
    return this.http.put<IDetailSortie>(`${this.resourceUrl}/${getDetailSortieIdentifier(detailSortie) as number}`, detailSortie, {
      observe: 'response',
    });
  }

  partialUpdate(detailSortie: IDetailSortie): Observable<EntityResponseType> {
    return this.http.patch<IDetailSortie>(`${this.resourceUrl}/${getDetailSortieIdentifier(detailSortie) as number}`, detailSortie, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetailSortie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetailSortie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDetailSortieToCollectionIfMissing(
    detailSortieCollection: IDetailSortie[],
    ...detailSortiesToCheck: (IDetailSortie | null | undefined)[]
  ): IDetailSortie[] {
    const detailSorties: IDetailSortie[] = detailSortiesToCheck.filter(isPresent);
    if (detailSorties.length > 0) {
      const detailSortieCollectionIdentifiers = detailSortieCollection.map(
        detailSortieItem => getDetailSortieIdentifier(detailSortieItem)!
      );
      const detailSortiesToAdd = detailSorties.filter(detailSortieItem => {
        const detailSortieIdentifier = getDetailSortieIdentifier(detailSortieItem);
        if (detailSortieIdentifier == null || detailSortieCollectionIdentifiers.includes(detailSortieIdentifier)) {
          return false;
        }
        detailSortieCollectionIdentifiers.push(detailSortieIdentifier);
        return true;
      });
      return [...detailSortiesToAdd, ...detailSortieCollection];
    }
    return detailSortieCollection;
  }
}
