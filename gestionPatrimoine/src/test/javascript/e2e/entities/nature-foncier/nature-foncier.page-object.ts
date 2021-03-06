import { element, by, ElementFinder } from 'protractor';

export class NatureFoncierComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-nature-foncier div table .btn-danger'));
  title = element.all(by.css('jhi-nature-foncier div h2#page-heading span')).first();
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

export class NatureFoncierUpdatePage {
  pageTitle = element(by.id('jhi-nature-foncier-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  typeFoncierInput = element(by.id('field_typeFoncier'));
  pieceJointeInput = element(by.id('field_pieceJointe'));

  nomCorpsSelect = element(by.id('field_nomCorps'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setTypeFoncierInput(typeFoncier: string): Promise<void> {
    await this.typeFoncierInput.sendKeys(typeFoncier);
  }

  async getTypeFoncierInput(): Promise<string> {
    return await this.typeFoncierInput.getAttribute('value');
  }

  async setPieceJointeInput(pieceJointe: string): Promise<void> {
    await this.pieceJointeInput.sendKeys(pieceJointe);
  }

  async getPieceJointeInput(): Promise<string> {
    return await this.pieceJointeInput.getAttribute('value');
  }

  async nomCorpsSelectLastOption(): Promise<void> {
    await this.nomCorpsSelect.all(by.tagName('option')).last().click();
  }

  async nomCorpsSelectOption(option: string): Promise<void> {
    await this.nomCorpsSelect.sendKeys(option);
  }

  getNomCorpsSelect(): ElementFinder {
    return this.nomCorpsSelect;
  }

  async getNomCorpsSelectedOption(): Promise<string> {
    return await this.nomCorpsSelect.element(by.css('option:checked')).getText();
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

export class NatureFoncierDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-natureFoncier-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-natureFoncier'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
