import * as dayjs from "dayjs";

export interface IEtatCategorie {
  reference?: string;
  categorie?: string;
  beneficiaire?: string;
  nomMatiere?: string;
  caracteristique?: string;
  dateSignalisation?: dayjs.Dayjs | null,
  etat_matiere?: string;
  photo?: string;
  photo_content_type?: string;
}

export class EtatCategorie implements IEtatCategorie {
  constructor(
    public categorie?: string,
    public reference?: string,
    public nomMatiere?: string,
    public caracteristique?: string,
    public beneficiaire?: string,
    public etat_matiere?: string,
    public photo?: string,
    public photo_content_type?: string,
    public dateSignalisation?: dayjs.Dayjs | null,
  ) {}
}

export function getEtatCategorieMatiere(etat: IEtatCategorie): string | undefined {
  return etat.categorie;
}
