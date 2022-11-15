import { Component, ElementRef, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBatiment, Batiment } from '../batiment.model';
import { BatimentService } from '../service/batiment.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { ICorpsEtat } from 'app/entities/corps-etat/corps-etat.model';
import { CorpsEtatService } from 'app/entities/corps-etat/service/corps-etat.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

@Component({
  selector: 'jhi-signaler',
  templateUrl: './signaler.component.html',
  styleUrls: ['./signaler.component.scss']
})
export class SignalerComponent implements OnInit {
  isSaving = false;

  etablissementsSharedCollection: IEtablissement[] = [];
  corpsEtatsSharedCollection: ICorpsEtat[] = [];

  editForm = this.fb.group({
    id: [],
    designation: [null, [Validators.required]],
    nbrPiece: [],
    surface: [],
    sourceFinancement: [],
    dateSignalisation: [null, [Validators.required]],
    photoSignalisationGrosOeuvre: [null, [Validators.required]],
    photoSignalisationGrosOeuvreContentType: [null, [Validators.required]],
    photoSignalisationSecondOeuvre: [null, [Validators.required]],
    photoSignalisationSecondOeuvreContentType: [null, [Validators.required]],
    etatGrosOeuvre: [null, [Validators.required]],
    etatSecondOeuvre: [null, [Validators.required]],
    observation: [],
    descriptionSignalisation: [null, [Validators.required]],
    nomEtablissement: [],
    nomCorps: [],
  });

  constructor(
    protected batimentService: BatimentService,
    protected eventManager: EventManager,
    protected etablissementService: EtablissementService,
    protected corpsEtatService: CorpsEtatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected elementRef: ElementRef,
    protected dataUtils: DataUtils,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ batiment }) => {
      this.updateForm(batiment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const batiment = this.createFromForm();
    if (batiment.id !== undefined) {
      this.subscribeToSaveResponse(this.batimentService.update(batiment));
    } else {
      this.subscribeToSaveResponse(this.batimentService.create(batiment));
    }
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

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  trackCorpsEtatById(index: number, item: ICorpsEtat): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBatiment>>): void {
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

  protected updateForm(batiment: IBatiment): void {
    this.editForm.patchValue({
      id: batiment.id,
      designation: batiment.designation,
      nbrPiece: batiment.nbrPiece,
      surface: batiment.surface,
      sourceFinancement: batiment.sourceFinancement,
      dateSignalisation: batiment.dateSignalisation,
      photoSignalisationGrosOeuvre: batiment.photoSignalisationGrosOeuvre,
      photoSignalisationGrosOeuvreContentType: batiment.photoSignalisationGrosOeuvre,
      photoSignalisationSecondOeuvre: batiment.photoSignalisationSecondOeuvre,
      photoSignalisationSecondOeuvreContentType: batiment.photoSignalisationSecondOeuvre,
      etatGrosOeuvre: batiment.etatGrosOeuvre,
      etatSecondOeuvre: batiment.etatSecondOeuvre,
      observation: batiment.observation,
      descriptionSignalisation: batiment.descriptionSignalisation,
      nomEtablissement: batiment.nomEtablissement,
      nomCorps: batiment.nomCorps,
    });

    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      batiment.nomEtablissement
    );
    this.corpsEtatsSharedCollection = this.corpsEtatService.addCorpsEtatToCollectionIfMissing(
      this.corpsEtatsSharedCollection,
      batiment.nomCorps
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etablissementService
      .query()
      .pipe(map((res: HttpResponse<IEtablissement[]>) => res.body ?? []))
      .pipe(
        map((etablissements: IEtablissement[]) =>
          this.etablissementService.addEtablissementToCollectionIfMissing(etablissements, this.editForm.get('nomEtablissement')!.value)
        )
      )
      .subscribe((etablissements: IEtablissement[]) => (this.etablissementsSharedCollection = etablissements));

    this.corpsEtatService
      .query()
      .pipe(map((res: HttpResponse<ICorpsEtat[]>) => res.body ?? []))
      .pipe(
        map((corpsEtats: ICorpsEtat[]) =>
          this.corpsEtatService.addCorpsEtatToCollectionIfMissing(corpsEtats, this.editForm.get('nomCorps')!.value)
        )
      )
      .subscribe((corpsEtats: ICorpsEtat[]) => (this.corpsEtatsSharedCollection = corpsEtats));
  }

  protected createFromForm(): IBatiment {
    return {
      ...new Batiment(),
      id: this.editForm.get(['id'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      nbrPiece: this.editForm.get(['nbrPiece'])!.value,
      surface: this.editForm.get(['surface'])!.value,
      sourceFinancement: this.editForm.get(['sourceFinancement'])!.value,
      dateSignalisation: this.editForm.get(['dateSignalisation'])!.value
        ? dayjs(this.editForm.get(['dateSignalisation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      photoSignalisationGrosOeuvreContentType: this.editForm.get(['photoSignalisationGrosOeuvreContentType'])!.value,
      photoSignalisationGrosOeuvre: this.editForm.get(['photoSignalisationGrosOeuvre'])!.value,
      photoSignalisationSecondOeuvreContentType: this.editForm.get(['photoSignalisationSecondOeuvreContentType'])!.value,
      photoSignalisationSecondOeuvre: this.editForm.get(['photoSignalisationSecondOeuvre'])!.value,
      etatGrosOeuvre: this.editForm.get(['etatGrosOeuvre'])!.value,
      etatSecondOeuvre: this.editForm.get(['etatSecondOeuvre'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      descriptionSignalisation: this.editForm.get(['descriptionSignalisation'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
      nomCorps: this.editForm.get(['nomCorps'])!.value,
    };
  }
}
