import { element, by, ElementFinder } from 'protractor';

export class DetailSortieComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-detail-sortie div table .btn-danger'));
  title = element.all(by.css('jhi-detail-sortie div h2#page-heading span')).first();
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

export class DetailSortieUpdatePage {
  pageTitle = element(by.id('jhi-detail-sortie-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  pieceJointeInput = element(by.id('field_pieceJointe'));
  idPersInput = element(by.id('field_idPers'));

  typeBonSelect = element(by.id('field_typeBon'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setPieceJointeInput(pieceJointe: string): Promise<void> {
    await this.pieceJointeInput.sendKeys(pieceJointe);
  }

  async getPieceJointeInput(): Promise<string> {
    return await this.pieceJointeInput.getAttribute('value');
  }

  async setIdPersInput(idPers: string): Promise<void> {
    await this.idPersInput.sendKeys(idPers);
  }

  async getIdPersInput(): Promise<string> {
    return await this.idPersInput.getAttribute('value');
  }

  async typeBonSelectLastOption(): Promise<void> {
    await this.typeBonSelect.all(by.tagName('option')).last().click();
  }

  async typeBonSelectOption(option: string): Promise<void> {
    await this.typeBonSelect.sendKeys(option);
  }

  getTypeBonSelect(): ElementFinder {
    return this.typeBonSelect;
  }

  async getTypeBonSelectedOption(): Promise<string> {
    return await this.typeBonSelect.element(by.css('option:checked')).getText();
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

export class DetailSortieDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-detailSortie-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-detailSortie'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
