import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRequete } from '../requete.model';
import { RequeteService } from '../service/requete.service';

@Component({
  templateUrl: './requete-delete-dialog.component.html',
})
export class RequeteDeleteDialogComponent {
  requete?: IRequete;

  constructor(protected requeteService: RequeteService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.requeteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
