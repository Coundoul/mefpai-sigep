import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ChefProjetComponentsPage, ChefProjetDeleteDialog, ChefProjetUpdatePage } from './chef-projet.page-object';

const expect = chai.expect;

describe('ChefProjet e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let chefProjetComponentsPage: ChefProjetComponentsPage;
  let chefProjetUpdatePage: ChefProjetUpdatePage;
  let chefProjetDeleteDialog: ChefProjetDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ChefProjets', async () => {
    await navBarPage.goToEntity('chef-projet');
    chefProjetComponentsPage = new ChefProjetComponentsPage();
    await browser.wait(ec.visibilityOf(chefProjetComponentsPage.title), 5000);
    expect(await chefProjetComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.chefProjet.home.title');
    await browser.wait(ec.or(ec.visibilityOf(chefProjetComponentsPage.entities), ec.visibilityOf(chefProjetComponentsPage.noResult)), 1000);
  });

  it('should load create ChefProjet page', async () => {
    await chefProjetComponentsPage.clickOnCreateButton();
    chefProjetUpdatePage = new ChefProjetUpdatePage();
    expect(await chefProjetUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.chefProjet.home.createOrEditLabel');
    await chefProjetUpdatePage.cancel();
  });

  it('should create and save ChefProjets', async () => {
    const nbButtonsBeforeCreate = await chefProjetComponentsPage.countDeleteButtons();

    await chefProjetComponentsPage.clickOnCreateButton();

    await promise.all([
      chefProjetUpdatePage.setNomPersInput('nomPers'),
      chefProjetUpdatePage.setPrenomPersInput('prenomPers'),
      chefProjetUpdatePage.sexeSelectLastOption(),
      chefProjetUpdatePage.setMobileInput('mobile'),
      chefProjetUpdatePage.setAdresseInput('adresse'),
      chefProjetUpdatePage.directionSelectLastOption(),
    ]);

    expect(await chefProjetUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await chefProjetUpdatePage.getPrenomPersInput()).to.eq('prenomPers', 'Expected PrenomPers value to be equals to prenomPers');
    expect(await chefProjetUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await chefProjetUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await chefProjetUpdatePage.save();
    expect(await chefProjetUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await chefProjetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ChefProjet', async () => {
    const nbButtonsBeforeDelete = await chefProjetComponentsPage.countDeleteButtons();
    await chefProjetComponentsPage.clickOnLastDeleteButton();

    chefProjetDeleteDialog = new ChefProjetDeleteDialog();
    expect(await chefProjetDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.chefProjet.delete.question');
    await chefProjetDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(chefProjetComponentsPage.title), 5000);

    expect(await chefProjetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
