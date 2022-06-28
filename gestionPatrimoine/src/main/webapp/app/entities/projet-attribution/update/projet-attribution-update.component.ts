import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProjetAttribution, ProjetAttribution } from '../projet-attribution.model';
import { ProjetAttributionService } from '../service/projet-attribution.service';
import { IProjets } from 'app/entities/projets/projets.model';
import { ProjetsService } from 'app/entities/projets/service/projets.service';

@Component({
  selector: 'jhi-projet-attribution-update',
  templateUrl: './projet-attribution-update.component.html',
})
export class ProjetAttributionUpdateComponent implements OnInit {
  isSaving = false;

  projetsSharedCollection: IProjets[] = [];

  editForm = this.fb.group({
    id: [],
    dateAttribution: [],
    quantite: [null, [Validators.required]],
    idEquipement: [null, [Validators.required]],
    idPers: [null, [Validators.required]],
    nomProjet: [],
  });

  constructor(
    protected projetAttributionService: ProjetAttributionService,
    protected projetsService: ProjetsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projetAttribution }) => {
      if (projetAttribution.id === undefined) {
        const today = dayjs().startOf('day');
        projetAttribution.dateAttribution = today;
      }

      this.updateForm(projetAttribution);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const projetAttribution = this.createFromForm();
    if (projetAttribution.id !== undefined) {
      this.subscribeToSaveResponse(this.projetAttributionService.update(projetAttribution));
    } else {
      this.subscribeToSaveResponse(this.projetAttributionService.create(projetAttribution));
    }
  }

  trackProjetsById(index: number, item: IProjets): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjetAttribution>>): void {
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

  protected updateForm(projetAttribution: IProjetAttribution): void {
    this.editForm.patchValue({
      id: projetAttribution.id,
      dateAttribution: projetAttribution.dateAttribution ? projetAttribution.dateAttribution.format(DATE_TIME_FORMAT) : null,
      quantite: projetAttribution.quantite,
      idEquipement: projetAttribution.idEquipement,
      idPers: projetAttribution.idPers,
      nomProjet: projetAttribution.nomProjet,
    });

    this.projetsSharedCollection = this.projetsService.addProjetsToCollectionIfMissing(
      this.projetsSharedCollection,
      projetAttribution.nomProjet
    );
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

  protected createFromForm(): IProjetAttribution {
    return {
      ...new ProjetAttribution(),
      id: this.editForm.get(['id'])!.value,
      dateAttribution: this.editForm.get(['dateAttribution'])!.value
        ? dayjs(this.editForm.get(['dateAttribution'])!.value, DATE_TIME_FORMAT)
        : undefined,
      quantite: this.editForm.get(['quantite'])!.value,
      idEquipement: this.editForm.get(['idEquipement'])!.value,
      idPers: this.editForm.get(['idPers'])!.value,
      nomProjet: this.editForm.get(['nomProjet'])!.value,
    };
  }
}
