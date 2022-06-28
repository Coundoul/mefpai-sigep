import * as dayjs from 'dayjs';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { IAttribution } from 'app/entities/attribution/attribution.model';
import { Type } from 'app/entities/enumerations/type.model';

export interface IAffectations {
  id?: number;
  quantiteAffecter?: number;
  typeAttribution?: Type;
  beneficiaire?: string | null;
  idPers?: number;
  dateAttribution?: dayjs.Dayjs | null;
  equipement?: IEquipement | null;
  attributions?: IAttribution[] | null;
}

export class Affectations implements IAffectations {
  constructor(
    public id?: number,
    public quantiteAffecter?: number,
    public typeAttribution?: Type,
    public beneficiaire?: string | null,
    public idPers?: number,
    public dateAttribution?: dayjs.Dayjs | null,
    public equipement?: IEquipement | null,
    public attributions?: IAttribution[] | null
  ) {}
}

export function getAffectationsIdentifier(affectations: IAffectations): number | undefined {
  return affectations.id;
}
