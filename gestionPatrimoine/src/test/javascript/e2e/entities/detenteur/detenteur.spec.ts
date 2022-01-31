import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DetenteurComponentsPage, DetenteurDeleteDialog, DetenteurUpdatePage } from './detenteur.page-object';

const expect = chai.expect;

describe('Detenteur e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let detenteurComponentsPage: DetenteurComponentsPage;
  let detenteurUpdatePage: DetenteurUpdatePage;
  let detenteurDeleteDialog: DetenteurDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Detenteurs', async () => {
    await navBarPage.goToEntity('detenteur');
    detenteurComponentsPage = new DetenteurComponentsPage();
    await browser.wait(ec.visibilityOf(detenteurComponentsPage.title), 5000);
    expect(await detenteurComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.detenteur.home.title');
    await browser.wait(ec.or(ec.visibilityOf(detenteurComponentsPage.entities), ec.visibilityOf(detenteurComponentsPage.noResult)), 1000);
  });

  it('should load create Detenteur page', async () => {
    await detenteurComponentsPage.clickOnCreateButton();
    detenteurUpdatePage = new DetenteurUpdatePage();
    expect(await detenteurUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.detenteur.home.createOrEditLabel');
    await detenteurUpdatePage.cancel();
  });

  it('should create and save Detenteurs', async () => {
    const nbButtonsBeforeCreate = await detenteurComponentsPage.countDeleteButtons();

    await detenteurComponentsPage.clickOnCreateButton();

    await promise.all([
      detenteurUpdatePage.setNomPersInput('nomPers'),
      detenteurUpdatePage.setPrenomPersInput('prenomPers'),
      detenteurUpdatePage.sexeSelectLastOption(),
      detenteurUpdatePage.setMobileInput('mobile'),
      detenteurUpdatePage.setAdresseInput('adresse'),
      detenteurUpdatePage.directionSelectLastOption(),
    ]);

    expect(await detenteurUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await detenteurUpdatePage.getPrenomPersInput()).to.eq('prenomPers', 'Expected PrenomPers value to be equals to prenomPers');
    expect(await detenteurUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await detenteurUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await detenteurUpdatePage.save();
    expect(await detenteurUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await detenteurComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Detenteur', async () => {
    const nbButtonsBeforeDelete = await detenteurComponentsPage.countDeleteButtons();
    await detenteurComponentsPage.clickOnLastDeleteButton();

    detenteurDeleteDialog = new DetenteurDeleteDialog();
    expect(await detenteurDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.detenteur.delete.question');
    await detenteurDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(detenteurComponentsPage.title), 5000);

    expect(await detenteurComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
