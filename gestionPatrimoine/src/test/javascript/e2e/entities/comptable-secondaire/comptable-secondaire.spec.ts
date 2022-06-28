import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  ComptableSecondaireComponentsPage,
  ComptableSecondaireDeleteDialog,
  ComptableSecondaireUpdatePage,
} from './comptable-secondaire.page-object';

const expect = chai.expect;

describe('ComptableSecondaire e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let comptableSecondaireComponentsPage: ComptableSecondaireComponentsPage;
  let comptableSecondaireUpdatePage: ComptableSecondaireUpdatePage;
  let comptableSecondaireDeleteDialog: ComptableSecondaireDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ComptableSecondaires', async () => {
    await navBarPage.goToEntity('comptable-secondaire');
    comptableSecondaireComponentsPage = new ComptableSecondaireComponentsPage();
    await browser.wait(ec.visibilityOf(comptableSecondaireComponentsPage.title), 5000);
    expect(await comptableSecondaireComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.comptableSecondaire.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(comptableSecondaireComponentsPage.entities), ec.visibilityOf(comptableSecondaireComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ComptableSecondaire page', async () => {
    await comptableSecondaireComponentsPage.clickOnCreateButton();
    comptableSecondaireUpdatePage = new ComptableSecondaireUpdatePage();
    expect(await comptableSecondaireUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.comptableSecondaire.home.createOrEditLabel');
    await comptableSecondaireUpdatePage.cancel();
  });

  it('should create and save ComptableSecondaires', async () => {
    const nbButtonsBeforeCreate = await comptableSecondaireComponentsPage.countDeleteButtons();

    await comptableSecondaireComponentsPage.clickOnCreateButton();

    await promise.all([
      comptableSecondaireUpdatePage.setNomPersInput('nomPers'),
      comptableSecondaireUpdatePage.setPrenomPersInput('prenomPers'),
      comptableSecondaireUpdatePage.sexeSelectLastOption(),
      comptableSecondaireUpdatePage.setMobileInput('mobile'),
      comptableSecondaireUpdatePage.setAdresseInput('adresse'),
      comptableSecondaireUpdatePage.directionSelectLastOption(),
      comptableSecondaireUpdatePage.comptablePrincipaleSelectLastOption(),
    ]);

    expect(await comptableSecondaireUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await comptableSecondaireUpdatePage.getPrenomPersInput()).to.eq(
      'prenomPers',
      'Expected PrenomPers value to be equals to prenomPers'
    );
    expect(await comptableSecondaireUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await comptableSecondaireUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await comptableSecondaireUpdatePage.save();
    expect(await comptableSecondaireUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await comptableSecondaireComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ComptableSecondaire', async () => {
    const nbButtonsBeforeDelete = await comptableSecondaireComponentsPage.countDeleteButtons();
    await comptableSecondaireComponentsPage.clickOnLastDeleteButton();

    comptableSecondaireDeleteDialog = new ComptableSecondaireDeleteDialog();
    expect(await comptableSecondaireDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.comptableSecondaire.delete.question');
    await comptableSecondaireDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(comptableSecondaireComponentsPage.title), 5000);

    expect(await comptableSecondaireComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
