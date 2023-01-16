import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEquipement, getEquipementIdentifier } from 'app/entities/equipement/equipement.model';


export type EntityResponseType = HttpResponse<IEquipement>;
export type EntityArrayResponseType = HttpResponse<IEquipement[]>;

@Injectable({ providedIn: 'root' })
export class MatieresMaintenancesService {

  public resource = this.applicationConfigService.getEndpointFor('api/equipements', 'gestionequipement');

  public resourceUrl = this.applicationConfigService.getEndpointFor('api/equipements/EtatMatieres', 'gestionequipement');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEquipement>(`${this.resource}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEquipement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  addEquipementToCollectionIfMissing(
    equipementCollection: IEquipement[],
    ...equipementsToCheck: (IEquipement | null | undefined)[]
  ): IEquipement[] {
    const equipements: IEquipement[] = equipementsToCheck.filter(isPresent);
    if (equipements.length > 0) {
      const equipementCollectionIdentifiers = equipementCollection.map(equipementItem => getEquipementIdentifier(equipementItem)!);
      const equipementsToAdd = equipements.filter(equipementItem => {
        const equipementIdentifier = getEquipementIdentifier(equipementItem);
        if (equipementIdentifier == null || equipementCollectionIdentifiers.includes(equipementIdentifier)) {
          return false;
        }
        equipementCollectionIdentifiers.push(equipementIdentifier);
        return true;
      });
      return [...equipementsToAdd, ...equipementCollection];
    }
    return equipementCollection;
  }
}


