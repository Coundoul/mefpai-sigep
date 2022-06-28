import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMangasinierFichiste, MangasinierFichiste } from '../mangasinier-fichiste.model';
import { MangasinierFichisteService } from '../service/mangasinier-fichiste.service';
import { IComptablePrincipale } from 'app/entities/comptable-principale/comptable-principale.model';
import { ComptablePrincipaleService } from 'app/entities/comptable-principale/service/comptable-principale.service';

@Component({
  selector: 'jhi-mangasinier-fichiste-update',
  templateUrl: './mangasinier-fichiste-update.component.html',
})
export class MangasinierFichisteUpdateComponent implements OnInit {
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
    protected mangasinierFichisteService: MangasinierFichisteService,
    protected comptablePrincipaleService: ComptablePrincipaleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mangasinierFichiste }) => {
      this.updateForm(mangasinierFichiste);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mangasinierFichiste = this.createFromForm();
    if (mangasinierFichiste.id !== undefined) {
      this.subscribeToSaveResponse(this.mangasinierFichisteService.update(mangasinierFichiste));
    } else {
      this.subscribeToSaveResponse(this.mangasinierFichisteService.create(mangasinierFichiste));
    }
  }

  trackComptablePrincipaleById(index: number, item: IComptablePrincipale): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMangasinierFichiste>>): void {
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

  protected updateForm(mangasinierFichiste: IMangasinierFichiste): void {
    this.editForm.patchValue({
      id: mangasinierFichiste.id,
      nomPers: mangasinierFichiste.nomPers,
      prenomPers: mangasinierFichiste.prenomPers,
      sexe: mangasinierFichiste.sexe,
      mobile: mangasinierFichiste.mobile,
      adresse: mangasinierFichiste.adresse,
      direction: mangasinierFichiste.direction,
      comptablePrincipale: mangasinierFichiste.comptablePrincipale,
    });

    this.comptablePrincipalesSharedCollection = this.comptablePrincipaleService.addComptablePrincipaleToCollectionIfMissing(
      this.comptablePrincipalesSharedCollection,
      mangasinierFichiste.comptablePrincipale
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

  protected createFromForm(): IMangasinierFichiste {
    return {
      ...new MangasinierFichiste(),
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
