import * as dayjs from 'dayjs';
import { IUtilisateurFinal } from 'app/entities/utilisateur-final/utilisateur-final.model';
import { IAffectations } from 'app/entities/affectations/affectations.model';

export interface IAttribution {
  id?: number;
  quantiteAffecter?: number;
  idPers?: number;
  dateAffectation?: dayjs.Dayjs | null;
  nomUtilisateur?: IUtilisateurFinal | null;
  affectations?: IAffectations | null;
}

export class Attribution implements IAttribution {
  constructor(
    public id?: number,
    public quantiteAffecter?: number,
    public idPers?: number,
    public dateAffectation?: dayjs.Dayjs | null,
    public nomUtilisateur?: IUtilisateurFinal | null,
    public affectations?: IAffectations | null
  ) {}
}

export function getAttributionIdentifier(attribution: IAttribution): number | undefined {
  return attribution.id;
}
