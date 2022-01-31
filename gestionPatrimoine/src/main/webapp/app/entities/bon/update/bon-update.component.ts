import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBon, Bon } from '../bon.model';
import { BonService } from '../service/bon.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MatHorizontalStepper } from '@angular/material/stepper';

@Component({
  selector: 'jhi-bon-update',
  templateUrl: './bon-update.component.html',
  styleUrls: ['./bon-update.component.scss'],
})
export class BonUpdateComponent implements OnInit {
  isSaving = false;
  @ViewChild('stepper') stepper!: MatHorizontalStepper;
  step = 0;
  editForm = this.fb.group({
    id: [],
    typeBon: [null, [Validators.required]],
    quantiteLivre: [],
    quantiteCommande: [],
    dateCreation: [],
    etat: [],
  });

  constructor(protected bonService: BonService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bon }) => {
      if (bon.id === undefined) {
        const today = dayjs().startOf('day');
        bon.dateCreation = today;
      }

      this.updateForm(bon);
    });
  }

  ngAfterViewInit(): void {
    this.stepper._getIndicatorType = () => '';
  }

  previousState(): void {
    window.history.back();
  }

  public onSetepChange(event: any): void {
    this.step = event.selectedIndex;
  }

  save(): void {
    this.isSaving = true;
    const bon = this.createFromForm();
    if (bon.id !== undefined) {
      this.subscribeToSaveResponse(this.bonService.update(bon));
    } else {
      this.subscribeToSaveResponse(this.bonService.create(bon));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBon>>): void {
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

  protected updateForm(bon: IBon): void {
    this.editForm.patchValue({
      id: bon.id,
      typeBon: bon.typeBon,
      quantiteLivre: bon.quantiteLivre,
      quantiteCommande: bon.quantiteCommande,
      dateCreation: bon.dateCreation ? bon.dateCreation.format(DATE_TIME_FORMAT) : null,
      etat: bon.etat,
    });
  }

  protected createFromForm(): IBon {
    return {
      ...new Bon(),
      id: this.editForm.get(['id'])!.value,
      typeBon: this.editForm.get(['typeBon'])!.value,
      quantiteLivre: this.editForm.get(['quantiteLivre'])!.value,
      quantiteCommande: this.editForm.get(['quantiteCommande'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value
        ? dayjs(this.editForm.get(['dateCreation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      etat: this.editForm.get(['etat'])!.value,
    };
  }
}
