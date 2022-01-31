import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMangasinierFichiste, getMangasinierFichisteIdentifier } from '../mangasinier-fichiste.model';

export type EntityResponseType = HttpResponse<IMangasinierFichiste>;
export type EntityArrayResponseType = HttpResponse<IMangasinierFichiste[]>;

@Injectable({ providedIn: 'root' })
export class MangasinierFichisteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/mangasinier-fichistes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(mangasinierFichiste: IMangasinierFichiste): Observable<EntityResponseType> {
    return this.http.post<IMangasinierFichiste>(this.resourceUrl, mangasinierFichiste, { observe: 'response' });
  }

  update(mangasinierFichiste: IMangasinierFichiste): Observable<EntityResponseType> {
    return this.http.put<IMangasinierFichiste>(
      `${this.resourceUrl}/${getMangasinierFichisteIdentifier(mangasinierFichiste) as number}`,
      mangasinierFichiste,
      { observe: 'response' }
    );
  }

  partialUpdate(mangasinierFichiste: IMangasinierFichiste): Observable<EntityResponseType> {
    return this.http.patch<IMangasinierFichiste>(
      `${this.resourceUrl}/${getMangasinierFichisteIdentifier(mangasinierFichiste) as number}`,
      mangasinierFichiste,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMangasinierFichiste>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMangasinierFichiste[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMangasinierFichisteToCollectionIfMissing(
    mangasinierFichisteCollection: IMangasinierFichiste[],
    ...mangasinierFichistesToCheck: (IMangasinierFichiste | null | undefined)[]
  ): IMangasinierFichiste[] {
    const mangasinierFichistes: IMangasinierFichiste[] = mangasinierFichistesToCheck.filter(isPresent);
    if (mangasinierFichistes.length > 0) {
      const mangasinierFichisteCollectionIdentifiers = mangasinierFichisteCollection.map(
        mangasinierFichisteItem => getMangasinierFichisteIdentifier(mangasinierFichisteItem)!
      );
      const mangasinierFichistesToAdd = mangasinierFichistes.filter(mangasinierFichisteItem => {
        const mangasinierFichisteIdentifier = getMangasinierFichisteIdentifier(mangasinierFichisteItem);
        if (mangasinierFichisteIdentifier == null || mangasinierFichisteCollectionIdentifiers.includes(mangasinierFichisteIdentifier)) {
          return false;
        }
        mangasinierFichisteCollectionIdentifiers.push(mangasinierFichisteIdentifier);
        return true;
      });
      return [...mangasinierFichistesToAdd, ...mangasinierFichisteCollection];
    }
    return mangasinierFichisteCollection;
  }
}
