import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProjets } from '../projets.model';
import { ProjetsService } from '../service/projets.service';

@Component({
  templateUrl: './projets-delete-dialog.component.html',
})
export class ProjetsDeleteDialogComponent {
  projets?: IProjets;

  constructor(protected projetsService: ProjetsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.projetsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
