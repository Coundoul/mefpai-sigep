import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDetenteur, Detenteur } from '../detenteur.model';
import { DetenteurService } from '../service/detenteur.service';

@Component({
  selector: 'jhi-detenteur-update',
  templateUrl: './detenteur-update.component.html',
})
export class DetenteurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomPers: [null, [Validators.required]],
    prenomPers: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    mobile: [null, [Validators.required]],
    adresse: [],
    direction: [null, [Validators.required]],
  });

  constructor(protected detenteurService: DetenteurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detenteur }) => {
      this.updateForm(detenteur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detenteur = this.createFromForm();
    if (detenteur.id !== undefined) {
      this.subscribeToSaveResponse(this.detenteurService.update(detenteur));
    } else {
      this.subscribeToSaveResponse(this.detenteurService.create(detenteur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetenteur>>): void {
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

  protected updateForm(detenteur: IDetenteur): void {
    this.editForm.patchValue({
      id: detenteur.id,
      nomPers: detenteur.nomPers,
      prenomPers: detenteur.prenomPers,
      sexe: detenteur.sexe,
      mobile: detenteur.mobile,
      adresse: detenteur.adresse,
      direction: detenteur.direction,
    });
  }

  protected createFromForm(): IDetenteur {
    return {
      ...new Detenteur(),
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
