import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';

export interface IChefProjet {
  id?: number;
  nomPers?: string;
  prenomPers?: string;
  sexe?: Sexe;
  mobile?: string;
  adresse?: string | null;
  direction?: Direction | null;
}

export class ChefProjet implements IChefProjet {
  constructor(
    public id?: number,
    public nomPers?: string,
    public prenomPers?: string,
    public sexe?: Sexe,
    public mobile?: string,
    public adresse?: string | null,
    public direction?: Direction | null
  ) {}
}

export function getChefProjetIdentifier(chefProjet: IChefProjet): number | undefined {
  return chefProjet.id;
}
