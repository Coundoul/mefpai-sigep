import { IFiliereStabilise } from 'app/entities/filiere-stabilise/filiere-stabilise.model';
import { IBatiment } from 'app/entities/batiment/batiment.model';

export interface IAtelier {
  id?: number;
  nomAtelier?: string;
  surface?: number;
  description?: string;
  nomFiliere?: IFiliereStabilise | null;
  nomBatiment?: IBatiment | null;
}

export class Atelier implements IAtelier {
  constructor(
    public id?: number,
    public nomAtelier?: string,
    public surface?: number,
    public description?: string,
    public nomFiliere?: IFiliereStabilise | null,
    public nomBatiment?: IBatiment | null
  ) {}
}

export function getAtelierIdentifier(atelier: IAtelier): number | undefined {
  return atelier.id;
}
