import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IResponsable, Responsable } from '../responsable.model';
import { ResponsableService } from '../service/responsable.service';

@Component({
  selector: 'jhi-responsable-update',
  templateUrl: './responsable-update.component.html',
})
export class ResponsableUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomResponsable: [null, [Validators.required]],
    prenomResponsable: [null, [Validators.required]],
    email: [null, [Validators.required]],
    specialite: [null, [Validators.required]],
    numb1: [null, [Validators.required]],
    numb2: [],
    raisonSocial: [null, [Validators.required]],
  });

  constructor(protected responsableService: ResponsableService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ responsable }) => {
      this.updateForm(responsable);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const responsable = this.createFromForm();
    if (responsable.id !== undefined) {
      this.subscribeToSaveResponse(this.responsableService.update(responsable));
    } else {
      this.subscribeToSaveResponse(this.responsableService.create(responsable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResponsable>>): void {
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

  protected updateForm(responsable: IResponsable): void {
    this.editForm.patchValue({
      id: responsable.id,
      nomResponsable: responsable.nomResponsable,
      prenomResponsable: responsable.prenomResponsable,
      email: responsable.email,
      specialite: responsable.specialite,
      numb1: responsable.numb1,
      numb2: responsable.numb2,
      raisonSocial: responsable.raisonSocial,
    });
  }

  protected createFromForm(): IResponsable {
    return {
      ...new Responsable(),
      id: this.editForm.get(['id'])!.value,
      nomResponsable: this.editForm.get(['nomResponsable'])!.value,
      prenomResponsable: this.editForm.get(['prenomResponsable'])!.value,
      email: this.editForm.get(['email'])!.value,
      specialite: this.editForm.get(['specialite'])!.value,
      numb1: this.editForm.get(['numb1'])!.value,
      numb2: this.editForm.get(['numb2'])!.value,
      raisonSocial: this.editForm.get(['raisonSocial'])!.value,
    };
  }
}
