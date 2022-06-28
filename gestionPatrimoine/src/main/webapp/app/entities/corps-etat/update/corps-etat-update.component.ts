import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICorpsEtat, CorpsEtat } from '../corps-etat.model';
import { CorpsEtatService } from '../service/corps-etat.service';
import { IResponsable } from 'app/entities/responsable/responsable.model';
import { ResponsableService } from 'app/entities/responsable/service/responsable.service';

@Component({
  selector: 'jhi-corps-etat-update',
  templateUrl: './corps-etat-update.component.html',
})
export class CorpsEtatUpdateComponent implements OnInit {
  isSaving = false;

  responsablesSharedCollection: IResponsable[] = [];

  editForm = this.fb.group({
    id: [],
    nomCorps: [null, [Validators.required]],
    grosOeuvre: [null, [Validators.required]],
    descriptionGrosOeuvre: [null, [Validators.required]],
    secondOeuvre: [null, [Validators.required]],
    descriptionSecondOeuvre: [null, [Validators.required]],
    oservation: [null, [Validators.required]],
    etatCorps: [],
    nomResponsable: [],
  });

  constructor(
    protected corpsEtatService: CorpsEtatService,
    protected responsableService: ResponsableService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ corpsEtat }) => {
      this.updateForm(corpsEtat);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const corpsEtat = this.createFromForm();
    if (corpsEtat.id !== undefined) {
      this.subscribeToSaveResponse(this.corpsEtatService.update(corpsEtat));
    } else {
      this.subscribeToSaveResponse(this.corpsEtatService.create(corpsEtat));
    }
  }

  trackResponsableById(index: number, item: IResponsable): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICorpsEtat>>): void {
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

  protected updateForm(corpsEtat: ICorpsEtat): void {
    this.editForm.patchValue({
      id: corpsEtat.id,
      nomCorps: corpsEtat.nomCorps,
      grosOeuvre: corpsEtat.grosOeuvre,
      descriptionGrosOeuvre: corpsEtat.descriptionGrosOeuvre,
      secondOeuvre: corpsEtat.secondOeuvre,
      descriptionSecondOeuvre: corpsEtat.descriptionSecondOeuvre,
      oservation: corpsEtat.oservation,
      etatCorps: corpsEtat.etatCorps,
      nomResponsable: corpsEtat.nomResponsable,
    });

    this.responsablesSharedCollection = this.responsableService.addResponsableToCollectionIfMissing(
      this.responsablesSharedCollection,
      corpsEtat.nomResponsable
    );
  }

  protected loadRelationshipsOptions(): void {
    this.responsableService
      .query()
      .pipe(map((res: HttpResponse<IResponsable[]>) => res.body ?? []))
      .pipe(
        map((responsables: IResponsable[]) =>
          this.responsableService.addResponsableToCollectionIfMissing(responsables, this.editForm.get('nomResponsable')!.value)
        )
      )
      .subscribe((responsables: IResponsable[]) => (this.responsablesSharedCollection = responsables));
  }

  protected createFromForm(): ICorpsEtat {
    return {
      ...new CorpsEtat(),
      id: this.editForm.get(['id'])!.value,
      nomCorps: this.editForm.get(['nomCorps'])!.value,
      grosOeuvre: this.editForm.get(['grosOeuvre'])!.value,
      descriptionGrosOeuvre: this.editForm.get(['descriptionGrosOeuvre'])!.value,
      secondOeuvre: this.editForm.get(['secondOeuvre'])!.value,
      descriptionSecondOeuvre: this.editForm.get(['descriptionSecondOeuvre'])!.value,
      oservation: this.editForm.get(['oservation'])!.value,
      etatCorps: this.editForm.get(['etatCorps'])!.value,
      nomResponsable: this.editForm.get(['nomResponsable'])!.value,
    };
  }
}
