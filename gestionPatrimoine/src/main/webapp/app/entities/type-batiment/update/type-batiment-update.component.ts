import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITypeBatiment, TypeBatiment } from '../type-batiment.model';
import { TypeBatimentService } from '../service/type-batiment.service';
import { IBatiment } from 'app/entities/batiment/batiment.model';
import { BatimentService } from 'app/entities/batiment/service/batiment.service';

@Component({
  selector: 'jhi-type-batiment-update',
  templateUrl: './type-batiment-update.component.html',
})
export class TypeBatimentUpdateComponent implements OnInit {
  isSaving = false;

  batimentsSharedCollection: IBatiment[] = [];

  editForm = this.fb.group({
    id: [],
    typeBa: [null, [Validators.required]],
    nomBatiment: [],
  });

  constructor(
    protected typeBatimentService: TypeBatimentService,
    protected batimentService: BatimentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeBatiment }) => {
      this.updateForm(typeBatiment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeBatiment = this.createFromForm();
    if (typeBatiment.id !== undefined) {
      this.subscribeToSaveResponse(this.typeBatimentService.update(typeBatiment));
    } else {
      this.subscribeToSaveResponse(this.typeBatimentService.create(typeBatiment));
    }
  }

  trackBatimentById(index: number, item: IBatiment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeBatiment>>): void {
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

  protected updateForm(typeBatiment: ITypeBatiment): void {
    this.editForm.patchValue({
      id: typeBatiment.id,
      typeBa: typeBatiment.typeBa,
      nomBatiment: typeBatiment.nomBatiment,
    });

    this.batimentsSharedCollection = this.batimentService.addBatimentToCollectionIfMissing(
      this.batimentsSharedCollection,
      typeBatiment.nomBatiment
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): ITypeBatiment {
    return {
      ...new TypeBatiment(),
      id: this.editForm.get(['id'])!.value,
      typeBa: this.editForm.get(['typeBa'])!.value,
      nomBatiment: this.editForm.get(['nomBatiment'])!.value,
    };
  }
}
