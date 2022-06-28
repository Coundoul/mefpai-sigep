import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFormateurs, Formateurs } from '../formateurs.model';
import { FormateursService } from '../service/formateurs.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

@Component({
  selector: 'jhi-formateurs-update',
  templateUrl: './formateurs-update.component.html',
})
export class FormateursUpdateComponent implements OnInit {
  isSaving = false;

  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    nomFormateur: [null, [Validators.required]],
    prenomFormateur: [null, [Validators.required]],
    email: [null, [Validators.required]],
    numb1: [null, [Validators.required]],
    numb2: [],
    adresse: [null, [Validators.required]],
    ville: [null, [Validators.required]],
    specialite: [null, [Validators.required]],
    nomEtablissement: [],
  });

  constructor(
    protected formateursService: FormateursService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formateurs }) => {
      this.updateForm(formateurs);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formateurs = this.createFromForm();
    if (formateurs.id !== undefined) {
      this.subscribeToSaveResponse(this.formateursService.update(formateurs));
    } else {
      this.subscribeToSaveResponse(this.formateursService.create(formateurs));
    }
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormateurs>>): void {
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

  protected updateForm(formateurs: IFormateurs): void {
    this.editForm.patchValue({
      id: formateurs.id,
      nomFormateur: formateurs.nomFormateur,
      prenomFormateur: formateurs.prenomFormateur,
      email: formateurs.email,
      numb1: formateurs.numb1,
      numb2: formateurs.numb2,
      adresse: formateurs.adresse,
      ville: formateurs.ville,
      specialite: formateurs.specialite,
      nomEtablissement: formateurs.nomEtablissement,
    });

    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      formateurs.nomEtablissement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etablissementService
      .query()
      .pipe(map((res: HttpResponse<IEtablissement[]>) => res.body ?? []))
      .pipe(
        map((etablissements: IEtablissement[]) =>
          this.etablissementService.addEtablissementToCollectionIfMissing(etablissements, this.editForm.get('nomEtablissement')!.value)
        )
      )
      .subscribe((etablissements: IEtablissement[]) => (this.etablissementsSharedCollection = etablissements));
  }

  protected createFromForm(): IFormateurs {
    return {
      ...new Formateurs(),
      id: this.editForm.get(['id'])!.value,
      nomFormateur: this.editForm.get(['nomFormateur'])!.value,
      prenomFormateur: this.editForm.get(['prenomFormateur'])!.value,
      email: this.editForm.get(['email'])!.value,
      numb1: this.editForm.get(['numb1'])!.value,
      numb2: this.editForm.get(['numb2'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      ville: this.editForm.get(['ville'])!.value,
      specialite: this.editForm.get(['specialite'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
    };
  }
}
