import { NomRegion } from 'app/entities/enumerations/nom-region.model';

export interface IRegion {
  id?: number;
  nomRegion?: NomRegion;
}

export class Region implements IRegion {
  constructor(public id?: number, public nomRegion?: NomRegion) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}
