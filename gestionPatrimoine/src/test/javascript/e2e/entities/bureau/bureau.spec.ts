import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BureauComponentsPage, BureauDeleteDialog, BureauUpdatePage } from './bureau.page-object';

const expect = chai.expect;

describe('Bureau e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let bureauComponentsPage: BureauComponentsPage;
  let bureauUpdatePage: BureauUpdatePage;
  let bureauDeleteDialog: BureauDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Bureaus', async () => {
    await navBarPage.goToEntity('bureau');
    bureauComponentsPage = new BureauComponentsPage();
    await browser.wait(ec.visibilityOf(bureauComponentsPage.title), 5000);
    expect(await bureauComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.bureau.home.title');
    await browser.wait(ec.or(ec.visibilityOf(bureauComponentsPage.entities), ec.visibilityOf(bureauComponentsPage.noResult)), 1000);
  });

  it('should load create Bureau page', async () => {
    await bureauComponentsPage.clickOnCreateButton();
    bureauUpdatePage = new BureauUpdatePage();
    expect(await bureauUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.bureau.home.createOrEditLabel');
    await bureauUpdatePage.cancel();
  });

  it('should create and save Bureaus', async () => {
    const nbButtonsBeforeCreate = await bureauComponentsPage.countDeleteButtons();

    await bureauComponentsPage.clickOnCreateButton();

    await promise.all([
      bureauUpdatePage.nomStructureSelectLastOption(),
      bureauUpdatePage.directionSelectLastOption(),
      bureauUpdatePage.setNomEtablissementInput('nomEtablissement'),
    ]);

    expect(await bureauUpdatePage.getNomEtablissementInput()).to.eq(
      'nomEtablissement',
      'Expected NomEtablissement value to be equals to nomEtablissement'
    );

    await bureauUpdatePage.save();
    expect(await bureauUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await bureauComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Bureau', async () => {
    const nbButtonsBeforeDelete = await bureauComponentsPage.countDeleteButtons();
    await bureauComponentsPage.clickOnLastDeleteButton();

    bureauDeleteDialog = new BureauDeleteDialog();
    expect(await bureauDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.bureau.delete.question');
    await bureauDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(bureauComponentsPage.title), 5000);

    expect(await bureauComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
