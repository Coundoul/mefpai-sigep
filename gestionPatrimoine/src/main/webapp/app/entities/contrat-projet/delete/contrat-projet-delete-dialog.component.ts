import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContratProjet } from '../contrat-projet.model';
import { ContratProjetService } from '../service/contrat-projet.service';

@Component({
  templateUrl: './contrat-projet-delete-dialog.component.html',
})
export class ContratProjetDeleteDialogComponent {
  contratProjet?: IContratProjet;

  constructor(protected contratProjetService: ContratProjetService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contratProjetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
