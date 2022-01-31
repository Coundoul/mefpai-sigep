import { element, by, ElementFinder } from 'protractor';

export class UtilisateurFinalComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-utilisateur-final div table .btn-danger'));
  title = element.all(by.css('jhi-utilisateur-final div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class UtilisateurFinalUpdatePage {
  pageTitle = element(by.id('jhi-utilisateur-final-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomUtilisateurInput = element(by.id('field_nomUtilisateur'));
  prenomUtilisateurInput = element(by.id('field_prenomUtilisateur'));
  emailInstitutionnelInput = element(by.id('field_emailInstitutionnel'));
  mobileInput = element(by.id('field_mobile'));
  sexeInput = element(by.id('field_sexe'));
  departementInput = element(by.id('field_departement'));
  serviceDepInput = element(by.id('field_serviceDep'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomUtilisateurInput(nomUtilisateur: string): Promise<void> {
    await this.nomUtilisateurInput.sendKeys(nomUtilisateur);
  }

  async getNomUtilisateurInput(): Promise<string> {
    return await this.nomUtilisateurInput.getAttribute('value');
  }

  async setPrenomUtilisateurInput(prenomUtilisateur: string): Promise<void> {
    await this.prenomUtilisateurInput.sendKeys(prenomUtilisateur);
  }

  async getPrenomUtilisateurInput(): Promise<string> {
    return await this.prenomUtilisateurInput.getAttribute('value');
  }

  async setEmailInstitutionnelInput(emailInstitutionnel: string): Promise<void> {
    await this.emailInstitutionnelInput.sendKeys(emailInstitutionnel);
  }

  async getEmailInstitutionnelInput(): Promise<string> {
    return await this.emailInstitutionnelInput.getAttribute('value');
  }

  async setMobileInput(mobile: string): Promise<void> {
    await this.mobileInput.sendKeys(mobile);
  }

  async getMobileInput(): Promise<string> {
    return await this.mobileInput.getAttribute('value');
  }

  async setSexeInput(sexe: string): Promise<void> {
    await this.sexeInput.sendKeys(sexe);
  }

  async getSexeInput(): Promise<string> {
    return await this.sexeInput.getAttribute('value');
  }

  async setDepartementInput(departement: string): Promise<void> {
    await this.departementInput.sendKeys(departement);
  }

  async getDepartementInput(): Promise<string> {
    return await this.departementInput.getAttribute('value');
  }

  async setServiceDepInput(serviceDep: string): Promise<void> {
    await this.serviceDepInput.sendKeys(serviceDep);
  }

  async getServiceDepInput(): Promise<string> {
    return await this.serviceDepInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class UtilisateurFinalDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-utilisateurFinal-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-utilisateurFinal'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
