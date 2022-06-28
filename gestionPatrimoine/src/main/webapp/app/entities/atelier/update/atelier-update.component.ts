import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAtelier, Atelier } from '../atelier.model';
import { AtelierService } from '../service/atelier.service';
import { IFiliereStabilise } from 'app/entities/filiere-stabilise/filiere-stabilise.model';
import { FiliereStabiliseService } from 'app/entities/filiere-stabilise/service/filiere-stabilise.service';
import { IBatiment } from 'app/entities/batiment/batiment.model';
import { BatimentService } from 'app/entities/batiment/service/batiment.service';

@Component({
  selector: 'jhi-atelier-update',
  templateUrl: './atelier-update.component.html',
})
export class AtelierUpdateComponent implements OnInit {
  isSaving = false;

  filiereStabilisesSharedCollection: IFiliereStabilise[] = [];
  batimentsSharedCollection: IBatiment[] = [];

  editForm = this.fb.group({
    id: [],
    nomAtelier: [null, [Validators.required]],
    surface: [null, [Validators.required]],
    description: [null, [Validators.required]],
    nomFiliere: [],
    nomBatiment: [],
  });

  constructor(
    protected atelierService: AtelierService,
    protected filiereStabiliseService: FiliereStabiliseService,
    protected batimentService: BatimentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ atelier }) => {
      this.updateForm(atelier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const atelier = this.createFromForm();
    if (atelier.id !== undefined) {
      this.subscribeToSaveResponse(this.atelierService.update(atelier));
    } else {
      this.subscribeToSaveResponse(this.atelierService.create(atelier));
    }
  }

  trackFiliereStabiliseById(index: number, item: IFiliereStabilise): number {
    return item.id!;
  }

  trackBatimentById(index: number, item: IBatiment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAtelier>>): void {
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

  protected updateForm(atelier: IAtelier): void {
    this.editForm.patchValue({
      id: atelier.id,
      nomAtelier: atelier.nomAtelier,
      surface: atelier.surface,
      description: atelier.description,
      nomFiliere: atelier.nomFiliere,
      nomBatiment: atelier.nomBatiment,
    });

    this.filiereStabilisesSharedCollection = this.filiereStabiliseService.addFiliereStabiliseToCollectionIfMissing(
      this.filiereStabilisesSharedCollection,
      atelier.nomFiliere
    );
    this.batimentsSharedCollection = this.batimentService.addBatimentToCollectionIfMissing(
      this.batimentsSharedCollection,
      atelier.nomBatiment
    );
  }

  protected loadRelationshipsOptions(): void {
    this.filiereStabiliseService
      .query()
      .pipe(map((res: HttpResponse<IFiliereStabilise[]>) => res.body ?? []))
      .pipe(
        map((filiereStabilises: IFiliereStabilise[]) =>
          this.filiereStabiliseService.addFiliereStabiliseToCollectionIfMissing(filiereStabilises, this.editForm.get('nomFiliere')!.value)
        )
      )
      .subscribe((filiereStabilises: IFiliereStabilise[]) => (this.filiereStabilisesSharedCollection = filiereStabilises));

    this.batimentService
      .query()
      .pipe(map((res: HttpResponse<IBatiment[]>) => res.body ?? []))
      .pipe(
        map((batiments: IBatiment[]) =>
          this.batimentService.addBatimentToCollectionIfMissing(batiments, this.editForm.get('nomBatiment')!.value)
        )
      )
      .subscribe((batiments: IBatiment[]) => (this.batimentsSharedCollection = batiments));
  }

  protected createFromForm(): IAtelier {
    return {
      ...new Atelier(),
      id: this.editForm.get(['id'])!.value,
      nomAtelier: this.editForm.get(['nomAtelier'])!.value,
      surface: this.editForm.get(['surface'])!.value,
      description: this.editForm.get(['description'])!.value,
      nomFiliere: this.editForm.get(['nomFiliere'])!.value,
      nomBatiment: this.editForm.get(['nomBatiment'])!.value,
    };
  }
}
