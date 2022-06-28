import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAttribution, Attribution } from '../attribution.model';
import { AttributionService } from '../service/attribution.service';
import { IUtilisateurFinal } from 'app/entities/utilisateur-final/utilisateur-final.model';
import { UtilisateurFinalService } from 'app/entities/utilisateur-final/service/utilisateur-final.service';
import { IAffectations } from 'app/entities/affectations/affectations.model';
import { AffectationsService } from 'app/entities/affectations/service/affectations.service';

@Component({
  selector: 'jhi-attribution-update',
  templateUrl: './attribution-update.component.html',
})
export class AttributionUpdateComponent implements OnInit {
  isSaving = false;

  utilisateurFinalsSharedCollection: IUtilisateurFinal[] = [];
  affectationsSharedCollection: IAffectations[] = [];

  editForm = this.fb.group({
    id: [],
    quantiteAffecter: [null, [Validators.required]],
    idPers: [null, [Validators.required]],
    dateAffectation: [],
    nomUtilisateur: [],
    affectations: [],
  });

  constructor(
    protected attributionService: AttributionService,
    protected utilisateurFinalService: UtilisateurFinalService,
    protected affectationsService: AffectationsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attribution }) => {
      if (attribution.id === undefined) {
        const today = dayjs().startOf('day');
        attribution.dateAffectation = today;
      }

      this.updateForm(attribution);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attribution = this.createFromForm();
    if (attribution.id !== undefined) {
      this.subscribeToSaveResponse(this.attributionService.update(attribution));
    } else {
      this.subscribeToSaveResponse(this.attributionService.create(attribution));
    }
  }

  trackUtilisateurFinalById(index: number, item: IUtilisateurFinal): number {
    return item.id!;
  }

  trackAffectationsById(index: number, item: IAffectations): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttribution>>): void {
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

  protected updateForm(attribution: IAttribution): void {
    this.editForm.patchValue({
      id: attribution.id,
      quantiteAffecter: attribution.quantiteAffecter,
      idPers: attribution.idPers,
      dateAffectation: attribution.dateAffectation ? attribution.dateAffectation.format(DATE_TIME_FORMAT) : null,
      nomUtilisateur: attribution.nomUtilisateur,
      affectations: attribution.affectations,
    });

    this.utilisateurFinalsSharedCollection = this.utilisateurFinalService.addUtilisateurFinalToCollectionIfMissing(
      this.utilisateurFinalsSharedCollection,
      attribution.nomUtilisateur
    );
    this.affectationsSharedCollection = this.affectationsService.addAffectationsToCollectionIfMissing(
      this.affectationsSharedCollection,
      attribution.affectations
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurFinalService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateurFinal[]>) => res.body ?? []))
      .pipe(
        map((utilisateurFinals: IUtilisateurFinal[]) =>
          this.utilisateurFinalService.addUtilisateurFinalToCollectionIfMissing(
            utilisateurFinals,
            this.editForm.get('nomUtilisateur')!.value
          )
        )
      )
      .subscribe((utilisateurFinals: IUtilisateurFinal[]) => (this.utilisateurFinalsSharedCollection = utilisateurFinals));

    this.affectationsService
      .query()
      .pipe(map((res: HttpResponse<IAffectations[]>) => res.body ?? []))
      .pipe(
        map((affectations: IAffectations[]) =>
          this.affectationsService.addAffectationsToCollectionIfMissing(affectations, this.editForm.get('affectations')!.value)
        )
      )
      .subscribe((affectations: IAffectations[]) => (this.affectationsSharedCollection = affectations));
  }

  protected createFromForm(): IAttribution {
    return {
      ...new Attribution(),
      id: this.editForm.get(['id'])!.value,
      quantiteAffecter: this.editForm.get(['quantiteAffecter'])!.value,
      idPers: this.editForm.get(['idPers'])!.value,
      dateAffectation: this.editForm.get(['dateAffectation'])!.value
        ? dayjs(this.editForm.get(['dateAffectation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      nomUtilisateur: this.editForm.get(['nomUtilisateur'])!.value,
      affectations: this.editForm.get(['affectations'])!.value,
    };
  }
}
