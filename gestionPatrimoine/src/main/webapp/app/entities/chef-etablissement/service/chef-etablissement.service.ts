import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChefEtablissement, getChefEtablissementIdentifier } from '../chef-etablissement.model';

export type EntityResponseType = HttpResponse<IChefEtablissement>;
export type EntityArrayResponseType = HttpResponse<IChefEtablissement[]>;

@Injectable({ providedIn: 'root' })
export class ChefEtablissementService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/chef-etablissements');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(chefEtablissement: IChefEtablissement): Observable<EntityResponseType> {
    return this.http.post<IChefEtablissement>(this.resourceUrl, chefEtablissement, { observe: 'response' });
  }

  update(chefEtablissement: IChefEtablissement): Observable<EntityResponseType> {
    return this.http.put<IChefEtablissement>(
      `${this.resourceUrl}/${getChefEtablissementIdentifier(chefEtablissement) as number}`,
      chefEtablissement,
      { observe: 'response' }
    );
  }

  partialUpdate(chefEtablissement: IChefEtablissement): Observable<EntityResponseType> {
    return this.http.patch<IChefEtablissement>(
      `${this.resourceUrl}/${getChefEtablissementIdentifier(chefEtablissement) as number}`,
      chefEtablissement,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChefEtablissement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChefEtablissement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChefEtablissementToCollectionIfMissing(
    chefEtablissementCollection: IChefEtablissement[],
    ...chefEtablissementsToCheck: (IChefEtablissement | null | undefined)[]
  ): IChefEtablissement[] {
    const chefEtablissements: IChefEtablissement[] = chefEtablissementsToCheck.filter(isPresent);
    if (chefEtablissements.length > 0) {
      const chefEtablissementCollectionIdentifiers = chefEtablissementCollection.map(
        chefEtablissementItem => getChefEtablissementIdentifier(chefEtablissementItem)!
      );
      const chefEtablissementsToAdd = chefEtablissements.filter(chefEtablissementItem => {
        const chefEtablissementIdentifier = getChefEtablissementIdentifier(chefEtablissementItem);
        if (chefEtablissementIdentifier == null || chefEtablissementCollectionIdentifiers.includes(chefEtablissementIdentifier)) {
          return false;
        }
        chefEtablissementCollectionIdentifiers.push(chefEtablissementIdentifier);
        return true;
      });
      return [...chefEtablissementsToAdd, ...chefEtablissementCollection];
    }
    return chefEtablissementCollection;
  }
}
