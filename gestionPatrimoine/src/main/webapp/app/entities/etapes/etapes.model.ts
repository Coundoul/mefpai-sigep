import * as dayjs from 'dayjs';
import { IProjets } from 'app/entities/projets/projets.model';

export interface IEtapes {
  id?: number;
  dateDebut?: dayjs.Dayjs;
  dateFin?: dayjs.Dayjs;
  nomTache?: string;
  duration?: string | null;
  nomProjet?: IProjets | null;
}

export class Etapes implements IEtapes {
  constructor(
    public id?: number,
    public dateDebut?: dayjs.Dayjs,
    public dateFin?: dayjs.Dayjs,
    public nomTache?: string,
    public duration?: string | null,
    public nomProjet?: IProjets | null
  ) {}
}

export function getEtapesIdentifier(etapes: IEtapes): number | undefined {
  return etapes.id;
}
