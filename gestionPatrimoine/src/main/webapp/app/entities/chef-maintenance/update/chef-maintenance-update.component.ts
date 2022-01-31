import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IChefMaintenance, ChefMaintenance } from '../chef-maintenance.model';
import { ChefMaintenanceService } from '../service/chef-maintenance.service';

@Component({
  selector: 'jhi-chef-maintenance-update',
  templateUrl: './chef-maintenance-update.component.html',
})
export class ChefMaintenanceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomPers: [null, [Validators.required]],
    prenomPers: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    mobile: [null, [Validators.required]],
    adresse: [],
    direction: [],
  });

  constructor(
    protected chefMaintenanceService: ChefMaintenanceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chefMaintenance }) => {
      this.updateForm(chefMaintenance);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chefMaintenance = this.createFromForm();
    if (chefMaintenance.id !== undefined) {
      this.subscribeToSaveResponse(this.chefMaintenanceService.update(chefMaintenance));
    } else {
      this.subscribeToSaveResponse(this.chefMaintenanceService.create(chefMaintenance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChefMaintenance>>): void {
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

  protected updateForm(chefMaintenance: IChefMaintenance): void {
    this.editForm.patchValue({
      id: chefMaintenance.id,
      nomPers: chefMaintenance.nomPers,
      prenomPers: chefMaintenance.prenomPers,
      sexe: chefMaintenance.sexe,
      mobile: chefMaintenance.mobile,
      adresse: chefMaintenance.adresse,
      direction: chefMaintenance.direction,
    });
  }

  protected createFromForm(): IChefMaintenance {
    return {
      ...new ChefMaintenance(),
      id: this.editForm.get(['id'])!.value,
      nomPers: this.editForm.get(['nomPers'])!.value,
      prenomPers: this.editForm.get(['prenomPers'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      direction: this.editForm.get(['direction'])!.value,
    };
  }
}
