import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttributionInfrastructure } from '../attribution-infrastructure.model';
import { AttributionInfrastructureService } from '../service/attribution-infrastructure.service';

@Component({
  templateUrl: './attribution-infrastructure-delete-dialog.component.html',
})
export class AttributionInfrastructureDeleteDialogComponent {
  attributionInfrastructure?: IAttributionInfrastructure;

  constructor(protected attributionInfrastructureService: AttributionInfrastructureService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attributionInfrastructureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
