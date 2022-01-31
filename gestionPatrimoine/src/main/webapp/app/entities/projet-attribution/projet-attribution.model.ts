import * as dayjs from 'dayjs';
import { IProjets } from 'app/entities/projets/projets.model';

export interface IProjetAttribution {
  id?: number;
  dateAttribution?: dayjs.Dayjs | null;
  quantite?: number;
  idEquipement?: number;
  idPers?: number;
  nomProjet?: IProjets | null;
}

export class ProjetAttribution implements IProjetAttribution {
  constructor(
    public id?: number,
    public dateAttribution?: dayjs.Dayjs | null,
    public quantite?: number,
    public idEquipement?: number,
    public idPers?: number,
    public nomProjet?: IProjets | null
  ) {}
}

export function getProjetAttributionIdentifier(projetAttribution: IProjetAttribution): number | undefined {
  return projetAttribution.id;
}
