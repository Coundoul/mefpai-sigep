import { IBon } from 'app/entities/bon/bon.model';

export interface IDetailSortie {
  id?: number;
  pieceJointe?: string;
  idPers?: number;
  typeBon?: IBon | null;
}

export class DetailSortie implements IDetailSortie {
  constructor(public id?: number, public pieceJointe?: string, public idPers?: number, public typeBon?: IBon | null) {}
}

export function getDetailSortieIdentifier(detailSortie: IDetailSortie): number | undefined {
  return detailSortie.id;
}
