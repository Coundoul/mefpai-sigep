import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EtablissementComponentsPage, EtablissementDeleteDialog, EtablissementUpdatePage } from './etablissement.page-object';

const expect = chai.expect;

describe('Etablissement e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let etablissementComponentsPage: EtablissementComponentsPage;
  let etablissementUpdatePage: EtablissementUpdatePage;
  let etablissementDeleteDialog: EtablissementDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Etablissements', async () => {
    await navBarPage.goToEntity('etablissement');
    etablissementComponentsPage = new EtablissementComponentsPage();
    await browser.wait(ec.visibilityOf(etablissementComponentsPage.title), 5000);
    expect(await etablissementComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.etablissement.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(etablissementComponentsPage.entities), ec.visibilityOf(etablissementComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Etablissement page', async () => {
    await etablissementComponentsPage.clickOnCreateButton();
    etablissementUpdatePage = new EtablissementUpdatePage();
    expect(await etablissementUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.etablissement.home.createOrEditLabel');
    await etablissementUpdatePage.cancel();
  });

  it('should create and save Etablissements', async () => {
    const nbButtonsBeforeCreate = await etablissementComponentsPage.countDeleteButtons();

    await etablissementComponentsPage.clickOnCreateButton();

    await promise.all([
      etablissementUpdatePage.setNomEtablissementInput('nomEtablissement'),
      etablissementUpdatePage.setSurfaceBatieInput('5'),
      etablissementUpdatePage.setSuperficieInput('5'),
      etablissementUpdatePage.setIdPersInput('5'),
      etablissementUpdatePage.nomQuartierSelectLastOption(),
    ]);

    expect(await etablissementUpdatePage.getNomEtablissementInput()).to.eq(
      'nomEtablissement',
      'Expected NomEtablissement value to be equals to nomEtablissement'
    );
    expect(await etablissementUpdatePage.getSurfaceBatieInput()).to.eq('5', 'Expected surfaceBatie value to be equals to 5');
    expect(await etablissementUpdatePage.getSuperficieInput()).to.eq('5', 'Expected superficie value to be equals to 5');
    expect(await etablissementUpdatePage.getIdPersInput()).to.eq('5', 'Expected idPers value to be equals to 5');

    await etablissementUpdatePage.save();
    expect(await etablissementUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await etablissementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Etablissement', async () => {
    const nbButtonsBeforeDelete = await etablissementComponentsPage.countDeleteButtons();
    await etablissementComponentsPage.clickOnLastDeleteButton();

    etablissementDeleteDialog = new EtablissementDeleteDialog();
    expect(await etablissementDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.etablissement.delete.question');
    await etablissementDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(etablissementComponentsPage.title), 5000);

    expect(await etablissementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
