import { IQuartier } from 'app/entities/quartier/quartier.model';
import { IEquipement } from 'app/entities/equipement/equipement.model';

export interface IMagazin {
  id?: number;
  nomMagazin?: string;
  surfaceBatie?: number;
  superficie?: number;
  idPers?: number;
  nomQuartier?: IQuartier | null;
  references?: IEquipement[] | null;
}

export class Magazin implements IMagazin {
  constructor(
    public id?: number,
    public nomMagazin?: string,
    public surfaceBatie?: number,
    public superficie?: number,
    public idPers?: number,
    public nomQuartier?: IQuartier | null,
    public references?: IEquipement[] | null
  ) {}
}

export function getMagazinIdentifier(magazin: IMagazin): number | undefined {
  return magazin.id;
}
