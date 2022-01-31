import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalles } from '../salles.model';
import { SallesService } from '../service/salles.service';

@Component({
  templateUrl: './salles-delete-dialog.component.html',
})
export class SallesDeleteDialogComponent {
  salles?: ISalles;

  constructor(protected sallesService: SallesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sallesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
