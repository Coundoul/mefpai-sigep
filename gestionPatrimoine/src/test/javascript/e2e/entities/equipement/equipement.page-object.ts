import { element, by, ElementFinder } from 'protractor';

export class EquipementComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-equipement div table .btn-danger'));
  title = element.all(by.css('jhi-equipement div h2#page-heading span')).first();
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

export class EquipementUpdatePage {
  pageTitle = element(by.id('jhi-equipement-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  referenceInput = element(by.id('field_reference'));
  descriptionInput = element(by.id('field_description'));
  prixUnitaireInput = element(by.id('field_prixUnitaire'));
  typeMatiereInput = element(by.id('field_typeMatiere'));
  quantiteInput = element(by.id('field_quantite'));
  etatMatiereInput = element(by.id('field_etatMatiere'));
  groupInput = element(by.id('field_group'));
  photoInput = element(by.id('file_photo'));

  nomMagazinSelect = element(by.id('field_nomMagazin'));
  nomFournisseurSelect = element(by.id('field_nomFournisseur'));
  bonSelect = element(by.id('field_bon'));
  categorieSelect = element(by.id('field_categorie'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setReferenceInput(reference: string): Promise<void> {
    await this.referenceInput.sendKeys(reference);
  }

  async getReferenceInput(): Promise<string> {
    return await this.referenceInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setPrixUnitaireInput(prixUnitaire: string): Promise<void> {
    await this.prixUnitaireInput.sendKeys(prixUnitaire);
  }

  async getPrixUnitaireInput(): Promise<string> {
    return await this.prixUnitaireInput.getAttribute('value');
  }

  async setTypeMatiereInput(typeMatiere: string): Promise<void> {
    await this.typeMatiereInput.sendKeys(typeMatiere);
  }

  async getTypeMatiereInput(): Promise<string> {
    return await this.typeMatiereInput.getAttribute('value');
  }

  async setQuantiteInput(quantite: string): Promise<void> {
    await this.quantiteInput.sendKeys(quantite);
  }

  async getQuantiteInput(): Promise<string> {
    return await this.quantiteInput.getAttribute('value');
  }

  async setEtatMatiereInput(etatMatiere: string): Promise<void> {
    await this.etatMatiereInput.sendKeys(etatMatiere);
  }

  async getEtatMatiereInput(): Promise<string> {
    return await this.etatMatiereInput.getAttribute('value');
  }

  getGroupInput(): ElementFinder {
    return this.groupInput;
  }

  async setPhotoInput(photo: string): Promise<void> {
    await this.photoInput.sendKeys(photo);
  }

  async getPhotoInput(): Promise<string> {
    return await this.photoInput.getAttribute('value');
  }

  async nomMagazinSelectLastOption(): Promise<void> {
    await this.nomMagazinSelect.all(by.tagName('option')).last().click();
  }

  async nomMagazinSelectOption(option: string): Promise<void> {
    await this.nomMagazinSelect.sendKeys(option);
  }

  getNomMagazinSelect(): ElementFinder {
    return this.nomMagazinSelect;
  }

  async getNomMagazinSelectedOption(): Promise<string> {
    return await this.nomMagazinSelect.element(by.css('option:checked')).getText();
  }

  async nomFournisseurSelectLastOption(): Promise<void> {
    await this.nomFournisseurSelect.all(by.tagName('option')).last().click();
  }

  async nomFournisseurSelectOption(option: string): Promise<void> {
    await this.nomFournisseurSelect.sendKeys(option);
  }

  getNomFournisseurSelect(): ElementFinder {
    return this.nomFournisseurSelect;
  }

  async getNomFournisseurSelectedOption(): Promise<string> {
    return await this.nomFournisseurSelect.element(by.css('option:checked')).getText();
  }

  async bonSelectLastOption(): Promise<void> {
    await this.bonSelect.all(by.tagName('option')).last().click();
  }

  async bonSelectOption(option: string): Promise<void> {
    await this.bonSelect.sendKeys(option);
  }

  getBonSelect(): ElementFinder {
    return this.bonSelect;
  }

  async getBonSelectedOption(): Promise<string> {
    return await this.bonSelect.element(by.css('option:checked')).getText();
  }

  async categorieSelectLastOption(): Promise<void> {
    await this.categorieSelect.all(by.tagName('option')).last().click();
  }

  async categorieSelectOption(option: string): Promise<void> {
    await this.categorieSelect.sendKeys(option);
  }

  getCategorieSelect(): ElementFinder {
    return this.categorieSelect;
  }

  async getCategorieSelectedOption(): Promise<string> {
    return await this.categorieSelect.element(by.css('option:checked')).getText();
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

export class EquipementDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-equipement-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-equipement'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
