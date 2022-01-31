import { element, by, ElementFinder } from 'protractor';

export class FournisseurComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fournisseur div table .btn-danger'));
  title = element.all(by.css('jhi-fournisseur div h2#page-heading span')).first();
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

export class FournisseurUpdatePage {
  pageTitle = element(by.id('jhi-fournisseur-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  codeFournisseuerInput = element(by.id('field_codeFournisseuer'));
  nomFournisseurInput = element(by.id('field_nomFournisseur'));
  prenomFournisseurInput = element(by.id('field_prenomFournisseur'));
  sexeSelect = element(by.id('field_sexe'));
  raisonSocialInput = element(by.id('field_raisonSocial'));
  adresseInput = element(by.id('field_adresse'));
  num1Input = element(by.id('field_num1'));
  num2Input = element(by.id('field_num2'));
  villeInput = element(by.id('field_ville'));
  emailInput = element(by.id('field_email'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCodeFournisseuerInput(codeFournisseuer: string): Promise<void> {
    await this.codeFournisseuerInput.sendKeys(codeFournisseuer);
  }

  async getCodeFournisseuerInput(): Promise<string> {
    return await this.codeFournisseuerInput.getAttribute('value');
  }

  async setNomFournisseurInput(nomFournisseur: string): Promise<void> {
    await this.nomFournisseurInput.sendKeys(nomFournisseur);
  }

  async getNomFournisseurInput(): Promise<string> {
    return await this.nomFournisseurInput.getAttribute('value');
  }

  async setPrenomFournisseurInput(prenomFournisseur: string): Promise<void> {
    await this.prenomFournisseurInput.sendKeys(prenomFournisseur);
  }

  async getPrenomFournisseurInput(): Promise<string> {
    return await this.prenomFournisseurInput.getAttribute('value');
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

  async setRaisonSocialInput(raisonSocial: string): Promise<void> {
    await this.raisonSocialInput.sendKeys(raisonSocial);
  }

  async getRaisonSocialInput(): Promise<string> {
    return await this.raisonSocialInput.getAttribute('value');
  }

  async setAdresseInput(adresse: string): Promise<void> {
    await this.adresseInput.sendKeys(adresse);
  }

  async getAdresseInput(): Promise<string> {
    return await this.adresseInput.getAttribute('value');
  }

  async setNum1Input(num1: string): Promise<void> {
    await this.num1Input.sendKeys(num1);
  }

  async getNum1Input(): Promise<string> {
    return await this.num1Input.getAttribute('value');
  }

  async setNum2Input(num2: string): Promise<void> {
    await this.num2Input.sendKeys(num2);
  }

  async getNum2Input(): Promise<string> {
    return await this.num2Input.getAttribute('value');
  }

  async setVilleInput(ville: string): Promise<void> {
    await this.villeInput.sendKeys(ville);
  }

  async getVilleInput(): Promise<string> {
    return await this.villeInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
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

export class FournisseurDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fournisseur-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fournisseur'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
