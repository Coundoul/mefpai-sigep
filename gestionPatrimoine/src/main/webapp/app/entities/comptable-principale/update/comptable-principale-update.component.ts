import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IComptablePrincipale, ComptablePrincipale } from '../comptable-principale.model';
import { ComptablePrincipaleService } from '../service/comptable-principale.service';

@Component({
  selector: 'jhi-comptable-principale-update',
  templateUrl: './comptable-principale-update.component.html',
})
export class ComptablePrincipaleUpdateComponent implements OnInit {
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

  constructor(
    protected comptablePrincipaleService: ComptablePrincipaleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comptablePrincipale }) => {
      this.updateForm(comptablePrincipale);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comptablePrincipale = this.createFromForm();
    if (comptablePrincipale.id !== undefined) {
      this.subscribeToSaveResponse(this.comptablePrincipaleService.update(comptablePrincipale));
    } else {
      this.subscribeToSaveResponse(this.comptablePrincipaleService.create(comptablePrincipale));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComptablePrincipale>>): void {
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

  protected updateForm(comptablePrincipale: IComptablePrincipale): void {
    this.editForm.patchValue({
      id: comptablePrincipale.id,
      nomPers: comptablePrincipale.nomPers,
      prenomPers: comptablePrincipale.prenomPers,
      sexe: comptablePrincipale.sexe,
      mobile: comptablePrincipale.mobile,
      adresse: comptablePrincipale.adresse,
      direction: comptablePrincipale.direction,
    });
  }

  protected createFromForm(): IComptablePrincipale {
    return {
      ...new ComptablePrincipale(),
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
