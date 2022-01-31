import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContratProjet, getContratProjetIdentifier } from '../contrat-projet.model';

export type EntityResponseType = HttpResponse<IContratProjet>;
export type EntityArrayResponseType = HttpResponse<IContratProjet[]>;

@Injectable({ providedIn: 'root' })
export class ContratProjetService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/contrat-projets');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(contratProjet: IContratProjet): Observable<EntityResponseType> {
    return this.http.post<IContratProjet>(this.resourceUrl, contratProjet, { observe: 'response' });
  }

  update(contratProjet: IContratProjet): Observable<EntityResponseType> {
    return this.http.put<IContratProjet>(`${this.resourceUrl}/${getContratProjetIdentifier(contratProjet) as number}`, contratProjet, {
      observe: 'response',
    });
  }

  partialUpdate(contratProjet: IContratProjet): Observable<EntityResponseType> {
    return this.http.patch<IContratProjet>(`${this.resourceUrl}/${getContratProjetIdentifier(contratProjet) as number}`, contratProjet, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContratProjet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContratProjet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContratProjetToCollectionIfMissing(
    contratProjetCollection: IContratProjet[],
    ...contratProjetsToCheck: (IContratProjet | null | undefined)[]
  ): IContratProjet[] {
    const contratProjets: IContratProjet[] = contratProjetsToCheck.filter(isPresent);
    if (contratProjets.length > 0) {
      const contratProjetCollectionIdentifiers = contratProjetCollection.map(
        contratProjetItem => getContratProjetIdentifier(contratProjetItem)!
      );
      const contratProjetsToAdd = contratProjets.filter(contratProjetItem => {
        const contratProjetIdentifier = getContratProjetIdentifier(contratProjetItem);
        if (contratProjetIdentifier == null || contratProjetCollectionIdentifiers.includes(contratProjetIdentifier)) {
          return false;
        }
        contratProjetCollectionIdentifiers.push(contratProjetIdentifier);
        return true;
      });
      return [...contratProjetsToAdd, ...contratProjetCollection];
    }
    return contratProjetCollection;
  }
}
