import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategorieMatiere, getCategorieMatiereIdentifier } from '../categorie-matiere.model';

export type EntityResponseType = HttpResponse<ICategorieMatiere>;
export type EntityArrayResponseType = HttpResponse<ICategorieMatiere[]>;

@Injectable({ providedIn: 'root' })
export class CategorieMatiereService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/categorie-matieres', 'gestionequipement');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(categorieMatiere: ICategorieMatiere): Observable<EntityResponseType> {
    return this.http.post<ICategorieMatiere>(this.resourceUrl, categorieMatiere, { observe: 'response' });
  }

  update(categorieMatiere: ICategorieMatiere): Observable<EntityResponseType> {
    return this.http.put<ICategorieMatiere>(
      `${this.resourceUrl}/${getCategorieMatiereIdentifier(categorieMatiere) as number}`,
      categorieMatiere,
      { observe: 'response' }
    );
  }

  partialUpdate(categorieMatiere: ICategorieMatiere): Observable<EntityResponseType> {
    return this.http.patch<ICategorieMatiere>(
      `${this.resourceUrl}/${getCategorieMatiereIdentifier(categorieMatiere) as number}`,
      categorieMatiere,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategorieMatiere>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategorieMatiere[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCategorieMatiereToCollectionIfMissing(
    categorieMatiereCollection: ICategorieMatiere[],
    ...categorieMatieresToCheck: (ICategorieMatiere | null | undefined)[]
  ): ICategorieMatiere[] {
    const categorieMatieres: ICategorieMatiere[] = categorieMatieresToCheck.filter(isPresent);
    if (categorieMatieres.length > 0) {
      const categorieMatiereCollectionIdentifiers = categorieMatiereCollection.map(
        categorieMatiereItem => getCategorieMatiereIdentifier(categorieMatiereItem)!
      );
      const categorieMatieresToAdd = categorieMatieres.filter(categorieMatiereItem => {
        const categorieMatiereIdentifier = getCategorieMatiereIdentifier(categorieMatiereItem);
        if (categorieMatiereIdentifier == null || categorieMatiereCollectionIdentifiers.includes(categorieMatiereIdentifier)) {
          return false;
        }
        categorieMatiereCollectionIdentifiers.push(categorieMatiereIdentifier);
        return true;
      });
      return [...categorieMatieresToAdd, ...categorieMatiereCollection];
    }
    return categorieMatiereCollection;
  }
}
