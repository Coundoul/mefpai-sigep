import { IBatiment } from 'app/entities/batiment/batiment.model';
import { Classe } from 'app/entities/enumerations/classe.model';

export interface ISalles {
  id?: number;
  nomSalle?: string;
  classe?: Classe;
  nomBatiment?: IBatiment | null;
}

export class Salles implements ISalles {
  constructor(public id?: number, public nomSalle?: string, public classe?: Classe, public nomBatiment?: IBatiment | null) {}
}

export function getSallesIdentifier(salles: ISalles): number | undefined {
  return salles.id;
}
