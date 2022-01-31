
export interface IInventaire {
  reference?: string;
  entreeTotal?: string;
  sortieTotal?: string;
  compter?: string;
}

export interface IDetailInventaire {
  reference?: string;
  quantite?: string;
  etatMatiere?: string;
  typeAttribution?: string;
  quantiteAffecter?: string;
  dateAttribution?: string;
  beneficiaire?: string;
  idPers?: string;
}

export class Inventaire implements IInventaire {
  constructor(
    public reference?: string,
    public entreeTotal?: string,
    public sortieTotal?: string,
    public compter?: string,
  ) {}
}

export class DetailInventaire implements IDetailInventaire {
  constructor(
    public reference?: string,
    public quantite?: string,
    public etatMatiere?: string,
    public typeAttribution?: string,
    public quantiteAffecter?: string,
    public dateAttribution?: string,
    public beneficiaire?: string,
    public idPers?: string
  ) {}
}

export function getEquipementInventaire(inventaire: IInventaire): string | undefined {
  return inventaire.reference;
}

export function getDetailEquipementInventaire(detailinventaire: IDetailInventaire): string | undefined {
  return detailinventaire.reference;
}
