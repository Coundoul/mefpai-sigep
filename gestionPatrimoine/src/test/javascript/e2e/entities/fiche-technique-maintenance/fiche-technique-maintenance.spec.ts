import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  FicheTechniqueMaintenanceComponentsPage,
  FicheTechniqueMaintenanceDeleteDialog,
  FicheTechniqueMaintenanceUpdatePage,
} from './fiche-technique-maintenance.page-object';

const expect = chai.expect;

describe('FicheTechniqueMaintenance e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ficheTechniqueMaintenanceComponentsPage: FicheTechniqueMaintenanceComponentsPage;
  let ficheTechniqueMaintenanceUpdatePage: FicheTechniqueMaintenanceUpdatePage;
  let ficheTechniqueMaintenanceDeleteDialog: FicheTechniqueMaintenanceDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FicheTechniqueMaintenances', async () => {
    await navBarPage.goToEntity('fiche-technique-maintenance');
    ficheTechniqueMaintenanceComponentsPage = new FicheTechniqueMaintenanceComponentsPage();
    await browser.wait(ec.visibilityOf(ficheTechniqueMaintenanceComponentsPage.title), 5000);
    expect(await ficheTechniqueMaintenanceComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.ficheTechniqueMaintenance.home.title');
    await browser.wait(
      ec.or(
        ec.visibilityOf(ficheTechniqueMaintenanceComponentsPage.entities),
        ec.visibilityOf(ficheTechniqueMaintenanceComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create FicheTechniqueMaintenance page', async () => {
    await ficheTechniqueMaintenanceComponentsPage.clickOnCreateButton();
    ficheTechniqueMaintenanceUpdatePage = new FicheTechniqueMaintenanceUpdatePage();
    expect(await ficheTechniqueMaintenanceUpdatePage.getPageTitle()).to.eq(
      'gestionPatrimoineApp.ficheTechniqueMaintenance.home.createOrEditLabel'
    );
    await ficheTechniqueMaintenanceUpdatePage.cancel();
  });

  it('should create and save FicheTechniqueMaintenances', async () => {
    const nbButtonsBeforeCreate = await ficheTechniqueMaintenanceComponentsPage.countDeleteButtons();

    await ficheTechniqueMaintenanceComponentsPage.clickOnCreateButton();

    await promise.all([
      ficheTechniqueMaintenanceUpdatePage.setPieceJointeInput('pieceJointe'),
      ficheTechniqueMaintenanceUpdatePage.setIdPersInput('5'),
      ficheTechniqueMaintenanceUpdatePage.setDateDepotInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      ficheTechniqueMaintenanceUpdatePage.typeSelectLastOption(),
    ]);

    expect(await ficheTechniqueMaintenanceUpdatePage.getPieceJointeInput()).to.eq(
      'pieceJointe',
      'Expected PieceJointe value to be equals to pieceJointe'
    );
    expect(await ficheTechniqueMaintenanceUpdatePage.getIdPersInput()).to.eq('5', 'Expected idPers value to be equals to 5');
    expect(await ficheTechniqueMaintenanceUpdatePage.getDateDepotInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dateDepot value to be equals to 2000-12-31'
    );

    await ficheTechniqueMaintenanceUpdatePage.save();
    expect(await ficheTechniqueMaintenanceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ficheTechniqueMaintenanceComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FicheTechniqueMaintenance', async () => {
    const nbButtonsBeforeDelete = await ficheTechniqueMaintenanceComponentsPage.countDeleteButtons();
    await ficheTechniqueMaintenanceComponentsPage.clickOnLastDeleteButton();

    ficheTechniqueMaintenanceDeleteDialog = new FicheTechniqueMaintenanceDeleteDialog();
    expect(await ficheTechniqueMaintenanceDeleteDialog.getDialogTitle()).to.eq(
      'gestionPatrimoineApp.ficheTechniqueMaintenance.delete.question'
    );
    await ficheTechniqueMaintenanceDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(ficheTechniqueMaintenanceComponentsPage.title), 5000);

    expect(await ficheTechniqueMaintenanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
