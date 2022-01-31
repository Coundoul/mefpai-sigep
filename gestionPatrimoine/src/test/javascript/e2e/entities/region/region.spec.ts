import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { RegionComponentsPage, RegionDeleteDialog, RegionUpdatePage } from './region.page-object';

const expect = chai.expect;

describe('Region e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let regionComponentsPage: RegionComponentsPage;
  let regionUpdatePage: RegionUpdatePage;
  let regionDeleteDialog: RegionDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Regions', async () => {
    await navBarPage.goToEntity('region');
    regionComponentsPage = new RegionComponentsPage();
    await browser.wait(ec.visibilityOf(regionComponentsPage.title), 5000);
    expect(await regionComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.region.home.title');
    await browser.wait(ec.or(ec.visibilityOf(regionComponentsPage.entities), ec.visibilityOf(regionComponentsPage.noResult)), 1000);
  });

  it('should load create Region page', async () => {
    await regionComponentsPage.clickOnCreateButton();
    regionUpdatePage = new RegionUpdatePage();
    expect(await regionUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.region.home.createOrEditLabel');
    await regionUpdatePage.cancel();
  });

  it('should create and save Regions', async () => {
    const nbButtonsBeforeCreate = await regionComponentsPage.countDeleteButtons();

    await regionComponentsPage.clickOnCreateButton();

    await promise.all([regionUpdatePage.nomRegionSelectLastOption()]);

    await regionUpdatePage.save();
    expect(await regionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await regionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Region', async () => {
    const nbButtonsBeforeDelete = await regionComponentsPage.countDeleteButtons();
    await regionComponentsPage.clickOnLastDeleteButton();

    regionDeleteDialog = new RegionDeleteDialog();
    expect(await regionDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.region.delete.question');
    await regionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(regionComponentsPage.title), 5000);

    expect(await regionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
