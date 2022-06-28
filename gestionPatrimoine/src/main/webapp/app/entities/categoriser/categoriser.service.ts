import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/core/request/request-util';
import { IEtatCategorie } from './categoriser.model';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

export type EntityResponseType = HttpResponse<IEtatCategorie>;
export type EntityArrayResponseType = HttpResponse<IEtatCategorie[]>;

@Injectable({ providedIn: 'root' })
export class CategoriserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/inventaires/categorie', 'gestionequipement');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  listEtatCategorieMatiere(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtatCategorie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
