import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProjetsComponentsPage, ProjetsDeleteDialog, ProjetsUpdatePage } from './projets.page-object';

const expect = chai.expect;

describe('Projets e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let projetsComponentsPage: ProjetsComponentsPage;
  let projetsUpdatePage: ProjetsUpdatePage;
  let projetsDeleteDialog: ProjetsDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Projets', async () => {
    await navBarPage.goToEntity('projets');
    projetsComponentsPage = new ProjetsComponentsPage();
    await browser.wait(ec.visibilityOf(projetsComponentsPage.title), 5000);
    expect(await projetsComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.projets.home.title');
    await browser.wait(ec.or(ec.visibilityOf(projetsComponentsPage.entities), ec.visibilityOf(projetsComponentsPage.noResult)), 1000);
  });

  it('should load create Projets page', async () => {
    await projetsComponentsPage.clickOnCreateButton();
    projetsUpdatePage = new ProjetsUpdatePage();
    expect(await projetsUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.projets.home.createOrEditLabel');
    await projetsUpdatePage.cancel();
  });

  it('should create and save Projets', async () => {
    const nbButtonsBeforeCreate = await projetsComponentsPage.countDeleteButtons();

    await projetsComponentsPage.clickOnCreateButton();

    await promise.all([
      projetsUpdatePage.typeProjetSelectLastOption(),
      projetsUpdatePage.setNomProjetInput('nomProjet'),
      projetsUpdatePage.setDateDebutInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      projetsUpdatePage.setDateFinInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      projetsUpdatePage.setDescriptionInput('description'),
      projetsUpdatePage.nomSelectLastOption(),
      projetsUpdatePage.nomEtablissementSelectLastOption(),
      projetsUpdatePage.nomBatimentSelectLastOption(),
    ]);

    expect(await projetsUpdatePage.getNomProjetInput()).to.eq('nomProjet', 'Expected NomProjet value to be equals to nomProjet');
    expect(await projetsUpdatePage.getDateDebutInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dateDebut value to be equals to 2000-12-31'
    );
    expect(await projetsUpdatePage.getDateFinInput()).to.contain('2001-01-01T02:30', 'Expected dateFin value to be equals to 2000-12-31');
    expect(await projetsUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    const selectedExtension = projetsUpdatePage.getExtensionInput();
    if (await selectedExtension.isSelected()) {
      await projetsUpdatePage.getExtensionInput().click();
      expect(await projetsUpdatePage.getExtensionInput().isSelected(), 'Expected extension not to be selected').to.be.false;
    } else {
      await projetsUpdatePage.getExtensionInput().click();
      expect(await projetsUpdatePage.getExtensionInput().isSelected(), 'Expected extension to be selected').to.be.true;
    }

    await projetsUpdatePage.save();
    expect(await projetsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await projetsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Projets', async () => {
    const nbButtonsBeforeDelete = await projetsComponentsPage.countDeleteButtons();
    await projetsComponentsPage.clickOnLastDeleteButton();

    projetsDeleteDialog = new ProjetsDeleteDialog();
    expect(await projetsDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.projets.delete.question');
    await projetsDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(projetsComponentsPage.title), 5000);

    expect(await projetsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
