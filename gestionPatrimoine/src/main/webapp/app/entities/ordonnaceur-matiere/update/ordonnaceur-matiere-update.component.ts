import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOrdonnaceurMatiere, OrdonnaceurMatiere } from '../ordonnaceur-matiere.model';
import { OrdonnaceurMatiereService } from '../service/ordonnaceur-matiere.service';

@Component({
  selector: 'jhi-ordonnaceur-matiere-update',
  templateUrl: './ordonnaceur-matiere-update.component.html',
})
export class OrdonnaceurMatiereUpdateComponent implements OnInit {
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
    protected ordonnaceurMatiereService: OrdonnaceurMatiereService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordonnaceurMatiere }) => {
      this.updateForm(ordonnaceurMatiere);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ordonnaceurMatiere = this.createFromForm();
    if (ordonnaceurMatiere.id !== undefined) {
      this.subscribeToSaveResponse(this.ordonnaceurMatiereService.update(ordonnaceurMatiere));
    } else {
      this.subscribeToSaveResponse(this.ordonnaceurMatiereService.create(ordonnaceurMatiere));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrdonnaceurMatiere>>): void {
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

  protected updateForm(ordonnaceurMatiere: IOrdonnaceurMatiere): void {
    this.editForm.patchValue({
      id: ordonnaceurMatiere.id,
      nomPers: ordonnaceurMatiere.nomPers,
      prenomPers: ordonnaceurMatiere.prenomPers,
      sexe: ordonnaceurMatiere.sexe,
      mobile: ordonnaceurMatiere.mobile,
      adresse: ordonnaceurMatiere.adresse,
      direction: ordonnaceurMatiere.direction,
    });
  }

  protected createFromForm(): IOrdonnaceurMatiere {
    return {
      ...new OrdonnaceurMatiere(),
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
