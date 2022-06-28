import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFicheTechniqueMaintenance } from '../fiche-technique-maintenance.model';
import { FicheTechniqueMaintenanceService } from '../service/fiche-technique-maintenance.service';

@Component({
  templateUrl: './fiche-technique-maintenance-delete-dialog.component.html',
})
export class FicheTechniqueMaintenanceDeleteDialogComponent {
  ficheTechniqueMaintenance?: IFicheTechniqueMaintenance;

  constructor(protected ficheTechniqueMaintenanceService: FicheTechniqueMaintenanceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ficheTechniqueMaintenanceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
