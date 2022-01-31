import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBureau, getBureauIdentifier } from '../bureau.model';

export type EntityResponseType = HttpResponse<IBureau>;
export type EntityArrayResponseType = HttpResponse<IBureau[]>;

@Injectable({ providedIn: 'root' })
export class BureauService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/bureaus', 'gestionmaintenance');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(bureau: IBureau): Observable<EntityResponseType> {
    return this.http.post<IBureau>(this.resourceUrl, bureau, { observe: 'response' });
  }

  update(bureau: IBureau): Observable<EntityResponseType> {
    return this.http.put<IBureau>(`${this.resourceUrl}/${getBureauIdentifier(bureau) as number}`, bureau, { observe: 'response' });
  }

  partialUpdate(bureau: IBureau): Observable<EntityResponseType> {
    return this.http.patch<IBureau>(`${this.resourceUrl}/${getBureauIdentifier(bureau) as number}`, bureau, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBureau>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBureau[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBureauToCollectionIfMissing(bureauCollection: IBureau[], ...bureausToCheck: (IBureau | null | undefined)[]): IBureau[] {
    const bureaus: IBureau[] = bureausToCheck.filter(isPresent);
    if (bureaus.length > 0) {
      const bureauCollectionIdentifiers = bureauCollection.map(bureauItem => getBureauIdentifier(bureauItem)!);
      const bureausToAdd = bureaus.filter(bureauItem => {
        const bureauIdentifier = getBureauIdentifier(bureauItem);
        if (bureauIdentifier == null || bureauCollectionIdentifiers.includes(bureauIdentifier)) {
          return false;
        }
        bureauCollectionIdentifiers.push(bureauIdentifier);
        return true;
      });
      return [...bureausToAdd, ...bureauCollection];
    }
    return bureauCollection;
  }
}
