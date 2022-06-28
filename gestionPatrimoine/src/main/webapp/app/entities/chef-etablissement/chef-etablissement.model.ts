import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IChefEtablissement {
  id?: number;
  nomPers?: string;
  prenomPers?: string;
  sexe?: Sexe;
  mobile?: string;
  adresse?: string | null;
}

export class ChefEtablissement implements IChefEtablissement {
  constructor(
    public id?: number,
    public nomPers?: string,
    public prenomPers?: string,
    public sexe?: Sexe,
    public mobile?: string,
    public adresse?: string | null
  ) {}
}

export function getChefEtablissementIdentifier(chefEtablissement: IChefEtablissement): number | undefined {
  return chefEtablissement.id;
}
