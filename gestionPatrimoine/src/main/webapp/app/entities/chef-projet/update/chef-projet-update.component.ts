import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IChefProjet, ChefProjet } from '../chef-projet.model';
import { ChefProjetService } from '../service/chef-projet.service';

@Component({
  selector: 'jhi-chef-projet-update',
  templateUrl: './chef-projet-update.component.html',
})
export class ChefProjetUpdateComponent implements OnInit {
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

  constructor(protected chefProjetService: ChefProjetService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chefProjet }) => {
      this.updateForm(chefProjet);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chefProjet = this.createFromForm();
    if (chefProjet.id !== undefined) {
      this.subscribeToSaveResponse(this.chefProjetService.update(chefProjet));
    } else {
      this.subscribeToSaveResponse(this.chefProjetService.create(chefProjet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChefProjet>>): void {
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

  protected updateForm(chefProjet: IChefProjet): void {
    this.editForm.patchValue({
      id: chefProjet.id,
      nomPers: chefProjet.nomPers,
      prenomPers: chefProjet.prenomPers,
      sexe: chefProjet.sexe,
      mobile: chefProjet.mobile,
      adresse: chefProjet.adresse,
      direction: chefProjet.direction,
    });
  }

  protected createFromForm(): IChefProjet {
    return {
      ...new ChefProjet(),
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
