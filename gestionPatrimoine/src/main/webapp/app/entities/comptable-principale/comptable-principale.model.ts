import { IMangasinierFichiste } from 'app/entities/mangasinier-fichiste/mangasinier-fichiste.model';
import { IComptableSecondaire } from 'app/entities/comptable-secondaire/comptable-secondaire.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';

export interface IComptablePrincipale {
  id?: number;
  nomPers?: string;
  prenomPers?: string;
  sexe?: Sexe;
  mobile?: string;
  adresse?: string | null;
  direction?: Direction;
  mangasinierFichistes?: IMangasinierFichiste[] | null;
  comptableSecondaires?: IComptableSecondaire[] | null;
}

export class ComptablePrincipale implements IComptablePrincipale {
  constructor(
    public id?: number,
    public nomPers?: string,
    public prenomPers?: string,
    public sexe?: Sexe,
    public mobile?: string,
    public adresse?: string | null,
    public direction?: Direction,
    public mangasinierFichistes?: IMangasinierFichiste[] | null,
    public comptableSecondaires?: IComptableSecondaire[] | null
  ) {}
}

export function getComptablePrincipaleIdentifier(comptablePrincipale: IComptablePrincipale): number | undefined {
  return comptablePrincipale.id;
}
