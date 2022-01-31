import * as dayjs from 'dayjs';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { TypeBon } from 'app/entities/enumerations/type-bon.model';

export interface IBon {
  id?: number;
  typeBon?: TypeBon;
  quantiteLivre?: number | null;
  quantiteCommande?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  etat?: boolean | null;
  references?: IEquipement[] | null;
}

export class Bon implements IBon {
  constructor(
    public id?: number,
    public typeBon?: TypeBon,
    public quantiteLivre?: number | null,
    public quantiteCommande?: number | null,
    public dateCreation?: dayjs.Dayjs | null,
    public etat?: boolean | null,
    public references?: IEquipement[] | null
  ) {
    this.etat = this.etat ?? false;
  }
}

export function getBonIdentifier(bon: IBon): number | undefined {
  return bon.id;
}
