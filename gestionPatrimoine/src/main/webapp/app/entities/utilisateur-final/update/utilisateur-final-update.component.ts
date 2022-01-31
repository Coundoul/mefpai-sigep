import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUtilisateurFinal, UtilisateurFinal } from '../utilisateur-final.model';
import { UtilisateurFinalService } from '../service/utilisateur-final.service';

@Component({
  selector: 'jhi-utilisateur-final-update',
  templateUrl: './utilisateur-final-update.component.html',
})
export class UtilisateurFinalUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomUtilisateur: [null, [Validators.required]],
    prenomUtilisateur: [null, [Validators.required]],
    emailInstitutionnel: [null, [Validators.required]],
    mobile: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    departement: [],
    serviceDep: [],
  });

  constructor(
    protected utilisateurFinalService: UtilisateurFinalService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateurFinal }) => {
      this.updateForm(utilisateurFinal);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utilisateurFinal = this.createFromForm();
    if (utilisateurFinal.id !== undefined) {
      this.subscribeToSaveResponse(this.utilisateurFinalService.update(utilisateurFinal));
    } else {
      this.subscribeToSaveResponse(this.utilisateurFinalService.create(utilisateurFinal));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilisateurFinal>>): void {
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

  protected updateForm(utilisateurFinal: IUtilisateurFinal): void {
    this.editForm.patchValue({
      id: utilisateurFinal.id,
      nomUtilisateur: utilisateurFinal.nomUtilisateur,
      prenomUtilisateur: utilisateurFinal.prenomUtilisateur,
      emailInstitutionnel: utilisateurFinal.emailInstitutionnel,
      mobile: utilisateurFinal.mobile,
      sexe: utilisateurFinal.sexe,
      departement: utilisateurFinal.departement,
      serviceDep: utilisateurFinal.serviceDep,
    });
  }

  protected createFromForm(): IUtilisateurFinal {
    return {
      ...new UtilisateurFinal(),
      id: this.editForm.get(['id'])!.value,
      nomUtilisateur: this.editForm.get(['nomUtilisateur'])!.value,
      prenomUtilisateur: this.editForm.get(['prenomUtilisateur'])!.value,
      emailInstitutionnel: this.editForm.get(['emailInstitutionnel'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      departement: this.editForm.get(['departement'])!.value,
      serviceDep: this.editForm.get(['serviceDep'])!.value,
    };
  }
}
