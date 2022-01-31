import { IProjets } from 'app/entities/projets/projets.model';
import { TypeMaitre } from 'app/entities/enumerations/type-maitre.model';

export interface IIntervenant {
  id?: number;
  nomIntervenant?: string;
  prenomIntervenant?: string;
  emailProfessionnel?: string;
  raisonSocial?: string;
  maitre?: TypeMaitre;
  role?: string;
  nomProjet?: IProjets | null;
}

export class Intervenant implements IIntervenant {
  constructor(
    public id?: number,
    public nomIntervenant?: string,
    public prenomIntervenant?: string,
    public emailProfessionnel?: string,
    public raisonSocial?: string,
    public maitre?: TypeMaitre,
    public role?: string,
    public nomProjet?: IProjets | null
  ) {}
}

export function getIntervenantIdentifier(intervenant: IIntervenant): number | undefined {
  return intervenant.id;
}
