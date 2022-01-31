import { element, by, ElementFinder } from 'protractor';

export class BatimentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-batiment div table .btn-danger'));
  title = element.all(by.css('jhi-batiment div h2#page-heading span')).first();
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

export class BatimentUpdatePage {
  pageTitle = element(by.id('jhi-batiment-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomBatimentInput = element(by.id('field_nomBatiment'));
  nbrPieceInput = element(by.id('field_nbrPiece'));
  designationInput = element(by.id('field_designation'));
  surfaceInput = element(by.id('field_surface'));
  etatGeneralInput = element(by.id('field_etatGeneral'));
  descriptionInput = element(by.id('field_description'));
  nombreSalleInput = element(by.id('field_nombreSalle'));

  nomEtablissementSelect = element(by.id('field_nomEtablissement'));
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

  async setNomBatimentInput(nomBatiment: string): Promise<void> {
    await this.nomBatimentInput.sendKeys(nomBatiment);
  }

  async getNomBatimentInput(): Promise<string> {
    return await this.nomBatimentInput.getAttribute('value');
  }

  async setNbrPieceInput(nbrPiece: string): Promise<void> {
    await this.nbrPieceInput.sendKeys(nbrPiece);
  }

  async getNbrPieceInput(): Promise<string> {
    return await this.nbrPieceInput.getAttribute('value');
  }

  async setDesignationInput(designation: string): Promise<void> {
    await this.designationInput.sendKeys(designation);
  }

  async getDesignationInput(): Promise<string> {
    return await this.designationInput.getAttribute('value');
  }

  async setSurfaceInput(surface: string): Promise<void> {
    await this.surfaceInput.sendKeys(surface);
  }

  async getSurfaceInput(): Promise<string> {
    return await this.surfaceInput.getAttribute('value');
  }

  getEtatGeneralInput(): ElementFinder {
    return this.etatGeneralInput;
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setNombreSalleInput(nombreSalle: string): Promise<void> {
    await this.nombreSalleInput.sendKeys(nombreSalle);
  }

  async getNombreSalleInput(): Promise<string> {
    return await this.nombreSalleInput.getAttribute('value');
  }

  async nomEtablissementSelectLastOption(): Promise<void> {
    await this.nomEtablissementSelect.all(by.tagName('option')).last().click();
  }

  async nomEtablissementSelectOption(option: string): Promise<void> {
    await this.nomEtablissementSelect.sendKeys(option);
  }

  getNomEtablissementSelect(): ElementFinder {
    return this.nomEtablissementSelect;
  }

  async getNomEtablissementSelectedOption(): Promise<string> {
    return await this.nomEtablissementSelect.element(by.css('option:checked')).getText();
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

export class BatimentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-batiment-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-batiment'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
