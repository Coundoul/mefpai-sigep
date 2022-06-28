import { element, by, ElementFinder } from 'protractor';

export class AttributionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-attribution div table .btn-danger'));
  title = element.all(by.css('jhi-attribution div h2#page-heading span')).first();
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

export class AttributionUpdatePage {
  pageTitle = element(by.id('jhi-attribution-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  quantiteAffecterInput = element(by.id('field_quantiteAffecter'));
  idPersInput = element(by.id('field_idPers'));
  dateAffectationInput = element(by.id('field_dateAffectation'));

  nomUtilisateurSelect = element(by.id('field_nomUtilisateur'));
  affectationsSelect = element(by.id('field_affectations'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setQuantiteAffecterInput(quantiteAffecter: string): Promise<void> {
    await this.quantiteAffecterInput.sendKeys(quantiteAffecter);
  }

  async getQuantiteAffecterInput(): Promise<string> {
    return await this.quantiteAffecterInput.getAttribute('value');
  }

  async setIdPersInput(idPers: string): Promise<void> {
    await this.idPersInput.sendKeys(idPers);
  }

  async getIdPersInput(): Promise<string> {
    return await this.idPersInput.getAttribute('value');
  }

  async setDateAffectationInput(dateAffectation: string): Promise<void> {
    await this.dateAffectationInput.sendKeys(dateAffectation);
  }

  async getDateAffectationInput(): Promise<string> {
    return await this.dateAffectationInput.getAttribute('value');
  }

  async nomUtilisateurSelectLastOption(): Promise<void> {
    await this.nomUtilisateurSelect.all(by.tagName('option')).last().click();
  }

  async nomUtilisateurSelectOption(option: string): Promise<void> {
    await this.nomUtilisateurSelect.sendKeys(option);
  }

  getNomUtilisateurSelect(): ElementFinder {
    return this.nomUtilisateurSelect;
  }

  async getNomUtilisateurSelectedOption(): Promise<string> {
    return await this.nomUtilisateurSelect.element(by.css('option:checked')).getText();
  }

  async affectationsSelectLastOption(): Promise<void> {
    await this.affectationsSelect.all(by.tagName('option')).last().click();
  }

  async affectationsSelectOption(option: string): Promise<void> {
    await this.affectationsSelect.sendKeys(option);
  }

  getAffectationsSelect(): ElementFinder {
    return this.affectationsSelect;
  }

  async getAffectationsSelectedOption(): Promise<string> {
    return await this.affectationsSelect.element(by.css('option:checked')).getText();
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

export class AttributionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-attribution-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-attribution'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
