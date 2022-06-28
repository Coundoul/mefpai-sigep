import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuartier } from '../quartier.model';
import { QuartierService } from '../service/quartier.service';

@Component({
  templateUrl: './quartier-delete-dialog.component.html',
})
export class QuartierDeleteDialogComponent {
  quartier?: IQuartier;

  constructor(protected quartierService: QuartierService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quartierService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
