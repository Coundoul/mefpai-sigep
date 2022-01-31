import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ChefMaintenanceComponentsPage, ChefMaintenanceDeleteDialog, ChefMaintenanceUpdatePage } from './chef-maintenance.page-object';

const expect = chai.expect;

describe('ChefMaintenance e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let chefMaintenanceComponentsPage: ChefMaintenanceComponentsPage;
  let chefMaintenanceUpdatePage: ChefMaintenanceUpdatePage;
  let chefMaintenanceDeleteDialog: ChefMaintenanceDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ChefMaintenances', async () => {
    await navBarPage.goToEntity('chef-maintenance');
    chefMaintenanceComponentsPage = new ChefMaintenanceComponentsPage();
    await browser.wait(ec.visibilityOf(chefMaintenanceComponentsPage.title), 5000);
    expect(await chefMaintenanceComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.chefMaintenance.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(chefMaintenanceComponentsPage.entities), ec.visibilityOf(chefMaintenanceComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ChefMaintenance page', async () => {
    await chefMaintenanceComponentsPage.clickOnCreateButton();
    chefMaintenanceUpdatePage = new ChefMaintenanceUpdatePage();
    expect(await chefMaintenanceUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.chefMaintenance.home.createOrEditLabel');
    await chefMaintenanceUpdatePage.cancel();
  });

  it('should create and save ChefMaintenances', async () => {
    const nbButtonsBeforeCreate = await chefMaintenanceComponentsPage.countDeleteButtons();

    await chefMaintenanceComponentsPage.clickOnCreateButton();

    await promise.all([
      chefMaintenanceUpdatePage.setNomPersInput('nomPers'),
      chefMaintenanceUpdatePage.setPrenomPersInput('prenomPers'),
      chefMaintenanceUpdatePage.sexeSelectLastOption(),
      chefMaintenanceUpdatePage.setMobileInput('mobile'),
      chefMaintenanceUpdatePage.setAdresseInput('adresse'),
      chefMaintenanceUpdatePage.directionSelectLastOption(),
    ]);

    expect(await chefMaintenanceUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await chefMaintenanceUpdatePage.getPrenomPersInput()).to.eq(
      'prenomPers',
      'Expected PrenomPers value to be equals to prenomPers'
    );
    expect(await chefMaintenanceUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await chefMaintenanceUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await chefMaintenanceUpdatePage.save();
    expect(await chefMaintenanceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await chefMaintenanceComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ChefMaintenance', async () => {
    const nbButtonsBeforeDelete = await chefMaintenanceComponentsPage.countDeleteButtons();
    await chefMaintenanceComponentsPage.clickOnLastDeleteButton();

    chefMaintenanceDeleteDialog = new ChefMaintenanceDeleteDialog();
    expect(await chefMaintenanceDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.chefMaintenance.delete.question');
    await chefMaintenanceDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(chefMaintenanceComponentsPage.title), 5000);

    expect(await chefMaintenanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
