import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { IntendantComponentsPage, IntendantDeleteDialog, IntendantUpdatePage } from './intendant.page-object';

const expect = chai.expect;

describe('Intendant e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let intendantComponentsPage: IntendantComponentsPage;
  let intendantUpdatePage: IntendantUpdatePage;
  let intendantDeleteDialog: IntendantDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Intendants', async () => {
    await navBarPage.goToEntity('intendant');
    intendantComponentsPage = new IntendantComponentsPage();
    await browser.wait(ec.visibilityOf(intendantComponentsPage.title), 5000);
    expect(await intendantComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.intendant.home.title');
    await browser.wait(ec.or(ec.visibilityOf(intendantComponentsPage.entities), ec.visibilityOf(intendantComponentsPage.noResult)), 1000);
  });

  it('should load create Intendant page', async () => {
    await intendantComponentsPage.clickOnCreateButton();
    intendantUpdatePage = new IntendantUpdatePage();
    expect(await intendantUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.intendant.home.createOrEditLabel');
    await intendantUpdatePage.cancel();
  });

  it('should create and save Intendants', async () => {
    const nbButtonsBeforeCreate = await intendantComponentsPage.countDeleteButtons();

    await intendantComponentsPage.clickOnCreateButton();

    await promise.all([
      intendantUpdatePage.setNomPersInput('nomPers'),
      intendantUpdatePage.setPrenomPersInput('prenomPers'),
      intendantUpdatePage.sexeSelectLastOption(),
      intendantUpdatePage.setMobileInput('mobile'),
      intendantUpdatePage.setAdresseInput('adresse'),
    ]);

    expect(await intendantUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await intendantUpdatePage.getPrenomPersInput()).to.eq('prenomPers', 'Expected PrenomPers value to be equals to prenomPers');
    expect(await intendantUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await intendantUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await intendantUpdatePage.save();
    expect(await intendantUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await intendantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Intendant', async () => {
    const nbButtonsBeforeDelete = await intendantComponentsPage.countDeleteButtons();
    await intendantComponentsPage.clickOnLastDeleteButton();

    intendantDeleteDialog = new IntendantDeleteDialog();
    expect(await intendantDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.intendant.delete.question');
    await intendantDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(intendantComponentsPage.title), 5000);

    expect(await intendantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
