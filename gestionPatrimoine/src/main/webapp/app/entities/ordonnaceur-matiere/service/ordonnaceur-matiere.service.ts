import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrdonnaceurMatiere, getOrdonnaceurMatiereIdentifier } from '../ordonnaceur-matiere.model';

export type EntityResponseType = HttpResponse<IOrdonnaceurMatiere>;
export type EntityArrayResponseType = HttpResponse<IOrdonnaceurMatiere[]>;

@Injectable({ providedIn: 'root' })
export class OrdonnaceurMatiereService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/ordonnaceur-matieres');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(ordonnaceurMatiere: IOrdonnaceurMatiere): Observable<EntityResponseType> {
    return this.http.post<IOrdonnaceurMatiere>(this.resourceUrl, ordonnaceurMatiere, { observe: 'response' });
  }

  update(ordonnaceurMatiere: IOrdonnaceurMatiere): Observable<EntityResponseType> {
    return this.http.put<IOrdonnaceurMatiere>(
      `${this.resourceUrl}/${getOrdonnaceurMatiereIdentifier(ordonnaceurMatiere) as number}`,
      ordonnaceurMatiere,
      { observe: 'response' }
    );
  }

  partialUpdate(ordonnaceurMatiere: IOrdonnaceurMatiere): Observable<EntityResponseType> {
    return this.http.patch<IOrdonnaceurMatiere>(
      `${this.resourceUrl}/${getOrdonnaceurMatiereIdentifier(ordonnaceurMatiere) as number}`,
      ordonnaceurMatiere,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrdonnaceurMatiere>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrdonnaceurMatiere[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrdonnaceurMatiereToCollectionIfMissing(
    ordonnaceurMatiereCollection: IOrdonnaceurMatiere[],
    ...ordonnaceurMatieresToCheck: (IOrdonnaceurMatiere | null | undefined)[]
  ): IOrdonnaceurMatiere[] {
    const ordonnaceurMatieres: IOrdonnaceurMatiere[] = ordonnaceurMatieresToCheck.filter(isPresent);
    if (ordonnaceurMatieres.length > 0) {
      const ordonnaceurMatiereCollectionIdentifiers = ordonnaceurMatiereCollection.map(
        ordonnaceurMatiereItem => getOrdonnaceurMatiereIdentifier(ordonnaceurMatiereItem)!
      );
      const ordonnaceurMatieresToAdd = ordonnaceurMatieres.filter(ordonnaceurMatiereItem => {
        const ordonnaceurMatiereIdentifier = getOrdonnaceurMatiereIdentifier(ordonnaceurMatiereItem);
        if (ordonnaceurMatiereIdentifier == null || ordonnaceurMatiereCollectionIdentifiers.includes(ordonnaceurMatiereIdentifier)) {
          return false;
        }
        ordonnaceurMatiereCollectionIdentifiers.push(ordonnaceurMatiereIdentifier);
        return true;
      });
      return [...ordonnaceurMatieresToAdd, ...ordonnaceurMatiereCollection];
    }
    return ordonnaceurMatiereCollection;
  }
}
