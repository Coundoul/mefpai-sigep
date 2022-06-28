import * as dayjs from 'dayjs';
import { IContratProjet } from 'app/entities/contrat-projet/contrat-projet.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { IIntervenant } from 'app/entities/intervenant/intervenant.model';
import { TypeProjet } from 'app/entities/enumerations/type-projet.model';

export interface IProjets {
  id?: number;
  typeProjet?: TypeProjet;
  nomProjet?: string;
  dateDebut?: dayjs.Dayjs;
  dateFin?: dayjs.Dayjs;
  description?: string;
  extension?: boolean;
  nom?: IContratProjet | null;
  nomEtablissement?: IEtablissement | null;
  nomBatiment?: IEtablissement | null;
  nomIntervenants?: IIntervenant[] | null;
}

export class Projets implements IProjets {
  constructor(
    public id?: number,
    public typeProjet?: TypeProjet,
    public nomProjet?: string,
    public dateDebut?: dayjs.Dayjs,
    public dateFin?: dayjs.Dayjs,
    public description?: string,
    public extension?: boolean,
    public nom?: IContratProjet | null,
    public nomEtablissement?: IEtablissement | null,
    public nomBatiment?: IEtablissement | null,
    public nomIntervenants?: IIntervenant[] | null
  ) {
    this.extension = this.extension ?? false;
  }
}

export function getProjetsIdentifier(projets: IProjets): number | undefined {
  return projets.id;
}
