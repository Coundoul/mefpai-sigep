import { element, by, ElementFinder } from 'protractor';

export class BonComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-bon div table .btn-danger'));
  title = element.all(by.css('jhi-bon div h2#page-heading span')).first();
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

export class BonUpdatePage {
  pageTitle = element(by.id('jhi-bon-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  typeBonSelect = element(by.id('field_typeBon'));
  quantiteLivreInput = element(by.id('field_quantiteLivre'));
  quantiteCommandeInput = element(by.id('field_quantiteCommande'));
  dateCreationInput = element(by.id('field_dateCreation'));
  etatInput = element(by.id('field_etat'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setTypeBonSelect(typeBon: string): Promise<void> {
    await this.typeBonSelect.sendKeys(typeBon);
  }

  async getTypeBonSelect(): Promise<string> {
    return await this.typeBonSelect.element(by.css('option:checked')).getText();
  }

  async typeBonSelectLastOption(): Promise<void> {
    await this.typeBonSelect.all(by.tagName('option')).last().click();
  }

  async setQuantiteLivreInput(quantiteLivre: string): Promise<void> {
    await this.quantiteLivreInput.sendKeys(quantiteLivre);
  }

  async getQuantiteLivreInput(): Promise<string> {
    return await this.quantiteLivreInput.getAttribute('value');
  }

  async setQuantiteCommandeInput(quantiteCommande: string): Promise<void> {
    await this.quantiteCommandeInput.sendKeys(quantiteCommande);
  }

  async getQuantiteCommandeInput(): Promise<string> {
    return await this.quantiteCommandeInput.getAttribute('value');
  }

  async setDateCreationInput(dateCreation: string): Promise<void> {
    await this.dateCreationInput.sendKeys(dateCreation);
  }

  async getDateCreationInput(): Promise<string> {
    return await this.dateCreationInput.getAttribute('value');
  }

  getEtatInput(): ElementFinder {
    return this.etatInput;
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

export class BonDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-bon-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-bon'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
