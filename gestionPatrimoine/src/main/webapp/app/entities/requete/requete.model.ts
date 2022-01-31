import * as dayjs from 'dayjs';
import { IBureau } from 'app/entities/bureau/bureau.model';

export interface IRequete {
  id?: number;
  type?: string;
  typePanne?: number;
  datePost?: number;
  description?: string;
  etatTraite?: boolean | null;
  dateLancement?: dayjs.Dayjs | null;
  idPers?: number;
  nomStructure?: IBureau | null;
}

export class Requete implements IRequete {
  constructor(
    public id?: number,
    public type?: string,
    public typePanne?: number,
    public datePost?: number,
    public description?: string,
    public etatTraite?: boolean | null,
    public dateLancement?: dayjs.Dayjs | null,
    public idPers?: number,
    public nomStructure?: IBureau | null
  ) {
    this.etatTraite = this.etatTraite ?? false;
  }
}

export function getRequeteIdentifier(requete: IRequete): number | undefined {
  return requete.id;
}
