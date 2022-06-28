import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIntervenant } from '../intervenant.model';
import { IntervenantService } from '../service/intervenant.service';

@Component({
  templateUrl: './intervenant-delete-dialog.component.html',
})
export class IntervenantDeleteDialogComponent {
  intervenant?: IIntervenant;

  constructor(protected intervenantService: IntervenantService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.intervenantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
