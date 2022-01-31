import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetailInventaire, IInventaire } from './inventaire.model';


export type EntityResponseType = HttpResponse<IInventaire>;
export type EntityResponseTypeDetail = HttpResponse<IDetailInventaire>;
export type EntityArrayResponseType = HttpResponse<IInventaire[]>;
export type EntityArrayResponseTypeDetail = HttpResponse<IDetailInventaire[]>;

@Injectable({ providedIn: 'root' })
export class InventaireService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/inventaires', 'gestionequipement');

  public resourceUrlDetail = this.applicationConfigService.getEndpointFor('api/inventaires', 'gestionequipement');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  listtableauInventaire(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInventaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  detail(reference: string): Observable<EntityArrayResponseTypeDetail> {
    const options = createRequestOption(reference);
    return this.http.get<IDetailInventaire[]>(`${this.resourceUrlDetail}/${reference}`, { params: options, observe: 'response' });
  }
}
