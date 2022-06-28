import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFiliereStabilise, getFiliereStabiliseIdentifier } from '../filiere-stabilise.model';

export type EntityResponseType = HttpResponse<IFiliereStabilise>;
export type EntityArrayResponseType = HttpResponse<IFiliereStabilise[]>;

@Injectable({ providedIn: 'root' })
export class FiliereStabiliseService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/filiere-stabilises');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(filiereStabilise: IFiliereStabilise): Observable<EntityResponseType> {
    return this.http.post<IFiliereStabilise>(this.resourceUrl, filiereStabilise, { observe: 'response' });
  }

  update(filiereStabilise: IFiliereStabilise): Observable<EntityResponseType> {
    return this.http.put<IFiliereStabilise>(
      `${this.resourceUrl}/${getFiliereStabiliseIdentifier(filiereStabilise) as number}`,
      filiereStabilise,
      { observe: 'response' }
    );
  }

  partialUpdate(filiereStabilise: IFiliereStabilise): Observable<EntityResponseType> {
    return this.http.patch<IFiliereStabilise>(
      `${this.resourceUrl}/${getFiliereStabiliseIdentifier(filiereStabilise) as number}`,
      filiereStabilise,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFiliereStabilise>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFiliereStabilise[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFiliereStabiliseToCollectionIfMissing(
    filiereStabiliseCollection: IFiliereStabilise[],
    ...filiereStabilisesToCheck: (IFiliereStabilise | null | undefined)[]
  ): IFiliereStabilise[] {
    const filiereStabilises: IFiliereStabilise[] = filiereStabilisesToCheck.filter(isPresent);
    if (filiereStabilises.length > 0) {
      const filiereStabiliseCollectionIdentifiers = filiereStabiliseCollection.map(
        filiereStabiliseItem => getFiliereStabiliseIdentifier(filiereStabiliseItem)!
      );
      const filiereStabilisesToAdd = filiereStabilises.filter(filiereStabiliseItem => {
        const filiereStabiliseIdentifier = getFiliereStabiliseIdentifier(filiereStabiliseItem);
        if (filiereStabiliseIdentifier == null || filiereStabiliseCollectionIdentifiers.includes(filiereStabiliseIdentifier)) {
          return false;
        }
        filiereStabiliseCollectionIdentifiers.push(filiereStabiliseIdentifier);
        return true;
      });
      return [...filiereStabilisesToAdd, ...filiereStabiliseCollection];
    }
    return filiereStabiliseCollection;
  }
}
