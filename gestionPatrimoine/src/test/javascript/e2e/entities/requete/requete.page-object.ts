import { element, by, ElementFinder } from 'protractor';

export class RequeteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-requete div table .btn-danger'));
  title = element.all(by.css('jhi-requete div h2#page-heading span')).first();
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

export class RequeteUpdatePage {
  pageTitle = element(by.id('jhi-requete-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  typeInput = element(by.id('field_type'));
  typePanneInput = element(by.id('field_typePanne'));
  datePostInput = element(by.id('field_datePost'));
  descriptionInput = element(by.id('field_description'));
  etatTraiteInput = element(by.id('field_etatTraite'));
  dateLancementInput = element(by.id('field_dateLancement'));
  idPersInput = element(by.id('field_idPers'));

  nomStructureSelect = element(by.id('field_nomStructure'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setTypeInput(type: string): Promise<void> {
    await this.typeInput.sendKeys(type);
  }

  async getTypeInput(): Promise<string> {
    return await this.typeInput.getAttribute('value');
  }

  async setTypePanneInput(typePanne: string): Promise<void> {
    await this.typePanneInput.sendKeys(typePanne);
  }

  async getTypePanneInput(): Promise<string> {
    return await this.typePanneInput.getAttribute('value');
  }

  async setDatePostInput(datePost: string): Promise<void> {
    await this.datePostInput.sendKeys(datePost);
  }

  async getDatePostInput(): Promise<string> {
    return await this.datePostInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  getEtatTraiteInput(): ElementFinder {
    return this.etatTraiteInput;
  }

  async setDateLancementInput(dateLancement: string): Promise<void> {
    await this.dateLancementInput.sendKeys(dateLancement);
  }

  async getDateLancementInput(): Promise<string> {
    return await this.dateLancementInput.getAttribute('value');
  }

  async setIdPersInput(idPers: string): Promise<void> {
    await this.idPersInput.sendKeys(idPers);
  }

  async getIdPersInput(): Promise<string> {
    return await this.idPersInput.getAttribute('value');
  }

  async nomStructureSelectLastOption(): Promise<void> {
    await this.nomStructureSelect.all(by.tagName('option')).last().click();
  }

  async nomStructureSelectOption(option: string): Promise<void> {
    await this.nomStructureSelect.sendKeys(option);
  }

  getNomStructureSelect(): ElementFinder {
    return this.nomStructureSelect;
  }

  async getNomStructureSelectedOption(): Promise<string> {
    return await this.nomStructureSelect.element(by.css('option:checked')).getText();
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

export class RequeteDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-requete-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-requete'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
