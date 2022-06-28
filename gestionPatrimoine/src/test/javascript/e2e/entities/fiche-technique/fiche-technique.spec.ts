import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FicheTechniqueComponentsPage, FicheTechniqueDeleteDialog, FicheTechniqueUpdatePage } from './fiche-technique.page-object';

const expect = chai.expect;

describe('FicheTechnique e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ficheTechniqueComponentsPage: FicheTechniqueComponentsPage;
  let ficheTechniqueUpdatePage: FicheTechniqueUpdatePage;
  let ficheTechniqueDeleteDialog: FicheTechniqueDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FicheTechniques', async () => {
    await navBarPage.goToEntity('fiche-technique');
    ficheTechniqueComponentsPage = new FicheTechniqueComponentsPage();
    await browser.wait(ec.visibilityOf(ficheTechniqueComponentsPage.title), 5000);
    expect(await ficheTechniqueComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.ficheTechnique.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(ficheTechniqueComponentsPage.entities), ec.visibilityOf(ficheTechniqueComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FicheTechnique page', async () => {
    await ficheTechniqueComponentsPage.clickOnCreateButton();
    ficheTechniqueUpdatePage = new FicheTechniqueUpdatePage();
    expect(await ficheTechniqueUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.ficheTechnique.home.createOrEditLabel');
    await ficheTechniqueUpdatePage.cancel();
  });

  it('should create and save FicheTechniques', async () => {
    const nbButtonsBeforeCreate = await ficheTechniqueComponentsPage.countDeleteButtons();

    await ficheTechniqueComponentsPage.clickOnCreateButton();

    await promise.all([
      ficheTechniqueUpdatePage.setPieceJointeInput('pieceJointe'),
      ficheTechniqueUpdatePage.setDateDepotInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      ficheTechniqueUpdatePage.nomResponsableSelectLastOption(),
    ]);

    expect(await ficheTechniqueUpdatePage.getPieceJointeInput()).to.eq(
      'pieceJointe',
      'Expected PieceJointe value to be equals to pieceJointe'
    );
    expect(await ficheTechniqueUpdatePage.getDateDepotInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dateDepot value to be equals to 2000-12-31'
    );

    await ficheTechniqueUpdatePage.save();
    expect(await ficheTechniqueUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ficheTechniqueComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FicheTechnique', async () => {
    const nbButtonsBeforeDelete = await ficheTechniqueComponentsPage.countDeleteButtons();
    await ficheTechniqueComponentsPage.clickOnLastDeleteButton();

    ficheTechniqueDeleteDialog = new FicheTechniqueDeleteDialog();
    expect(await ficheTechniqueDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.ficheTechnique.delete.question');
    await ficheTechniqueDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(ficheTechniqueComponentsPage.title), 5000);

    expect(await ficheTechniqueComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
