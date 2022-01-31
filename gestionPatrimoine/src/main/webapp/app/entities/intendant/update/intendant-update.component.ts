import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IIntendant, Intendant } from '../intendant.model';
import { IntendantService } from '../service/intendant.service';

@Component({
  selector: 'jhi-intendant-update',
  templateUrl: './intendant-update.component.html',
})
export class IntendantUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomPers: [null, [Validators.required]],
    prenomPers: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    mobile: [null, [Validators.required]],
    adresse: [],
  });

  constructor(protected intendantService: IntendantService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intendant }) => {
      this.updateForm(intendant);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const intendant = this.createFromForm();
    if (intendant.id !== undefined) {
      this.subscribeToSaveResponse(this.intendantService.update(intendant));
    } else {
      this.subscribeToSaveResponse(this.intendantService.create(intendant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIntendant>>): void {
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

  protected updateForm(intendant: IIntendant): void {
    this.editForm.patchValue({
      id: intendant.id,
      nomPers: intendant.nomPers,
      prenomPers: intendant.prenomPers,
      sexe: intendant.sexe,
      mobile: intendant.mobile,
      adresse: intendant.adresse,
    });
  }

  protected createFromForm(): IIntendant {
    return {
      ...new Intendant(),
      id: this.editForm.get(['id'])!.value,
      nomPers: this.editForm.get(['nomPers'])!.value,
      prenomPers: this.editForm.get(['prenomPers'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
    };
  }
}
