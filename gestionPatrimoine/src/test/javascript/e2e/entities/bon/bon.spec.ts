import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BonComponentsPage, BonDeleteDialog, BonUpdatePage } from './bon.page-object';

const expect = chai.expect;

describe('Bon e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let bonComponentsPage: BonComponentsPage;
  let bonUpdatePage: BonUpdatePage;
  let bonDeleteDialog: BonDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Bons', async () => {
    await navBarPage.goToEntity('bon');
    bonComponentsPage = new BonComponentsPage();
    await browser.wait(ec.visibilityOf(bonComponentsPage.title), 5000);
    expect(await bonComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.bon.home.title');
    await browser.wait(ec.or(ec.visibilityOf(bonComponentsPage.entities), ec.visibilityOf(bonComponentsPage.noResult)), 1000);
  });

  it('should load create Bon page', async () => {
    await bonComponentsPage.clickOnCreateButton();
    bonUpdatePage = new BonUpdatePage();
    expect(await bonUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.bon.home.createOrEditLabel');
    await bonUpdatePage.cancel();
  });

  it('should create and save Bons', async () => {
    const nbButtonsBeforeCreate = await bonComponentsPage.countDeleteButtons();

    await bonComponentsPage.clickOnCreateButton();

    await promise.all([
      bonUpdatePage.typeBonSelectLastOption(),
      bonUpdatePage.setQuantiteLivreInput('5'),
      bonUpdatePage.setQuantiteCommandeInput('5'),
      bonUpdatePage.setDateCreationInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
    ]);

    expect(await bonUpdatePage.getQuantiteLivreInput()).to.eq('5', 'Expected quantiteLivre value to be equals to 5');
    expect(await bonUpdatePage.getQuantiteCommandeInput()).to.eq('5', 'Expected quantiteCommande value to be equals to 5');
    expect(await bonUpdatePage.getDateCreationInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dateCreation value to be equals to 2000-12-31'
    );
    const selectedEtat = bonUpdatePage.getEtatInput();
    if (await selectedEtat.isSelected()) {
      await bonUpdatePage.getEtatInput().click();
      expect(await bonUpdatePage.getEtatInput().isSelected(), 'Expected etat not to be selected').to.be.false;
    } else {
      await bonUpdatePage.getEtatInput().click();
      expect(await bonUpdatePage.getEtatInput().isSelected(), 'Expected etat to be selected').to.be.true;
    }

    await bonUpdatePage.save();
    expect(await bonUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await bonComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Bon', async () => {
    const nbButtonsBeforeDelete = await bonComponentsPage.countDeleteButtons();
    await bonComponentsPage.clickOnLastDeleteButton();

    bonDeleteDialog = new BonDeleteDialog();
    expect(await bonDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.bon.delete.question');
    await bonDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(bonComponentsPage.title), 5000);

    expect(await bonComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
