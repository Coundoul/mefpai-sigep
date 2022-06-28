import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EquipementComponentsPage, EquipementDeleteDialog, EquipementUpdatePage } from './equipement.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Equipement e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let equipementComponentsPage: EquipementComponentsPage;
  let equipementUpdatePage: EquipementUpdatePage;
  let equipementDeleteDialog: EquipementDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Equipements', async () => {
    await navBarPage.goToEntity('equipement');
    equipementComponentsPage = new EquipementComponentsPage();
    await browser.wait(ec.visibilityOf(equipementComponentsPage.title), 5000);
    expect(await equipementComponentsPage.getTitle()).to.eq('gestionPatrimoineApp.equipement.home.title');
    await browser.wait(ec.or(ec.visibilityOf(equipementComponentsPage.entities), ec.visibilityOf(equipementComponentsPage.noResult)), 1000);
  });

  it('should load create Equipement page', async () => {
    await equipementComponentsPage.clickOnCreateButton();
    equipementUpdatePage = new EquipementUpdatePage();
    expect(await equipementUpdatePage.getPageTitle()).to.eq('gestionPatrimoineApp.equipement.home.createOrEditLabel');
    await equipementUpdatePage.cancel();
  });

  it('should create and save Equipements', async () => {
    const nbButtonsBeforeCreate = await equipementComponentsPage.countDeleteButtons();

    await equipementComponentsPage.clickOnCreateButton();

    await promise.all([
      equipementUpdatePage.setReferenceInput('reference'),
      equipementUpdatePage.setDescriptionInput('description'),
      equipementUpdatePage.setPrixUnitaireInput('5'),
      equipementUpdatePage.setTypeMatiereInput('typeMatiere'),
      equipementUpdatePage.setQuantiteInput('5'),
      equipementUpdatePage.setEtatMatiereInput('etatMatiere'),
      equipementUpdatePage.setPhotoInput(absolutePath),
      equipementUpdatePage.nomMagazinSelectLastOption(),
      equipementUpdatePage.nomFournisseurSelectLastOption(),
      equipementUpdatePage.bonSelectLastOption(),
      equipementUpdatePage.categorieSelectLastOption(),
    ]);

    expect(await equipementUpdatePage.getReferenceInput()).to.eq('reference', 'Expected Reference value to be equals to reference');
    expect(await equipementUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    expect(await equipementUpdatePage.getPrixUnitaireInput()).to.eq('5', 'Expected prixUnitaire value to be equals to 5');
    expect(await equipementUpdatePage.getTypeMatiereInput()).to.eq('typeMatiere', 'Expected TypeMatiere value to be equals to typeMatiere');
    expect(await equipementUpdatePage.getQuantiteInput()).to.eq('5', 'Expected quantite value to be equals to 5');
    expect(await equipementUpdatePage.getEtatMatiereInput()).to.eq('etatMatiere', 'Expected EtatMatiere value to be equals to etatMatiere');
    const selectedGroup = equipementUpdatePage.getGroupInput();
    if (await selectedGroup.isSelected()) {
      await equipementUpdatePage.getGroupInput().click();
      expect(await equipementUpdatePage.getGroupInput().isSelected(), 'Expected group not to be selected').to.be.false;
    } else {
      await equipementUpdatePage.getGroupInput().click();
      expect(await equipementUpdatePage.getGroupInput().isSelected(), 'Expected group to be selected').to.be.true;
    }
    expect(await equipementUpdatePage.getPhotoInput()).to.endsWith(
      fileNameToUpload,
      'Expected Photo value to be end with ' + fileNameToUpload
    );

    await equipementUpdatePage.save();
    expect(await equipementUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await equipementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Equipement', async () => {
    const nbButtonsBeforeDelete = await equipementComponentsPage.countDeleteButtons();
    await equipementComponentsPage.clickOnLastDeleteButton();

    equipementDeleteDialog = new EquipementDeleteDialog();
    expect(await equipementDeleteDialog.getDialogTitle()).to.eq('gestionPatrimoineApp.equipement.delete.question');
    await equipementDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(equipementComponentsPage.title), 5000);

    expect(await equipementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
