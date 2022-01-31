import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';

export interface IDirecteur {
  id?: number;
  nomPers?: string;
  prenomPers?: string;
  sexe?: Sexe;
  mobile?: string;
  adresse?: string | null;
  direction?: Direction;
}

export class Directeur implements IDirecteur {
  constructor(
    public id?: number,
    public nomPers?: string,
    public prenomPers?: string,
    public sexe?: Sexe,
    public mobile?: string,
    public adresse?: string | null,
    public direction?: Direction
  ) {}
}

export function getDirecteurIdentifier(directeur: IDirecteur): number | undefined {
  return directeur.id;
}
