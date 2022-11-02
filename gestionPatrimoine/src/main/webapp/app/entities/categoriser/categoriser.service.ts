import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/core/request/request-util';
import { IEtatCategorie } from './categoriser.model';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

export type EntityResponseType = HttpResponse<IEtatCategorie>;
export type EntityArrayResponseType = HttpResponse<IEtatCategorie[]>;

@Injectable({ providedIn: 'root' })
export class CategoriserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/inventaires/categorie', 'gestionequipement');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  listEtatCategorieMatiere(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEtatCategorie[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(etatCategorie: IEtatCategorie): IEtatCategorie {
    return Object.assign({}, etatCategorie, {
      dateAttribution: etatCategorie.dateSignalisation?.isValid() ? etatCategorie.dateSignalisation.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateSignalisation = res.body.dateSignalisation ? dayjs(res.body.dateSignalisation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((etatCategorie: IEtatCategorie) => {
        etatCategorie.dateSignalisation = etatCategorie.dateSignalisation ? dayjs(etatCategorie.dateSignalisation) : undefined;
      });
    }
    return res;
  }
}
