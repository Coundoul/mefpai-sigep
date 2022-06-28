import * as dayjs from 'dayjs';
import { IResponsable } from 'app/entities/responsable/responsable.model';

export interface IFicheTechnique {
  id?: number;
  pieceJointe?: string;
  dateDepot?: dayjs.Dayjs | null;
  nomResponsable?: IResponsable | null;
}

export class FicheTechnique implements IFicheTechnique {
  constructor(
    public id?: number,
    public pieceJointe?: string,
    public dateDepot?: dayjs.Dayjs | null,
    public nomResponsable?: IResponsable | null
  ) {}
}

export function getFicheTechniqueIdentifier(ficheTechnique: IFicheTechnique): number | undefined {
  return ficheTechnique.id;
}
