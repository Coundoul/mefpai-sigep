import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { TypeBatimentComponentsPage, TypeBatimentDeleteDialog, TypeBatimentUpdatePage } from './type-batiment.page-object';

const expect = chai.expect;

describe('TypeBatiment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let typeBatimentComponentsPage: TypeBatimentComponentsPage;
  let typeBatimentUpdatePage: TypeBatimentUpdatePage;
  let typeBatimentDeleteDialog: TypeBatimentDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TypeBatiments', async () => {
    await navBarPage.goToEntity('type-batiment');
    typeBatimentComponentsPage = new TypeBatimentComponentsPage();
    await browser.wait(ec.visibilityOf(typeBatimentComponentsPage.title), 5000);
    expect(await typeBatimentComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.typeBatiment.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(typeBatimentComponentsPage.entities), ec.visibilityOf(typeBatimentComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TypeBatiment page', async () => {
    await typeBatimentComponentsPage.clickOnCreateButton();
    typeBatimentUpdatePage = new TypeBatimentUpdatePage();
    expect(await typeBatimentUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.typeBatiment.home.createOrEditLabel');
    await typeBatimentUpdatePage.cancel();
  });

  it('should create and save TypeBatiments', async () => {
    const nbButtonsBeforeCreate = await typeBatimentComponentsPage.countDeleteButtons();

    await typeBatimentComponentsPage.clickOnCreateButton();

    await promise.all([typeBatimentUpdatePage.setTypeBaInput('typeBa'), typeBatimentUpdatePage.nomBatimentSelectLastOption()]);

    expect(await typeBatimentUpdatePage.getTypeBaInput()).to.eq('typeBa', 'Expected TypeBa value to be equals to typeBa');

    await typeBatimentUpdatePage.save();
    expect(await typeBatimentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await typeBatimentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last TypeBatiment', async () => {
    const nbButtonsBeforeDelete = await typeBatimentComponentsPage.countDeleteButtons();
    await typeBatimentComponentsPage.clickOnLastDeleteButton();

    typeBatimentDeleteDialog = new TypeBatimentDeleteDialog();
    expect(await typeBatimentDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.typeBatiment.delete.question');
    await typeBatimentDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(typeBatimentComponentsPage.title), 5000);

    expect(await typeBatimentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
