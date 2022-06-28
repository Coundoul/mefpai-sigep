import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDetailSortie, DetailSortie } from '../detail-sortie.model';
import { DetailSortieService } from '../service/detail-sortie.service';
import { IBon } from 'app/entities/bon/bon.model';
import { BonService } from 'app/entities/bon/service/bon.service';

@Component({
  selector: 'jhi-detail-sortie-update',
  templateUrl: './detail-sortie-update.component.html',
})
export class DetailSortieUpdateComponent implements OnInit {
  isSaving = false;

  typeBonsCollection: IBon[] = [];

  editForm = this.fb.group({
    id: [],
    pieceJointe: [null, [Validators.required]],
    idPers: [null, [Validators.required]],
    typeBon: [],
  });

  constructor(
    protected detailSortieService: DetailSortieService,
    protected bonService: BonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailSortie }) => {
      this.updateForm(detailSortie);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detailSortie = this.createFromForm();
    if (detailSortie.id !== undefined) {
      this.subscribeToSaveResponse(this.detailSortieService.update(detailSortie));
    } else {
      this.subscribeToSaveResponse(this.detailSortieService.create(detailSortie));
    }
  }

  trackBonById(index: number, item: IBon): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetailSortie>>): void {
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

  protected updateForm(detailSortie: IDetailSortie): void {
    this.editForm.patchValue({
      id: detailSortie.id,
      pieceJointe: detailSortie.pieceJointe,
      idPers: detailSortie.idPers,
      typeBon: detailSortie.typeBon,
    });

    this.typeBonsCollection = this.bonService.addBonToCollectionIfMissing(this.typeBonsCollection, detailSortie.typeBon);
  }

  protected loadRelationshipsOptions(): void {
    this.bonService
      .query({ filter: 'detailsortie-is-null' })
      .pipe(map((res: HttpResponse<IBon[]>) => res.body ?? []))
      .pipe(map((bons: IBon[]) => this.bonService.addBonToCollectionIfMissing(bons, this.editForm.get('typeBon')!.value)))
      .subscribe((bons: IBon[]) => (this.typeBonsCollection = bons));
  }

  protected createFromForm(): IDetailSortie {
    return {
      ...new DetailSortie(),
      id: this.editForm.get(['id'])!.value,
      pieceJointe: this.editForm.get(['pieceJointe'])!.value,
      idPers: this.editForm.get(['idPers'])!.value,
      typeBon: this.editForm.get(['typeBon'])!.value,
    };
  }
}
