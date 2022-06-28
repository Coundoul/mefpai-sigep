import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBureau, Bureau } from '../bureau.model';
import { BureauService } from '../service/bureau.service';

@Component({
  selector: 'jhi-bureau-update',
  templateUrl: './bureau-update.component.html',
})
export class BureauUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomStructure: [null, [Validators.required]],
    direction: [],
    nomEtablissement: [],
  });

  constructor(protected bureauService: BureauService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bureau }) => {
      this.updateForm(bureau);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bureau = this.createFromForm();
    if (bureau.id !== undefined) {
      this.subscribeToSaveResponse(this.bureauService.update(bureau));
    } else {
      this.subscribeToSaveResponse(this.bureauService.create(bureau));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBureau>>): void {
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

  protected updateForm(bureau: IBureau): void {
    this.editForm.patchValue({
      id: bureau.id,
      nomStructure: bureau.nomStructure,
      direction: bureau.direction,
      nomEtablissement: bureau.nomEtablissement,
    });
  }

  protected createFromForm(): IBureau {
    return {
      ...new Bureau(),
      id: this.editForm.get(['id'])!.value,
      nomStructure: this.editForm.get(['nomStructure'])!.value,
      direction: this.editForm.get(['direction'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
    };
  }
}
