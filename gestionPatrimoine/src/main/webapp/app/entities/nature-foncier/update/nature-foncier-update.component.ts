import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INatureFoncier, NatureFoncier } from '../nature-foncier.model';
import { NatureFoncierService } from '../service/nature-foncier.service';
import { ICorpsEtat } from 'app/entities/corps-etat/corps-etat.model';
import { CorpsEtatService } from 'app/entities/corps-etat/service/corps-etat.service';

@Component({
  selector: 'jhi-nature-foncier-update',
  templateUrl: './nature-foncier-update.component.html',
})
export class NatureFoncierUpdateComponent implements OnInit {
  isSaving = false;

  corpsEtatsSharedCollection: ICorpsEtat[] = [];

  editForm = this.fb.group({
    id: [],
    typeFoncier: [null, [Validators.required]],
    pieceJointe: [null, [Validators.required]],
    nomCorps: [],
  });

  constructor(
    protected natureFoncierService: NatureFoncierService,
    protected corpsEtatService: CorpsEtatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ natureFoncier }) => {
      this.updateForm(natureFoncier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const natureFoncier = this.createFromForm();
    if (natureFoncier.id !== undefined) {
      this.subscribeToSaveResponse(this.natureFoncierService.update(natureFoncier));
    } else {
      this.subscribeToSaveResponse(this.natureFoncierService.create(natureFoncier));
    }
  }

  trackCorpsEtatById(index: number, item: ICorpsEtat): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INatureFoncier>>): void {
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

  protected updateForm(natureFoncier: INatureFoncier): void {
    this.editForm.patchValue({
      id: natureFoncier.id,
      typeFoncier: natureFoncier.typeFoncier,
      pieceJointe: natureFoncier.pieceJointe,
      nomCorps: natureFoncier.nomCorps,
    });

    this.corpsEtatsSharedCollection = this.corpsEtatService.addCorpsEtatToCollectionIfMissing(
      this.corpsEtatsSharedCollection,
      natureFoncier.nomCorps
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): INatureFoncier {
    return {
      ...new NatureFoncier(),
      id: this.editForm.get(['id'])!.value,
      typeFoncier: this.editForm.get(['typeFoncier'])!.value,
      pieceJointe: this.editForm.get(['pieceJointe'])!.value,
      nomCorps: this.editForm.get(['nomCorps'])!.value,
    };
  }
}
