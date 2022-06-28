import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IComptableSecondaire } from '../comptable-secondaire.model';
import { ComptableSecondaireService } from '../service/comptable-secondaire.service';

@Component({
  templateUrl: './comptable-secondaire-delete-dialog.component.html',
})
export class ComptableSecondaireDeleteDialogComponent {
  comptableSecondaire?: IComptableSecondaire;

  constructor(protected comptableSecondaireService: ComptableSecondaireService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.comptableSecondaireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
