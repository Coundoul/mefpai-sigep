import { IResponsable } from 'app/entities/responsable/responsable.model';

export interface ICorpsEtat {
  id?: number;
  nomCorps?: string;
  grosOeuvre?: string;
  descriptionGrosOeuvre?: string;
  secondOeuvre?: string;
  descriptionSecondOeuvre?: string;
  oservation?: boolean;
  etatCorps?: boolean | null;
  nomResponsable?: IResponsable | null;
}

export class CorpsEtat implements ICorpsEtat {
  constructor(
    public id?: number,
    public nomCorps?: string,
    public grosOeuvre?: string,
    public descriptionGrosOeuvre?: string,
    public secondOeuvre?: string,
    public descriptionSecondOeuvre?: string,
    public oservation?: boolean,
    public etatCorps?: boolean | null,
    public nomResponsable?: IResponsable | null
  ) {
    this.oservation = this.oservation ?? false;
    this.etatCorps = this.etatCorps ?? false;
  }
}

export function getCorpsEtatIdentifier(corpsEtat: ICorpsEtat): number | undefined {
  return corpsEtat.id;
}
