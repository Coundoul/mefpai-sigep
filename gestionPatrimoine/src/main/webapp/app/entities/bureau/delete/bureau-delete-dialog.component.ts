import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBureau } from '../bureau.model';
import { BureauService } from '../service/bureau.service';

@Component({
  templateUrl: './bureau-delete-dialog.component.html',
})
export class BureauDeleteDialogComponent {
  bureau?: IBureau;

  constructor(protected bureauService: BureauService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bureauService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
