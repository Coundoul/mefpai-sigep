import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFicheTechnique, getFicheTechniqueIdentifier } from '../fiche-technique.model';

export type EntityResponseType = HttpResponse<IFicheTechnique>;
export type EntityArrayResponseType = HttpResponse<IFicheTechnique[]>;

@Injectable({ providedIn: 'root' })
export class FicheTechniqueService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/fiche-techniques', 'gestionmaintenance');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(ficheTechnique: IFicheTechnique): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ficheTechnique);
    return this.http
      .post<IFicheTechnique>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ficheTechnique: IFicheTechnique): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ficheTechnique);
    return this.http
      .put<IFicheTechnique>(`${this.resourceUrl}/${getFicheTechniqueIdentifier(ficheTechnique) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(ficheTechnique: IFicheTechnique): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ficheTechnique);
    return this.http
      .patch<IFicheTechnique>(`${this.resourceUrl}/${getFicheTechniqueIdentifier(ficheTechnique) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFicheTechnique>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFicheTechnique[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFicheTechniqueToCollectionIfMissing(
    ficheTechniqueCollection: IFicheTechnique[],
    ...ficheTechniquesToCheck: (IFicheTechnique | null | undefined)[]
  ): IFicheTechnique[] {
    const ficheTechniques: IFicheTechnique[] = ficheTechniquesToCheck.filter(isPresent);
    if (ficheTechniques.length > 0) {
      const ficheTechniqueCollectionIdentifiers = ficheTechniqueCollection.map(
        ficheTechniqueItem => getFicheTechniqueIdentifier(ficheTechniqueItem)!
      );
      const ficheTechniquesToAdd = ficheTechniques.filter(ficheTechniqueItem => {
        const ficheTechniqueIdentifier = getFicheTechniqueIdentifier(ficheTechniqueItem);
        if (ficheTechniqueIdentifier == null || ficheTechniqueCollectionIdentifiers.includes(ficheTechniqueIdentifier)) {
          return false;
        }
        ficheTechniqueCollectionIdentifiers.push(ficheTechniqueIdentifier);
        return true;
      });
      return [...ficheTechniquesToAdd, ...ficheTechniqueCollection];
    }
    return ficheTechniqueCollection;
  }

  protected convertDateFromClient(ficheTechnique: IFicheTechnique): IFicheTechnique {
    return Object.assign({}, ficheTechnique, {
      dateDepot: ficheTechnique.dateDepot?.isValid() ? ficheTechnique.dateDepot.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDepot = res.body.dateDepot ? dayjs(res.body.dateDepot) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ficheTechnique: IFicheTechnique) => {
        ficheTechnique.dateDepot = ficheTechnique.dateDepot ? dayjs(ficheTechnique.dateDepot) : undefined;
      });
    }
    return res;
  }
}
