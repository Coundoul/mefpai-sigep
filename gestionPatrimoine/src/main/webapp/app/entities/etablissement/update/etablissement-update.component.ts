import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEtablissement, Etablissement } from '../etablissement.model';
import { EtablissementService } from '../service/etablissement.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { MatHorizontalStepper } from '@angular/material/stepper';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-etablissement-update',
  templateUrl: './etablissement-update.component.html',
  styleUrls: ['./etablissement-update.component.scss'],
})
export class EtablissementUpdateComponent implements OnInit {
  isSaving = false;
  @ViewChild('stepper') stepper!: MatHorizontalStepper;
  step = 0;

  quartiersSharedCollection: IQuartier[] = [];

  editForm = this.fb.group({
    id: [],
    nomEtablissement: [null, [Validators.required]],
    photo: [null, [Validators.required]],
    photoContentType: [],
    adresse: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    email: [null, [Validators.required]],
    surfaceBatie: [null, [Validators.required]],
    superficie: [null, [Validators.required]],
    statusFoncier: [null, [Validators.required]],
    nombreApprenants: [null, [Validators.required]],
    proprietaire: [null, [Validators.required]],
    possibiliteExtension: [null, [Validators.required]],
    descriptionExtension: [],
    branchementEau: [null, [Validators.required]],
    branchementElectricite: [null, [Validators.required]],
    puissanceSouscrite: [null, [Validators.required]],
    typeReseau: [null, [Validators.required]],
    circuitTerre: [null, [Validators.required]],
    protectionArret: [null, [Validators.required]],
    protectionFoudre: [null, [Validators.required]],
    connexionTel: [null, [Validators.required]],
    connexionInternet: [null, [Validators.required]],
    environnement: [],
    dispositif: [],
    idPers: [null, [Validators.required]],
    nomQuartier: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected quartierService: QuartierService,
    protected fb: FormBuilder,
    protected elementRef: ElementRef,
    protected eventManager: EventManager
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etablissement }) => {
      this.updateForm(etablissement);

      this.loadRelationshipsOptions();
    });
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
    const etablissement = this.createFromForm();
    if (etablissement.id !== undefined) {
      this.subscribeToSaveResponse(this.etablissementService.update(etablissement));
    } else {
      this.subscribeToSaveResponse(this.etablissementService.create(etablissement));
    }
  }

  trackQuartierById(index: number, item: IQuartier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtablissement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
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

  protected updateForm(etablissement: IEtablissement): void {
    this.editForm.patchValue({
      id: etablissement.id,
      nomEtablissement: etablissement.nomEtablissement,
      photo: etablissement.photo,
      photoContentType: etablissement.photoContentType,
      adresse: etablissement.adresse,
      telephone: etablissement.telephone,
      email: etablissement.email,
      surfaceBatie: etablissement.surfaceBatie,
      superficie: etablissement.superficie,
      statusFoncier: etablissement.statusFoncier,
      nombreApprenants: etablissement.nombreApprenants,
      proprietaire: etablissement.proprietaire,
      possibilteExtension: etablissement.possibiliteExtension,
      descriptionExtension: etablissement.descriptionExtension,
      branchementEau: etablissement.branchementEau,
      branchementElectricite: etablissement.branchementEau,
      puissanceSouscrite: etablissement.puissanceSouscrite,
      typeReseau: etablissement.typeReseau,
      circuitTerre: etablissement.circuitTerre,
      protectionArret: etablissement.protectionArret,
      protectionFoudre: etablissement.protectionFoudre,
      connexionTel: etablissement.connexionTel,
      connexionInternet: etablissement.connexionInternet,
      environnement: etablissement.environnement,
      dispositif: etablissement.dispositif,
      idPers: etablissement.idPers,
      nomQuartier: etablissement.nomQuartier,
    });
  }

  protected loadRelationshipsOptions(): void {
    this.quartierService
      .query()
      .pipe(map((res: HttpResponse<IQuartier[]>) => res.body ?? []))
      .pipe(
        map((quartiers: IQuartier[]) =>
          this.quartierService.addQuartierToCollectionIfMissing(quartiers, this.editForm.get('nomQuartier')!.value)
        )
      )
      .subscribe((quartiers: IQuartier[]) => (this.quartiersSharedCollection = quartiers));
  }

  protected createFromForm(): IEtablissement {
    return {
      ...new Etablissement(),
      id: this.editForm.get(['id'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      email: this.editForm.get(['email'])!.value,
      surfaceBatie: this.editForm.get(['surfaceBatie'])!.value,
      superficie: this.editForm.get(['superficie'])!.value,
      statusFoncier: this.editForm.get(['statusFoncier'])!.value,
      nombreApprenants: this.editForm.get(['nombreApprenants'])!.value,
      proprietaire: this.editForm.get(['proprietaire'])!.value,
      possibiliteExtension: this.editForm.get(['possibiliteExtension'])!.value,
      descriptionExtension: this.editForm.get(['descriptionExtension'])!.value,
      branchementEau: this.editForm.get(['branchementEau'])!.value,
      branchementElectricite: this.editForm.get(['branchementElectricite'])!.value,
      puissanceSouscrite: this.editForm.get(['puissanceSouscrite'])!.value,
      typeReseau: this.editForm.get(['typeReseau'])!.value,
      circuitTerre: this.editForm.get(['circuitTerre'])!.value,
      protectionArret: this.editForm.get(['protectionArret'])!.value,
      protectionFoudre: this.editForm.get(['protectionFoudre'])!.value,
      connexionTel: this.editForm.get(['connexionTel'])!.value,
      connexionInternet: this.editForm.get(['connexionInternet'])!.value,
      environnement: this.editForm.get(['environnement'])!.value,
      dispositif: this.editForm.get(['dispositif'])!.value,
      idPers: this.editForm.get(['idPers'])!.value,
      nomQuartier: this.editForm.get(['nomQuartier'])!.value,
    };
  }
}
