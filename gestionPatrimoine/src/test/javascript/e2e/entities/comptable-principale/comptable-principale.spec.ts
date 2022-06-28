import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  ComptablePrincipaleComponentsPage,
  ComptablePrincipaleDeleteDialog,
  ComptablePrincipaleUpdatePage,
} from './comptable-principale.page-object';

const expect = chai.expect;

describe('ComptablePrincipale e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let comptablePrincipaleComponentsPage: ComptablePrincipaleComponentsPage;
  let comptablePrincipaleUpdatePage: ComptablePrincipaleUpdatePage;
  let comptablePrincipaleDeleteDialog: ComptablePrincipaleDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ComptablePrincipales', async () => {
    await navBarPage.goToEntity('comptable-principale');
    comptablePrincipaleComponentsPage = new ComptablePrincipaleComponentsPage();
    await browser.wait(ec.visibilityOf(comptablePrincipaleComponentsPage.title), 5000);
    expect(await comptablePrincipaleComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.comptablePrincipale.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(comptablePrincipaleComponentsPage.entities), ec.visibilityOf(comptablePrincipaleComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ComptablePrincipale page', async () => {
    await comptablePrincipaleComponentsPage.clickOnCreateButton();
    comptablePrincipaleUpdatePage = new ComptablePrincipaleUpdatePage();
    expect(await comptablePrincipaleUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.comptablePrincipale.home.createOrEditLabel');
    await comptablePrincipaleUpdatePage.cancel();
  });

  it('should create and save ComptablePrincipales', async () => {
    const nbButtonsBeforeCreate = await comptablePrincipaleComponentsPage.countDeleteButtons();

    await comptablePrincipaleComponentsPage.clickOnCreateButton();

    await promise.all([
      comptablePrincipaleUpdatePage.setNomPersInput('nomPers'),
      comptablePrincipaleUpdatePage.setPrenomPersInput('prenomPers'),
      comptablePrincipaleUpdatePage.sexeSelectLastOption(),
      comptablePrincipaleUpdatePage.setMobileInput('mobile'),
      comptablePrincipaleUpdatePage.setAdresseInput('adresse'),
      comptablePrincipaleUpdatePage.directionSelectLastOption(),
    ]);

    expect(await comptablePrincipaleUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await comptablePrincipaleUpdatePage.getPrenomPersInput()).to.eq(
      'prenomPers',
      'Expected PrenomPers value to be equals to prenomPers'
    );
    expect(await comptablePrincipaleUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await comptablePrincipaleUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await comptablePrincipaleUpdatePage.save();
    expect(await comptablePrincipaleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await comptablePrincipaleComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ComptablePrincipale', async () => {
    const nbButtonsBeforeDelete = await comptablePrincipaleComponentsPage.countDeleteButtons();
    await comptablePrincipaleComponentsPage.clickOnLastDeleteButton();

    comptablePrincipaleDeleteDialog = new ComptablePrincipaleDeleteDialog();
    expect(await comptablePrincipaleDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.comptablePrincipale.delete.question');
    await comptablePrincipaleDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(comptablePrincipaleComponentsPage.title), 5000);

    expect(await comptablePrincipaleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
