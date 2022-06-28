import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICorpsEtat } from '../corps-etat.model';
import { CorpsEtatService } from '../service/corps-etat.service';

@Component({
  templateUrl: './corps-etat-delete-dialog.component.html',
})
export class CorpsEtatDeleteDialogComponent {
  corpsEtat?: ICorpsEtat;

  constructor(protected corpsEtatService: CorpsEtatService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.corpsEtatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
