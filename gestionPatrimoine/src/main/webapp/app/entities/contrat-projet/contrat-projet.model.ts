import { IProjets } from 'app/entities/projets/projets.model';

export interface IContratProjet {
  id?: number;
  nom?: string;
  nomProjet?: IProjets | null;
}

export class ContratProjet implements IContratProjet {
  constructor(public id?: number, public nom?: string, public nomProjet?: IProjets | null) {}
}

export function getContratProjetIdentifier(contratProjet: IContratProjet): number | undefined {
  return contratProjet.id;
}
