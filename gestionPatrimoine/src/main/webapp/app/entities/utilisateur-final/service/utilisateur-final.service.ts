import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUtilisateurFinal, getUtilisateurFinalIdentifier } from '../utilisateur-final.model';

export type EntityResponseType = HttpResponse<IUtilisateurFinal>;
export type EntityArrayResponseType = HttpResponse<IUtilisateurFinal[]>;

@Injectable({ providedIn: 'root' })
export class UtilisateurFinalService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/utilisateur-finals', 'gestionequipement');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(utilisateurFinal: IUtilisateurFinal): Observable<EntityResponseType> {
    return this.http.post<IUtilisateurFinal>(this.resourceUrl, utilisateurFinal, { observe: 'response' });
  }

  update(utilisateurFinal: IUtilisateurFinal): Observable<EntityResponseType> {
    return this.http.put<IUtilisateurFinal>(
      `${this.resourceUrl}/${getUtilisateurFinalIdentifier(utilisateurFinal) as number}`,
      utilisateurFinal,
      { observe: 'response' }
    );
  }

  partialUpdate(utilisateurFinal: IUtilisateurFinal): Observable<EntityResponseType> {
    return this.http.patch<IUtilisateurFinal>(
      `${this.resourceUrl}/${getUtilisateurFinalIdentifier(utilisateurFinal) as number}`,
      utilisateurFinal,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUtilisateurFinal>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUtilisateurFinal[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUtilisateurFinalToCollectionIfMissing(
    utilisateurFinalCollection: IUtilisateurFinal[],
    ...utilisateurFinalsToCheck: (IUtilisateurFinal | null | undefined)[]
  ): IUtilisateurFinal[] {
    const utilisateurFinals: IUtilisateurFinal[] = utilisateurFinalsToCheck.filter(isPresent);
    if (utilisateurFinals.length > 0) {
      const utilisateurFinalCollectionIdentifiers = utilisateurFinalCollection.map(
        utilisateurFinalItem => getUtilisateurFinalIdentifier(utilisateurFinalItem)!
      );
      const utilisateurFinalsToAdd = utilisateurFinals.filter(utilisateurFinalItem => {
        const utilisateurFinalIdentifier = getUtilisateurFinalIdentifier(utilisateurFinalItem);
        if (utilisateurFinalIdentifier == null || utilisateurFinalCollectionIdentifiers.includes(utilisateurFinalIdentifier)) {
          return false;
        }
        utilisateurFinalCollectionIdentifiers.push(utilisateurFinalIdentifier);
        return true;
      });
      return [...utilisateurFinalsToAdd, ...utilisateurFinalCollection];
    }
    return utilisateurFinalCollection;
  }
}
