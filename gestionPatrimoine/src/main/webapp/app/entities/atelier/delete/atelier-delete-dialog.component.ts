import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAtelier } from '../atelier.model';
import { AtelierService } from '../service/atelier.service';

@Component({
  templateUrl: './atelier-delete-dialog.component.html',
})
export class AtelierDeleteDialogComponent {
  atelier?: IAtelier;

  constructor(protected atelierService: AtelierService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.atelierService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
