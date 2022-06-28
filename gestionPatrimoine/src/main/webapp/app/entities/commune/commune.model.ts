import { IDepartement } from 'app/entities/departement/departement.model';

export interface ICommune {
  id?: number;
  nomCommune?: string;
  nomDepartement?: IDepartement | null;
}

export class Commune implements ICommune {
  constructor(public id?: number, public nomCommune?: string, public nomDepartement?: IDepartement | null) {}
}

export function getCommuneIdentifier(commune: ICommune): number | undefined {
  return commune.id;
}
