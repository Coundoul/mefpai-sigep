import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FormateursComponentsPage, FormateursDeleteDialog, FormateursUpdatePage } from './formateurs.page-object';

const expect = chai.expect;

describe('Formateurs e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let formateursComponentsPage: FormateursComponentsPage;
  let formateursUpdatePage: FormateursUpdatePage;
  let formateursDeleteDialog: FormateursDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Formateurs', async () => {
    await navBarPage.goToEntity('formateurs');
    formateursComponentsPage = new FormateursComponentsPage();
    await browser.wait(ec.visibilityOf(formateursComponentsPage.title), 5000);
    expect(await formateursComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.formateurs.home.title');
    await browser.wait(ec.or(ec.visibilityOf(formateursComponentsPage.entities), ec.visibilityOf(formateursComponentsPage.noResult)), 1000);
  });

  it('should load create Formateurs page', async () => {
    await formateursComponentsPage.clickOnCreateButton();
    formateursUpdatePage = new FormateursUpdatePage();
    expect(await formateursUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.formateurs.home.createOrEditLabel');
    await formateursUpdatePage.cancel();
  });

  it('should create and save Formateurs', async () => {
    const nbButtonsBeforeCreate = await formateursComponentsPage.countDeleteButtons();

    await formateursComponentsPage.clickOnCreateButton();

    await promise.all([
      formateursUpdatePage.setNomFormateurInput('nomFormateur'),
      formateursUpdatePage.setPrenomFormateurInput('prenomFormateur'),
      formateursUpdatePage.setEmailInput('email'),
      formateursUpdatePage.setNumb1Input('numb1'),
      formateursUpdatePage.setNumb2Input('numb2'),
      formateursUpdatePage.setAdresseInput('adresse'),
      formateursUpdatePage.setVilleInput('ville'),
      formateursUpdatePage.setSpecialiteInput('specialite'),
      formateursUpdatePage.nomEtablissementSelectLastOption(),
    ]);

    expect(await formateursUpdatePage.getNomFormateurInput()).to.eq(
      'nomFormateur',
      'Expected NomFormateur value to be equals to nomFormateur'
    );
    expect(await formateursUpdatePage.getPrenomFormateurInput()).to.eq(
      'prenomFormateur',
      'Expected PrenomFormateur value to be equals to prenomFormateur'
    );
    expect(await formateursUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await formateursUpdatePage.getNumb1Input()).to.eq('numb1', 'Expected Numb1 value to be equals to numb1');
    expect(await formateursUpdatePage.getNumb2Input()).to.eq('numb2', 'Expected Numb2 value to be equals to numb2');
    expect(await formateursUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');
    expect(await formateursUpdatePage.getVilleInput()).to.eq('ville', 'Expected Ville value to be equals to ville');
    expect(await formateursUpdatePage.getSpecialiteInput()).to.eq('specialite', 'Expected Specialite value to be equals to specialite');

    await formateursUpdatePage.save();
    expect(await formateursUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await formateursComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Formateurs', async () => {
    const nbButtonsBeforeDelete = await formateursComponentsPage.countDeleteButtons();
    await formateursComponentsPage.clickOnLastDeleteButton();

    formateursDeleteDialog = new FormateursDeleteDialog();
    expect(await formateursDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.formateurs.delete.question');
    await formateursDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(formateursComponentsPage.title), 5000);

    expect(await formateursComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
