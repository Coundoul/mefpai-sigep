import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  ChefEtablissementComponentsPage,
  ChefEtablissementDeleteDialog,
  ChefEtablissementUpdatePage,
} from './chef-etablissement.page-object';

const expect = chai.expect;

describe('ChefEtablissement e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let chefEtablissementComponentsPage: ChefEtablissementComponentsPage;
  let chefEtablissementUpdatePage: ChefEtablissementUpdatePage;
  let chefEtablissementDeleteDialog: ChefEtablissementDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ChefEtablissements', async () => {
    await navBarPage.goToEntity('chef-etablissement');
    chefEtablissementComponentsPage = new ChefEtablissementComponentsPage();
    await browser.wait(ec.visibilityOf(chefEtablissementComponentsPage.title), 5000);
    expect(await chefEtablissementComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.chefEtablissement.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(chefEtablissementComponentsPage.entities), ec.visibilityOf(chefEtablissementComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ChefEtablissement page', async () => {
    await chefEtablissementComponentsPage.clickOnCreateButton();
    chefEtablissementUpdatePage = new ChefEtablissementUpdatePage();
    expect(await chefEtablissementUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.chefEtablissement.home.createOrEditLabel');
    await chefEtablissementUpdatePage.cancel();
  });

  it('should create and save ChefEtablissements', async () => {
    const nbButtonsBeforeCreate = await chefEtablissementComponentsPage.countDeleteButtons();

    await chefEtablissementComponentsPage.clickOnCreateButton();

    await promise.all([
      chefEtablissementUpdatePage.setNomPersInput('nomPers'),
      chefEtablissementUpdatePage.setPrenomPersInput('prenomPers'),
      chefEtablissementUpdatePage.sexeSelectLastOption(),
      chefEtablissementUpdatePage.setMobileInput('mobile'),
      chefEtablissementUpdatePage.setAdresseInput('adresse'),
    ]);

    expect(await chefEtablissementUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await chefEtablissementUpdatePage.getPrenomPersInput()).to.eq(
      'prenomPers',
      'Expected PrenomPers value to be equals to prenomPers'
    );
    expect(await chefEtablissementUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await chefEtablissementUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await chefEtablissementUpdatePage.save();
    expect(await chefEtablissementUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await chefEtablissementComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ChefEtablissement', async () => {
    const nbButtonsBeforeDelete = await chefEtablissementComponentsPage.countDeleteButtons();
    await chefEtablissementComponentsPage.clickOnLastDeleteButton();

    chefEtablissementDeleteDialog = new ChefEtablissementDeleteDialog();
    expect(await chefEtablissementDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.chefEtablissement.delete.question');
    await chefEtablissementDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(chefEtablissementComponentsPage.title), 5000);

    expect(await chefEtablissementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
