import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAffectations } from '../affectations.model';
import { AffectationsService } from '../service/affectations.service';

@Component({
  templateUrl: './affectations-delete-dialog.component.html',
})
export class AffectationsDeleteDialogComponent {
  affectations?: IAffectations;

  constructor(protected affectationsService: AffectationsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.affectationsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
