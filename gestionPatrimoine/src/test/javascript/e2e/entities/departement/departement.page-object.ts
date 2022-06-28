import { element, by, ElementFinder } from 'protractor';

export class DepartementComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-departement div table .btn-danger'));
  title = element.all(by.css('jhi-departement div h2#page-heading span')).first();
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

export class DepartementUpdatePage {
  pageTitle = element(by.id('jhi-departement-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomDepartementInput = element(by.id('field_nomDepartement'));

  nomRegionSelect = element(by.id('field_nomRegion'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomDepartementInput(nomDepartement: string): Promise<void> {
    await this.nomDepartementInput.sendKeys(nomDepartement);
  }

  async getNomDepartementInput(): Promise<string> {
    return await this.nomDepartementInput.getAttribute('value');
  }

  async nomRegionSelectLastOption(): Promise<void> {
    await this.nomRegionSelect.all(by.tagName('option')).last().click();
  }

  async nomRegionSelectOption(option: string): Promise<void> {
    await this.nomRegionSelect.sendKeys(option);
  }

  getNomRegionSelect(): ElementFinder {
    return this.nomRegionSelect;
  }

  async getNomRegionSelectedOption(): Promise<string> {
    return await this.nomRegionSelect.element(by.css('option:checked')).getText();
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

export class DepartementDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-departement-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-departement'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
