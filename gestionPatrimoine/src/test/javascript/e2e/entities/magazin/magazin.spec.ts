import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { MagazinComponentsPage, MagazinDeleteDialog, MagazinUpdatePage } from './magazin.page-object';

const expect = chai.expect;

describe('Magazin e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let magazinComponentsPage: MagazinComponentsPage;
  let magazinUpdatePage: MagazinUpdatePage;
  let magazinDeleteDialog: MagazinDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Magazins', async () => {
    await navBarPage.goToEntity('magazin');
    magazinComponentsPage = new MagazinComponentsPage();
    await browser.wait(ec.visibilityOf(magazinComponentsPage.title), 5000);
    expect(await magazinComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.magazin.home.title');
    await browser.wait(ec.or(ec.visibilityOf(magazinComponentsPage.entities), ec.visibilityOf(magazinComponentsPage.noResult)), 1000);
  });

  it('should load create Magazin page', async () => {
    await magazinComponentsPage.clickOnCreateButton();
    magazinUpdatePage = new MagazinUpdatePage();
    expect(await magazinUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.magazin.home.createOrEditLabel');
    await magazinUpdatePage.cancel();
  });

  it('should create and save Magazins', async () => {
    const nbButtonsBeforeCreate = await magazinComponentsPage.countDeleteButtons();

    await magazinComponentsPage.clickOnCreateButton();

    await promise.all([
      magazinUpdatePage.setNomMagazinInput('nomMagazin'),
      magazinUpdatePage.setSurfaceBatieInput('5'),
      magazinUpdatePage.setSuperficieInput('5'),
      magazinUpdatePage.setIdPersInput('5'),
      magazinUpdatePage.nomQuartierSelectLastOption(),
    ]);

    expect(await magazinUpdatePage.getNomMagazinInput()).to.eq('nomMagazin', 'Expected NomMagazin value to be equals to nomMagazin');
    expect(await magazinUpdatePage.getSurfaceBatieInput()).to.eq('5', 'Expected surfaceBatie value to be equals to 5');
    expect(await magazinUpdatePage.getSuperficieInput()).to.eq('5', 'Expected superficie value to be equals to 5');
    expect(await magazinUpdatePage.getIdPersInput()).to.eq('5', 'Expected idPers value to be equals to 5');

    await magazinUpdatePage.save();
    expect(await magazinUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await magazinComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Magazin', async () => {
    const nbButtonsBeforeDelete = await magazinComponentsPage.countDeleteButtons();
    await magazinComponentsPage.clickOnLastDeleteButton();

    magazinDeleteDialog = new MagazinDeleteDialog();
    expect(await magazinDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.magazin.delete.question');
    await magazinDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(magazinComponentsPage.title), 5000);

    expect(await magazinComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
