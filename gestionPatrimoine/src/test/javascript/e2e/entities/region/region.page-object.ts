import { element, by, ElementFinder } from 'protractor';

export class RegionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-region div table .btn-danger'));
  title = element.all(by.css('jhi-region div h2#page-heading span')).first();
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

export class RegionUpdatePage {
  pageTitle = element(by.id('jhi-region-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
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

  async setNomRegionSelect(nomRegion: string): Promise<void> {
    await this.nomRegionSelect.sendKeys(nomRegion);
  }

  async getNomRegionSelect(): Promise<string> {
    return await this.nomRegionSelect.element(by.css('option:checked')).getText();
  }

  async nomRegionSelectLastOption(): Promise<void> {
    await this.nomRegionSelect.all(by.tagName('option')).last().click();
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

export class RegionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-region-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-region'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
