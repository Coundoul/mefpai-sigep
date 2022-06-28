import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { NatureFoncierComponentsPage, NatureFoncierDeleteDialog, NatureFoncierUpdatePage } from './nature-foncier.page-object';

const expect = chai.expect;

describe('NatureFoncier e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let natureFoncierComponentsPage: NatureFoncierComponentsPage;
  let natureFoncierUpdatePage: NatureFoncierUpdatePage;
  let natureFoncierDeleteDialog: NatureFoncierDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load NatureFonciers', async () => {
    await navBarPage.goToEntity('nature-foncier');
    natureFoncierComponentsPage = new NatureFoncierComponentsPage();
    await browser.wait(ec.visibilityOf(natureFoncierComponentsPage.title), 5000);
    expect(await natureFoncierComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.natureFoncier.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(natureFoncierComponentsPage.entities), ec.visibilityOf(natureFoncierComponentsPage.noResult)),
      1000
    );
  });

  it('should load create NatureFoncier page', async () => {
    await natureFoncierComponentsPage.clickOnCreateButton();
    natureFoncierUpdatePage = new NatureFoncierUpdatePage();
    expect(await natureFoncierUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.natureFoncier.home.createOrEditLabel');
    await natureFoncierUpdatePage.cancel();
  });

  it('should create and save NatureFonciers', async () => {
    const nbButtonsBeforeCreate = await natureFoncierComponentsPage.countDeleteButtons();

    await natureFoncierComponentsPage.clickOnCreateButton();

    await promise.all([
      natureFoncierUpdatePage.setTypeFoncierInput('typeFoncier'),
      natureFoncierUpdatePage.setPieceJointeInput('pieceJointe'),
      natureFoncierUpdatePage.nomCorpsSelectLastOption(),
    ]);

    expect(await natureFoncierUpdatePage.getTypeFoncierInput()).to.eq(
      'typeFoncier',
      'Expected TypeFoncier value to be equals to typeFoncier'
    );
    expect(await natureFoncierUpdatePage.getPieceJointeInput()).to.eq(
      'pieceJointe',
      'Expected PieceJointe value to be equals to pieceJointe'
    );

    await natureFoncierUpdatePage.save();
    expect(await natureFoncierUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await natureFoncierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last NatureFoncier', async () => {
    const nbButtonsBeforeDelete = await natureFoncierComponentsPage.countDeleteButtons();
    await natureFoncierComponentsPage.clickOnLastDeleteButton();

    natureFoncierDeleteDialog = new NatureFoncierDeleteDialog();
    expect(await natureFoncierDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.natureFoncier.delete.question');
    await natureFoncierDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(natureFoncierComponentsPage.title), 5000);

    expect(await natureFoncierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
