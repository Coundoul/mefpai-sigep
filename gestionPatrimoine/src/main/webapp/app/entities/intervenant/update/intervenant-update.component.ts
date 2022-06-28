import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIntervenant, Intervenant } from '../intervenant.model';
import { IntervenantService } from '../service/intervenant.service';
import { IProjets } from 'app/entities/projets/projets.model';
import { ProjetsService } from 'app/entities/projets/service/projets.service';

@Component({
  selector: 'jhi-intervenant-update',
  templateUrl: './intervenant-update.component.html',
})
export class IntervenantUpdateComponent implements OnInit {
  isSaving = false;

  projetsSharedCollection: IProjets[] = [];

  editForm = this.fb.group({
    id: [],
    nomIntervenant: [null, [Validators.required]],
    prenomIntervenant: [null, [Validators.required]],
    emailProfessionnel: [null, [Validators.required]],
    raisonSocial: [null, [Validators.required]],
    maitre: [null, [Validators.required]],
    role: [null, [Validators.required]],
    nomProjet: [],
  });

  constructor(
    protected intervenantService: IntervenantService,
    protected projetsService: ProjetsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intervenant }) => {
      this.updateForm(intervenant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const intervenant = this.createFromForm();
    if (intervenant.id !== undefined) {
      this.subscribeToSaveResponse(this.intervenantService.update(intervenant));
    } else {
      this.subscribeToSaveResponse(this.intervenantService.create(intervenant));
    }
  }

  trackProjetsById(index: number, item: IProjets): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIntervenant>>): void {
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

  protected updateForm(intervenant: IIntervenant): void {
    this.editForm.patchValue({
      id: intervenant.id,
      nomIntervenant: intervenant.nomIntervenant,
      prenomIntervenant: intervenant.prenomIntervenant,
      emailProfessionnel: intervenant.emailProfessionnel,
      raisonSocial: intervenant.raisonSocial,
      maitre: intervenant.maitre,
      role: intervenant.role,
      nomProjet: intervenant.nomProjet,
    });

    this.projetsSharedCollection = this.projetsService.addProjetsToCollectionIfMissing(this.projetsSharedCollection, intervenant.nomProjet);
  }

  protected loadRelationshipsOptions(): void {
    this.projetsService
      .query()
      .pipe(map((res: HttpResponse<IProjets[]>) => res.body ?? []))
      .pipe(
        map((projets: IProjets[]) => this.projetsService.addProjetsToCollectionIfMissing(projets, this.editForm.get('nomProjet')!.value))
      )
      .subscribe((projets: IProjets[]) => (this.projetsSharedCollection = projets));
  }

  protected createFromForm(): IIntervenant {
    return {
      ...new Intervenant(),
      id: this.editForm.get(['id'])!.value,
      nomIntervenant: this.editForm.get(['nomIntervenant'])!.value,
      prenomIntervenant: this.editForm.get(['prenomIntervenant'])!.value,
      emailProfessionnel: this.editForm.get(['emailProfessionnel'])!.value,
      raisonSocial: this.editForm.get(['raisonSocial'])!.value,
      maitre: this.editForm.get(['maitre'])!.value,
      role: this.editForm.get(['role'])!.value,
      nomProjet: this.editForm.get(['nomProjet'])!.value,
    };
  }
}
