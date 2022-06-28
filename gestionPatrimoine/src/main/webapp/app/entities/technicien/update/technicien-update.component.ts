import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITechnicien, Technicien } from '../technicien.model';
import { TechnicienService } from '../service/technicien.service';
import { IChefMaintenance } from 'app/entities/chef-maintenance/chef-maintenance.model';
import { ChefMaintenanceService } from 'app/entities/chef-maintenance/service/chef-maintenance.service';

@Component({
  selector: 'jhi-technicien-update',
  templateUrl: './technicien-update.component.html',
})
export class TechnicienUpdateComponent implements OnInit {
  isSaving = false;

  chefMaintenancesSharedCollection: IChefMaintenance[] = [];

  editForm = this.fb.group({
    id: [],
    nomPers: [null, [Validators.required]],
    prenomPers: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    mobile: [null, [Validators.required]],
    adresse: [],
    direction: [],
    chefMaintenance: [],
  });

  constructor(
    protected technicienService: TechnicienService,
    protected chefMaintenanceService: ChefMaintenanceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ technicien }) => {
      this.updateForm(technicien);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const technicien = this.createFromForm();
    if (technicien.id !== undefined) {
      this.subscribeToSaveResponse(this.technicienService.update(technicien));
    } else {
      this.subscribeToSaveResponse(this.technicienService.create(technicien));
    }
  }

  trackChefMaintenanceById(index: number, item: IChefMaintenance): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechnicien>>): void {
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

  protected updateForm(technicien: ITechnicien): void {
    this.editForm.patchValue({
      id: technicien.id,
      nomPers: technicien.nomPers,
      prenomPers: technicien.prenomPers,
      sexe: technicien.sexe,
      mobile: technicien.mobile,
      adresse: technicien.adresse,
      direction: technicien.direction,
      chefMaintenance: technicien.chefMaintenance,
    });

    this.chefMaintenancesSharedCollection = this.chefMaintenanceService.addChefMaintenanceToCollectionIfMissing(
      this.chefMaintenancesSharedCollection,
      technicien.chefMaintenance
    );
  }

  protected loadRelationshipsOptions(): void {
    this.chefMaintenanceService
      .query()
      .pipe(map((res: HttpResponse<IChefMaintenance[]>) => res.body ?? []))
      .pipe(
        map((chefMaintenances: IChefMaintenance[]) =>
          this.chefMaintenanceService.addChefMaintenanceToCollectionIfMissing(chefMaintenances, this.editForm.get('chefMaintenance')!.value)
        )
      )
      .subscribe((chefMaintenances: IChefMaintenance[]) => (this.chefMaintenancesSharedCollection = chefMaintenances));
  }

  protected createFromForm(): ITechnicien {
    return {
      ...new Technicien(),
      id: this.editForm.get(['id'])!.value,
      nomPers: this.editForm.get(['nomPers'])!.value,
      prenomPers: this.editForm.get(['prenomPers'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      direction: this.editForm.get(['direction'])!.value,
      chefMaintenance: this.editForm.get(['chefMaintenance'])!.value,
    };
  }
}
