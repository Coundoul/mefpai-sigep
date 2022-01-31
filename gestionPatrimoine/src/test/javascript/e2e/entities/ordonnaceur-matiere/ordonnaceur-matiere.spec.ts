import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  OrdonnaceurMatiereComponentsPage,
  OrdonnaceurMatiereDeleteDialog,
  OrdonnaceurMatiereUpdatePage,
} from './ordonnaceur-matiere.page-object';

const expect = chai.expect;

describe('OrdonnaceurMatiere e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ordonnaceurMatiereComponentsPage: OrdonnaceurMatiereComponentsPage;
  let ordonnaceurMatiereUpdatePage: OrdonnaceurMatiereUpdatePage;
  let ordonnaceurMatiereDeleteDialog: OrdonnaceurMatiereDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load OrdonnaceurMatieres', async () => {
    await navBarPage.goToEntity('ordonnaceur-matiere');
    ordonnaceurMatiereComponentsPage = new OrdonnaceurMatiereComponentsPage();
    await browser.wait(ec.visibilityOf(ordonnaceurMatiereComponentsPage.title), 5000);
    expect(await ordonnaceurMatiereComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.ordonnaceurMatiere.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(ordonnaceurMatiereComponentsPage.entities), ec.visibilityOf(ordonnaceurMatiereComponentsPage.noResult)),
      1000
    );
  });

  it('should load create OrdonnaceurMatiere page', async () => {
    await ordonnaceurMatiereComponentsPage.clickOnCreateButton();
    ordonnaceurMatiereUpdatePage = new OrdonnaceurMatiereUpdatePage();
    expect(await ordonnaceurMatiereUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.ordonnaceurMatiere.home.createOrEditLabel');
    await ordonnaceurMatiereUpdatePage.cancel();
  });

  it('should create and save OrdonnaceurMatieres', async () => {
    const nbButtonsBeforeCreate = await ordonnaceurMatiereComponentsPage.countDeleteButtons();

    await ordonnaceurMatiereComponentsPage.clickOnCreateButton();

    await promise.all([
      ordonnaceurMatiereUpdatePage.setNomPersInput('nomPers'),
      ordonnaceurMatiereUpdatePage.setPrenomPersInput('prenomPers'),
      ordonnaceurMatiereUpdatePage.sexeSelectLastOption(),
      ordonnaceurMatiereUpdatePage.setMobileInput('mobile'),
      ordonnaceurMatiereUpdatePage.setAdresseInput('adresse'),
      ordonnaceurMatiereUpdatePage.directionSelectLastOption(),
    ]);

    expect(await ordonnaceurMatiereUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await ordonnaceurMatiereUpdatePage.getPrenomPersInput()).to.eq(
      'prenomPers',
      'Expected PrenomPers value to be equals to prenomPers'
    );
    expect(await ordonnaceurMatiereUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await ordonnaceurMatiereUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await ordonnaceurMatiereUpdatePage.save();
    expect(await ordonnaceurMatiereUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ordonnaceurMatiereComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last OrdonnaceurMatiere', async () => {
    const nbButtonsBeforeDelete = await ordonnaceurMatiereComponentsPage.countDeleteButtons();
    await ordonnaceurMatiereComponentsPage.clickOnLastDeleteButton();

    ordonnaceurMatiereDeleteDialog = new OrdonnaceurMatiereDeleteDialog();
    expect(await ordonnaceurMatiereDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.ordonnaceurMatiere.delete.question');
    await ordonnaceurMatiereDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(ordonnaceurMatiereComponentsPage.title), 5000);

    expect(await ordonnaceurMatiereComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
