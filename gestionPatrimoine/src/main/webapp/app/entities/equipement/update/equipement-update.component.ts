import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { MatHorizontalStepper } from '@angular/material/stepper';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEquipement, Equipement } from '../equipement.model';
import { EquipementService } from '../service/equipement.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMagazin } from 'app/entities/magazin/magazin.model';
import { MagazinService } from 'app/entities/magazin/service/magazin.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { IBon } from 'app/entities/bon/bon.model';
import { BonService } from 'app/entities/bon/service/bon.service';
import { ICategorieMatiere } from 'app/entities/categorie-matiere/categorie-matiere.model';
import { CategorieMatiereService } from 'app/entities/categorie-matiere/service/categorie-matiere.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategorieMatiereUpdateComponent } from 'app/entities/categorie-matiere/update/categorie-matiere-update.component';
import { MagazinUpdateComponent } from 'app/entities/magazin/update/magazin-update.component';
import { FournisseurUpdateComponent } from 'app/entities/fournisseur/update/fournisseur-update.component';

@Component({
  selector: 'jhi-equipement-update',
  templateUrl: './equipement-update.component.html',
  styleUrls: ['./equipement-update.component.scss'],
})
export class EquipementUpdateComponent implements OnInit {
  isSaving = false;
  @ViewChild('stepper') stepper!: MatHorizontalStepper;
  step = 0;

  magazinsSharedCollection: IMagazin[] = [];
  fournisseursSharedCollection: IFournisseur[] = [];
  bonsSharedCollection: IBon[] = [];
  categorieMatieresSharedCollection: ICategorieMatiere[] = [];

  editForm = this.fb.group({
    id: [],
    reference: [null, [Validators.required]],
    nomMatiere: [null, [Validators.required]],
    caracteristique: [null, [Validators.required]],
    description: [],
    typeMatiere: [null, [Validators.required]],
    quantite: [null, [Validators.required]],
    etatMatiere: [],
    // dateSignalisation: [],
    group: [null, [Validators.required]],
    photo: [],
    photo1: [],
    photo2: [],
    photoContentType: [],
    photo1ContentType: [],
    photo2ContentType: [],
    nomMagazin: [],
    nomFournisseur: [],
    bon: [],
    categorie: [],
  });
  categorieMatiere: any;
  magazin: any;
  fournisseur: any;

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected equipementService: EquipementService,
    protected magazinService: MagazinService,
    protected fournisseurService: FournisseurService,
    protected bonService: BonService,
    protected modalService: NgbModal,
    protected categorieMatiereService: CategorieMatiereService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipement }) => {
      // if (equipement.id === undefined) {
      //   const today = dayjs().startOf('day');
      //   equipement.dateSignalisation = today;
      // }
      this.updateForm(equipement);

      this.loadRelationshipsOptions();
    });
  }

  openMagazin(): void {
    const modalRef = this.modalService.open(MagazinUpdateComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.magazin = this.magazin;
  }
  openFournisseur(): void {
    const modalRef = this.modalService.open(FournisseurUpdateComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fournisseur = this.fournisseur;
  }
  openCategorie(): void {
    const modalRef = this.modalService.open(CategorieMatiereUpdateComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.categorieMatiere = this.categorieMatiere;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('gestionPatrimoineApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  public onSetepChange(event: any): void {
    this.step = event.selectedIndex;
  }

  save(): void {
    this.isSaving = true;
    const equipement = this.createFromForm();
    //alert("1112");
    if (equipement.id !== undefined) {
      this.subscribeToSaveResponse(this.equipementService.update(equipement));
    } else {
      this.subscribeToSaveResponse(this.equipementService.create(equipement));
    }
  }

  trackMagazinById(index: number, item: IMagazin): number {
    return item.id!;
  }

  trackFournisseurById(index: number, item: IFournisseur): number {
    return item.id!;
  }

  trackBonById(index: number, item: IBon): number {
    return item.id!;
  }

  trackCategorieMatiereById(index: number, item: ICategorieMatiere): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquipement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSave(),
      () => this.onSaveError()
    );
  }

  protected onSave(): void {
    this.router.navigate(['/equipement']);
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(equipement: IEquipement): void {
    this.editForm.patchValue({
      id: equipement.id,
      reference: equipement.reference,
      nomMatiere: equipement.nomMatiere,
      caracteristique: equipement.caracteristique,
      description: equipement.description,
      typeMatiere: equipement.typeMatiere,
      quantite: equipement.quantite,
      etatMatiere: equipement.etatMatiere,
      // dateSignalisation: equipement.dateSignalisation ? equipement.dateSignalisation.format(DATE_TIME_FORMAT) : null,
      group: equipement.group,
      photo: equipement.photo,
      photo1: equipement.photo1,
      photo2: equipement.photo2,
      photoContentType: equipement.photoContentType,
      photo1ContentType: equipement.photo1ContentType,
      photo2ContentType: equipement.photo2ContentType,
      nomMagazin: equipement.nomMagazin,
      nomFournisseur: equipement.nomFournisseur,
      bon: equipement.bon,
      categorie: equipement.categorie,
    });

    this.magazinsSharedCollection = this.magazinService.addMagazinToCollectionIfMissing(
      this.magazinsSharedCollection,
      equipement.nomMagazin
    );
    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing(
      this.fournisseursSharedCollection,
      equipement.nomFournisseur
    );
    this.bonsSharedCollection = this.bonService.addBonToCollectionIfMissing(this.bonsSharedCollection, equipement.bon);
    this.categorieMatieresSharedCollection = this.categorieMatiereService.addCategorieMatiereToCollectionIfMissing(
      this.categorieMatieresSharedCollection,
      equipement.categorie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.magazinService
      .query()
      .pipe(map((res: HttpResponse<IMagazin[]>) => res.body ?? []))
      .pipe(
        map((magazins: IMagazin[]) => this.magazinService.addMagazinToCollectionIfMissing(magazins, this.editForm.get('nomMagazin')!.value))
      )
      .subscribe((magazins: IMagazin[]) => (this.magazinsSharedCollection = magazins));

    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing(fournisseurs, this.editForm.get('nomFournisseur')!.value)
        )
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursSharedCollection = fournisseurs));

    this.bonService
      .query()
      .pipe(map((res: HttpResponse<IBon[]>) => res.body ?? []))
      .pipe(map((bons: IBon[]) => this.bonService.addBonToCollectionIfMissing(bons, this.editForm.get('bon')!.value)))
      .subscribe((bons: IBon[]) => (this.bonsSharedCollection = bons));

    this.categorieMatiereService
      .query()
      .pipe(map((res: HttpResponse<ICategorieMatiere[]>) => res.body ?? []))
      .pipe(
        map((categorieMatieres: ICategorieMatiere[]) =>
          this.categorieMatiereService.addCategorieMatiereToCollectionIfMissing(categorieMatieres, this.editForm.get('categorie')!.value)
        )
      )
      .subscribe((categorieMatieres: ICategorieMatiere[]) => (this.categorieMatieresSharedCollection = categorieMatieres));
  }

  protected createFromForm(): IEquipement {
    return {
      ...new Equipement(),
      id: this.editForm.get(['id'])!.value,
      reference: this.editForm.get(['reference'])!.value,
      nomMatiere: this.editForm.get(['nomMatiere'])!.value,
      caracteristique: this.editForm.get(['caracteristique'])!.value,
      description: this.editForm.get(['description'])!.value,
      typeMatiere: this.editForm.get(['typeMatiere'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      etatMatiere: this.editForm.get(['etatMatiere'])!.value,
      // dateSignalisation: this.editForm.get(['dateSignalisation'])!.value
      // ? dayjs(this.editForm.get(['dateSignalisation'])!.value, DATE_TIME_FORMAT)
      //   : undefined,
      group: this.editForm.get(['group'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo1ContentType: this.editForm.get(['photo1ContentType'])!.value,
      photo2ContentType: this.editForm.get(['photo2ContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      photo1: this.editForm.get(['photo1'])!.value,
      photo2: this.editForm.get(['photo2'])!.value,
      nomMagazin: this.editForm.get(['nomMagazin'])!.value,
      nomFournisseur: this.editForm.get(['nomFournisseur'])!.value,
      bon: this.editForm.get(['bon'])!.value,
      categorie: this.editForm.get(['categorie'])!.value,
    };
  }
}
