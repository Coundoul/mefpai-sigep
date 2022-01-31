import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { TechnicienComponentsPage, TechnicienDeleteDialog, TechnicienUpdatePage } from './technicien.page-object';

const expect = chai.expect;

describe('Technicien e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let technicienComponentsPage: TechnicienComponentsPage;
  let technicienUpdatePage: TechnicienUpdatePage;
  let technicienDeleteDialog: TechnicienDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Techniciens', async () => {
    await navBarPage.goToEntity('technicien');
    technicienComponentsPage = new TechnicienComponentsPage();
    await browser.wait(ec.visibilityOf(technicienComponentsPage.title), 5000);
    expect(await technicienComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.technicien.home.title');
    await browser.wait(ec.or(ec.visibilityOf(technicienComponentsPage.entities), ec.visibilityOf(technicienComponentsPage.noResult)), 1000);
  });

  it('should load create Technicien page', async () => {
    await technicienComponentsPage.clickOnCreateButton();
    technicienUpdatePage = new TechnicienUpdatePage();
    expect(await technicienUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.technicien.home.createOrEditLabel');
    await technicienUpdatePage.cancel();
  });

  it('should create and save Techniciens', async () => {
    const nbButtonsBeforeCreate = await technicienComponentsPage.countDeleteButtons();

    await technicienComponentsPage.clickOnCreateButton();

    await promise.all([
      technicienUpdatePage.setNomPersInput('nomPers'),
      technicienUpdatePage.setPrenomPersInput('prenomPers'),
      technicienUpdatePage.sexeSelectLastOption(),
      technicienUpdatePage.setMobileInput('mobile'),
      technicienUpdatePage.setAdresseInput('adresse'),
      technicienUpdatePage.directionSelectLastOption(),
      technicienUpdatePage.chefMaintenanceSelectLastOption(),
    ]);

    expect(await technicienUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await technicienUpdatePage.getPrenomPersInput()).to.eq('prenomPers', 'Expected PrenomPers value to be equals to prenomPers');
    expect(await technicienUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await technicienUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await technicienUpdatePage.save();
    expect(await technicienUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await technicienComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Technicien', async () => {
    const nbButtonsBeforeDelete = await technicienComponentsPage.countDeleteButtons();
    await technicienComponentsPage.clickOnLastDeleteButton();

    technicienDeleteDialog = new TechnicienDeleteDialog();
    expect(await technicienDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.technicien.delete.question');
    await technicienDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(technicienComponentsPage.title), 5000);

    expect(await technicienComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
