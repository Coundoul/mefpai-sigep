import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProjetAttribution } from '../projet-attribution.model';
import { ProjetAttributionService } from '../service/projet-attribution.service';

@Component({
  templateUrl: './projet-attribution-delete-dialog.component.html',
})
export class ProjetAttributionDeleteDialogComponent {
  projetAttribution?: IProjetAttribution;

  constructor(protected projetAttributionService: ProjetAttributionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.projetAttributionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
