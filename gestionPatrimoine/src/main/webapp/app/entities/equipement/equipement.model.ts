import * as dayjs from 'dayjs';
import { IMagazin } from 'app/entities/magazin/magazin.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { IBon } from 'app/entities/bon/bon.model';
import { ICategorieMatiere } from 'app/entities/categorie-matiere/categorie-matiere.model';

export interface IEquipement {
  [x: string]: any;
  id?: number;
  reference?: string;
  nomMatiere?: string | null;
  caracteristique?: string | null;
  description?: string | null;
  typeMatiere?: string;
  quantite?: number;
  etatMatiere?: string | null;
  dateSignalisation?: dayjs.Dayjs | null;
  group?: boolean;
  photoContentType?: string | null;
  photo1ContentType?: string | null;
  photo2ContentType?: string | null;
  photo?: string | null;
  photo1?: string | null;
  photo2?: string | null;
  nomMagazin?: IMagazin | null;
  nomFournisseur?: IFournisseur | null;
  bon?: IBon | null;
  categorie?: ICategorieMatiere | null;
}

export class Equipement implements IEquipement {
  constructor(
    public id?: number,
    public reference?: string,
    public nomMatiere?: string | null,
    public caracteristique?: string | null,
    public description?: string | null,
    public typeMatiere?: string,
    public quantite?: number,
    public etatMatiere?: string | null,
    public dateSignalisation?: dayjs.Dayjs | null,
    public group?: boolean,
    public photoContentType?: string | null,
    public photo1ContentType?: string | null,
    public photo2ContentType?: string | null,
    public photo?: string | null,
    public photo1?: string | null,
    public photo2?: string | null,
    public nomMagazin?: IMagazin | null,
    public nomFournisseur?: IFournisseur | null,
    public bon?: IBon | null,
    public categorie?: ICategorieMatiere | null
  ) {
    this.group = this.group ?? false;
  }
  [x: string]: any;
}

export function getEquipementIdentifier(equipement: IEquipement): number | undefined {
  return equipement.id;
}
