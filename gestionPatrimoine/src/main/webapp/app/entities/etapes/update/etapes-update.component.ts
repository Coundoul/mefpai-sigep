import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEtapes, Etapes } from '../etapes.model';
import { EtapesService } from '../service/etapes.service';
import { IProjets } from 'app/entities/projets/projets.model';
import { ProjetsService } from 'app/entities/projets/service/projets.service';

@Component({
  selector: 'jhi-etapes-update',
  templateUrl: './etapes-update.component.html',
})
export class EtapesUpdateComponent implements OnInit {
  isSaving = false;

  projetsSharedCollection: IProjets[] = [];

  editForm = this.fb.group({
    id: [],
    dateDebut: [null, [Validators.required]],
    dateFin: [null, [Validators.required]],
    nomTache: [null, [Validators.required]],
    duration: [],
    nomProjet: [],
  });

  constructor(
    protected etapesService: EtapesService,
    protected projetsService: ProjetsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etapes }) => {
      if (etapes.id === undefined) {
        const today = dayjs().startOf('day');
        etapes.dateDebut = today;
        etapes.dateFin = today;
      }

      this.updateForm(etapes);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etapes = this.createFromForm();
    if (etapes.id !== undefined) {
      this.subscribeToSaveResponse(this.etapesService.update(etapes));
    } else {
      this.subscribeToSaveResponse(this.etapesService.create(etapes));
    }
  }

  trackProjetsById(index: number, item: IProjets): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtapes>>): void {
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

  protected updateForm(etapes: IEtapes): void {
    this.editForm.patchValue({
      id: etapes.id,
      dateDebut: etapes.dateDebut ? etapes.dateDebut.format(DATE_TIME_FORMAT) : null,
      dateFin: etapes.dateFin ? etapes.dateFin.format(DATE_TIME_FORMAT) : null,
      nomTache: etapes.nomTache,
      duration: etapes.duration,
      nomProjet: etapes.nomProjet,
    });

    this.projetsSharedCollection = this.projetsService.addProjetsToCollectionIfMissing(this.projetsSharedCollection, etapes.nomProjet);
  }

  protected loadRelationshipsOptions(): void {
    this.projetsService
      .query()
      .pipe(map((res: HttpResponse<IProjets[]>) => res.body ?? []))
      .pipe(
        map((projets: IProjets[]) => this.projetsService.addProjetsToCollectionIfMissing(projets, this.editForm.get('nomProjet')!.value))
      )
      .subscribe((projets: IProjets[]) => (this.projetsSharedCollection = projets));
  }

  protected createFromForm(): IEtapes {
    return {
      ...new Etapes(),
      id: this.editForm.get(['id'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value ? dayjs(this.editForm.get(['dateDebut'])!.value, DATE_TIME_FORMAT) : undefined,
      dateFin: this.editForm.get(['dateFin'])!.value ? dayjs(this.editForm.get(['dateFin'])!.value, DATE_TIME_FORMAT) : undefined,
      nomTache: this.editForm.get(['nomTache'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      nomProjet: this.editForm.get(['nomProjet'])!.value,
    };
  }
}
