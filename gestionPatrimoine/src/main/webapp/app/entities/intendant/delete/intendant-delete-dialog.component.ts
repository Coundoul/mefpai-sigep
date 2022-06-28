import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIntendant } from '../intendant.model';
import { IntendantService } from '../service/intendant.service';

@Component({
  templateUrl: './intendant-delete-dialog.component.html',
})
export class IntendantDeleteDialogComponent {
  intendant?: IIntendant;

  constructor(protected intendantService: IntendantService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.intendantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
