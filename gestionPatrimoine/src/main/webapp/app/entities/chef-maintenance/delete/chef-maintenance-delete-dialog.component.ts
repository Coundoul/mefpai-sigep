import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChefMaintenance } from '../chef-maintenance.model';
import { ChefMaintenanceService } from '../service/chef-maintenance.service';

@Component({
  templateUrl: './chef-maintenance-delete-dialog.component.html',
})
export class ChefMaintenanceDeleteDialogComponent {
  chefMaintenance?: IChefMaintenance;

  constructor(protected chefMaintenanceService: ChefMaintenanceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chefMaintenanceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
