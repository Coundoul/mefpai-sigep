import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AtelierComponentsPage, AtelierDeleteDialog, AtelierUpdatePage } from './atelier.page-object';

const expect = chai.expect;

describe('Atelier e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let atelierComponentsPage: AtelierComponentsPage;
  let atelierUpdatePage: AtelierUpdatePage;
  let atelierDeleteDialog: AtelierDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Ateliers', async () => {
    await navBarPage.goToEntity('atelier');
    atelierComponentsPage = new AtelierComponentsPage();
    await browser.wait(ec.visibilityOf(atelierComponentsPage.title), 5000);
    expect(await atelierComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.atelier.home.title');
    await browser.wait(ec.or(ec.visibilityOf(atelierComponentsPage.entities), ec.visibilityOf(atelierComponentsPage.noResult)), 1000);
  });

  it('should load create Atelier page', async () => {
    await atelierComponentsPage.clickOnCreateButton();
    atelierUpdatePage = new AtelierUpdatePage();
    expect(await atelierUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.atelier.home.createOrEditLabel');
    await atelierUpdatePage.cancel();
  });

  it('should create and save Ateliers', async () => {
    const nbButtonsBeforeCreate = await atelierComponentsPage.countDeleteButtons();

    await atelierComponentsPage.clickOnCreateButton();

    await promise.all([
      atelierUpdatePage.setNomAtelierInput('nomAtelier'),
      atelierUpdatePage.setSurfaceInput('5'),
      atelierUpdatePage.setDescriptionInput('description'),
      atelierUpdatePage.nomFiliereSelectLastOption(),
      atelierUpdatePage.nomBatimentSelectLastOption(),
    ]);

    expect(await atelierUpdatePage.getNomAtelierInput()).to.eq('nomAtelier', 'Expected NomAtelier value to be equals to nomAtelier');
    expect(await atelierUpdatePage.getSurfaceInput()).to.eq('5', 'Expected surface value to be equals to 5');
    expect(await atelierUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');

    await atelierUpdatePage.save();
    expect(await atelierUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await atelierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Atelier', async () => {
    const nbButtonsBeforeDelete = await atelierComponentsPage.countDeleteButtons();
    await atelierComponentsPage.clickOnLastDeleteButton();

    atelierDeleteDialog = new AtelierDeleteDialog();
    expect(await atelierDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.atelier.delete.question');
    await atelierDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(atelierComponentsPage.title), 5000);

    expect(await atelierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
