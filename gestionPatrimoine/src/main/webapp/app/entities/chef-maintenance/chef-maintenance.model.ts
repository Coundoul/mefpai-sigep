import { ITechnicien } from 'app/entities/technicien/technicien.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';

export interface IChefMaintenance {
  id?: number;
  nomPers?: string;
  prenomPers?: string;
  sexe?: Sexe;
  mobile?: string;
  adresse?: string | null;
  direction?: Direction | null;
  techniciens?: ITechnicien[] | null;
}

export class ChefMaintenance implements IChefMaintenance {
  constructor(
    public id?: number,
    public nomPers?: string,
    public prenomPers?: string,
    public sexe?: Sexe,
    public mobile?: string,
    public adresse?: string | null,
    public direction?: Direction | null,
    public techniciens?: ITechnicien[] | null
  ) {}
}

export function getChefMaintenanceIdentifier(chefMaintenance: IChefMaintenance): number | undefined {
  return chefMaintenance.id;
}
