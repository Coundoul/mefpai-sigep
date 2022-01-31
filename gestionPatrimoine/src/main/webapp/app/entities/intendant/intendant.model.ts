import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IIntendant {
  id?: number;
  nomPers?: string;
  prenomPers?: string;
  sexe?: Sexe;
  mobile?: string;
  adresse?: string | null;
}

export class Intendant implements IIntendant {
  constructor(
    public id?: number,
    public nomPers?: string,
    public prenomPers?: string,
    public sexe?: Sexe,
    public mobile?: string,
    public adresse?: string | null
  ) {}
}

export function getIntendantIdentifier(intendant: IIntendant): number | undefined {
  return intendant.id;
}
