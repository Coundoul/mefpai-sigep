import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMagazin, Magazin } from '../magazin.model';
import { MagazinService } from '../service/magazin.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-magazin-update',
  templateUrl: './magazin-update.component.html',
})
export class MagazinUpdateComponent implements OnInit {
  isSaving = false;

  quartiersSharedCollection: IQuartier[] = [];

  editForm = this.fb.group({
    id: [],
    nomMagazin: [null, [Validators.required]],
    surfaceBatie: [null, [Validators.required]],
    superficie: [null, [Validators.required]],
    idPers: [null, [Validators.required]],
    nomQuartier: [],
  });

  constructor(
    protected magazinService: MagazinService,
    protected quartierService: QuartierService,
    protected activatedRoute: ActivatedRoute,
    private router: Router,
    protected fb: FormBuilder,
    public activeModal: NgbActiveModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ magazin }) => {
      this.updateForm(magazin);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  save(): void {
    this.isSaving = true;
    const magazin = this.createFromForm();
    if (magazin.id !== undefined) {
      this.subscribeToSaveResponse(this.magazinService.update(magazin));
    } else {
      this.subscribeToSaveResponse(this.magazinService.create(magazin));
    }
  }

  trackQuartierById(index: number, item: IQuartier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMagazin>>): void {
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

  protected updateForm(magazin: IMagazin): void {
    this.editForm.patchValue({
      id: magazin.id,
      nomMagazin: magazin.nomMagazin,
      surfaceBatie: magazin.surfaceBatie,
      superficie: magazin.superficie,
      idPers: magazin.idPers,
      nomQuartier: magazin.nomQuartier,
    });

    this.quartiersSharedCollection = this.quartierService.addQuartierToCollectionIfMissing(
      this.quartiersSharedCollection,
      magazin.nomQuartier
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quartierService
      .query()
      .pipe(map((res: HttpResponse<IQuartier[]>) => res.body ?? []))
      .pipe(
        map((quartiers: IQuartier[]) =>
          this.quartierService.addQuartierToCollectionIfMissing(quartiers, this.editForm.get('nomQuartier')!.value)
        )
      )
      .subscribe((quartiers: IQuartier[]) => (this.quartiersSharedCollection = quartiers));
  }

  protected createFromForm(): IMagazin {
    return {
      ...new Magazin(),
      // id: this.editForm.get(['id'])!.value,
      nomMagazin: this.editForm.get(['nomMagazin'])!.value,
      surfaceBatie: this.editForm.get(['surfaceBatie'])!.value,
      superficie: this.editForm.get(['superficie'])!.value,
      idPers: this.editForm.get(['idPers'])!.value,
      nomQuartier: this.editForm.get(['nomQuartier'])!.value,
    };
  }
}
