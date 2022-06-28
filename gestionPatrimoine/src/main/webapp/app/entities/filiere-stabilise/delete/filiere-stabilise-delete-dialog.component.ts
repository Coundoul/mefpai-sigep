import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFiliereStabilise } from '../filiere-stabilise.model';
import { FiliereStabiliseService } from '../service/filiere-stabilise.service';

@Component({
  templateUrl: './filiere-stabilise-delete-dialog.component.html',
})
export class FiliereStabiliseDeleteDialogComponent {
  filiereStabilise?: IFiliereStabilise;

  constructor(protected filiereStabiliseService: FiliereStabiliseService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.filiereStabiliseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
