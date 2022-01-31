import { ICommune } from 'app/entities/commune/commune.model';

export interface IQuartier {
  id?: number;
  nomQuartier?: string;
  nomCommune?: ICommune | null;
}

export class Quartier implements IQuartier {
  constructor(public id?: number, public nomQuartier?: string, public nomCommune?: ICommune | null) {}
}

export function getQuartierIdentifier(quartier: IQuartier): number | undefined {
  return quartier.id;
}
