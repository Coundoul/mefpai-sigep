import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFicheTechniqueMaintenance } from '../fiche-technique-maintenance.model';

@Component({
  selector: 'jhi-fiche-technique-maintenance-detail',
  templateUrl: './fiche-technique-maintenance-detail.component.html',
})
export class FicheTechniqueMaintenanceDetailComponent implements OnInit {
  ficheTechniqueMaintenance: IFicheTechniqueMaintenance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ficheTechniqueMaintenance }) => {
      this.ficheTechniqueMaintenance = ficheTechniqueMaintenance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
