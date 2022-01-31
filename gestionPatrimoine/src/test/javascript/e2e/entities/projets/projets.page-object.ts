import { element, by, ElementFinder } from 'protractor';

export class ProjetsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-projets div table .btn-danger'));
  title = element.all(by.css('jhi-projets div h2#page-heading span')).first();
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

export class ProjetsUpdatePage {
  pageTitle = element(by.id('jhi-projets-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  typeProjetSelect = element(by.id('field_typeProjet'));
  nomProjetInput = element(by.id('field_nomProjet'));
  dateDebutInput = element(by.id('field_dateDebut'));
  dateFinInput = element(by.id('field_dateFin'));
  descriptionInput = element(by.id('field_description'));
  extensionInput = element(by.id('field_extension'));

  nomSelect = element(by.id('field_nom'));
  nomEtablissementSelect = element(by.id('field_nomEtablissement'));
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

  async setTypeProjetSelect(typeProjet: string): Promise<void> {
    await this.typeProjetSelect.sendKeys(typeProjet);
  }

  async getTypeProjetSelect(): Promise<string> {
    return await this.typeProjetSelect.element(by.css('option:checked')).getText();
  }

  async typeProjetSelectLastOption(): Promise<void> {
    await this.typeProjetSelect.all(by.tagName('option')).last().click();
  }

  async setNomProjetInput(nomProjet: string): Promise<void> {
    await this.nomProjetInput.sendKeys(nomProjet);
  }

  async getNomProjetInput(): Promise<string> {
    return await this.nomProjetInput.getAttribute('value');
  }

  async setDateDebutInput(dateDebut: string): Promise<void> {
    await this.dateDebutInput.sendKeys(dateDebut);
  }

  async getDateDebutInput(): Promise<string> {
    return await this.dateDebutInput.getAttribute('value');
  }

  async setDateFinInput(dateFin: string): Promise<void> {
    await this.dateFinInput.sendKeys(dateFin);
  }

  async getDateFinInput(): Promise<string> {
    return await this.dateFinInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  getExtensionInput(): ElementFinder {
    return this.extensionInput;
  }

  async nomSelectLastOption(): Promise<void> {
    await this.nomSelect.all(by.tagName('option')).last().click();
  }

  async nomSelectOption(option: string): Promise<void> {
    await this.nomSelect.sendKeys(option);
  }

  getNomSelect(): ElementFinder {
    return this.nomSelect;
  }

  async getNomSelectedOption(): Promise<string> {
    return await this.nomSelect.element(by.css('option:checked')).getText();
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

export class ProjetsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-projets-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-projets'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
