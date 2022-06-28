import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRequete, Requete } from '../requete.model';
import { RequeteService } from '../service/requete.service';
import { IBureau } from 'app/entities/bureau/bureau.model';
import { BureauService } from 'app/entities/bureau/service/bureau.service';

@Component({
  selector: 'jhi-requete-update',
  templateUrl: './requete-update.component.html',
})
export class RequeteUpdateComponent implements OnInit {
  isSaving = false;

  bureausSharedCollection: IBureau[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    typePanne: [null, [Validators.required]],
    datePost: [null, [Validators.required]],
    description: [null, [Validators.required]],
    etatTraite: [],
    dateLancement: [],
    idPers: [null, [Validators.required]],
    nomStructure: [],
  });

  constructor(
    protected requeteService: RequeteService,
    protected bureauService: BureauService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ requete }) => {
      if (requete.id === undefined) {
        const today = dayjs().startOf('day');
        requete.dateLancement = today;
      }

      this.updateForm(requete);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const requete = this.createFromForm();
    if (requete.id !== undefined) {
      this.subscribeToSaveResponse(this.requeteService.update(requete));
    } else {
      this.subscribeToSaveResponse(this.requeteService.create(requete));
    }
  }

  trackBureauById(index: number, item: IBureau): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRequete>>): void {
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

  protected updateForm(requete: IRequete): void {
    this.editForm.patchValue({
      id: requete.id,
      type: requete.type,
      typePanne: requete.typePanne,
      datePost: requete.datePost,
      description: requete.description,
      etatTraite: requete.etatTraite,
      dateLancement: requete.dateLancement ? requete.dateLancement.format(DATE_TIME_FORMAT) : null,
      idPers: requete.idPers,
      nomStructure: requete.nomStructure,
    });

    this.bureausSharedCollection = this.bureauService.addBureauToCollectionIfMissing(this.bureausSharedCollection, requete.nomStructure);
  }

  protected loadRelationshipsOptions(): void {
    this.bureauService
      .query()
      .pipe(map((res: HttpResponse<IBureau[]>) => res.body ?? []))
      .pipe(
        map((bureaus: IBureau[]) => this.bureauService.addBureauToCollectionIfMissing(bureaus, this.editForm.get('nomStructure')!.value))
      )
      .subscribe((bureaus: IBureau[]) => (this.bureausSharedCollection = bureaus));
  }

  protected createFromForm(): IRequete {
    return {
      ...new Requete(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      typePanne: this.editForm.get(['typePanne'])!.value,
      datePost: this.editForm.get(['datePost'])!.value,
      description: this.editForm.get(['description'])!.value,
      etatTraite: this.editForm.get(['etatTraite'])!.value,
      dateLancement: this.editForm.get(['dateLancement'])!.value
        ? dayjs(this.editForm.get(['dateLancement'])!.value, DATE_TIME_FORMAT)
        : undefined,
      idPers: this.editForm.get(['idPers'])!.value,
      nomStructure: this.editForm.get(['nomStructure'])!.value,
    };
  }
}
