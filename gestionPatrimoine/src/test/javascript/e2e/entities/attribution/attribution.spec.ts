import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AttributionComponentsPage, AttributionDeleteDialog, AttributionUpdatePage } from './attribution.page-object';

const expect = chai.expect;

describe('Attribution e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let attributionComponentsPage: AttributionComponentsPage;
  let attributionUpdatePage: AttributionUpdatePage;
  let attributionDeleteDialog: AttributionDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Attributions', async () => {
    await navBarPage.goToEntity('attribution');
    attributionComponentsPage = new AttributionComponentsPage();
    await browser.wait(ec.visibilityOf(attributionComponentsPage.title), 5000);
    expect(await attributionComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.attribution.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(attributionComponentsPage.entities), ec.visibilityOf(attributionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Attribution page', async () => {
    await attributionComponentsPage.clickOnCreateButton();
    attributionUpdatePage = new AttributionUpdatePage();
    expect(await attributionUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.attribution.home.createOrEditLabel');
    await attributionUpdatePage.cancel();
  });

  it('should create and save Attributions', async () => {
    const nbButtonsBeforeCreate = await attributionComponentsPage.countDeleteButtons();

    await attributionComponentsPage.clickOnCreateButton();

    await promise.all([
      attributionUpdatePage.setQuantiteAffecterInput('5'),
      attributionUpdatePage.setIdPersInput('5'),
      attributionUpdatePage.setDateAffectationInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      attributionUpdatePage.nomUtilisateurSelectLastOption(),
      attributionUpdatePage.affectationsSelectLastOption(),
    ]);

    expect(await attributionUpdatePage.getQuantiteAffecterInput()).to.eq('5', 'Expected quantiteAffecter value to be equals to 5');
    expect(await attributionUpdatePage.getIdPersInput()).to.eq('5', 'Expected idPers value to be equals to 5');
    expect(await attributionUpdatePage.getDateAffectationInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dateAffectation value to be equals to 2000-12-31'
    );

    await attributionUpdatePage.save();
    expect(await attributionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await attributionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Attribution', async () => {
    const nbButtonsBeforeDelete = await attributionComponentsPage.countDeleteButtons();
    await attributionComponentsPage.clickOnLastDeleteButton();

    attributionDeleteDialog = new AttributionDeleteDialog();
    expect(await attributionDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.attribution.delete.question');
    await attributionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(attributionComponentsPage.title), 5000);

    expect(await attributionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
