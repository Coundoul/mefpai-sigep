import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DirecteurComponentsPage, DirecteurDeleteDialog, DirecteurUpdatePage } from './directeur.page-object';

const expect = chai.expect;

describe('Directeur e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let directeurComponentsPage: DirecteurComponentsPage;
  let directeurUpdatePage: DirecteurUpdatePage;
  let directeurDeleteDialog: DirecteurDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Directeurs', async () => {
    await navBarPage.goToEntity('directeur');
    directeurComponentsPage = new DirecteurComponentsPage();
    await browser.wait(ec.visibilityOf(directeurComponentsPage.title), 5000);
    expect(await directeurComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.directeur.home.title');
    await browser.wait(ec.or(ec.visibilityOf(directeurComponentsPage.entities), ec.visibilityOf(directeurComponentsPage.noResult)), 1000);
  });

  it('should load create Directeur page', async () => {
    await directeurComponentsPage.clickOnCreateButton();
    directeurUpdatePage = new DirecteurUpdatePage();
    expect(await directeurUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.directeur.home.createOrEditLabel');
    await directeurUpdatePage.cancel();
  });

  it('should create and save Directeurs', async () => {
    const nbButtonsBeforeCreate = await directeurComponentsPage.countDeleteButtons();

    await directeurComponentsPage.clickOnCreateButton();

    await promise.all([
      directeurUpdatePage.setNomPersInput('nomPers'),
      directeurUpdatePage.setPrenomPersInput('prenomPers'),
      directeurUpdatePage.sexeSelectLastOption(),
      directeurUpdatePage.setMobileInput('mobile'),
      directeurUpdatePage.setAdresseInput('adresse'),
      directeurUpdatePage.directionSelectLastOption(),
    ]);

    expect(await directeurUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await directeurUpdatePage.getPrenomPersInput()).to.eq('prenomPers', 'Expected PrenomPers value to be equals to prenomPers');
    expect(await directeurUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await directeurUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await directeurUpdatePage.save();
    expect(await directeurUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await directeurComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Directeur', async () => {
    const nbButtonsBeforeDelete = await directeurComponentsPage.countDeleteButtons();
    await directeurComponentsPage.clickOnLastDeleteButton();

    directeurDeleteDialog = new DirecteurDeleteDialog();
    expect(await directeurDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.directeur.delete.question');
    await directeurDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(directeurComponentsPage.title), 5000);

    expect(await directeurComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
