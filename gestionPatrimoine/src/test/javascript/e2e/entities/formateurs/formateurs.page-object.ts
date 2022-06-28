import { element, by, ElementFinder } from 'protractor';

export class FormateursComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-formateurs div table .btn-danger'));
  title = element.all(by.css('jhi-formateurs div h2#page-heading span')).first();
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

export class FormateursUpdatePage {
  pageTitle = element(by.id('jhi-formateurs-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFormateurInput = element(by.id('field_nomFormateur'));
  prenomFormateurInput = element(by.id('field_prenomFormateur'));
  emailInput = element(by.id('field_email'));
  numb1Input = element(by.id('field_numb1'));
  numb2Input = element(by.id('field_numb2'));
  adresseInput = element(by.id('field_adresse'));
  villeInput = element(by.id('field_ville'));
  specialiteInput = element(by.id('field_specialite'));

  nomEtablissementSelect = element(by.id('field_nomEtablissement'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomFormateurInput(nomFormateur: string): Promise<void> {
    await this.nomFormateurInput.sendKeys(nomFormateur);
  }

  async getNomFormateurInput(): Promise<string> {
    return await this.nomFormateurInput.getAttribute('value');
  }

  async setPrenomFormateurInput(prenomFormateur: string): Promise<void> {
    await this.prenomFormateurInput.sendKeys(prenomFormateur);
  }

  async getPrenomFormateurInput(): Promise<string> {
    return await this.prenomFormateurInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
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

  async setAdresseInput(adresse: string): Promise<void> {
    await this.adresseInput.sendKeys(adresse);
  }

  async getAdresseInput(): Promise<string> {
    return await this.adresseInput.getAttribute('value');
  }

  async setVilleInput(ville: string): Promise<void> {
    await this.villeInput.sendKeys(ville);
  }

  async getVilleInput(): Promise<string> {
    return await this.villeInput.getAttribute('value');
  }

  async setSpecialiteInput(specialite: string): Promise<void> {
    await this.specialiteInput.sendKeys(specialite);
  }

  async getSpecialiteInput(): Promise<string> {
    return await this.specialiteInput.getAttribute('value');
  }

  async nomEtablissementSelectLastOption(): Promise<void> {
    await this.nomEtablissementSelect.all(by.tagName('option')).last().click();
  }

  async nomEtablissementSelectOption(option: string): Promise<void> {
    await this.nomEtablissementSelect.sendKeys(option);
  }

  getNomEtablissementSelect(): ElementFinder {
    return this.nomEtablissementSelect;
  }

  async getNomEtablissementSelectedOption(): Promise<string> {
    return await this.nomEtablissementSelect.element(by.css('option:checked')).getText();
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

export class FormateursDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-formateurs-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-formateurs'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
