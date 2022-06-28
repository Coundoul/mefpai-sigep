import { IQuartier } from 'app/entities/quartier/quartier.model';
import { IProjets } from 'app/entities/projets/projets.model';

export interface IEtablissement {
  id?: number;
  nomEtablissement?: string;
  adresse?: string;
  telephone?: string;
  email?: string;
  surfaceBatie?: number;
  superficie?: number;
  statusFoncier?: string;
  nombreApprenants?: number;
  proprietaire?: string;
  possibiliteExtension?: string;
  descriptionExtension?: string | null;
  branchementEau?: string;
  branchementElectricite?: string;
  puissanceSouscrite?: string;
  typeReseau?: string;
  circuitTerre?: string;
  protectionArret?: string;
  protectionFoudre?: string;
  connexionTel?: string;
  connexionInternet?: string;
  environnement?: string | null;
  dispositif?: string | null;
  idPers?: number;
  nomQuartier?: IQuartier | null;
  nomProjets?: IProjets[] | null;
}

export class Etablissement implements IEtablissement {
  constructor(
    public id?: number,
    public nomEtablissement?: string,
    public adresse?: string,
    public telephone?: string,
    public email?: string,
    public surfaceBatie?: number,
    public superficie?: number,
    public statusFoncier?: string,
    public nombreApprenants?: number,
    public proprietaire?: string,
    public possibiliteExtension?: string,
    public descriptionExtension?: string | null,
    public branchementEau?: string,
    public branchementElectricite?: string,
    public puissanceSouscrite?: string,
    public typeReseau?: string,
    public circuitTerre?: string,
    public protectionArret?: string,
    public protectionFoudre?: string,
    public connexionTel?: string,
    public connexionInternet?: string,
    public environnement?: string | null,
    public dispositif?: string | null,
    public idPers?: number,
    public nomQuartier?: IQuartier | null,
    public nomProjets?: IProjets[] | null
  ) {}
}

export function getEtablissementIdentifier(etablissement: IEtablissement): number | undefined {
  return etablissement.id;
}
