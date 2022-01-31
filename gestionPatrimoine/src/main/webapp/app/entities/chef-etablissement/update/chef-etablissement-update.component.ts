import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IChefEtablissement, ChefEtablissement } from '../chef-etablissement.model';
import { ChefEtablissementService } from '../service/chef-etablissement.service';

@Component({
  selector: 'jhi-chef-etablissement-update',
  templateUrl: './chef-etablissement-update.component.html',
})
export class ChefEtablissementUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomPers: [null, [Validators.required]],
    prenomPers: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    mobile: [null, [Validators.required]],
    adresse: [],
  });

  constructor(
    protected chefEtablissementService: ChefEtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chefEtablissement }) => {
      this.updateForm(chefEtablissement);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chefEtablissement = this.createFromForm();
    if (chefEtablissement.id !== undefined) {
      this.subscribeToSaveResponse(this.chefEtablissementService.update(chefEtablissement));
    } else {
      this.subscribeToSaveResponse(this.chefEtablissementService.create(chefEtablissement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChefEtablissement>>): void {
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

  protected updateForm(chefEtablissement: IChefEtablissement): void {
    this.editForm.patchValue({
      id: chefEtablissement.id,
      nomPers: chefEtablissement.nomPers,
      prenomPers: chefEtablissement.prenomPers,
      sexe: chefEtablissement.sexe,
      mobile: chefEtablissement.mobile,
      adresse: chefEtablissement.adresse,
    });
  }

  protected createFromForm(): IChefEtablissement {
    return {
      ...new ChefEtablissement(),
      id: this.editForm.get(['id'])!.value,
      nomPers: this.editForm.get(['nomPers'])!.value,
      prenomPers: this.editForm.get(['prenomPers'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
    };
  }
}
