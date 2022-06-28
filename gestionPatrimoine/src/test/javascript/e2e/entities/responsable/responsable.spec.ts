import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ResponsableComponentsPage, ResponsableDeleteDialog, ResponsableUpdatePage } from './responsable.page-object';

const expect = chai.expect;

describe('Responsable e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let responsableComponentsPage: ResponsableComponentsPage;
  let responsableUpdatePage: ResponsableUpdatePage;
  let responsableDeleteDialog: ResponsableDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Responsables', async () => {
    await navBarPage.goToEntity('responsable');
    responsableComponentsPage = new ResponsableComponentsPage();
    await browser.wait(ec.visibilityOf(responsableComponentsPage.title), 5000);
    expect(await responsableComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.responsable.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(responsableComponentsPage.entities), ec.visibilityOf(responsableComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Responsable page', async () => {
    await responsableComponentsPage.clickOnCreateButton();
    responsableUpdatePage = new ResponsableUpdatePage();
    expect(await responsableUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.responsable.home.createOrEditLabel');
    await responsableUpdatePage.cancel();
  });

  it('should create and save Responsables', async () => {
    const nbButtonsBeforeCreate = await responsableComponentsPage.countDeleteButtons();

    await responsableComponentsPage.clickOnCreateButton();

    await promise.all([
      responsableUpdatePage.setNomResponsableInput('nomResponsable'),
      responsableUpdatePage.setPrenomResponsableInput('prenomResponsable'),
      responsableUpdatePage.setEmailInput('email'),
      responsableUpdatePage.setSpecialiteInput('specialite'),
      responsableUpdatePage.setNumb1Input('numb1'),
      responsableUpdatePage.setNumb2Input('numb2'),
      responsableUpdatePage.setRaisonSocialInput('raisonSocial'),
    ]);

    expect(await responsableUpdatePage.getNomResponsableInput()).to.eq(
      'nomResponsable',
      'Expected NomResponsable value to be equals to nomResponsable'
    );
    expect(await responsableUpdatePage.getPrenomResponsableInput()).to.eq(
      'prenomResponsable',
      'Expected PrenomResponsable value to be equals to prenomResponsable'
    );
    expect(await responsableUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await responsableUpdatePage.getSpecialiteInput()).to.eq('specialite', 'Expected Specialite value to be equals to specialite');
    expect(await responsableUpdatePage.getNumb1Input()).to.eq('numb1', 'Expected Numb1 value to be equals to numb1');
    expect(await responsableUpdatePage.getNumb2Input()).to.eq('numb2', 'Expected Numb2 value to be equals to numb2');
    expect(await responsableUpdatePage.getRaisonSocialInput()).to.eq(
      'raisonSocial',
      'Expected RaisonSocial value to be equals to raisonSocial'
    );

    await responsableUpdatePage.save();
    expect(await responsableUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await responsableComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Responsable', async () => {
    const nbButtonsBeforeDelete = await responsableComponentsPage.countDeleteButtons();
    await responsableComponentsPage.clickOnLastDeleteButton();

    responsableDeleteDialog = new ResponsableDeleteDialog();
    expect(await responsableDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.responsable.delete.question');
    await responsableDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(responsableComponentsPage.title), 5000);

    expect(await responsableComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
