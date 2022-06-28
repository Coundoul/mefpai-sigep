import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  AttributionInfrastructureComponentsPage,
  AttributionInfrastructureDeleteDialog,
  AttributionInfrastructureUpdatePage,
} from './attribution-infrastructure.page-object';

const expect = chai.expect;

describe('AttributionInfrastructure e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let attributionInfrastructureComponentsPage: AttributionInfrastructureComponentsPage;
  let attributionInfrastructureUpdatePage: AttributionInfrastructureUpdatePage;
  let attributionInfrastructureDeleteDialog: AttributionInfrastructureDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AttributionInfrastructures', async () => {
    await navBarPage.goToEntity('attribution-infrastructure');
    attributionInfrastructureComponentsPage = new AttributionInfrastructureComponentsPage();
    await browser.wait(ec.visibilityOf(attributionInfrastructureComponentsPage.title), 5000);
    expect(await attributionInfrastructureComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.attributionInfrastructure.home.title');
    await browser.wait(
      ec.or(
        ec.visibilityOf(attributionInfrastructureComponentsPage.entities),
        ec.visibilityOf(attributionInfrastructureComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create AttributionInfrastructure page', async () => {
    await attributionInfrastructureComponentsPage.clickOnCreateButton();
    attributionInfrastructureUpdatePage = new AttributionInfrastructureUpdatePage();
    expect(await attributionInfrastructureUpdatePage.getPageTitle()).to.eq(
      'gestionPatrimoineApp.attributionInfrastructure.home.createOrEditLabel'
    );
    await attributionInfrastructureUpdatePage.cancel();
  });

  it('should create and save AttributionInfrastructures', async () => {
    const nbButtonsBeforeCreate = await attributionInfrastructureComponentsPage.countDeleteButtons();

    await attributionInfrastructureComponentsPage.clickOnCreateButton();

    await promise.all([
      attributionInfrastructureUpdatePage.setDateAttributionInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      attributionInfrastructureUpdatePage.setQuantiteInput('5'),
      attributionInfrastructureUpdatePage.setIdEquipementInput('5'),
      attributionInfrastructureUpdatePage.setIdPersInput('5'),
      attributionInfrastructureUpdatePage.nomEtablissementSelectLastOption(),
    ]);

    expect(await attributionInfrastructureUpdatePage.getDateAttributionInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dateAttribution value to be equals to 2000-12-31'
    );
    expect(await attributionInfrastructureUpdatePage.getQuantiteInput()).to.eq('5', 'Expected quantite value to be equals to 5');
    expect(await attributionInfrastructureUpdatePage.getIdEquipementInput()).to.eq('5', 'Expected idEquipement value to be equals to 5');
    expect(await attributionInfrastructureUpdatePage.getIdPersInput()).to.eq('5', 'Expected idPers value to be equals to 5');

    await attributionInfrastructureUpdatePage.save();
    expect(await attributionInfrastructureUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await attributionInfrastructureComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last AttributionInfrastructure', async () => {
    const nbButtonsBeforeDelete = await attributionInfrastructureComponentsPage.countDeleteButtons();
    await attributionInfrastructureComponentsPage.clickOnLastDeleteButton();

    attributionInfrastructureDeleteDialog = new AttributionInfrastructureDeleteDialog();
    expect(await attributionInfrastructureDeleteDialog.getDialogTitle()).to.eq(
      'gestionPatrimoineApp.attributionInfrastructure.delete.question'
    );
    await attributionInfrastructureDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(attributionInfrastructureComponentsPage.title), 5000);

    expect(await attributionInfrastructureComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
