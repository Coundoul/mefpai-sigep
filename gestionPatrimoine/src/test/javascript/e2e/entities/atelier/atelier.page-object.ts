import { element, by, ElementFinder } from 'protractor';

export class AtelierComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-atelier div table .btn-danger'));
  title = element.all(by.css('jhi-atelier div h2#page-heading span')).first();
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

export class AtelierUpdatePage {
  pageTitle = element(by.id('jhi-atelier-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomAtelierInput = element(by.id('field_nomAtelier'));
  surfaceInput = element(by.id('field_surface'));
  descriptionInput = element(by.id('field_description'));

  nomFiliereSelect = element(by.id('field_nomFiliere'));
  nomBatimentSelect = element(by.id('field_nomBatiment'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomAtelierInput(nomAtelier: string): Promise<void> {
    await this.nomAtelierInput.sendKeys(nomAtelier);
  }

  async getNomAtelierInput(): Promise<string> {
    return await this.nomAtelierInput.getAttribute('value');
  }

  async setSurfaceInput(surface: string): Promise<void> {
    await this.surfaceInput.sendKeys(surface);
  }

  async getSurfaceInput(): Promise<string> {
    return await this.surfaceInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async nomFiliereSelectLastOption(): Promise<void> {
    await this.nomFiliereSelect.all(by.tagName('option')).last().click();
  }

  async nomFiliereSelectOption(option: string): Promise<void> {
    await this.nomFiliereSelect.sendKeys(option);
  }

  getNomFiliereSelect(): ElementFinder {
    return this.nomFiliereSelect;
  }

  async getNomFiliereSelectedOption(): Promise<string> {
    return await this.nomFiliereSelect.element(by.css('option:checked')).getText();
  }

  async nomBatimentSelectLastOption(): Promise<void> {
    await this.nomBatimentSelect.all(by.tagName('option')).last().click();
  }

  async nomBatimentSelectOption(option: string): Promise<void> {
    await this.nomBatimentSelect.sendKeys(option);
  }

  getNomBatimentSelect(): ElementFinder {
    return this.nomBatimentSelect;
  }

  async getNomBatimentSelectedOption(): Promise<string> {
    return await this.nomBatimentSelect.element(by.css('option:checked')).getText();
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

export class AtelierDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-atelier-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-atelier'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
