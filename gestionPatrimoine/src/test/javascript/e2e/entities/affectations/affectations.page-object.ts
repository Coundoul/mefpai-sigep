import { element, by, ElementFinder } from 'protractor';

export class AffectationsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-affectations div table .btn-danger'));
  title = element.all(by.css('jhi-affectations div h2#page-heading span')).first();
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

export class AffectationsUpdatePage {
  pageTitle = element(by.id('jhi-affectations-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  quantiteAffecterInput = element(by.id('field_quantiteAffecter'));
  typeAttributionSelect = element(by.id('field_typeAttribution'));
  idPersInput = element(by.id('field_idPers'));
  dateAttributionInput = element(by.id('field_dateAttribution'));

  equipementSelect = element(by.id('field_equipement'));

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

  async setTypeAttributionSelect(typeAttribution: string): Promise<void> {
    await this.typeAttributionSelect.sendKeys(typeAttribution);
  }

  async getTypeAttributionSelect(): Promise<string> {
    return await this.typeAttributionSelect.element(by.css('option:checked')).getText();
  }

  async typeAttributionSelectLastOption(): Promise<void> {
    await this.typeAttributionSelect.all(by.tagName('option')).last().click();
  }

  async setIdPersInput(idPers: string): Promise<void> {
    await this.idPersInput.sendKeys(idPers);
  }

  async getIdPersInput(): Promise<string> {
    return await this.idPersInput.getAttribute('value');
  }

  async setDateAttributionInput(dateAttribution: string): Promise<void> {
    await this.dateAttributionInput.sendKeys(dateAttribution);
  }

  async getDateAttributionInput(): Promise<string> {
    return await this.dateAttributionInput.getAttribute('value');
  }

  async equipementSelectLastOption(): Promise<void> {
    await this.equipementSelect.all(by.tagName('option')).last().click();
  }

  async equipementSelectOption(option: string): Promise<void> {
    await this.equipementSelect.sendKeys(option);
  }

  getEquipementSelect(): ElementFinder {
    return this.equipementSelect;
  }

  async getEquipementSelectedOption(): Promise<string> {
    return await this.equipementSelect.element(by.css('option:checked')).getText();
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

export class AffectationsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-affectations-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-affectations'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
