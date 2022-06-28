import { IComptablePrincipale } from 'app/entities/comptable-principale/comptable-principale.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';

export interface IComptableSecondaire {
  id?: number;
  nomPers?: string;
  prenomPers?: string;
  sexe?: Sexe;
  mobile?: string;
  adresse?: string | null;
  direction?: Direction;
  comptablePrincipale?: IComptablePrincipale | null;
}

export class ComptableSecondaire implements IComptableSecondaire {
  constructor(
    public id?: number,
    public nomPers?: string,
    public prenomPers?: string,
    public sexe?: Sexe,
    public mobile?: string,
    public adresse?: string | null,
    public direction?: Direction,
    public comptablePrincipale?: IComptablePrincipale | null
  ) {}
}

export function getComptableSecondaireIdentifier(comptableSecondaire: IComptableSecondaire): number | undefined {
  return comptableSecondaire.id;
}
