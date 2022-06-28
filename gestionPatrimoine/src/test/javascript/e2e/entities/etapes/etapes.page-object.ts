import { element, by, ElementFinder } from 'protractor';

export class EtapesComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-etapes div table .btn-danger'));
  title = element.all(by.css('jhi-etapes div h2#page-heading span')).first();
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

export class EtapesUpdatePage {
  pageTitle = element(by.id('jhi-etapes-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  dateDebutInput = element(by.id('field_dateDebut'));
  dateFinInput = element(by.id('field_dateFin'));
  nomTacheInput = element(by.id('field_nomTache'));
  durationInput = element(by.id('field_duration'));

  nomProjetSelect = element(by.id('field_nomProjet'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
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

  async setNomTacheInput(nomTache: string): Promise<void> {
    await this.nomTacheInput.sendKeys(nomTache);
  }

  async getNomTacheInput(): Promise<string> {
    return await this.nomTacheInput.getAttribute('value');
  }

  async setDurationInput(duration: string): Promise<void> {
    await this.durationInput.sendKeys(duration);
  }

  async getDurationInput(): Promise<string> {
    return await this.durationInput.getAttribute('value');
  }

  async nomProjetSelectLastOption(): Promise<void> {
    await this.nomProjetSelect.all(by.tagName('option')).last().click();
  }

  async nomProjetSelectOption(option: string): Promise<void> {
    await this.nomProjetSelect.sendKeys(option);
  }

  getNomProjetSelect(): ElementFinder {
    return this.nomProjetSelect;
  }

  async getNomProjetSelectedOption(): Promise<string> {
    return await this.nomProjetSelect.element(by.css('option:checked')).getText();
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

export class EtapesDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-etapes-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-etapes'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
