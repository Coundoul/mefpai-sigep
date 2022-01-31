import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DetailSortieComponentsPage, DetailSortieDeleteDialog, DetailSortieUpdatePage } from './detail-sortie.page-object';

const expect = chai.expect;

describe('DetailSortie e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let detailSortieComponentsPage: DetailSortieComponentsPage;
  let detailSortieUpdatePage: DetailSortieUpdatePage;
  let detailSortieDeleteDialog: DetailSortieDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DetailSorties', async () => {
    await navBarPage.goToEntity('detail-sortie');
    detailSortieComponentsPage = new DetailSortieComponentsPage();
    await browser.wait(ec.visibilityOf(detailSortieComponentsPage.title), 5000);
    expect(await detailSortieComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.detailSortie.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(detailSortieComponentsPage.entities), ec.visibilityOf(detailSortieComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DetailSortie page', async () => {
    await detailSortieComponentsPage.clickOnCreateButton();
    detailSortieUpdatePage = new DetailSortieUpdatePage();
    expect(await detailSortieUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.detailSortie.home.createOrEditLabel');
    await detailSortieUpdatePage.cancel();
  });

  it('should create and save DetailSorties', async () => {
    const nbButtonsBeforeCreate = await detailSortieComponentsPage.countDeleteButtons();

    await detailSortieComponentsPage.clickOnCreateButton();

    await promise.all([
      detailSortieUpdatePage.setPieceJointeInput('pieceJointe'),
      detailSortieUpdatePage.setIdPersInput('5'),
      detailSortieUpdatePage.typeBonSelectLastOption(),
    ]);

    expect(await detailSortieUpdatePage.getPieceJointeInput()).to.eq(
      'pieceJointe',
      'Expected PieceJointe value to be equals to pieceJointe'
    );
    expect(await detailSortieUpdatePage.getIdPersInput()).to.eq('5', 'Expected idPers value to be equals to 5');

    await detailSortieUpdatePage.save();
    expect(await detailSortieUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await detailSortieComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last DetailSortie', async () => {
    const nbButtonsBeforeDelete = await detailSortieComponentsPage.countDeleteButtons();
    await detailSortieComponentsPage.clickOnLastDeleteButton();

    detailSortieDeleteDialog = new DetailSortieDeleteDialog();
    expect(await detailSortieDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.detailSortie.delete.question');
    await detailSortieDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(detailSortieComponentsPage.title), 5000);

    expect(await detailSortieComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
