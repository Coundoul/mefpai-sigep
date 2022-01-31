import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAffectations, Affectations } from '../affectations.model';
import { AffectationsService } from '../service/affectations.service';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { EquipementService } from 'app/entities/equipement/service/equipement.service';
import { UtilisateurFinalService } from 'app/entities/utilisateur-final/service/utilisateur-final.service';

@Component({
  selector: 'jhi-affectations-update',
  templateUrl: './affectations-update.component.html',
  styleUrls: ['./affectations-update.component.scss'],
})
export class AffectationsUpdateComponent implements OnInit {
  isSaving = false;

  equipementsSharedCollection: IEquipement[] = [];

  editForm = this.fb.group({
    id: [],
    quantiteAffecter: [null, [Validators.required]],
    typeAttribution: [null, [Validators.required]],
    beneficiaire: [],
    idPers: [null, [Validators.required]],
    dateAttribution: [],
    equipement: [],
  });

  constructor(
    protected affectationsService: AffectationsService,
    protected equipementService: EquipementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected utilisateurService: UtilisateurFinalService,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ affectations }) => {
      if (affectations.id === undefined) {
        const today = dayjs().startOf('day');
        affectations.dateAttribution = today;
      }

      this.updateForm(affectations);

      this.loadRelationshipsOptions();
    });
  }



  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const affectations = this.createFromForm();
    if (affectations.id !== undefined) {
      this.subscribeToSaveResponse(this.affectationsService.update(affectations));
    } else {
      this.subscribeToSaveResponse(this.affectationsService.create(affectations));
    }
  }

  trackEquipementById(index: number, item: IEquipement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAffectations>>): void {
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

  protected updateForm(affectations: IAffectations): void {
    this.editForm.patchValue({
      id: affectations.id,
      quantiteAffecter: affectations.quantiteAffecter,
      beneficiaire: affectations.beneficiaire,
      typeAttribution: affectations.typeAttribution,
      idPers: affectations.idPers,
      dateAttribution: affectations.dateAttribution ? affectations.dateAttribution.format(DATE_TIME_FORMAT) : null,
      equipement: affectations.equipement,
    });

    this.equipementsSharedCollection = this.equipementService.addEquipementToCollectionIfMissing(
      this.equipementsSharedCollection,
      affectations.equipement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.equipementService
      .query()
      .pipe(map((res: HttpResponse<IEquipement[]>) => res.body ?? []))
      .pipe(
        map((equipements: IEquipement[]) =>
          this.equipementService.addEquipementToCollectionIfMissing(equipements, this.editForm.get('equipement')!.value)
        )
      )
      .subscribe((equipements: IEquipement[]) => (this.equipementsSharedCollection = equipements));
  }

  protected createFromForm(): IAffectations {
    return {
      ...new Affectations(),
      id: this.editForm.get(['id'])!.value,
      quantiteAffecter: this.editForm.get(['quantiteAffecter'])!.value,
      beneficiaire: this.editForm.get(['beneficiaire'])!.value,
      typeAttribution: this.editForm.get(['typeAttribution'])!.value,
      idPers: this.editForm.get(['idPers'])!.value,
      dateAttribution: this.editForm.get(['dateAttribution'])!.value
        ? dayjs(this.editForm.get(['dateAttribution'])!.value, DATE_TIME_FORMAT)
        : undefined,
      equipement: this.editForm.get(['equipement'])!.value,
    };
  }
}
