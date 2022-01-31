import { element, by, ElementFinder } from 'protractor';

export class SallesComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-salles div table .btn-danger'));
  title = element.all(by.css('jhi-salles div h2#page-heading span')).first();
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

export class SallesUpdatePage {
  pageTitle = element(by.id('jhi-salles-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomSalleInput = element(by.id('field_nomSalle'));
  classeSelect = element(by.id('field_classe'));

  nomBatimentSelect = element(by.id('field_nomBatiment'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomSalleInput(nomSalle: string): Promise<void> {
    await this.nomSalleInput.sendKeys(nomSalle);
  }

  async getNomSalleInput(): Promise<string> {
    return await this.nomSalleInput.getAttribute('value');
  }

  async setClasseSelect(classe: string): Promise<void> {
    await this.classeSelect.sendKeys(classe);
  }

  async getClasseSelect(): Promise<string> {
    return await this.classeSelect.element(by.css('option:checked')).getText();
  }

  async classeSelectLastOption(): Promise<void> {
    await this.classeSelect.all(by.tagName('option')).last().click();
  }

  async nomBatimentSelectLastOption(): Promise<void> {
    await this.nomBatimentSelect.all(by.tagName('option')).last().click();
  }

  async nomBatimentSelectOption(option: string): Promise<void> {
    await this.nomBatimentSelect.sendKeys(option);
  }

  getNomBatimentSelect(): ElementFinder {
    return this.nomBatimentSelect;
  }

  async getNomBatimentSelectedOption(): Promise<string> {
    return await this.nomBatimentSelect.element(by.css('option:checked')).getText();
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

export class SallesDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-salles-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-salles'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
