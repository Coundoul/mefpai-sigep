import { element, by, ElementFinder } from 'protractor';

export class OrdonnaceurMatiereComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-ordonnaceur-matiere div table .btn-danger'));
  title = element.all(by.css('jhi-ordonnaceur-matiere div h2#page-heading span')).first();
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

export class OrdonnaceurMatiereUpdatePage {
  pageTitle = element(by.id('jhi-ordonnaceur-matiere-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomPersInput = element(by.id('field_nomPers'));
  prenomPersInput = element(by.id('field_prenomPers'));
  sexeSelect = element(by.id('field_sexe'));
  mobileInput = element(by.id('field_mobile'));
  adresseInput = element(by.id('field_adresse'));
  directionSelect = element(by.id('field_direction'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomPersInput(nomPers: string): Promise<void> {
    await this.nomPersInput.sendKeys(nomPers);
  }

  async getNomPersInput(): Promise<string> {
    return await this.nomPersInput.getAttribute('value');
  }

  async setPrenomPersInput(prenomPers: string): Promise<void> {
    await this.prenomPersInput.sendKeys(prenomPers);
  }

  async getPrenomPersInput(): Promise<string> {
    return await this.prenomPersInput.getAttribute('value');
  }

  async setSexeSelect(sexe: string): Promise<void> {
    await this.sexeSelect.sendKeys(sexe);
  }

  async getSexeSelect(): Promise<string> {
    return await this.sexeSelect.element(by.css('option:checked')).getText();
  }

  async sexeSelectLastOption(): Promise<void> {
    await this.sexeSelect.all(by.tagName('option')).last().click();
  }

  async setMobileInput(mobile: string): Promise<void> {
    await this.mobileInput.sendKeys(mobile);
  }

  async getMobileInput(): Promise<string> {
    return await this.mobileInput.getAttribute('value');
  }

  async setAdresseInput(adresse: string): Promise<void> {
    await this.adresseInput.sendKeys(adresse);
  }

  async getAdresseInput(): Promise<string> {
    return await this.adresseInput.getAttribute('value');
  }

  async setDirectionSelect(direction: string): Promise<void> {
    await this.directionSelect.sendKeys(direction);
  }

  async getDirectionSelect(): Promise<string> {
    return await this.directionSelect.element(by.css('option:checked')).getText();
  }

  async directionSelectLastOption(): Promise<void> {
    await this.directionSelect.all(by.tagName('option')).last().click();
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

export class OrdonnaceurMatiereDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-ordonnaceurMatiere-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-ordonnaceurMatiere'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
