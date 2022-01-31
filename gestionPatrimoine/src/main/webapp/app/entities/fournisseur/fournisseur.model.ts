import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IFournisseur {
  id?: number;
  codeFournisseuer?: string;
  nomFournisseur?: string;
  prenomFournisseur?: string;
  sexe?: Sexe;
  raisonSocial?: string;
  adresse?: string;
  num1?: string;
  num2?: string | null;
  ville?: string;
  email?: string;
}

export class Fournisseur implements IFournisseur {
  constructor(
    public id?: number,
    public codeFournisseuer?: string,
    public nomFournisseur?: string,
    public prenomFournisseur?: string,
    public sexe?: Sexe,
    public raisonSocial?: string,
    public adresse?: string,
    public num1?: string,
    public num2?: string | null,
    public ville?: string,
    public email?: string
  ) {}
}

export function getFournisseurIdentifier(fournisseur: IFournisseur): number | undefined {
  return fournisseur.id;
}
