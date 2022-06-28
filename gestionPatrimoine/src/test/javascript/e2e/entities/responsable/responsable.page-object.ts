import { element, by, ElementFinder } from 'protractor';

export class ResponsableComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-responsable div table .btn-danger'));
  title = element.all(by.css('jhi-responsable div h2#page-heading span')).first();
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

export class ResponsableUpdatePage {
  pageTitle = element(by.id('jhi-responsable-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomResponsableInput = element(by.id('field_nomResponsable'));
  prenomResponsableInput = element(by.id('field_prenomResponsable'));
  emailInput = element(by.id('field_email'));
  specialiteInput = element(by.id('field_specialite'));
  numb1Input = element(by.id('field_numb1'));
  numb2Input = element(by.id('field_numb2'));
  raisonSocialInput = element(by.id('field_raisonSocial'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomResponsableInput(nomResponsable: string): Promise<void> {
    await this.nomResponsableInput.sendKeys(nomResponsable);
  }

  async getNomResponsableInput(): Promise<string> {
    return await this.nomResponsableInput.getAttribute('value');
  }

  async setPrenomResponsableInput(prenomResponsable: string): Promise<void> {
    await this.prenomResponsableInput.sendKeys(prenomResponsable);
  }

  async getPrenomResponsableInput(): Promise<string> {
    return await this.prenomResponsableInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
  }

  async setSpecialiteInput(specialite: string): Promise<void> {
    await this.specialiteInput.sendKeys(specialite);
  }

  async getSpecialiteInput(): Promise<string> {
    return await this.specialiteInput.getAttribute('value');
  }

  async setNumb1Input(numb1: string): Promise<void> {
    await this.numb1Input.sendKeys(numb1);
  }

  async getNumb1Input(): Promise<string> {
    return await this.numb1Input.getAttribute('value');
  }

  async setNumb2Input(numb2: string): Promise<void> {
    await this.numb2Input.sendKeys(numb2);
  }

  async getNumb2Input(): Promise<string> {
    return await this.numb2Input.getAttribute('value');
  }

  async setRaisonSocialInput(raisonSocial: string): Promise<void> {
    await this.raisonSocialInput.sendKeys(raisonSocial);
  }

  async getRaisonSocialInput(): Promise<string> {
    return await this.raisonSocialInput.getAttribute('value');
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

export class ResponsableDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-responsable-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-responsable'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
