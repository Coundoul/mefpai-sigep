export interface IUtilisateurFinal {
  id?: number;
  nomUtilisateur?: string;
  prenomUtilisateur?: string;
  emailInstitutionnel?: string;
  mobile?: string;
  sexe?: string;
  departement?: string | null;
  serviceDep?: string | null;
}

export class UtilisateurFinal implements IUtilisateurFinal {
  constructor(
    public id?: number,
    public nomUtilisateur?: string,
    public prenomUtilisateur?: string,
    public emailInstitutionnel?: string,
    public mobile?: string,
    public sexe?: string,
    public departement?: string | null,
    public serviceDep?: string | null
  ) {}
}

export function getUtilisateurFinalIdentifier(utilisateurFinal: IUtilisateurFinal): number | undefined {
  return utilisateurFinal.id;
}
