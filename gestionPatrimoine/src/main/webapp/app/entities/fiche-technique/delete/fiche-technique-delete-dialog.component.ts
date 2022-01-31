import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFicheTechnique } from '../fiche-technique.model';
import { FicheTechniqueService } from '../service/fiche-technique.service';

@Component({
  templateUrl: './fiche-technique-delete-dialog.component.html',
})
export class FicheTechniqueDeleteDialogComponent {
  ficheTechnique?: IFicheTechnique;

  constructor(protected ficheTechniqueService: FicheTechniqueService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ficheTechniqueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
