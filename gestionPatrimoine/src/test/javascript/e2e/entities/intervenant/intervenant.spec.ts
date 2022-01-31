import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { IntervenantComponentsPage, IntervenantDeleteDialog, IntervenantUpdatePage } from './intervenant.page-object';

const expect = chai.expect;

describe('Intervenant e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let intervenantComponentsPage: IntervenantComponentsPage;
  let intervenantUpdatePage: IntervenantUpdatePage;
  let intervenantDeleteDialog: IntervenantDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Intervenants', async () => {
    await navBarPage.goToEntity('intervenant');
    intervenantComponentsPage = new IntervenantComponentsPage();
    await browser.wait(ec.visibilityOf(intervenantComponentsPage.title), 5000);
    expect(await intervenantComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.intervenant.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(intervenantComponentsPage.entities), ec.visibilityOf(intervenantComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Intervenant page', async () => {
    await intervenantComponentsPage.clickOnCreateButton();
    intervenantUpdatePage = new IntervenantUpdatePage();
    expect(await intervenantUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.intervenant.home.createOrEditLabel');
    await intervenantUpdatePage.cancel();
  });

  it('should create and save Intervenants', async () => {
    const nbButtonsBeforeCreate = await intervenantComponentsPage.countDeleteButtons();

    await intervenantComponentsPage.clickOnCreateButton();

    await promise.all([
      intervenantUpdatePage.setNomIntervenantInput('nomIntervenant'),
      intervenantUpdatePage.setPrenomIntervenantInput('prenomIntervenant'),
      intervenantUpdatePage.setEmailProfessionnelInput('emailProfessionnel'),
      intervenantUpdatePage.setRaisonSocialInput('raisonSocial'),
      intervenantUpdatePage.maitreSelectLastOption(),
      intervenantUpdatePage.setRoleInput('role'),
      intervenantUpdatePage.nomProjetSelectLastOption(),
    ]);

    expect(await intervenantUpdatePage.getNomIntervenantInput()).to.eq(
      'nomIntervenant',
      'Expected NomIntervenant value to be equals to nomIntervenant'
    );
    expect(await intervenantUpdatePage.getPrenomIntervenantInput()).to.eq(
      'prenomIntervenant',
      'Expected PrenomIntervenant value to be equals to prenomIntervenant'
    );
    expect(await intervenantUpdatePage.getEmailProfessionnelInput()).to.eq(
      'emailProfessionnel',
      'Expected EmailProfessionnel value to be equals to emailProfessionnel'
    );
    expect(await intervenantUpdatePage.getRaisonSocialInput()).to.eq(
      'raisonSocial',
      'Expected RaisonSocial value to be equals to raisonSocial'
    );
    expect(await intervenantUpdatePage.getRoleInput()).to.eq('role', 'Expected Role value to be equals to role');

    await intervenantUpdatePage.save();
    expect(await intervenantUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await intervenantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Intervenant', async () => {
    const nbButtonsBeforeDelete = await intervenantComponentsPage.countDeleteButtons();
    await intervenantComponentsPage.clickOnLastDeleteButton();

    intervenantDeleteDialog = new IntervenantDeleteDialog();
    expect(await intervenantDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.intervenant.delete.question');
    await intervenantDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(intervenantComponentsPage.title), 5000);

    expect(await intervenantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
