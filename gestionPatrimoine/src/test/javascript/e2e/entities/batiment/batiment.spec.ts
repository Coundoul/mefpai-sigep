import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BatimentComponentsPage, BatimentDeleteDialog, BatimentUpdatePage } from './batiment.page-object';

const expect = chai.expect;

describe('Batiment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let batimentComponentsPage: BatimentComponentsPage;
  let batimentUpdatePage: BatimentUpdatePage;
  let batimentDeleteDialog: BatimentDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Batiments', async () => {
    await navBarPage.goToEntity('batiment');
    batimentComponentsPage = new BatimentComponentsPage();
    await browser.wait(ec.visibilityOf(batimentComponentsPage.title), 5000);
    expect(await batimentComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.batiment.home.title');
    await browser.wait(ec.or(ec.visibilityOf(batimentComponentsPage.entities), ec.visibilityOf(batimentComponentsPage.noResult)), 1000);
  });

  it('should load create Batiment page', async () => {
    await batimentComponentsPage.clickOnCreateButton();
    batimentUpdatePage = new BatimentUpdatePage();
    expect(await batimentUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.batiment.home.createOrEditLabel');
    await batimentUpdatePage.cancel();
  });

  it('should create and save Batiments', async () => {
    const nbButtonsBeforeCreate = await batimentComponentsPage.countDeleteButtons();

    await batimentComponentsPage.clickOnCreateButton();

    await promise.all([
      batimentUpdatePage.setNomBatimentInput('nomBatiment'),
      batimentUpdatePage.setNbrPieceInput('nbrPiece'),
      batimentUpdatePage.setDesignationInput('designation'),
      batimentUpdatePage.setSurfaceInput('5'),
      batimentUpdatePage.setDescriptionInput('description'),
      batimentUpdatePage.setNombreSalleInput('5'),
      batimentUpdatePage.nomEtablissementSelectLastOption(),
      batimentUpdatePage.nomCorpsSelectLastOption(),
    ]);

    expect(await batimentUpdatePage.getNomBatimentInput()).to.eq('nomBatiment', 'Expected NomBatiment value to be equals to nomBatiment');
    expect(await batimentUpdatePage.getNbrPieceInput()).to.eq('nbrPiece', 'Expected NbrPiece value to be equals to nbrPiece');
    expect(await batimentUpdatePage.getDesignationInput()).to.eq('designation', 'Expected Designation value to be equals to designation');
    expect(await batimentUpdatePage.getSurfaceInput()).to.eq('5', 'Expected surface value to be equals to 5');
    const selectedEtatGeneral = batimentUpdatePage.getEtatGeneralInput();
    if (await selectedEtatGeneral.isSelected()) {
      await batimentUpdatePage.getEtatGeneralInput().click();
      expect(await batimentUpdatePage.getEtatGeneralInput().isSelected(), 'Expected etatGeneral not to be selected').to.be.false;
    } else {
      await batimentUpdatePage.getEtatGeneralInput().click();
      expect(await batimentUpdatePage.getEtatGeneralInput().isSelected(), 'Expected etatGeneral to be selected').to.be.true;
    }
    expect(await batimentUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    expect(await batimentUpdatePage.getNombreSalleInput()).to.eq('5', 'Expected nombreSalle value to be equals to 5');

    await batimentUpdatePage.save();
    expect(await batimentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await batimentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Batiment', async () => {
    const nbButtonsBeforeDelete = await batimentComponentsPage.countDeleteButtons();
    await batimentComponentsPage.clickOnLastDeleteButton();

    batimentDeleteDialog = new BatimentDeleteDialog();
    expect(await batimentDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.batiment.delete.question');
    await batimentDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(batimentComponentsPage.title), 5000);

    expect(await batimentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
