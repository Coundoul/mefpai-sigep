import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITechnicien } from '../technicien.model';
import { TechnicienService } from '../service/technicien.service';

@Component({
  templateUrl: './technicien-delete-dialog.component.html',
})
export class TechnicienDeleteDialogComponent {
  technicien?: ITechnicien;

  constructor(protected technicienService: TechnicienService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.technicienService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
