import { ICorpsEtat } from 'app/entities/corps-etat/corps-etat.model';

export interface INatureFoncier {
  id?: number;
  typeFoncier?: string;
  pieceJointe?: string;
  nomCorps?: ICorpsEtat | null;
}

export class NatureFoncier implements INatureFoncier {
  constructor(public id?: number, public typeFoncier?: string, public pieceJointe?: string, public nomCorps?: ICorpsEtat | null) {}
}

export function getNatureFoncierIdentifier(natureFoncier: INatureFoncier): number | undefined {
  return natureFoncier.id;
}
