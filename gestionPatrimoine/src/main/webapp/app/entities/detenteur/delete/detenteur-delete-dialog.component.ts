import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetenteur } from '../detenteur.model';
import { DetenteurService } from '../service/detenteur.service';

@Component({
  templateUrl: './detenteur-delete-dialog.component.html',
})
export class DetenteurDeleteDialogComponent {
  detenteur?: IDetenteur;

  constructor(protected detenteurService: DetenteurService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detenteurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
