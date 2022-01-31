import { element, by, ElementFinder } from 'protractor';

export class IntervenantComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-intervenant div table .btn-danger'));
  title = element.all(by.css('jhi-intervenant div h2#page-heading span')).first();
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

export class IntervenantUpdatePage {
  pageTitle = element(by.id('jhi-intervenant-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomIntervenantInput = element(by.id('field_nomIntervenant'));
  prenomIntervenantInput = element(by.id('field_prenomIntervenant'));
  emailProfessionnelInput = element(by.id('field_emailProfessionnel'));
  raisonSocialInput = element(by.id('field_raisonSocial'));
  maitreSelect = element(by.id('field_maitre'));
  roleInput = element(by.id('field_role'));

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

  async setNomIntervenantInput(nomIntervenant: string): Promise<void> {
    await this.nomIntervenantInput.sendKeys(nomIntervenant);
  }

  async getNomIntervenantInput(): Promise<string> {
    return await this.nomIntervenantInput.getAttribute('value');
  }

  async setPrenomIntervenantInput(prenomIntervenant: string): Promise<void> {
    await this.prenomIntervenantInput.sendKeys(prenomIntervenant);
  }

  async getPrenomIntervenantInput(): Promise<string> {
    return await this.prenomIntervenantInput.getAttribute('value');
  }

  async setEmailProfessionnelInput(emailProfessionnel: string): Promise<void> {
    await this.emailProfessionnelInput.sendKeys(emailProfessionnel);
  }

  async getEmailProfessionnelInput(): Promise<string> {
    return await this.emailProfessionnelInput.getAttribute('value');
  }

  async setRaisonSocialInput(raisonSocial: string): Promise<void> {
    await this.raisonSocialInput.sendKeys(raisonSocial);
  }

  async getRaisonSocialInput(): Promise<string> {
    return await this.raisonSocialInput.getAttribute('value');
  }

  async setMaitreSelect(maitre: string): Promise<void> {
    await this.maitreSelect.sendKeys(maitre);
  }

  async getMaitreSelect(): Promise<string> {
    return await this.maitreSelect.element(by.css('option:checked')).getText();
  }

  async maitreSelectLastOption(): Promise<void> {
    await this.maitreSelect.all(by.tagName('option')).last().click();
  }

  async setRoleInput(role: string): Promise<void> {
    await this.roleInput.sendKeys(role);
  }

  async getRoleInput(): Promise<string> {
    return await this.roleInput.getAttribute('value');
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

export class IntervenantDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-intervenant-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-intervenant'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
