import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBatiment } from '../batiment.model';
import { BatimentService } from '../service/batiment.service';

@Component({
  templateUrl: './batiment-delete-dialog.component.html',
})
export class BatimentDeleteDialogComponent {
  batiment?: IBatiment;

  constructor(protected batimentService: BatimentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.batimentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
