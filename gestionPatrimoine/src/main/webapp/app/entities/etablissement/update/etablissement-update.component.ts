import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEtablissement, Etablissement } from '../etablissement.model';
import { EtablissementService } from '../service/etablissement.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';
import { MatHorizontalStepper } from '@angular/material/stepper';
import { EventManager } from 'app/core/util/event-manager.service';

@Component({
  selector: 'jhi-etablissement-update',
  templateUrl: './etablissement-update.component.html',
  styleUrls: ['./etablissement-update.component.scss'],
})
export class EtablissementUpdateComponent implements OnInit {
  isSaving = false;
  @ViewChild('stepper') stepper!: MatHorizontalStepper;
  step = 0;

  quartiersSharedCollection: IQuartier[] = [];

  editForm = this.fb.group({
    id: [],
    nomEtablissement: [null, [Validators.required]],
    adresse: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    email: [null, [Validators.required]],
    surfaceBatie: [null, [Validators.required]],
    superficie: [null, [Validators.required]],
    statusFoncier: [null, [Validators.required]],
    nombreApprenants: [null, [Validators.required]],
    proprietaire: [null, [Validators.required]],
    possibiliteExtension: [null, [Validators.required]],
    descriptionExtension: [],
    branchementEau: [null, [Validators.required]],
    branchementElectricite: [null, [Validators.required]],
    puissanceSouscrite: [null, [Validators.required]],
    typeReseau: [null, [Validators.required]],
    circuitTerre: [null, [Validators.required]],
    protectionArret: [null, [Validators.required]],
    protectionFoudre: [null, [Validators.required]],
    connexionTel: [null, [Validators.required]],
    connexionInternet: [null, [Validators.required]],
    environnement: [],
    dispositif: [],
    idPers: [null, [Validators.required]],
    nomQuartier: [],
  });

  constructor(
    protected etablissementService: EtablissementService,
    protected quartierService: QuartierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected eventManager: EventManager
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etablissement }) => {
      this.updateForm(etablissement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  public onSetepChange(event: any): void {
    this.step = event.selectedIndex;
  }

  save(): void {
    this.isSaving = true;
    const etablissement = this.createFromForm();
    if (etablissement.id !== undefined) {
      this.subscribeToSaveResponse(this.etablissementService.update(etablissement));
    } else {
      this.subscribeToSaveResponse(this.etablissementService.create(etablissement));
    }
  }

  trackQuartierById(index: number, item: IQuartier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtablissement>>): void {
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

  protected updateForm(etablissement: IEtablissement): void {
    this.editForm.patchValue({
      id: etablissement.id,
      nomEtablissement: etablissement.nomEtablissement,
      adresse: etablissement.adresse,
      telephone: etablissement.telephone,
      email: etablissement.email,
      surfaceBatie: etablissement.surfaceBatie,
      superficie: etablissement.superficie,
      statusFoncier: etablissement.statusFoncier,
      nombreApprenants: etablissement.nombreApprenants,
      proprietaire: etablissement.proprietaire,
      possibilteExtension: etablissement.possibiliteExtension,
      descriptionExtension: etablissement.descriptionExtension,
      branchementEau: etablissement.branchementEau,
      branchementElectricite: etablissement.branchementEau,
      puissanceSouscrite: etablissement.puissanceSouscrite,
      typeReseau: etablissement.typeReseau,
      circuitTerre: etablissement.circuitTerre,
      protectionArret: etablissement.protectionArret,
      protectionFoudre: etablissement.protectionFoudre,
      connexionTel: etablissement.connexionTel,
      connexionInternet: etablissement.connexionInternet,
      environnement: etablissement.environnement,
      dispositif: etablissement.dispositif,
      idPers: etablissement.idPers,
      nomQuartier: etablissement.nomQuartier,
    });

    this.quartiersSharedCollection = this.quartierService.addQuartierToCollectionIfMissing(
      this.quartiersSharedCollection,
      etablissement.nomQuartier
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

  protected createFromForm(): IEtablissement {
    return {
      ...new Etablissement(),
      id: this.editForm.get(['id'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      email: this.editForm.get(['email'])!.value,
      surfaceBatie: this.editForm.get(['surfaceBatie'])!.value,
      superficie: this.editForm.get(['superficie'])!.value,
      statusFoncier: this.editForm.get(['statusFoncier'])!.value,
      nombreApprenants: this.editForm.get(['nombreApprenants'])!.value,
      proprietaire: this.editForm.get(['proprietaire'])!.value,
      possibiliteExtension: this.editForm.get(['possibiliteExtension'])!.value,
      descriptionExtension: this.editForm.get(['descriptionExtension'])!.value,
      branchementEau: this.editForm.get(['branchementEau'])!.value,
      branchementElectricite: this.editForm.get(['branchementElectricite'])!.value,
      puissanceSouscrite: this.editForm.get(['puissanceSouscrite'])!.value,
      typeReseau: this.editForm.get(['typeReseau'])!.value,
      circuitTerre: this.editForm.get(['circuitTerre'])!.value,
      protectionArret: this.editForm.get(['protectionArret'])!.value,
      protectionFoudre: this.editForm.get(['protectionFoudre'])!.value,
      connexionTel: this.editForm.get(['connexionTel'])!.value,
      connexionInternet: this.editForm.get(['connexionInternet'])!.value,
      environnement: this.editForm.get(['environnement'])!.value,
      dispositif: this.editForm.get(['dispositif'])!.value,
      idPers: this.editForm.get(['idPers'])!.value,
      nomQuartier: this.editForm.get(['nomQuartier'])!.value,
    };
  }
}
