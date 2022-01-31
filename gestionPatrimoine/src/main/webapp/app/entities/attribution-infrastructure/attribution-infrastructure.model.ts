import * as dayjs from 'dayjs';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';

export interface IAttributionInfrastructure {
  id?: number;
  dateAttribution?: dayjs.Dayjs | null;
  quantite?: number;
  idEquipement?: number;
  idPers?: number;
  nomEtablissement?: IEtablissement | null;
}

export class AttributionInfrastructure implements IAttributionInfrastructure {
  constructor(
    public id?: number,
    public dateAttribution?: dayjs.Dayjs | null,
    public quantite?: number,
    public idEquipement?: number,
    public idPers?: number,
    public nomEtablissement?: IEtablissement | null
  ) {}
}

export function getAttributionInfrastructureIdentifier(attributionInfrastructure: IAttributionInfrastructure): number | undefined {
  return attributionInfrastructure.id;
}
