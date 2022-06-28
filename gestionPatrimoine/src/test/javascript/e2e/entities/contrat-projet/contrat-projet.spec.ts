import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ContratProjetComponentsPage, ContratProjetDeleteDialog, ContratProjetUpdatePage } from './contrat-projet.page-object';

const expect = chai.expect;

describe('ContratProjet e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contratProjetComponentsPage: ContratProjetComponentsPage;
  let contratProjetUpdatePage: ContratProjetUpdatePage;
  let contratProjetDeleteDialog: ContratProjetDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ContratProjets', async () => {
    await navBarPage.goToEntity('contrat-projet');
    contratProjetComponentsPage = new ContratProjetComponentsPage();
    await browser.wait(ec.visibilityOf(contratProjetComponentsPage.title), 5000);
    expect(await contratProjetComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.contratProjet.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(contratProjetComponentsPage.entities), ec.visibilityOf(contratProjetComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ContratProjet page', async () => {
    await contratProjetComponentsPage.clickOnCreateButton();
    contratProjetUpdatePage = new ContratProjetUpdatePage();
    expect(await contratProjetUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.contratProjet.home.createOrEditLabel');
    await contratProjetUpdatePage.cancel();
  });

  it('should create and save ContratProjets', async () => {
    const nbButtonsBeforeCreate = await contratProjetComponentsPage.countDeleteButtons();

    await contratProjetComponentsPage.clickOnCreateButton();

    await promise.all([contratProjetUpdatePage.setNomInput('nom')]);

    expect(await contratProjetUpdatePage.getNomInput()).to.eq('nom', 'Expected Nom value to be equals to nom');

    await contratProjetUpdatePage.save();
    expect(await contratProjetUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await contratProjetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ContratProjet', async () => {
    const nbButtonsBeforeDelete = await contratProjetComponentsPage.countDeleteButtons();
    await contratProjetComponentsPage.clickOnLastDeleteButton();

    contratProjetDeleteDialog = new ContratProjetDeleteDialog();
    expect(await contratProjetDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.contratProjet.delete.question');
    await contratProjetDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(contratProjetComponentsPage.title), 5000);

    expect(await contratProjetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
