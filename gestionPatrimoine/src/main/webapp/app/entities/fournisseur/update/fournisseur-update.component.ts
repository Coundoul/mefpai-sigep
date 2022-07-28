import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFournisseur, Fournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-fournisseur-update',
  templateUrl: './fournisseur-update.component.html',
})
export class FournisseurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    codeFournisseuer: [null, [Validators.required]],
    nomFournisseur: [null, [Validators.required]],
    prenomFournisseur: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    raisonSocial: [null, [Validators.required]],
    adresse: [null, [Validators.required]],
    num1: [null, [Validators.required]],
    num2: [],
    ville: [null, [Validators.required]],
    email: [null, [Validators.required]],
  });

  constructor(
    protected fournisseurService: FournisseurService,
    private router: Router,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    public activeModal: NgbActiveModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fournisseur }) => {
      this.updateForm(fournisseur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fournisseur = this.createFromForm();
    if (fournisseur.id !== undefined) {
      this.subscribeToSaveResponse(this.fournisseurService.update(fournisseur));
      this.activeModal.dismiss();
      location.reload();
    } else {
      this.subscribeToSaveResponse(this.fournisseurService.create(fournisseur));
      this.activeModal.dismiss();
      location.reload();
    }
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFournisseur>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.router.navigate(['/equipement/new']);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fournisseur: IFournisseur): void {
    this.editForm.patchValue({
      id: fournisseur.id,
      codeFournisseuer: fournisseur.codeFournisseuer,
      nomFournisseur: fournisseur.nomFournisseur,
      prenomFournisseur: fournisseur.prenomFournisseur,
      sexe: fournisseur.sexe,
      raisonSocial: fournisseur.raisonSocial,
      adresse: fournisseur.adresse,
      num1: fournisseur.num1,
      num2: fournisseur.num2,
      ville: fournisseur.ville,
      email: fournisseur.email,
    });
  }

  protected createFromForm(): IFournisseur {
    return {
      ...new Fournisseur(),
      // id: this.editForm.get(['id'])!.value,
      codeFournisseuer: this.editForm.get(['codeFournisseuer'])!.value,
      nomFournisseur: this.editForm.get(['nomFournisseur'])!.value,
      prenomFournisseur: this.editForm.get(['prenomFournisseur'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      raisonSocial: this.editForm.get(['raisonSocial'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      num1: this.editForm.get(['num1'])!.value,
      num2: this.editForm.get(['num2'])!.value,
      ville: this.editForm.get(['ville'])!.value,
      email: this.editForm.get(['email'])!.value,
    };
  }
}
