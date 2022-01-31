import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IContratProjet, ContratProjet } from '../contrat-projet.model';
import { ContratProjetService } from '../service/contrat-projet.service';

@Component({
  selector: 'jhi-contrat-projet-update',
  templateUrl: './contrat-projet-update.component.html',
})
export class ContratProjetUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
  });

  constructor(protected contratProjetService: ContratProjetService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contratProjet }) => {
      this.updateForm(contratProjet);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contratProjet = this.createFromForm();
    if (contratProjet.id !== undefined) {
      this.subscribeToSaveResponse(this.contratProjetService.update(contratProjet));
    } else {
      this.subscribeToSaveResponse(this.contratProjetService.create(contratProjet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContratProjet>>): void {
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

  protected updateForm(contratProjet: IContratProjet): void {
    this.editForm.patchValue({
      id: contratProjet.id,
      nom: contratProjet.nom,
    });
  }

  protected createFromForm(): IContratProjet {
    return {
      ...new ContratProjet(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }
}
