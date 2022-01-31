import { element, by, ElementFinder } from 'protractor';

export class CorpsEtatComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-corps-etat div table .btn-danger'));
  title = element.all(by.css('jhi-corps-etat div h2#page-heading span')).first();
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

export class CorpsEtatUpdatePage {
  pageTitle = element(by.id('jhi-corps-etat-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomCorpsInput = element(by.id('field_nomCorps'));
  grosOeuvreInput = element(by.id('field_grosOeuvre'));
  descriptionGrosOeuvreInput = element(by.id('field_descriptionGrosOeuvre'));
  secondOeuvreInput = element(by.id('field_secondOeuvre'));
  descriptionSecondOeuvreInput = element(by.id('field_descriptionSecondOeuvre'));
  oservationInput = element(by.id('field_oservation'));
  etatCorpsInput = element(by.id('field_etatCorps'));

  nomResponsableSelect = element(by.id('field_nomResponsable'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomCorpsInput(nomCorps: string): Promise<void> {
    await this.nomCorpsInput.sendKeys(nomCorps);
  }

  async getNomCorpsInput(): Promise<string> {
    return await this.nomCorpsInput.getAttribute('value');
  }

  async setGrosOeuvreInput(grosOeuvre: string): Promise<void> {
    await this.grosOeuvreInput.sendKeys(grosOeuvre);
  }

  async getGrosOeuvreInput(): Promise<string> {
    return await this.grosOeuvreInput.getAttribute('value');
  }

  async setDescriptionGrosOeuvreInput(descriptionGrosOeuvre: string): Promise<void> {
    await this.descriptionGrosOeuvreInput.sendKeys(descriptionGrosOeuvre);
  }

  async getDescriptionGrosOeuvreInput(): Promise<string> {
    return await this.descriptionGrosOeuvreInput.getAttribute('value');
  }

  async setSecondOeuvreInput(secondOeuvre: string): Promise<void> {
    await this.secondOeuvreInput.sendKeys(secondOeuvre);
  }

  async getSecondOeuvreInput(): Promise<string> {
    return await this.secondOeuvreInput.getAttribute('value');
  }

  async setDescriptionSecondOeuvreInput(descriptionSecondOeuvre: string): Promise<void> {
    await this.descriptionSecondOeuvreInput.sendKeys(descriptionSecondOeuvre);
  }

  async getDescriptionSecondOeuvreInput(): Promise<string> {
    return await this.descriptionSecondOeuvreInput.getAttribute('value');
  }

  getOservationInput(): ElementFinder {
    return this.oservationInput;
  }

  getEtatCorpsInput(): ElementFinder {
    return this.etatCorpsInput;
  }

  async nomResponsableSelectLastOption(): Promise<void> {
    await this.nomResponsableSelect.all(by.tagName('option')).last().click();
  }

  async nomResponsableSelectOption(option: string): Promise<void> {
    await this.nomResponsableSelect.sendKeys(option);
  }

  getNomResponsableSelect(): ElementFinder {
    return this.nomResponsableSelect;
  }

  async getNomResponsableSelectedOption(): Promise<string> {
    return await this.nomResponsableSelect.element(by.css('option:checked')).getText();
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

export class CorpsEtatDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-corpsEtat-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-corpsEtat'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
