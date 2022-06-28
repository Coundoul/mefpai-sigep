import { element, by, ElementFinder } from 'protractor';

export class EtablissementComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-etablissement div table .btn-danger'));
  title = element.all(by.css('jhi-etablissement div h2#page-heading span')).first();
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

export class EtablissementUpdatePage {
  pageTitle = element(by.id('jhi-etablissement-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomEtablissementInput = element(by.id('field_nomEtablissement'));
  surfaceBatieInput = element(by.id('field_surfaceBatie'));
  superficieInput = element(by.id('field_superficie'));
  idPersInput = element(by.id('field_idPers'));

  nomQuartierSelect = element(by.id('field_nomQuartier'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomEtablissementInput(nomEtablissement: string): Promise<void> {
    await this.nomEtablissementInput.sendKeys(nomEtablissement);
  }

  async getNomEtablissementInput(): Promise<string> {
    return await this.nomEtablissementInput.getAttribute('value');
  }

  async setSurfaceBatieInput(surfaceBatie: string): Promise<void> {
    await this.surfaceBatieInput.sendKeys(surfaceBatie);
  }

  async getSurfaceBatieInput(): Promise<string> {
    return await this.surfaceBatieInput.getAttribute('value');
  }

  async setSuperficieInput(superficie: string): Promise<void> {
    await this.superficieInput.sendKeys(superficie);
  }

  async getSuperficieInput(): Promise<string> {
    return await this.superficieInput.getAttribute('value');
  }

  async setIdPersInput(idPers: string): Promise<void> {
    await this.idPersInput.sendKeys(idPers);
  }

  async getIdPersInput(): Promise<string> {
    return await this.idPersInput.getAttribute('value');
  }

  async nomQuartierSelectLastOption(): Promise<void> {
    await this.nomQuartierSelect.all(by.tagName('option')).last().click();
  }

  async nomQuartierSelectOption(option: string): Promise<void> {
    await this.nomQuartierSelect.sendKeys(option);
  }

  getNomQuartierSelect(): ElementFinder {
    return this.nomQuartierSelect;
  }

  async getNomQuartierSelectedOption(): Promise<string> {
    return await this.nomQuartierSelect.element(by.css('option:checked')).getText();
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

export class EtablissementDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-etablissement-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-etablissement'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
