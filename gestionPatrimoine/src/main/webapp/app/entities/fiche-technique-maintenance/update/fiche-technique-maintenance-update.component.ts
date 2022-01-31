import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFicheTechniqueMaintenance, FicheTechniqueMaintenance } from '../fiche-technique-maintenance.model';
import { FicheTechniqueMaintenanceService } from '../service/fiche-technique-maintenance.service';
import { IRequete } from 'app/entities/requete/requete.model';
import { RequeteService } from 'app/entities/requete/service/requete.service';

@Component({
  selector: 'jhi-fiche-technique-maintenance-update',
  templateUrl: './fiche-technique-maintenance-update.component.html',
})
export class FicheTechniqueMaintenanceUpdateComponent implements OnInit {
  isSaving = false;

  requetesSharedCollection: IRequete[] = [];

  editForm = this.fb.group({
    id: [],
    pieceJointe: [null, [Validators.required]],
    idPers: [null, [Validators.required]],
    dateDepot: [],
    type: [],
  });

  constructor(
    protected ficheTechniqueMaintenanceService: FicheTechniqueMaintenanceService,
    protected requeteService: RequeteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ficheTechniqueMaintenance }) => {
      if (ficheTechniqueMaintenance.id === undefined) {
        const today = dayjs().startOf('day');
        ficheTechniqueMaintenance.dateDepot = today;
      }

      this.updateForm(ficheTechniqueMaintenance);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ficheTechniqueMaintenance = this.createFromForm();
    if (ficheTechniqueMaintenance.id !== undefined) {
      this.subscribeToSaveResponse(this.ficheTechniqueMaintenanceService.update(ficheTechniqueMaintenance));
    } else {
      this.subscribeToSaveResponse(this.ficheTechniqueMaintenanceService.create(ficheTechniqueMaintenance));
    }
  }

  trackRequeteById(index: number, item: IRequete): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFicheTechniqueMaintenance>>): void {
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

  protected updateForm(ficheTechniqueMaintenance: IFicheTechniqueMaintenance): void {
    this.editForm.patchValue({
      id: ficheTechniqueMaintenance.id,
      pieceJointe: ficheTechniqueMaintenance.pieceJointe,
      idPers: ficheTechniqueMaintenance.idPers,
      dateDepot: ficheTechniqueMaintenance.dateDepot ? ficheTechniqueMaintenance.dateDepot.format(DATE_TIME_FORMAT) : null,
      type: ficheTechniqueMaintenance.type,
    });

    this.requetesSharedCollection = this.requeteService.addRequeteToCollectionIfMissing(
      this.requetesSharedCollection,
      ficheTechniqueMaintenance.type
    );
  }

  protected loadRelationshipsOptions(): void {
    this.requeteService
      .query()
      .pipe(map((res: HttpResponse<IRequete[]>) => res.body ?? []))
      .pipe(map((requetes: IRequete[]) => this.requeteService.addRequeteToCollectionIfMissing(requetes, this.editForm.get('type')!.value)))
      .subscribe((requetes: IRequete[]) => (this.requetesSharedCollection = requetes));
  }

  protected createFromForm(): IFicheTechniqueMaintenance {
    return {
      ...new FicheTechniqueMaintenance(),
      id: this.editForm.get(['id'])!.value,
      pieceJointe: this.editForm.get(['pieceJointe'])!.value,
      idPers: this.editForm.get(['idPers'])!.value,
      dateDepot: this.editForm.get(['dateDepot'])!.value ? dayjs(this.editForm.get(['dateDepot'])!.value, DATE_TIME_FORMAT) : undefined,
      type: this.editForm.get(['type'])!.value,
    };
  }
}
