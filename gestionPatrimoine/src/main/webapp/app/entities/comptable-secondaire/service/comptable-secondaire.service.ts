import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComptableSecondaire, getComptableSecondaireIdentifier } from '../comptable-secondaire.model';

export type EntityResponseType = HttpResponse<IComptableSecondaire>;
export type EntityArrayResponseType = HttpResponse<IComptableSecondaire[]>;

@Injectable({ providedIn: 'root' })
export class ComptableSecondaireService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/comptable-secondaires');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(comptableSecondaire: IComptableSecondaire): Observable<EntityResponseType> {
    return this.http.post<IComptableSecondaire>(this.resourceUrl, comptableSecondaire, { observe: 'response' });
  }

  update(comptableSecondaire: IComptableSecondaire): Observable<EntityResponseType> {
    return this.http.put<IComptableSecondaire>(
      `${this.resourceUrl}/${getComptableSecondaireIdentifier(comptableSecondaire) as number}`,
      comptableSecondaire,
      { observe: 'response' }
    );
  }

  partialUpdate(comptableSecondaire: IComptableSecondaire): Observable<EntityResponseType> {
    return this.http.patch<IComptableSecondaire>(
      `${this.resourceUrl}/${getComptableSecondaireIdentifier(comptableSecondaire) as number}`,
      comptableSecondaire,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IComptableSecondaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IComptableSecondaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addComptableSecondaireToCollectionIfMissing(
    comptableSecondaireCollection: IComptableSecondaire[],
    ...comptableSecondairesToCheck: (IComptableSecondaire | null | undefined)[]
  ): IComptableSecondaire[] {
    const comptableSecondaires: IComptableSecondaire[] = comptableSecondairesToCheck.filter(isPresent);
    if (comptableSecondaires.length > 0) {
      const comptableSecondaireCollectionIdentifiers = comptableSecondaireCollection.map(
        comptableSecondaireItem => getComptableSecondaireIdentifier(comptableSecondaireItem)!
      );
      const comptableSecondairesToAdd = comptableSecondaires.filter(comptableSecondaireItem => {
        const comptableSecondaireIdentifier = getComptableSecondaireIdentifier(comptableSecondaireItem);
        if (comptableSecondaireIdentifier == null || comptableSecondaireCollectionIdentifiers.includes(comptableSecondaireIdentifier)) {
          return false;
        }
        comptableSecondaireCollectionIdentifiers.push(comptableSecondaireIdentifier);
        return true;
      });
      return [...comptableSecondairesToAdd, ...comptableSecondaireCollection];
    }
    return comptableSecondaireCollection;
  }
}
