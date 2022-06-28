import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CommuneComponentsPage, CommuneDeleteDialog, CommuneUpdatePage } from './commune.page-object';

const expect = chai.expect;

describe('Commune e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let communeComponentsPage: CommuneComponentsPage;
  let communeUpdatePage: CommuneUpdatePage;
  let communeDeleteDialog: CommuneDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Communes', async () => {
    await navBarPage.goToEntity('commune');
    communeComponentsPage = new CommuneComponentsPage();
    await browser.wait(ec.visibilityOf(communeComponentsPage.title), 5000);
    expect(await communeComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.commune.home.title');
    await browser.wait(ec.or(ec.visibilityOf(communeComponentsPage.entities), ec.visibilityOf(communeComponentsPage.noResult)), 1000);
  });

  it('should load create Commune page', async () => {
    await communeComponentsPage.clickOnCreateButton();
    communeUpdatePage = new CommuneUpdatePage();
    expect(await communeUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.commune.home.createOrEditLabel');
    await communeUpdatePage.cancel();
  });

  it('should create and save Communes', async () => {
    const nbButtonsBeforeCreate = await communeComponentsPage.countDeleteButtons();

    await communeComponentsPage.clickOnCreateButton();

    await promise.all([communeUpdatePage.setNomCommuneInput('nomCommune'), communeUpdatePage.nomDepartementSelectLastOption()]);

    expect(await communeUpdatePage.getNomCommuneInput()).to.eq('nomCommune', 'Expected NomCommune value to be equals to nomCommune');

    await communeUpdatePage.save();
    expect(await communeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await communeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Commune', async () => {
    const nbButtonsBeforeDelete = await communeComponentsPage.countDeleteButtons();
    await communeComponentsPage.clickOnLastDeleteButton();

    communeDeleteDialog = new CommuneDeleteDialog();
    expect(await communeDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.commune.delete.question');
    await communeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(communeComponentsPage.title), 5000);

    expect(await communeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
