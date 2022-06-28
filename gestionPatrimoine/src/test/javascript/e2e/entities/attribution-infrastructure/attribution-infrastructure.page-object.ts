import { element, by, ElementFinder } from 'protractor';

export class AttributionInfrastructureComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-attribution-infrastructure div table .btn-danger'));
  title = element.all(by.css('jhi-attribution-infrastructure div h2#page-heading span')).first();
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

export class AttributionInfrastructureUpdatePage {
  pageTitle = element(by.id('jhi-attribution-infrastructure-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  dateAttributionInput = element(by.id('field_dateAttribution'));
  quantiteInput = element(by.id('field_quantite'));
  idEquipementInput = element(by.id('field_idEquipement'));
  idPersInput = element(by.id('field_idPers'));

  nomEtablissementSelect = element(by.id('field_nomEtablissement'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDateAttributionInput(dateAttribution: string): Promise<void> {
    await this.dateAttributionInput.sendKeys(dateAttribution);
  }

  async getDateAttributionInput(): Promise<string> {
    return await this.dateAttributionInput.getAttribute('value');
  }

  async setQuantiteInput(quantite: string): Promise<void> {
    await this.quantiteInput.sendKeys(quantite);
  }

  async getQuantiteInput(): Promise<string> {
    return await this.quantiteInput.getAttribute('value');
  }

  async setIdEquipementInput(idEquipement: string): Promise<void> {
    await this.idEquipementInput.sendKeys(idEquipement);
  }

  async getIdEquipementInput(): Promise<string> {
    return await this.idEquipementInput.getAttribute('value');
  }

  async setIdPersInput(idPers: string): Promise<void> {
    await this.idPersInput.sendKeys(idPers);
  }

  async getIdPersInput(): Promise<string> {
    return await this.idPersInput.getAttribute('value');
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

export class AttributionInfrastructureDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-attributionInfrastructure-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-attributionInfrastructure'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
