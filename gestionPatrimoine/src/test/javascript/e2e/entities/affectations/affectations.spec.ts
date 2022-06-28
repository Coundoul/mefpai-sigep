import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AffectationsComponentsPage, AffectationsDeleteDialog, AffectationsUpdatePage } from './affectations.page-object';

const expect = chai.expect;

describe('Affectations e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let affectationsComponentsPage: AffectationsComponentsPage;
  let affectationsUpdatePage: AffectationsUpdatePage;
  let affectationsDeleteDialog: AffectationsDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Affectations', async () => {
    await navBarPage.goToEntity('affectations');
    affectationsComponentsPage = new AffectationsComponentsPage();
    await browser.wait(ec.visibilityOf(affectationsComponentsPage.title), 5000);
    expect(await affectationsComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.affectations.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(affectationsComponentsPage.entities), ec.visibilityOf(affectationsComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Affectations page', async () => {
    await affectationsComponentsPage.clickOnCreateButton();
    affectationsUpdatePage = new AffectationsUpdatePage();
    expect(await affectationsUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.affectations.home.createOrEditLabel');
    await affectationsUpdatePage.cancel();
  });

  it('should create and save Affectations', async () => {
    const nbButtonsBeforeCreate = await affectationsComponentsPage.countDeleteButtons();

    await affectationsComponentsPage.clickOnCreateButton();

    await promise.all([
      affectationsUpdatePage.setQuantiteAffecterInput('5'),
      affectationsUpdatePage.typeAttributionSelectLastOption(),
      affectationsUpdatePage.setIdPersInput('5'),
      affectationsUpdatePage.setDateAttributionInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      affectationsUpdatePage.equipementSelectLastOption(),
    ]);

    expect(await affectationsUpdatePage.getQuantiteAffecterInput()).to.eq('5', 'Expected quantiteAffecter value to be equals to 5');
    expect(await affectationsUpdatePage.getIdPersInput()).to.eq('5', 'Expected idPers value to be equals to 5');
    expect(await affectationsUpdatePage.getDateAttributionInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dateAttribution value to be equals to 2000-12-31'
    );

    await affectationsUpdatePage.save();
    expect(await affectationsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await affectationsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Affectations', async () => {
    const nbButtonsBeforeDelete = await affectationsComponentsPage.countDeleteButtons();
    await affectationsComponentsPage.clickOnLastDeleteButton();

    affectationsDeleteDialog = new AffectationsDeleteDialog();
    expect(await affectationsDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.affectations.delete.question');
    await affectationsDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(affectationsComponentsPage.title), 5000);

    expect(await affectationsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
