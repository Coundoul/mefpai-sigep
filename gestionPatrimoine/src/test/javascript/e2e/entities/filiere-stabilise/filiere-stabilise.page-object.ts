import { element, by, ElementFinder } from 'protractor';

export class FiliereStabiliseComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-filiere-stabilise div table .btn-danger'));
  title = element.all(by.css('jhi-filiere-stabilise div h2#page-heading span')).first();
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

export class FiliereStabiliseUpdatePage {
  pageTitle = element(by.id('jhi-filiere-stabilise-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFiliereInput = element(by.id('field_nomFiliere'));

  nomFormateurSelect = element(by.id('field_nomFormateur'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomFiliereInput(nomFiliere: string): Promise<void> {
    await this.nomFiliereInput.sendKeys(nomFiliere);
  }

  async getNomFiliereInput(): Promise<string> {
    return await this.nomFiliereInput.getAttribute('value');
  }

  async nomFormateurSelectLastOption(): Promise<void> {
    await this.nomFormateurSelect.all(by.tagName('option')).last().click();
  }

  async nomFormateurSelectOption(option: string): Promise<void> {
    await this.nomFormateurSelect.sendKeys(option);
  }

  getNomFormateurSelect(): ElementFinder {
    return this.nomFormateurSelect;
  }

  async getNomFormateurSelectedOption(): Promise<string> {
    return await this.nomFormateurSelect.element(by.css('option:checked')).getText();
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

export class FiliereStabiliseDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-filiereStabilise-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-filiereStabilise'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
