import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAttributionInfrastructure, AttributionInfrastructure } from '../attribution-infrastructure.model';
import { AttributionInfrastructureService } from '../service/attribution-infrastructure.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { EquipementService } from 'app/entities/equipement/service/equipement.service';
import { IEquipement } from 'app/entities/equipement/equipement.model';

@Component({
  selector: 'jhi-attribution-infrastructure-update',
  templateUrl: './attribution-infrastructure-update.component.html',
})
export class AttributionInfrastructureUpdateComponent implements OnInit {
  isSaving = false;
  etablissementsSharedCollection: IEtablissement[] = [];
  equipements: any;

  editForm = this.fb.group({
    id: [],
    dateAttribution: [],
    quantite: [null, [Validators.required]],
    idEquipement: [null, [Validators.required]],
    idPers: [null, [Validators.required]],
    nomEtablissement: [],
  });
  isLoading: any;

  constructor(
    protected attributionInfrastructureService: AttributionInfrastructureService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected equipementService: EquipementService,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.getStat();
    this.activatedRoute.data.subscribe(({ attributionInfrastructure }) => {
      if (attributionInfrastructure.id === undefined) {
        const today = dayjs().startOf('day');
        attributionInfrastructure.dateAttribution = today;
      }

      this.updateForm(attributionInfrastructure);

      this.loadRelationshipsOptions();
    });
  }

  getStat(): void {
    this.equipementService.query().subscribe(
      (res: HttpResponse<any>) => {
        this.isLoading = false;
        this.equipements = res.body;
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attributionInfrastructure = this.createFromForm();
    if (attributionInfrastructure.id !== undefined) {
      this.subscribeToSaveResponse(this.attributionInfrastructureService.update(attributionInfrastructure));
    } else {
      this.subscribeToSaveResponse(this.attributionInfrastructureService.create(attributionInfrastructure));
    }
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttributionInfrastructure>>): void {
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

  protected updateForm(attributionInfrastructure: IAttributionInfrastructure): void {
    this.editForm.patchValue({
      id: attributionInfrastructure.id,
      dateAttribution: attributionInfrastructure.dateAttribution
        ? attributionInfrastructure.dateAttribution.format(DATE_TIME_FORMAT)
        : null,
      quantite: attributionInfrastructure.quantite,
      idEquipement: attributionInfrastructure.idEquipement,
      idPers: attributionInfrastructure.idPers,
      nomEtablissement: attributionInfrastructure.nomEtablissement,
    });

    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      attributionInfrastructure.nomEtablissement
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

  protected createFromForm(): IAttributionInfrastructure {
    return {
      ...new AttributionInfrastructure(),
      id: this.editForm.get(['id'])!.value,
      dateAttribution: this.editForm.get(['dateAttribution'])!.value
        ? dayjs(this.editForm.get(['dateAttribution'])!.value, DATE_TIME_FORMAT)
        : undefined,
      quantite: this.editForm.get(['quantite'])!.value,
      idEquipement: this.editForm.get(['idEquipement'])!.value,
      idPers: this.editForm.get(['idPers'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
    };
  }
}
