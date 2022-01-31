import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SallesComponentsPage, SallesDeleteDialog, SallesUpdatePage } from './salles.page-object';

const expect = chai.expect;

describe('Salles e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sallesComponentsPage: SallesComponentsPage;
  let sallesUpdatePage: SallesUpdatePage;
  let sallesDeleteDialog: SallesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Salles', async () => {
    await navBarPage.goToEntity('salles');
    sallesComponentsPage = new SallesComponentsPage();
    await browser.wait(ec.visibilityOf(sallesComponentsPage.title), 5000);
    expect(await sallesComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.salles.home.title');
    await browser.wait(ec.or(ec.visibilityOf(sallesComponentsPage.entities), ec.visibilityOf(sallesComponentsPage.noResult)), 1000);
  });

  it('should load create Salles page', async () => {
    await sallesComponentsPage.clickOnCreateButton();
    sallesUpdatePage = new SallesUpdatePage();
    expect(await sallesUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.salles.home.createOrEditLabel');
    await sallesUpdatePage.cancel();
  });

  it('should create and save Salles', async () => {
    const nbButtonsBeforeCreate = await sallesComponentsPage.countDeleteButtons();

    await sallesComponentsPage.clickOnCreateButton();

    await promise.all([
      sallesUpdatePage.setNomSalleInput('nomSalle'),
      sallesUpdatePage.classeSelectLastOption(),
      sallesUpdatePage.nomBatimentSelectLastOption(),
    ]);

    expect(await sallesUpdatePage.getNomSalleInput()).to.eq('nomSalle', 'Expected NomSalle value to be equals to nomSalle');

    await sallesUpdatePage.save();
    expect(await sallesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sallesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Salles', async () => {
    const nbButtonsBeforeDelete = await sallesComponentsPage.countDeleteButtons();
    await sallesComponentsPage.clickOnLastDeleteButton();

    sallesDeleteDialog = new SallesDeleteDialog();
    expect(await sallesDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.salles.delete.question');
    await sallesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sallesComponentsPage.title), 5000);

    expect(await sallesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
