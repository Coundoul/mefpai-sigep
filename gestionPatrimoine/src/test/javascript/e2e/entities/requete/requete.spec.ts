import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { RequeteComponentsPage, RequeteDeleteDialog, RequeteUpdatePage } from './requete.page-object';

const expect = chai.expect;

describe('Requete e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let requeteComponentsPage: RequeteComponentsPage;
  let requeteUpdatePage: RequeteUpdatePage;
  let requeteDeleteDialog: RequeteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Requetes', async () => {
    await navBarPage.goToEntity('requete');
    requeteComponentsPage = new RequeteComponentsPage();
    await browser.wait(ec.visibilityOf(requeteComponentsPage.title), 5000);
    expect(await requeteComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.requete.home.title');
    await browser.wait(ec.or(ec.visibilityOf(requeteComponentsPage.entities), ec.visibilityOf(requeteComponentsPage.noResult)), 1000);
  });

  it('should load create Requete page', async () => {
    await requeteComponentsPage.clickOnCreateButton();
    requeteUpdatePage = new RequeteUpdatePage();
    expect(await requeteUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.requete.home.createOrEditLabel');
    await requeteUpdatePage.cancel();
  });

  it('should create and save Requetes', async () => {
    const nbButtonsBeforeCreate = await requeteComponentsPage.countDeleteButtons();

    await requeteComponentsPage.clickOnCreateButton();

    await promise.all([
      requeteUpdatePage.setTypeInput('type'),
      requeteUpdatePage.setTypePanneInput('5'),
      requeteUpdatePage.setDatePostInput('5'),
      requeteUpdatePage.setDescriptionInput('description'),
      requeteUpdatePage.setDateLancementInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      requeteUpdatePage.setIdPersInput('5'),
      requeteUpdatePage.nomStructureSelectLastOption(),
    ]);

    expect(await requeteUpdatePage.getTypeInput()).to.eq('type', 'Expected Type value to be equals to type');
    expect(await requeteUpdatePage.getTypePanneInput()).to.eq('5', 'Expected typePanne value to be equals to 5');
    expect(await requeteUpdatePage.getDatePostInput()).to.eq('5', 'Expected datePost value to be equals to 5');
    expect(await requeteUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    const selectedEtatTraite = requeteUpdatePage.getEtatTraiteInput();
    if (await selectedEtatTraite.isSelected()) {
      await requeteUpdatePage.getEtatTraiteInput().click();
      expect(await requeteUpdatePage.getEtatTraiteInput().isSelected(), 'Expected etatTraite not to be selected').to.be.false;
    } else {
      await requeteUpdatePage.getEtatTraiteInput().click();
      expect(await requeteUpdatePage.getEtatTraiteInput().isSelected(), 'Expected etatTraite to be selected').to.be.true;
    }
    expect(await requeteUpdatePage.getDateLancementInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dateLancement value to be equals to 2000-12-31'
    );
    expect(await requeteUpdatePage.getIdPersInput()).to.eq('5', 'Expected idPers value to be equals to 5');

    await requeteUpdatePage.save();
    expect(await requeteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await requeteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Requete', async () => {
    const nbButtonsBeforeDelete = await requeteComponentsPage.countDeleteButtons();
    await requeteComponentsPage.clickOnLastDeleteButton();

    requeteDeleteDialog = new RequeteDeleteDialog();
    expect(await requeteDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.requete.delete.question');
    await requeteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(requeteComponentsPage.title), 5000);

    expect(await requeteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
