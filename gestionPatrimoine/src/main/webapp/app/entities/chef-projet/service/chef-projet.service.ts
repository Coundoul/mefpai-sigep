import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChefProjet, getChefProjetIdentifier } from '../chef-projet.model';

export type EntityResponseType = HttpResponse<IChefProjet>;
export type EntityArrayResponseType = HttpResponse<IChefProjet[]>;

@Injectable({ providedIn: 'root' })
export class ChefProjetService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/chef-projets');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(chefProjet: IChefProjet): Observable<EntityResponseType> {
    return this.http.post<IChefProjet>(this.resourceUrl, chefProjet, { observe: 'response' });
  }

  update(chefProjet: IChefProjet): Observable<EntityResponseType> {
    return this.http.put<IChefProjet>(`${this.resourceUrl}/${getChefProjetIdentifier(chefProjet) as number}`, chefProjet, {
      observe: 'response',
    });
  }

  partialUpdate(chefProjet: IChefProjet): Observable<EntityResponseType> {
    return this.http.patch<IChefProjet>(`${this.resourceUrl}/${getChefProjetIdentifier(chefProjet) as number}`, chefProjet, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChefProjet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChefProjet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChefProjetToCollectionIfMissing(
    chefProjetCollection: IChefProjet[],
    ...chefProjetsToCheck: (IChefProjet | null | undefined)[]
  ): IChefProjet[] {
    const chefProjets: IChefProjet[] = chefProjetsToCheck.filter(isPresent);
    if (chefProjets.length > 0) {
      const chefProjetCollectionIdentifiers = chefProjetCollection.map(chefProjetItem => getChefProjetIdentifier(chefProjetItem)!);
      const chefProjetsToAdd = chefProjets.filter(chefProjetItem => {
        const chefProjetIdentifier = getChefProjetIdentifier(chefProjetItem);
        if (chefProjetIdentifier == null || chefProjetCollectionIdentifiers.includes(chefProjetIdentifier)) {
          return false;
        }
        chefProjetCollectionIdentifiers.push(chefProjetIdentifier);
        return true;
      });
      return [...chefProjetsToAdd, ...chefProjetCollection];
    }
    return chefProjetCollection;
  }
}
