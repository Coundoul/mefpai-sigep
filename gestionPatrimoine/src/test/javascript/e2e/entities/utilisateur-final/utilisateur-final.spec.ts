import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { UtilisateurFinalComponentsPage, UtilisateurFinalDeleteDialog, UtilisateurFinalUpdatePage } from './utilisateur-final.page-object';

const expect = chai.expect;

describe('UtilisateurFinal e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let utilisateurFinalComponentsPage: UtilisateurFinalComponentsPage;
  let utilisateurFinalUpdatePage: UtilisateurFinalUpdatePage;
  let utilisateurFinalDeleteDialog: UtilisateurFinalDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load UtilisateurFinals', async () => {
    await navBarPage.goToEntity('utilisateur-final');
    utilisateurFinalComponentsPage = new UtilisateurFinalComponentsPage();
    await browser.wait(ec.visibilityOf(utilisateurFinalComponentsPage.title), 5000);
    expect(await utilisateurFinalComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.utilisateurFinal.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(utilisateurFinalComponentsPage.entities), ec.visibilityOf(utilisateurFinalComponentsPage.noResult)),
      1000
    );
  });

  it('should load create UtilisateurFinal page', async () => {
    await utilisateurFinalComponentsPage.clickOnCreateButton();
    utilisateurFinalUpdatePage = new UtilisateurFinalUpdatePage();
    expect(await utilisateurFinalUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.utilisateurFinal.home.createOrEditLabel');
    await utilisateurFinalUpdatePage.cancel();
  });

  it('should create and save UtilisateurFinals', async () => {
    const nbButtonsBeforeCreate = await utilisateurFinalComponentsPage.countDeleteButtons();

    await utilisateurFinalComponentsPage.clickOnCreateButton();

    await promise.all([
      utilisateurFinalUpdatePage.setNomUtilisateurInput('nomUtilisateur'),
      utilisateurFinalUpdatePage.setPrenomUtilisateurInput('prenomUtilisateur'),
      utilisateurFinalUpdatePage.setEmailInstitutionnelInput('emailInstitutionnel'),
      utilisateurFinalUpdatePage.setMobileInput('mobile'),
      utilisateurFinalUpdatePage.setSexeInput('sexe'),
      utilisateurFinalUpdatePage.setDepartementInput('departement'),
      utilisateurFinalUpdatePage.setServiceDepInput('serviceDep'),
    ]);

    expect(await utilisateurFinalUpdatePage.getNomUtilisateurInput()).to.eq(
      'nomUtilisateur',
      'Expected NomUtilisateur value to be equals to nomUtilisateur'
    );
    expect(await utilisateurFinalUpdatePage.getPrenomUtilisateurInput()).to.eq(
      'prenomUtilisateur',
      'Expected PrenomUtilisateur value to be equals to prenomUtilisateur'
    );
    expect(await utilisateurFinalUpdatePage.getEmailInstitutionnelInput()).to.eq(
      'emailInstitutionnel',
      'Expected EmailInstitutionnel value to be equals to emailInstitutionnel'
    );
    expect(await utilisateurFinalUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await utilisateurFinalUpdatePage.getSexeInput()).to.eq('sexe', 'Expected Sexe value to be equals to sexe');
    expect(await utilisateurFinalUpdatePage.getDepartementInput()).to.eq(
      'departement',
      'Expected Departement value to be equals to departement'
    );
    expect(await utilisateurFinalUpdatePage.getServiceDepInput()).to.eq(
      'serviceDep',
      'Expected ServiceDep value to be equals to serviceDep'
    );

    await utilisateurFinalUpdatePage.save();
    expect(await utilisateurFinalUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await utilisateurFinalComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last UtilisateurFinal', async () => {
    const nbButtonsBeforeDelete = await utilisateurFinalComponentsPage.countDeleteButtons();
    await utilisateurFinalComponentsPage.clickOnLastDeleteButton();

    utilisateurFinalDeleteDialog = new UtilisateurFinalDeleteDialog();
    expect(await utilisateurFinalDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.utilisateurFinal.delete.question');
    await utilisateurFinalDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(utilisateurFinalComponentsPage.title), 5000);

    expect(await utilisateurFinalComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
