import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEtapes } from '../etapes.model';
import { EtapesService } from '../service/etapes.service';

@Component({
  templateUrl: './etapes-delete-dialog.component.html',
})
export class EtapesDeleteDialogComponent {
  etapes?: IEtapes;

  constructor(protected etapesService: EtapesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.etapesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
