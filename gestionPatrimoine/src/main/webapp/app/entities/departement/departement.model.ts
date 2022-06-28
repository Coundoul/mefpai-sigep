import { IRegion } from 'app/entities/region/region.model';

export interface IDepartement {
  id?: number;
  nomDepartement?: string;
  nomRegion?: IRegion | null;
}

export class Departement implements IDepartement {
  constructor(public id?: number, public nomDepartement?: string, public nomRegion?: IRegion | null) {}
}

export function getDepartementIdentifier(departement: IDepartement): number | undefined {
  return departement.id;
}
