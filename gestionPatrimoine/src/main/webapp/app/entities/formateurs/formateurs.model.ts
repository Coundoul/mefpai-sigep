import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { IFiliereStabilise } from 'app/entities/filiere-stabilise/filiere-stabilise.model';

export interface IFormateurs {
  id?: number;
  nomFormateur?: string;
  prenomFormateur?: string;
  email?: string;
  numb1?: string;
  numb2?: string | null;
  adresse?: string;
  ville?: string;
  specialite?: string;
  nomEtablissement?: IEtablissement | null;
  nomFilieres?: IFiliereStabilise[] | null;
}

export class Formateurs implements IFormateurs {
  constructor(
    public id?: number,
    public nomFormateur?: string,
    public prenomFormateur?: string,
    public email?: string,
    public numb1?: string,
    public numb2?: string | null,
    public adresse?: string,
    public ville?: string,
    public specialite?: string,
    public nomEtablissement?: IEtablissement | null,
    public nomFilieres?: IFiliereStabilise[] | null
  ) {}
}

export function getFormateursIdentifier(formateurs: IFormateurs): number | undefined {
  return formateurs.id;
}
