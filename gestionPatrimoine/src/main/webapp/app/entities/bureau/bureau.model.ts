import { NomS } from 'app/entities/enumerations/nom-s.model';
import { Direction } from 'app/entities/enumerations/direction.model';

export interface IBureau {
  id?: number;
  nomStructure?: NomS;
  direction?: Direction | null;
  nomEtablissement?: string | null;
}

export class Bureau implements IBureau {
  constructor(
    public id?: number,
    public nomStructure?: NomS,
    public direction?: Direction | null,
    public nomEtablissement?: string | null
  ) {}
}

export function getBureauIdentifier(bureau: IBureau): number | undefined {
  return bureau.id;
}
