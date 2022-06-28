import { IBatiment } from 'app/entities/batiment/batiment.model';

export interface ITypeBatiment {
  id?: number;
  typeBa?: string;
  nomBatiment?: IBatiment | null;
}

export class TypeBatiment implements ITypeBatiment {
  constructor(public id?: number, public typeBa?: string, public nomBatiment?: IBatiment | null) {}
}

export function getTypeBatimentIdentifier(typeBatiment: ITypeBatiment): number | undefined {
  return typeBatiment.id;
}
