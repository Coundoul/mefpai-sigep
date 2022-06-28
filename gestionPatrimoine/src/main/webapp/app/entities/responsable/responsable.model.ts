export interface IResponsable {
  id?: number;
  nomResponsable?: string;
  prenomResponsable?: string;
  email?: string;
  specialite?: string;
  numb1?: string;
  numb2?: string | null;
  raisonSocial?: string;
}

export class Responsable implements IResponsable {
  constructor(
    public id?: number,
    public nomResponsable?: string,
    public prenomResponsable?: string,
    public email?: string,
    public specialite?: string,
    public numb1?: string,
    public numb2?: string | null,
    public raisonSocial?: string
  ) {}
}

export function getResponsableIdentifier(responsable: IResponsable): number | undefined {
  return responsable.id;
}
