import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  MangasinierFichisteComponentsPage,
  MangasinierFichisteDeleteDialog,
  MangasinierFichisteUpdatePage,
} from './mangasinier-fichiste.page-object';

const expect = chai.expect;

describe('MangasinierFichiste e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let mangasinierFichisteComponentsPage: MangasinierFichisteComponentsPage;
  let mangasinierFichisteUpdatePage: MangasinierFichisteUpdatePage;
  let mangasinierFichisteDeleteDialog: MangasinierFichisteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MangasinierFichistes', async () => {
    await navBarPage.goToEntity('mangasinier-fichiste');
    mangasinierFichisteComponentsPage = new MangasinierFichisteComponentsPage();
    await browser.wait(ec.visibilityOf(mangasinierFichisteComponentsPage.title), 5000);
    expect(await mangasinierFichisteComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.mangasinierFichiste.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(mangasinierFichisteComponentsPage.entities), ec.visibilityOf(mangasinierFichisteComponentsPage.noResult)),
      1000
    );
  });

  it('should load create MangasinierFichiste page', async () => {
    await mangasinierFichisteComponentsPage.clickOnCreateButton();
    mangasinierFichisteUpdatePage = new MangasinierFichisteUpdatePage();
    expect(await mangasinierFichisteUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.mangasinierFichiste.home.createOrEditLabel');
    await mangasinierFichisteUpdatePage.cancel();
  });

  it('should create and save MangasinierFichistes', async () => {
    const nbButtonsBeforeCreate = await mangasinierFichisteComponentsPage.countDeleteButtons();

    await mangasinierFichisteComponentsPage.clickOnCreateButton();

    await promise.all([
      mangasinierFichisteUpdatePage.setNomPersInput('nomPers'),
      mangasinierFichisteUpdatePage.setPrenomPersInput('prenomPers'),
      mangasinierFichisteUpdatePage.sexeSelectLastOption(),
      mangasinierFichisteUpdatePage.setMobileInput('mobile'),
      mangasinierFichisteUpdatePage.setAdresseInput('adresse'),
      mangasinierFichisteUpdatePage.directionSelectLastOption(),
      mangasinierFichisteUpdatePage.comptablePrincipaleSelectLastOption(),
    ]);

    expect(await mangasinierFichisteUpdatePage.getNomPersInput()).to.eq('nomPers', 'Expected NomPers value to be equals to nomPers');
    expect(await mangasinierFichisteUpdatePage.getPrenomPersInput()).to.eq(
      'prenomPers',
      'Expected PrenomPers value to be equals to prenomPers'
    );
    expect(await mangasinierFichisteUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await mangasinierFichisteUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');

    await mangasinierFichisteUpdatePage.save();
    expect(await mangasinierFichisteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await mangasinierFichisteComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last MangasinierFichiste', async () => {
    const nbButtonsBeforeDelete = await mangasinierFichisteComponentsPage.countDeleteButtons();
    await mangasinierFichisteComponentsPage.clickOnLastDeleteButton();

    mangasinierFichisteDeleteDialog = new MangasinierFichisteDeleteDialog();
    expect(await mangasinierFichisteDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.mangasinierFichiste.delete.question');
    await mangasinierFichisteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(mangasinierFichisteComponentsPage.title), 5000);

    expect(await mangasinierFichisteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
