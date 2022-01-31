import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrdonnaceurMatiere } from '../ordonnaceur-matiere.model';
import { OrdonnaceurMatiereService } from '../service/ordonnaceur-matiere.service';

@Component({
  templateUrl: './ordonnaceur-matiere-delete-dialog.component.html',
})
export class OrdonnaceurMatiereDeleteDialogComponent {
  ordonnaceurMatiere?: IOrdonnaceurMatiere;

  constructor(protected ordonnaceurMatiereService: OrdonnaceurMatiereService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ordonnaceurMatiereService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
