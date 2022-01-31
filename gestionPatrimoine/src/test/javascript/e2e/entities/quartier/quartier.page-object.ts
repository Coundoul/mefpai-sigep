import { element, by, ElementFinder } from 'protractor';

export class QuartierComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-quartier div table .btn-danger'));
  title = element.all(by.css('jhi-quartier div h2#page-heading span')).first();
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

export class QuartierUpdatePage {
  pageTitle = element(by.id('jhi-quartier-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomQuartierInput = element(by.id('field_nomQuartier'));

  nomCommuneSelect = element(by.id('field_nomCommune'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomQuartierInput(nomQuartier: string): Promise<void> {
    await this.nomQuartierInput.sendKeys(nomQuartier);
  }

  async getNomQuartierInput(): Promise<string> {
    return await this.nomQuartierInput.getAttribute('value');
  }

  async nomCommuneSelectLastOption(): Promise<void> {
    await this.nomCommuneSelect.all(by.tagName('option')).last().click();
  }

  async nomCommuneSelectOption(option: string): Promise<void> {
    await this.nomCommuneSelect.sendKeys(option);
  }

  getNomCommuneSelect(): ElementFinder {
    return this.nomCommuneSelect;
  }

  async getNomCommuneSelectedOption(): Promise<string> {
    return await this.nomCommuneSelect.element(by.css('option:checked')).getText();
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

export class QuartierDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-quartier-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-quartier'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
