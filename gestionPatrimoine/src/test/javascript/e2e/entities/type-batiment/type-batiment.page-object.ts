import { element, by, ElementFinder } from 'protractor';

export class TypeBatimentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-type-batiment div table .btn-danger'));
  title = element.all(by.css('jhi-type-batiment div h2#page-heading span')).first();
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

export class TypeBatimentUpdatePage {
  pageTitle = element(by.id('jhi-type-batiment-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  typeBaInput = element(by.id('field_typeBa'));

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

  async setTypeBaInput(typeBa: string): Promise<void> {
    await this.typeBaInput.sendKeys(typeBa);
  }

  async getTypeBaInput(): Promise<string> {
    return await this.typeBaInput.getAttribute('value');
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

export class TypeBatimentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-typeBatiment-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-typeBatiment'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
