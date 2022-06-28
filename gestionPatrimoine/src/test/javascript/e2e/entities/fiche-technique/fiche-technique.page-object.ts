import { element, by, ElementFinder } from 'protractor';

export class FicheTechniqueComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fiche-technique div table .btn-danger'));
  title = element.all(by.css('jhi-fiche-technique div h2#page-heading span')).first();
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

export class FicheTechniqueUpdatePage {
  pageTitle = element(by.id('jhi-fiche-technique-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  pieceJointeInput = element(by.id('field_pieceJointe'));
  dateDepotInput = element(by.id('field_dateDepot'));

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

  async setPieceJointeInput(pieceJointe: string): Promise<void> {
    await this.pieceJointeInput.sendKeys(pieceJointe);
  }

  async getPieceJointeInput(): Promise<string> {
    return await this.pieceJointeInput.getAttribute('value');
  }

  async setDateDepotInput(dateDepot: string): Promise<void> {
    await this.dateDepotInput.sendKeys(dateDepot);
  }

  async getDateDepotInput(): Promise<string> {
    return await this.dateDepotInput.getAttribute('value');
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

export class FicheTechniqueDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-ficheTechnique-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-ficheTechnique'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
