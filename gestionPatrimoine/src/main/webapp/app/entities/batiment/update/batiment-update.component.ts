import { Component, OnInit } from '@angular/core';
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

@Component({
  selector: 'jhi-batiment-update',
  templateUrl: './batiment-update.component.html',
})
export class BatimentUpdateComponent implements OnInit {
  isSaving = false;

  etablissementsSharedCollection: IEtablissement[] = [];
  corpsEtatsSharedCollection: ICorpsEtat[] = [];

  editForm = this.fb.group({
    id: [],
    nomBatiment: [null, [Validators.required]],
    nbrPiece: [null, [Validators.required]],
    designation: [null, [Validators.required]],
    surface: [null, [Validators.required]],
    etatGeneral: [null, [Validators.required]],
    description: [],
    nombreSalle: [null, [Validators.required]],
    nomEtablissement: [],
    nomCorps: [],
  });

  constructor(
    protected batimentService: BatimentService,
    protected etablissementService: EtablissementService,
    protected corpsEtatService: CorpsEtatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
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

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  trackCorpsEtatById(index: number, item: ICorpsEtat): number {
    return item.id!;
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
      nomBatiment: batiment.nomBatiment,
      nbrPiece: batiment.nbrPiece,
      designation: batiment.designation,
      surface: batiment.surface,
      etatGeneral: batiment.etatGeneral,
      description: batiment.description,
      nombreSalle: batiment.nombreSalle,
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
      nomBatiment: this.editForm.get(['nomBatiment'])!.value,
      nbrPiece: this.editForm.get(['nbrPiece'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      surface: this.editForm.get(['surface'])!.value,
      etatGeneral: this.editForm.get(['etatGeneral'])!.value,
      description: this.editForm.get(['description'])!.value,
      nombreSalle: this.editForm.get(['nombreSalle'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
      nomCorps: this.editForm.get(['nomCorps'])!.value,
    };
  }
}
