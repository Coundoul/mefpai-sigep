import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CategorieMatiereComponentsPage, CategorieMatiereDeleteDialog, CategorieMatiereUpdatePage } from './categorie-matiere.page-object';

const expect = chai.expect;

describe('CategorieMatiere e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let categorieMatiereComponentsPage: CategorieMatiereComponentsPage;
  let categorieMatiereUpdatePage: CategorieMatiereUpdatePage;
  let categorieMatiereDeleteDialog: CategorieMatiereDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CategorieMatieres', async () => {
    await navBarPage.goToEntity('categorie-matiere');
    categorieMatiereComponentsPage = new CategorieMatiereComponentsPage();
    await browser.wait(ec.visibilityOf(categorieMatiereComponentsPage.title), 5000);
    expect(await categorieMatiereComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.categorieMatiere.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(categorieMatiereComponentsPage.entities), ec.visibilityOf(categorieMatiereComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CategorieMatiere page', async () => {
    await categorieMatiereComponentsPage.clickOnCreateButton();
    categorieMatiereUpdatePage = new CategorieMatiereUpdatePage();
    expect(await categorieMatiereUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.categorieMatiere.home.createOrEditLabel');
    await categorieMatiereUpdatePage.cancel();
  });

  it('should create and save CategorieMatieres', async () => {
    const nbButtonsBeforeCreate = await categorieMatiereComponentsPage.countDeleteButtons();

    await categorieMatiereComponentsPage.clickOnCreateButton();

    await promise.all([categorieMatiereUpdatePage.setCategorieInput('categorie')]);

    expect(await categorieMatiereUpdatePage.getCategorieInput()).to.eq('categorie', 'Expected Categorie value to be equals to categorie');

    await categorieMatiereUpdatePage.save();
    expect(await categorieMatiereUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await categorieMatiereComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CategorieMatiere', async () => {
    const nbButtonsBeforeDelete = await categorieMatiereComponentsPage.countDeleteButtons();
    await categorieMatiereComponentsPage.clickOnLastDeleteButton();

    categorieMatiereDeleteDialog = new CategorieMatiereDeleteDialog();
    expect(await categorieMatiereDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.categorieMatiere.delete.question');
    await categorieMatiereDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(categorieMatiereComponentsPage.title), 5000);

    expect(await categorieMatiereComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
