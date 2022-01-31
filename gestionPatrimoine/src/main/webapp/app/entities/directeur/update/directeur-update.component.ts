import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDirecteur, Directeur } from '../directeur.model';
import { DirecteurService } from '../service/directeur.service';

@Component({
  selector: 'jhi-directeur-update',
  templateUrl: './directeur-update.component.html',
})
export class DirecteurUpdateComponent implements OnInit {
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

  constructor(protected directeurService: DirecteurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ directeur }) => {
      this.updateForm(directeur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const directeur = this.createFromForm();
    if (directeur.id !== undefined) {
      this.subscribeToSaveResponse(this.directeurService.update(directeur));
    } else {
      this.subscribeToSaveResponse(this.directeurService.create(directeur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDirecteur>>): void {
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

  protected updateForm(directeur: IDirecteur): void {
    this.editForm.patchValue({
      id: directeur.id,
      nomPers: directeur.nomPers,
      prenomPers: directeur.prenomPers,
      sexe: directeur.sexe,
      mobile: directeur.mobile,
      adresse: directeur.adresse,
      direction: directeur.direction,
    });
  }

  protected createFromForm(): IDirecteur {
    return {
      ...new Directeur(),
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
