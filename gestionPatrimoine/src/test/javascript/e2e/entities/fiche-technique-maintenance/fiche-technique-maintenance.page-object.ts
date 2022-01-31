import { element, by, ElementFinder } from 'protractor';

export class FicheTechniqueMaintenanceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fiche-technique-maintenance div table .btn-danger'));
  title = element.all(by.css('jhi-fiche-technique-maintenance div h2#page-heading span')).first();
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

export class FicheTechniqueMaintenanceUpdatePage {
  pageTitle = element(by.id('jhi-fiche-technique-maintenance-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  pieceJointeInput = element(by.id('field_pieceJointe'));
  idPersInput = element(by.id('field_idPers'));
  dateDepotInput = element(by.id('field_dateDepot'));

  typeSelect = element(by.id('field_type'));

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

  async setDateDepotInput(dateDepot: string): Promise<void> {
    await this.dateDepotInput.sendKeys(dateDepot);
  }

  async getDateDepotInput(): Promise<string> {
    return await this.dateDepotInput.getAttribute('value');
  }

  async typeSelectLastOption(): Promise<void> {
    await this.typeSelect.all(by.tagName('option')).last().click();
  }

  async typeSelectOption(option: string): Promise<void> {
    await this.typeSelect.sendKeys(option);
  }

  getTypeSelect(): ElementFinder {
    return this.typeSelect;
  }

  async getTypeSelectedOption(): Promise<string> {
    return await this.typeSelect.element(by.css('option:checked')).getText();
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

export class FicheTechniqueMaintenanceDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-ficheTechniqueMaintenance-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-ficheTechniqueMaintenance'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
