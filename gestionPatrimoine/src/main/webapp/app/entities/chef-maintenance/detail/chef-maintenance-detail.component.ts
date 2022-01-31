import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChefMaintenance } from '../chef-maintenance.model';

@Component({
  selector: 'jhi-chef-maintenance-detail',
  templateUrl: './chef-maintenance-detail.component.html',
})
export class ChefMaintenanceDetailComponent implements OnInit {
  chefMaintenance: IChefMaintenance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chefMaintenance }) => {
      this.chefMaintenance = chefMaintenance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
