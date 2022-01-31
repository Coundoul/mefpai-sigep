import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IComptableSecondaire, ComptableSecondaire } from '../comptable-secondaire.model';
import { ComptableSecondaireService } from '../service/comptable-secondaire.service';
import { IComptablePrincipale } from 'app/entities/comptable-principale/comptable-principale.model';
import { ComptablePrincipaleService } from 'app/entities/comptable-principale/service/comptable-principale.service';

@Component({
  selector: 'jhi-comptable-secondaire-update',
  templateUrl: './comptable-secondaire-update.component.html',
})
export class ComptableSecondaireUpdateComponent implements OnInit {
  isSaving = false;

  comptablePrincipalesSharedCollection: IComptablePrincipale[] = [];

  editForm = this.fb.group({
    id: [],
    nomPers: [null, [Validators.required]],
    prenomPers: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    mobile: [null, [Validators.required]],
    adresse: [],
    direction: [null, [Validators.required]],
    comptablePrincipale: [],
  });

  constructor(
    protected comptableSecondaireService: ComptableSecondaireService,
    protected comptablePrincipaleService: ComptablePrincipaleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comptableSecondaire }) => {
      this.updateForm(comptableSecondaire);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comptableSecondaire = this.createFromForm();
    if (comptableSecondaire.id !== undefined) {
      this.subscribeToSaveResponse(this.comptableSecondaireService.update(comptableSecondaire));
    } else {
      this.subscribeToSaveResponse(this.comptableSecondaireService.create(comptableSecondaire));
    }
  }

  trackComptablePrincipaleById(index: number, item: IComptablePrincipale): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComptableSecondaire>>): void {
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

  protected updateForm(comptableSecondaire: IComptableSecondaire): void {
    this.editForm.patchValue({
      id: comptableSecondaire.id,
      nomPers: comptableSecondaire.nomPers,
      prenomPers: comptableSecondaire.prenomPers,
      sexe: comptableSecondaire.sexe,
      mobile: comptableSecondaire.mobile,
      adresse: comptableSecondaire.adresse,
      direction: comptableSecondaire.direction,
      comptablePrincipale: comptableSecondaire.comptablePrincipale,
    });

    this.comptablePrincipalesSharedCollection = this.comptablePrincipaleService.addComptablePrincipaleToCollectionIfMissing(
      this.comptablePrincipalesSharedCollection,
      comptableSecondaire.comptablePrincipale
    );
  }

  protected loadRelationshipsOptions(): void {
    this.comptablePrincipaleService
      .query()
      .pipe(map((res: HttpResponse<IComptablePrincipale[]>) => res.body ?? []))
      .pipe(
        map((comptablePrincipales: IComptablePrincipale[]) =>
          this.comptablePrincipaleService.addComptablePrincipaleToCollectionIfMissing(
            comptablePrincipales,
            this.editForm.get('comptablePrincipale')!.value
          )
        )
      )
      .subscribe((comptablePrincipales: IComptablePrincipale[]) => (this.comptablePrincipalesSharedCollection = comptablePrincipales));
  }

  protected createFromForm(): IComptableSecondaire {
    return {
      ...new ComptableSecondaire(),
      id: this.editForm.get(['id'])!.value,
      nomPers: this.editForm.get(['nomPers'])!.value,
      prenomPers: this.editForm.get(['prenomPers'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      direction: this.editForm.get(['direction'])!.value,
      comptablePrincipale: this.editForm.get(['comptablePrincipale'])!.value,
    };
  }
}
