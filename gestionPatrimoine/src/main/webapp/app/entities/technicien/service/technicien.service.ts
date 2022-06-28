import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechnicien, getTechnicienIdentifier } from '../technicien.model';

export type EntityResponseType = HttpResponse<ITechnicien>;
export type EntityArrayResponseType = HttpResponse<ITechnicien[]>;

@Injectable({ providedIn: 'root' })
export class TechnicienService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/techniciens');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(technicien: ITechnicien): Observable<EntityResponseType> {
    return this.http.post<ITechnicien>(this.resourceUrl, technicien, { observe: 'response' });
  }

  update(technicien: ITechnicien): Observable<EntityResponseType> {
    return this.http.put<ITechnicien>(`${this.resourceUrl}/${getTechnicienIdentifier(technicien) as number}`, technicien, {
      observe: 'response',
    });
  }

  partialUpdate(technicien: ITechnicien): Observable<EntityResponseType> {
    return this.http.patch<ITechnicien>(`${this.resourceUrl}/${getTechnicienIdentifier(technicien) as number}`, technicien, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechnicien>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechnicien[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTechnicienToCollectionIfMissing(
    technicienCollection: ITechnicien[],
    ...techniciensToCheck: (ITechnicien | null | undefined)[]
  ): ITechnicien[] {
    const techniciens: ITechnicien[] = techniciensToCheck.filter(isPresent);
    if (techniciens.length > 0) {
      const technicienCollectionIdentifiers = technicienCollection.map(technicienItem => getTechnicienIdentifier(technicienItem)!);
      const techniciensToAdd = techniciens.filter(technicienItem => {
        const technicienIdentifier = getTechnicienIdentifier(technicienItem);
        if (technicienIdentifier == null || technicienCollectionIdentifiers.includes(technicienIdentifier)) {
          return false;
        }
        technicienCollectionIdentifiers.push(technicienIdentifier);
        return true;
      });
      return [...techniciensToAdd, ...technicienCollection];
    }
    return technicienCollection;
  }
}
