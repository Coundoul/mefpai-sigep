import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFicheTechnique, FicheTechnique } from '../fiche-technique.model';
import { FicheTechniqueService } from '../service/fiche-technique.service';
import { IResponsable } from 'app/entities/responsable/responsable.model';
import { ResponsableService } from 'app/entities/responsable/service/responsable.service';

@Component({
  selector: 'jhi-fiche-technique-update',
  templateUrl: './fiche-technique-update.component.html',
})
export class FicheTechniqueUpdateComponent implements OnInit {
  isSaving = false;

  responsablesSharedCollection: IResponsable[] = [];

  editForm = this.fb.group({
    id: [],
    pieceJointe: [null, [Validators.required]],
    dateDepot: [],
    nomResponsable: [],
  });

  constructor(
    protected ficheTechniqueService: FicheTechniqueService,
    protected responsableService: ResponsableService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ficheTechnique }) => {
      if (ficheTechnique.id === undefined) {
        const today = dayjs().startOf('day');
        ficheTechnique.dateDepot = today;
      }

      this.updateForm(ficheTechnique);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ficheTechnique = this.createFromForm();
    if (ficheTechnique.id !== undefined) {
      this.subscribeToSaveResponse(this.ficheTechniqueService.update(ficheTechnique));
    } else {
      this.subscribeToSaveResponse(this.ficheTechniqueService.create(ficheTechnique));
    }
  }

  trackResponsableById(index: number, item: IResponsable): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFicheTechnique>>): void {
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

  protected updateForm(ficheTechnique: IFicheTechnique): void {
    this.editForm.patchValue({
      id: ficheTechnique.id,
      pieceJointe: ficheTechnique.pieceJointe,
      dateDepot: ficheTechnique.dateDepot ? ficheTechnique.dateDepot.format(DATE_TIME_FORMAT) : null,
      nomResponsable: ficheTechnique.nomResponsable,
    });

    this.responsablesSharedCollection = this.responsableService.addResponsableToCollectionIfMissing(
      this.responsablesSharedCollection,
      ficheTechnique.nomResponsable
    );
  }

  protected loadRelationshipsOptions(): void {
    this.responsableService
      .query()
      .pipe(map((res: HttpResponse<IResponsable[]>) => res.body ?? []))
      .pipe(
        map((responsables: IResponsable[]) =>
          this.responsableService.addResponsableToCollectionIfMissing(responsables, this.editForm.get('nomResponsable')!.value)
        )
      )
      .subscribe((responsables: IResponsable[]) => (this.responsablesSharedCollection = responsables));
  }

  protected createFromForm(): IFicheTechnique {
    return {
      ...new FicheTechnique(),
      id: this.editForm.get(['id'])!.value,
      pieceJointe: this.editForm.get(['pieceJointe'])!.value,
      dateDepot: this.editForm.get(['dateDepot'])!.value ? dayjs(this.editForm.get(['dateDepot'])!.value, DATE_TIME_FORMAT) : undefined,
      nomResponsable: this.editForm.get(['nomResponsable'])!.value,
    };
  }
}
