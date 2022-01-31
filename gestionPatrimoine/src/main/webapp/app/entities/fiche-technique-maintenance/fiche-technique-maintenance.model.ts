import * as dayjs from 'dayjs';
import { IRequete } from 'app/entities/requete/requete.model';

export interface IFicheTechniqueMaintenance {
  id?: number;
  pieceJointe?: string;
  idPers?: number;
  dateDepot?: dayjs.Dayjs | null;
  type?: IRequete | null;
}

export class FicheTechniqueMaintenance implements IFicheTechniqueMaintenance {
  constructor(
    public id?: number,
    public pieceJointe?: string,
    public idPers?: number,
    public dateDepot?: dayjs.Dayjs | null,
    public type?: IRequete | null
  ) {}
}

export function getFicheTechniqueMaintenanceIdentifier(ficheTechniqueMaintenance: IFicheTechniqueMaintenance): number | undefined {
  return ficheTechniqueMaintenance.id;
}
