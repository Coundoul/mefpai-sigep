import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategorieMatiere } from '../categorie-matiere.model';
import { CategorieMatiereService } from '../service/categorie-matiere.service';

@Component({
  templateUrl: './categorie-matiere-delete-dialog.component.html',
})
export class CategorieMatiereDeleteDialogComponent {
  categorieMatiere?: ICategorieMatiere;

  constructor(protected categorieMatiereService: CategorieMatiereService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categorieMatiereService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
