import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChefProjet } from '../chef-projet.model';
import { ChefProjetService } from '../service/chef-projet.service';

@Component({
  templateUrl: './chef-projet-delete-dialog.component.html',
})
export class ChefProjetDeleteDialogComponent {
  chefProjet?: IChefProjet;

  constructor(protected chefProjetService: ChefProjetService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chefProjetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
