import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { ICorpsEtat } from 'app/entities/corps-etat/corps-etat.model';
import { ITypeBatiment } from 'app/entities/type-batiment/type-batiment.model';
import { IAtelier } from 'app/entities/atelier/atelier.model';
import * as dayjs from 'dayjs';

export interface IBatiment {
  id?: number;
  designation?: string;
  nbrPiece?: number;
  surface?: number;
  sourceFinancement?: string;
  photoContentType?: string;
  photo?: string;
  dateSignalisation?: dayjs.Dayjs | null;
  photoSignalisationGrosOeuvreContentType?: string;
  photoSignalisationGrosOeuvre?: string;
  photoSignalisationSecondOeuvreContentType?: string;
  photoSignalisationSecondOeuvre?: string;
  etatGrosOeuvre?: string;
  etatSecondOeuvre?: string;
  observation?: string;
  descriptionSignalisation?: string;
  nomEtablissement?: IEtablissement | null;
  nomCorps?: ICorpsEtat | null;
  typeBas?: ITypeBatiment[] | null;
  nomAteliers?: IAtelier[] | null;
}

export class Batiment implements IBatiment {
  constructor(
    public id?: number,
    public designation?: string,
    public nbrPiece?: number,
    public surface?: number,
    public sourceFinancement?: string,
    public photoContentType?: string,
    public photo?: string,
    public etatGrosOeuvre?: string,
    public etatSecondOeuvre?: string,
    public observation?: string,
    public nomEtablissement?: IEtablissement | null,
    public nomCorps?: ICorpsEtat | null,
    public typeBas?: ITypeBatiment[] | null,
    public nomAteliers?: IAtelier[] | null
  ) {}
}

export function getBatimentIdentifier(batiment: IBatiment): number | undefined {
  return batiment.id;
}

export function getBatimentEtablissementIdentifier(etablissement: IEtablissement): number | undefined {
  return etablissement.id;
}