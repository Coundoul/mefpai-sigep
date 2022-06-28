import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FournisseurComponentsPage, FournisseurDeleteDialog, FournisseurUpdatePage } from './fournisseur.page-object';

const expect = chai.expect;

describe('Fournisseur e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fournisseurComponentsPage: FournisseurComponentsPage;
  let fournisseurUpdatePage: FournisseurUpdatePage;
  let fournisseurDeleteDialog: FournisseurDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Fournisseurs', async () => {
    await navBarPage.goToEntity('fournisseur');
    fournisseurComponentsPage = new FournisseurComponentsPage();
    await browser.wait(ec.visibilityOf(fournisseurComponentsPage.title), 5000);
    expect(await fournisseurComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.fournisseur.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(fournisseurComponentsPage.entities), ec.visibilityOf(fournisseurComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Fournisseur page', async () => {
    await fournisseurComponentsPage.clickOnCreateButton();
    fournisseurUpdatePage = new FournisseurUpdatePage();
    expect(await fournisseurUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.fournisseur.home.createOrEditLabel');
    await fournisseurUpdatePage.cancel();
  });

  it('should create and save Fournisseurs', async () => {
    const nbButtonsBeforeCreate = await fournisseurComponentsPage.countDeleteButtons();

    await fournisseurComponentsPage.clickOnCreateButton();

    await promise.all([
      fournisseurUpdatePage.setCodeFournisseuerInput('codeFournisseuer'),
      fournisseurUpdatePage.setNomFournisseurInput('nomFournisseur'),
      fournisseurUpdatePage.setPrenomFournisseurInput('prenomFournisseur'),
      fournisseurUpdatePage.sexeSelectLastOption(),
      fournisseurUpdatePage.setRaisonSocialInput('raisonSocial'),
      fournisseurUpdatePage.setAdresseInput('adresse'),
      fournisseurUpdatePage.setNum1Input('num1'),
      fournisseurUpdatePage.setNum2Input('num2'),
      fournisseurUpdatePage.setVilleInput('ville'),
      fournisseurUpdatePage.setEmailInput('email'),
    ]);

    expect(await fournisseurUpdatePage.getCodeFournisseuerInput()).to.eq(
      'codeFournisseuer',
      'Expected CodeFournisseuer value to be equals to codeFournisseuer'
    );
    expect(await fournisseurUpdatePage.getNomFournisseurInput()).to.eq(
      'nomFournisseur',
      'Expected NomFournisseur value to be equals to nomFournisseur'
    );
    expect(await fournisseurUpdatePage.getPrenomFournisseurInput()).to.eq(
      'prenomFournisseur',
      'Expected PrenomFournisseur value to be equals to prenomFournisseur'
    );
    expect(await fournisseurUpdatePage.getRaisonSocialInput()).to.eq(
      'raisonSocial',
      'Expected RaisonSocial value to be equals to raisonSocial'
    );
    expect(await fournisseurUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');
    expect(await fournisseurUpdatePage.getNum1Input()).to.eq('num1', 'Expected Num1 value to be equals to num1');
    expect(await fournisseurUpdatePage.getNum2Input()).to.eq('num2', 'Expected Num2 value to be equals to num2');
    expect(await fournisseurUpdatePage.getVilleInput()).to.eq('ville', 'Expected Ville value to be equals to ville');
    expect(await fournisseurUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');

    await fournisseurUpdatePage.save();
    expect(await fournisseurUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fournisseurComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Fournisseur', async () => {
    const nbButtonsBeforeDelete = await fournisseurComponentsPage.countDeleteButtons();
    await fournisseurComponentsPage.clickOnLastDeleteButton();

    fournisseurDeleteDialog = new FournisseurDeleteDialog();
    expect(await fournisseurDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.fournisseur.delete.question');
    await fournisseurDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fournisseurComponentsPage.title), 5000);

    expect(await fournisseurComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
