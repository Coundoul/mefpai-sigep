import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  ProjetAttributionComponentsPage,
  ProjetAttributionDeleteDialog,
  ProjetAttributionUpdatePage,
} from './projet-attribution.page-object';

const expect = chai.expect;

describe('ProjetAttribution e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let projetAttributionComponentsPage: ProjetAttributionComponentsPage;
  let projetAttributionUpdatePage: ProjetAttributionUpdatePage;
  let projetAttributionDeleteDialog: ProjetAttributionDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ProjetAttributions', async () => {
    await navBarPage.goToEntity('projet-attribution');
    projetAttributionComponentsPage = new ProjetAttributionComponentsPage();
    await browser.wait(ec.visibilityOf(projetAttributionComponentsPage.title), 5000);
    expect(await projetAttributionComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.projetAttribution.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(projetAttributionComponentsPage.entities), ec.visibilityOf(projetAttributionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ProjetAttribution page', async () => {
    await projetAttributionComponentsPage.clickOnCreateButton();
    projetAttributionUpdatePage = new ProjetAttributionUpdatePage();
    expect(await projetAttributionUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.projetAttribution.home.createOrEditLabel');
    await projetAttributionUpdatePage.cancel();
  });

  it('should create and save ProjetAttributions', async () => {
    const nbButtonsBeforeCreate = await projetAttributionComponentsPage.countDeleteButtons();

    await projetAttributionComponentsPage.clickOnCreateButton();

    await promise.all([
      projetAttributionUpdatePage.setDateAttributionInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      projetAttributionUpdatePage.setQuantiteInput('5'),
      projetAttributionUpdatePage.setIdEquipementInput('5'),
      projetAttributionUpdatePage.setIdPersInput('5'),
      projetAttributionUpdatePage.nomProjetSelectLastOption(),
    ]);

    expect(await projetAttributionUpdatePage.getDateAttributionInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dateAttribution value to be equals to 2000-12-31'
    );
    expect(await projetAttributionUpdatePage.getQuantiteInput()).to.eq('5', 'Expected quantite value to be equals to 5');
    expect(await projetAttributionUpdatePage.getIdEquipementInput()).to.eq('5', 'Expected idEquipement value to be equals to 5');
    expect(await projetAttributionUpdatePage.getIdPersInput()).to.eq('5', 'Expected idPers value to be equals to 5');

    await projetAttributionUpdatePage.save();
    expect(await projetAttributionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await projetAttributionComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ProjetAttribution', async () => {
    const nbButtonsBeforeDelete = await projetAttributionComponentsPage.countDeleteButtons();
    await projetAttributionComponentsPage.clickOnLastDeleteButton();

    projetAttributionDeleteDialog = new ProjetAttributionDeleteDialog();
    expect(await projetAttributionDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.projetAttribution.delete.question');
    await projetAttributionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(projetAttributionComponentsPage.title), 5000);

    expect(await projetAttributionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
