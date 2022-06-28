import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProjets, Projets } from '../projets.model';
import { ProjetsService } from '../service/projets.service';
import { IContratProjet } from 'app/entities/contrat-projet/contrat-projet.model';
import { ContratProjetService } from 'app/entities/contrat-projet/service/contrat-projet.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

@Component({
  selector: 'jhi-projets-update',
  templateUrl: './projets-update.component.html',
})
export class ProjetsUpdateComponent implements OnInit {
  isSaving = false;

  nomsCollection: IContratProjet[] = [];
  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    typeProjet: [null, [Validators.required]],
    nomProjet: [null, [Validators.required]],
    dateDebut: [null, [Validators.required]],
    dateFin: [null, [Validators.required]],
    description: [null, [Validators.required]],
    extension: [null, [Validators.required]],
    nom: [],
    nomEtablissement: [],
    nomBatiment: [],
  });

  constructor(
    protected projetsService: ProjetsService,
    protected contratProjetService: ContratProjetService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projets }) => {
      if (projets.id === undefined) {
        const today = dayjs().startOf('day');
        projets.dateDebut = today;
        projets.dateFin = today;
      }

      this.updateForm(projets);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const projets = this.createFromForm();
    if (projets.id !== undefined) {
      this.subscribeToSaveResponse(this.projetsService.update(projets));
    } else {
      this.subscribeToSaveResponse(this.projetsService.create(projets));
    }
  }

  trackContratProjetById(index: number, item: IContratProjet): number {
    return item.id!;
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjets>>): void {
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

  protected updateForm(projets: IProjets): void {
    this.editForm.patchValue({
      id: projets.id,
      typeProjet: projets.typeProjet,
      nomProjet: projets.nomProjet,
      dateDebut: projets.dateDebut ? projets.dateDebut.format(DATE_TIME_FORMAT) : null,
      dateFin: projets.dateFin ? projets.dateFin.format(DATE_TIME_FORMAT) : null,
      description: projets.description,
      extension: projets.extension,
      nom: projets.nom,
      nomEtablissement: projets.nomEtablissement,
      nomBatiment: projets.nomBatiment,
    });

    this.nomsCollection = this.contratProjetService.addContratProjetToCollectionIfMissing(this.nomsCollection, projets.nom);
    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      projets.nomEtablissement,
      projets.nomBatiment
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contratProjetService
      .query({ filter: 'nomprojet-is-null' })
      .pipe(map((res: HttpResponse<IContratProjet[]>) => res.body ?? []))
      .pipe(
        map((contratProjets: IContratProjet[]) =>
          this.contratProjetService.addContratProjetToCollectionIfMissing(contratProjets, this.editForm.get('nom')!.value)
        )
      )
      .subscribe((contratProjets: IContratProjet[]) => (this.nomsCollection = contratProjets));

    this.etablissementService
      .query()
      .pipe(map((res: HttpResponse<IEtablissement[]>) => res.body ?? []))
      .pipe(
        map((etablissements: IEtablissement[]) =>
          this.etablissementService.addEtablissementToCollectionIfMissing(
            etablissements,
            this.editForm.get('nomEtablissement')!.value,
            this.editForm.get('nomBatiment')!.value
          )
        )
      )
      .subscribe((etablissements: IEtablissement[]) => (this.etablissementsSharedCollection = etablissements));
  }

  protected createFromForm(): IProjets {
    return {
      ...new Projets(),
      id: this.editForm.get(['id'])!.value,
      typeProjet: this.editForm.get(['typeProjet'])!.value,
      nomProjet: this.editForm.get(['nomProjet'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value ? dayjs(this.editForm.get(['dateDebut'])!.value, DATE_TIME_FORMAT) : undefined,
      dateFin: this.editForm.get(['dateFin'])!.value ? dayjs(this.editForm.get(['dateFin'])!.value, DATE_TIME_FORMAT) : undefined,
      description: this.editForm.get(['description'])!.value,
      extension: this.editForm.get(['extension'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
      nomBatiment: this.editForm.get(['nomBatiment'])!.value,
    };
  }
}
