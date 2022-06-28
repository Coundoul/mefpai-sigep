import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChefEtablissement } from '../chef-etablissement.model';
import { ChefEtablissementService } from '../service/chef-etablissement.service';

@Component({
  templateUrl: './chef-etablissement-delete-dialog.component.html',
})
export class ChefEtablissementDeleteDialogComponent {
  chefEtablissement?: IChefEtablissement;

  constructor(protected chefEtablissementService: ChefEtablissementService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chefEtablissementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
