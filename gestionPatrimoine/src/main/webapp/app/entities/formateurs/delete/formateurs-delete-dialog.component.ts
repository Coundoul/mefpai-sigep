import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormateurs } from '../formateurs.model';
import { FormateursService } from '../service/formateurs.service';

@Component({
  templateUrl: './formateurs-delete-dialog.component.html',
})
export class FormateursDeleteDialogComponent {
  formateurs?: IFormateurs;

  constructor(protected formateursService: FormateursService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formateursService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
