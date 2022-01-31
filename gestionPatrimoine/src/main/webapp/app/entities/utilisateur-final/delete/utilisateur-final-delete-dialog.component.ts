import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUtilisateurFinal } from '../utilisateur-final.model';
import { UtilisateurFinalService } from '../service/utilisateur-final.service';

@Component({
  templateUrl: './utilisateur-final-delete-dialog.component.html',
})
export class UtilisateurFinalDeleteDialogComponent {
  utilisateurFinal?: IUtilisateurFinal;

  constructor(protected utilisateurFinalService: UtilisateurFinalService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.utilisateurFinalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
