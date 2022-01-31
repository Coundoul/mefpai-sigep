import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFiliereStabilise, FiliereStabilise } from '../filiere-stabilise.model';
import { FiliereStabiliseService } from '../service/filiere-stabilise.service';
import { IFormateurs } from 'app/entities/formateurs/formateurs.model';
import { FormateursService } from 'app/entities/formateurs/service/formateurs.service';

@Component({
  selector: 'jhi-filiere-stabilise-update',
  templateUrl: './filiere-stabilise-update.component.html',
})
export class FiliereStabiliseUpdateComponent implements OnInit {
  isSaving = false;

  formateursSharedCollection: IFormateurs[] = [];

  editForm = this.fb.group({
    id: [],
    nomFiliere: [null, [Validators.required]],
    nomFormateur: [],
  });

  constructor(
    protected filiereStabiliseService: FiliereStabiliseService,
    protected formateursService: FormateursService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filiereStabilise }) => {
      this.updateForm(filiereStabilise);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filiereStabilise = this.createFromForm();
    if (filiereStabilise.id !== undefined) {
      this.subscribeToSaveResponse(this.filiereStabiliseService.update(filiereStabilise));
    } else {
      this.subscribeToSaveResponse(this.filiereStabiliseService.create(filiereStabilise));
    }
  }

  trackFormateursById(index: number, item: IFormateurs): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiliereStabilise>>): void {
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

  protected updateForm(filiereStabilise: IFiliereStabilise): void {
    this.editForm.patchValue({
      id: filiereStabilise.id,
      nomFiliere: filiereStabilise.nomFiliere,
      nomFormateur: filiereStabilise.nomFormateur,
    });

    this.formateursSharedCollection = this.formateursService.addFormateursToCollectionIfMissing(
      this.formateursSharedCollection,
      filiereStabilise.nomFormateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formateursService
      .query()
      .pipe(map((res: HttpResponse<IFormateurs[]>) => res.body ?? []))
      .pipe(
        map((formateurs: IFormateurs[]) =>
          this.formateursService.addFormateursToCollectionIfMissing(formateurs, this.editForm.get('nomFormateur')!.value)
        )
      )
      .subscribe((formateurs: IFormateurs[]) => (this.formateursSharedCollection = formateurs));
  }

  protected createFromForm(): IFiliereStabilise {
    return {
      ...new FiliereStabilise(),
      id: this.editForm.get(['id'])!.value,
      nomFiliere: this.editForm.get(['nomFiliere'])!.value,
      nomFormateur: this.editForm.get(['nomFormateur'])!.value,
    };
  }
}
