import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IComptablePrincipale } from '../comptable-principale.model';
import { ComptablePrincipaleService } from '../service/comptable-principale.service';

@Component({
  templateUrl: './comptable-principale-delete-dialog.component.html',
})
export class ComptablePrincipaleDeleteDialogComponent {
  comptablePrincipale?: IComptablePrincipale;

  constructor(protected comptablePrincipaleService: ComptablePrincipaleService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.comptablePrincipaleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
