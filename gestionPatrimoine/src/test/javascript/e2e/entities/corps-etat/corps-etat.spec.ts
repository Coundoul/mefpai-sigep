import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CorpsEtatComponentsPage, CorpsEtatDeleteDialog, CorpsEtatUpdatePage } from './corps-etat.page-object';

const expect = chai.expect;

describe('CorpsEtat e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let corpsEtatComponentsPage: CorpsEtatComponentsPage;
  let corpsEtatUpdatePage: CorpsEtatUpdatePage;
  let corpsEtatDeleteDialog: CorpsEtatDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CorpsEtats', async () => {
    await navBarPage.goToEntity('corps-etat');
    corpsEtatComponentsPage = new CorpsEtatComponentsPage();
    await browser.wait(ec.visibilityOf(corpsEtatComponentsPage.title), 5000);
    expect(await corpsEtatComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.corpsEtat.home.title');
    await browser.wait(ec.or(ec.visibilityOf(corpsEtatComponentsPage.entities), ec.visibilityOf(corpsEtatComponentsPage.noResult)), 1000);
  });

  it('should load create CorpsEtat page', async () => {
    await corpsEtatComponentsPage.clickOnCreateButton();
    corpsEtatUpdatePage = new CorpsEtatUpdatePage();
    expect(await corpsEtatUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.corpsEtat.home.createOrEditLabel');
    await corpsEtatUpdatePage.cancel();
  });

  it('should create and save CorpsEtats', async () => {
    const nbButtonsBeforeCreate = await corpsEtatComponentsPage.countDeleteButtons();

    await corpsEtatComponentsPage.clickOnCreateButton();

    await promise.all([
      corpsEtatUpdatePage.setNomCorpsInput('nomCorps'),
      corpsEtatUpdatePage.setGrosOeuvreInput('grosOeuvre'),
      corpsEtatUpdatePage.setDescriptionGrosOeuvreInput('descriptionGrosOeuvre'),
      corpsEtatUpdatePage.setSecondOeuvreInput('secondOeuvre'),
      corpsEtatUpdatePage.setDescriptionSecondOeuvreInput('descriptionSecondOeuvre'),
      corpsEtatUpdatePage.nomResponsableSelectLastOption(),
    ]);

    expect(await corpsEtatUpdatePage.getNomCorpsInput()).to.eq('nomCorps', 'Expected NomCorps value to be equals to nomCorps');
    expect(await corpsEtatUpdatePage.getGrosOeuvreInput()).to.eq('grosOeuvre', 'Expected GrosOeuvre value to be equals to grosOeuvre');
    expect(await corpsEtatUpdatePage.getDescriptionGrosOeuvreInput()).to.eq(
      'descriptionGrosOeuvre',
      'Expected DescriptionGrosOeuvre value to be equals to descriptionGrosOeuvre'
    );
    expect(await corpsEtatUpdatePage.getSecondOeuvreInput()).to.eq(
      'secondOeuvre',
      'Expected SecondOeuvre value to be equals to secondOeuvre'
    );
    expect(await corpsEtatUpdatePage.getDescriptionSecondOeuvreInput()).to.eq(
      'descriptionSecondOeuvre',
      'Expected DescriptionSecondOeuvre value to be equals to descriptionSecondOeuvre'
    );
    const selectedOservation = corpsEtatUpdatePage.getOservationInput();
    if (await selectedOservation.isSelected()) {
      await corpsEtatUpdatePage.getOservationInput().click();
      expect(await corpsEtatUpdatePage.getOservationInput().isSelected(), 'Expected oservation not to be selected').to.be.false;
    } else {
      await corpsEtatUpdatePage.getOservationInput().click();
      expect(await corpsEtatUpdatePage.getOservationInput().isSelected(), 'Expected oservation to be selected').to.be.true;
    }
    const selectedEtatCorps = corpsEtatUpdatePage.getEtatCorpsInput();
    if (await selectedEtatCorps.isSelected()) {
      await corpsEtatUpdatePage.getEtatCorpsInput().click();
      expect(await corpsEtatUpdatePage.getEtatCorpsInput().isSelected(), 'Expected etatCorps not to be selected').to.be.false;
    } else {
      await corpsEtatUpdatePage.getEtatCorpsInput().click();
      expect(await corpsEtatUpdatePage.getEtatCorpsInput().isSelected(), 'Expected etatCorps to be selected').to.be.true;
    }

    await corpsEtatUpdatePage.save();
    expect(await corpsEtatUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await corpsEtatComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CorpsEtat', async () => {
    const nbButtonsBeforeDelete = await corpsEtatComponentsPage.countDeleteButtons();
    await corpsEtatComponentsPage.clickOnLastDeleteButton();

    corpsEtatDeleteDialog = new CorpsEtatDeleteDialog();
    expect(await corpsEtatDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.corpsEtat.delete.question');
    await corpsEtatDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(corpsEtatComponentsPage.title), 5000);

    expect(await corpsEtatComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
