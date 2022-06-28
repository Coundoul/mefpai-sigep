import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FiliereStabiliseComponentsPage, FiliereStabiliseDeleteDialog, FiliereStabiliseUpdatePage } from './filiere-stabilise.page-object';

const expect = chai.expect;

describe('FiliereStabilise e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let filiereStabiliseComponentsPage: FiliereStabiliseComponentsPage;
  let filiereStabiliseUpdatePage: FiliereStabiliseUpdatePage;
  let filiereStabiliseDeleteDialog: FiliereStabiliseDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FiliereStabilises', async () => {
    await navBarPage.goToEntity('filiere-stabilise');
    filiereStabiliseComponentsPage = new FiliereStabiliseComponentsPage();
    await browser.wait(ec.visibilityOf(filiereStabiliseComponentsPage.title), 5000);
    expect(await filiereStabiliseComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.filiereStabilise.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(filiereStabiliseComponentsPage.entities), ec.visibilityOf(filiereStabiliseComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FiliereStabilise page', async () => {
    await filiereStabiliseComponentsPage.clickOnCreateButton();
    filiereStabiliseUpdatePage = new FiliereStabiliseUpdatePage();
    expect(await filiereStabiliseUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.filiereStabilise.home.createOrEditLabel');
    await filiereStabiliseUpdatePage.cancel();
  });

  it('should create and save FiliereStabilises', async () => {
    const nbButtonsBeforeCreate = await filiereStabiliseComponentsPage.countDeleteButtons();

    await filiereStabiliseComponentsPage.clickOnCreateButton();

    await promise.all([
      filiereStabiliseUpdatePage.setNomFiliereInput('nomFiliere'),
      filiereStabiliseUpdatePage.nomFormateurSelectLastOption(),
    ]);

    expect(await filiereStabiliseUpdatePage.getNomFiliereInput()).to.eq(
      'nomFiliere',
      'Expected NomFiliere value to be equals to nomFiliere'
    );

    await filiereStabiliseUpdatePage.save();
    expect(await filiereStabiliseUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await filiereStabiliseComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FiliereStabilise', async () => {
    const nbButtonsBeforeDelete = await filiereStabiliseComponentsPage.countDeleteButtons();
    await filiereStabiliseComponentsPage.clickOnLastDeleteButton();

    filiereStabiliseDeleteDialog = new FiliereStabiliseDeleteDialog();
    expect(await filiereStabiliseDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.filiereStabilise.delete.question');
    await filiereStabiliseDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(filiereStabiliseComponentsPage.title), 5000);

    expect(await filiereStabiliseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
