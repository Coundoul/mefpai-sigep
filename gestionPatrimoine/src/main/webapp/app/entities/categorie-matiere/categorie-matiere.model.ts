export interface ICategorieMatiere {
  id?: number;
  categorie?: string;
}

export class CategorieMatiere implements ICategorieMatiere {
  constructor(public id?: number, public categorie?: string) {}
}

export function getCategorieMatiereIdentifier(categorieMatiere: ICategorieMatiere): number | undefined {
  return categorieMatiere.id;
}
