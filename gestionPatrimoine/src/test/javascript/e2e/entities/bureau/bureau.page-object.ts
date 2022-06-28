import { element, by, ElementFinder } from 'protractor';

export class BureauComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-bureau div table .btn-danger'));
  title = element.all(by.css('jhi-bureau div h2#page-heading span')).first();
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

export class BureauUpdatePage {
  pageTitle = element(by.id('jhi-bureau-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomStructureSelect = element(by.id('field_nomStructure'));
  directionSelect = element(by.id('field_direction'));
  nomEtablissementInput = element(by.id('field_nomEtablissement'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomStructureSelect(nomStructure: string): Promise<void> {
    await this.nomStructureSelect.sendKeys(nomStructure);
  }

  async getNomStructureSelect(): Promise<string> {
    return await this.nomStructureSelect.element(by.css('option:checked')).getText();
  }

  async nomStructureSelectLastOption(): Promise<void> {
    await this.nomStructureSelect.all(by.tagName('option')).last().click();
  }

  async setDirectionSelect(direction: string): Promise<void> {
    await this.directionSelect.sendKeys(direction);
  }

  async getDirectionSelect(): Promise<string> {
    return await this.directionSelect.element(by.css('option:checked')).getText();
  }

  async directionSelectLastOption(): Promise<void> {
    await this.directionSelect.all(by.tagName('option')).last().click();
  }

  async setNomEtablissementInput(nomEtablissement: string): Promise<void> {
    await this.nomEtablissementInput.sendKeys(nomEtablissement);
  }

  async getNomEtablissementInput(): Promise<string> {
    return await this.nomEtablissementInput.getAttribute('value');
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

export class BureauDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-bureau-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-bureau'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
