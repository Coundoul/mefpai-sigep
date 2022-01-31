import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComptablePrincipale, getComptablePrincipaleIdentifier } from '../comptable-principale.model';

export type EntityResponseType = HttpResponse<IComptablePrincipale>;
export type EntityArrayResponseType = HttpResponse<IComptablePrincipale[]>;

@Injectable({ providedIn: 'root' })
export class ComptablePrincipaleService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/comptable-principales');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(comptablePrincipale: IComptablePrincipale): Observable<EntityResponseType> {
    return this.http.post<IComptablePrincipale>(this.resourceUrl, comptablePrincipale, { observe: 'response' });
  }

  update(comptablePrincipale: IComptablePrincipale): Observable<EntityResponseType> {
    return this.http.put<IComptablePrincipale>(
      `${this.resourceUrl}/${getComptablePrincipaleIdentifier(comptablePrincipale) as number}`,
      comptablePrincipale,
      { observe: 'response' }
    );
  }

  partialUpdate(comptablePrincipale: IComptablePrincipale): Observable<EntityResponseType> {
    return this.http.patch<IComptablePrincipale>(
      `${this.resourceUrl}/${getComptablePrincipaleIdentifier(comptablePrincipale) as number}`,
      comptablePrincipale,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IComptablePrincipale>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IComptablePrincipale[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addComptablePrincipaleToCollectionIfMissing(
    comptablePrincipaleCollection: IComptablePrincipale[],
    ...comptablePrincipalesToCheck: (IComptablePrincipale | null | undefined)[]
  ): IComptablePrincipale[] {
    const comptablePrincipales: IComptablePrincipale[] = comptablePrincipalesToCheck.filter(isPresent);
    if (comptablePrincipales.length > 0) {
      const comptablePrincipaleCollectionIdentifiers = comptablePrincipaleCollection.map(
        comptablePrincipaleItem => getComptablePrincipaleIdentifier(comptablePrincipaleItem)!
      );
      const comptablePrincipalesToAdd = comptablePrincipales.filter(comptablePrincipaleItem => {
        const comptablePrincipaleIdentifier = getComptablePrincipaleIdentifier(comptablePrincipaleItem);
        if (comptablePrincipaleIdentifier == null || comptablePrincipaleCollectionIdentifiers.includes(comptablePrincipaleIdentifier)) {
          return false;
        }
        comptablePrincipaleCollectionIdentifiers.push(comptablePrincipaleIdentifier);
        return true;
      });
      return [...comptablePrincipalesToAdd, ...comptablePrincipaleCollection];
    }
    return comptablePrincipaleCollection;
  }
}
