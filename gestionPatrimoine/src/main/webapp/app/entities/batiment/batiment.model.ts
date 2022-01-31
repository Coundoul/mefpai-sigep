import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { ICorpsEtat } from 'app/entities/corps-etat/corps-etat.model';
import { ITypeBatiment } from 'app/entities/type-batiment/type-batiment.model';
import { IAtelier } from 'app/entities/atelier/atelier.model';

export interface IBatiment {
  id?: number;
  nomBatiment?: string;
  nbrPiece?: string;
  designation?: string;
  surface?: number;
  etatGeneral?: boolean;
  description?: string | null;
  nombreSalle?: number;
  nomEtablissement?: IEtablissement | null;
  nomCorps?: ICorpsEtat | null;
  typeBas?: ITypeBatiment[] | null;
  nomAteliers?: IAtelier[] | null;
}

export class Batiment implements IBatiment {
  constructor(
    public id?: number,
    public nomBatiment?: string,
    public nbrPiece?: string,
    public designation?: string,
    public surface?: number,
    public etatGeneral?: boolean,
    public description?: string | null,
    public nombreSalle?: number,
    public nomEtablissement?: IEtablissement | null,
    public nomCorps?: ICorpsEtat | null,
    public typeBas?: ITypeBatiment[] | null,
    public nomAteliers?: IAtelier[] | null
  ) {
    this.etatGeneral = this.etatGeneral ?? false;
  }
}

export function getBatimentIdentifier(batiment: IBatiment): number | undefined {
  return batiment.id;
}
