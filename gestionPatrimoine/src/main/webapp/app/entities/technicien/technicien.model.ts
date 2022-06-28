import { IChefMaintenance } from 'app/entities/chef-maintenance/chef-maintenance.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';

export interface ITechnicien {
  id?: number;
  nomPers?: string;
  prenomPers?: string;
  sexe?: Sexe;
  mobile?: string;
  adresse?: string | null;
  direction?: Direction | null;
  chefMaintenance?: IChefMaintenance | null;
}

export class Technicien implements ITechnicien {
  constructor(
    public id?: number,
    public nomPers?: string,
    public prenomPers?: string,
    public sexe?: Sexe,
    public mobile?: string,
    public adresse?: string | null,
    public direction?: Direction | null,
    public chefMaintenance?: IChefMaintenance | null
  ) {}
}

export function getTechnicienIdentifier(technicien: ITechnicien): number | undefined {
  return technicien.id;
}
