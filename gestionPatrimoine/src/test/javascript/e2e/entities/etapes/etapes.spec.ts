import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EtapesComponentsPage, EtapesDeleteDialog, EtapesUpdatePage } from './etapes.page-object';

const expect = chai.expect;

describe('Etapes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let etapesComponentsPage: EtapesComponentsPage;
  let etapesUpdatePage: EtapesUpdatePage;
  let etapesDeleteDialog: EtapesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Etapes', async () => {
    await navBarPage.goToEntity('etapes');
    etapesComponentsPage = new EtapesComponentsPage();
    await browser.wait(ec.visibilityOf(etapesComponentsPage.title), 5000);
    expect(await etapesComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.etapes.home.title');
    await browser.wait(ec.or(ec.visibilityOf(etapesComponentsPage.entities), ec.visibilityOf(etapesComponentsPage.noResult)), 1000);
  });

  it('should load create Etapes page', async () => {
    await etapesComponentsPage.clickOnCreateButton();
    etapesUpdatePage = new EtapesUpdatePage();
    expect(await etapesUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.etapes.home.createOrEditLabel');
    await etapesUpdatePage.cancel();
  });

  it('should create and save Etapes', async () => {
    const nbButtonsBeforeCreate = await etapesComponentsPage.countDeleteButtons();

    await etapesComponentsPage.clickOnCreateButton();

    await promise.all([
      etapesUpdatePage.setDateDebutInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      etapesUpdatePage.setDateFinInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      etapesUpdatePage.setNomTacheInput('nomTache'),
      etapesUpdatePage.setDurationInput('PT12S'),
      etapesUpdatePage.nomProjetSelectLastOption(),
    ]);

    expect(await etapesUpdatePage.getDateDebutInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dateDebut value to be equals to 2000-12-31'
    );
    expect(await etapesUpdatePage.getDateFinInput()).to.contain('2001-01-01T02:30', 'Expected dateFin value to be equals to 2000-12-31');
    expect(await etapesUpdatePage.getNomTacheInput()).to.eq('nomTache', 'Expected NomTache value to be equals to nomTache');
    expect(await etapesUpdatePage.getDurationInput()).to.contain('12', 'Expected duration value to be equals to 12');

    await etapesUpdatePage.save();
    expect(await etapesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await etapesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Etapes', async () => {
    const nbButtonsBeforeDelete = await etapesComponentsPage.countDeleteButtons();
    await etapesComponentsPage.clickOnLastDeleteButton();

    etapesDeleteDialog = new EtapesDeleteDialog();
    expect(await etapesDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.etapes.delete.question');
    await etapesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(etapesComponentsPage.title), 5000);

    expect(await etapesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
