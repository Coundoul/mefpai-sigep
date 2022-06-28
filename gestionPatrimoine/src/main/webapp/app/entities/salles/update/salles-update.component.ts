import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISalles, Salles } from '../salles.model';
import { SallesService } from '../service/salles.service';
import { IBatiment } from 'app/entities/batiment/batiment.model';
import { BatimentService } from 'app/entities/batiment/service/batiment.service';

@Component({
  selector: 'jhi-salles-update',
  templateUrl: './salles-update.component.html',
})
export class SallesUpdateComponent implements OnInit {
  isSaving = false;

  batimentsSharedCollection: IBatiment[] = [];

  editForm = this.fb.group({
    id: [],
    nomSalle: [null, [Validators.required]],
    classe: [null, [Validators.required]],
    nomBatiment: [],
  });

  constructor(
    protected sallesService: SallesService,
    protected batimentService: BatimentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salles }) => {
      this.updateForm(salles);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salles = this.createFromForm();
    if (salles.id !== undefined) {
      this.subscribeToSaveResponse(this.sallesService.update(salles));
    } else {
      this.subscribeToSaveResponse(this.sallesService.create(salles));
    }
  }

  trackBatimentById(index: number, item: IBatiment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalles>>): void {
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

  protected updateForm(salles: ISalles): void {
    this.editForm.patchValue({
      id: salles.id,
      nomSalle: salles.nomSalle,
      classe: salles.classe,
      nomBatiment: salles.nomBatiment,
    });

    this.batimentsSharedCollection = this.batimentService.addBatimentToCollectionIfMissing(
      this.batimentsSharedCollection,
      salles.nomBatiment
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

  protected createFromForm(): ISalles {
    return {
      ...new Salles(),
      id: this.editForm.get(['id'])!.value,
      nomSalle: this.editForm.get(['nomSalle'])!.value,
      classe: this.editForm.get(['classe'])!.value,
      nomBatiment: this.editForm.get(['nomBatiment'])!.value,
    };
  }
}
