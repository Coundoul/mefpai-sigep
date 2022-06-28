import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { QuartierComponentsPage, QuartierDeleteDialog, QuartierUpdatePage } from './quartier.page-object';

const expect = chai.expect;

describe('Quartier e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let quartierComponentsPage: QuartierComponentsPage;
  let quartierUpdatePage: QuartierUpdatePage;
  let quartierDeleteDialog: QuartierDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Quartiers', async () => {
    await navBarPage.goToEntity('quartier');
    quartierComponentsPage = new QuartierComponentsPage();
    await browser.wait(ec.visibilityOf(quartierComponentsPage.title), 5000);
    expect(await quartierComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.quartier.home.title');
    await browser.wait(ec.or(ec.visibilityOf(quartierComponentsPage.entities), ec.visibilityOf(quartierComponentsPage.noResult)), 1000);
  });

  it('should load create Quartier page', async () => {
    await quartierComponentsPage.clickOnCreateButton();
    quartierUpdatePage = new QuartierUpdatePage();
    expect(await quartierUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.quartier.home.createOrEditLabel');
    await quartierUpdatePage.cancel();
  });

  it('should create and save Quartiers', async () => {
    const nbButtonsBeforeCreate = await quartierComponentsPage.countDeleteButtons();

    await quartierComponentsPage.clickOnCreateButton();

    await promise.all([quartierUpdatePage.setNomQuartierInput('nomQuartier'), quartierUpdatePage.nomCommuneSelectLastOption()]);

    expect(await quartierUpdatePage.getNomQuartierInput()).to.eq('nomQuartier', 'Expected NomQuartier value to be equals to nomQuartier');

    await quartierUpdatePage.save();
    expect(await quartierUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await quartierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Quartier', async () => {
    const nbButtonsBeforeDelete = await quartierComponentsPage.countDeleteButtons();
    await quartierComponentsPage.clickOnLastDeleteButton();

    quartierDeleteDialog = new QuartierDeleteDialog();
    expect(await quartierDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.quartier.delete.question');
    await quartierDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(quartierComponentsPage.title), 5000);

    expect(await quartierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
