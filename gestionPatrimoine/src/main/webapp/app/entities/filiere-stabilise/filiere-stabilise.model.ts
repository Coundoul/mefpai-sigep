import { IFormateurs } from 'app/entities/formateurs/formateurs.model';

export interface IFiliereStabilise {
  id?: number;
  nomFiliere?: string;
  nomFormateur?: IFormateurs | null;
}

export class FiliereStabilise implements IFiliereStabilise {
  constructor(public id?: number, public nomFiliere?: string, public nomFormateur?: IFormateurs | null) {}
}

export function getFiliereStabiliseIdentifier(filiereStabilise: IFiliereStabilise): number | undefined {
  return filiereStabilise.id;
}
