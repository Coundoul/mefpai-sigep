import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResponsable } from '../responsable.model';
import { ResponsableService } from '../service/responsable.service';

@Component({
  templateUrl: './responsable-delete-dialog.component.html',
})
export class ResponsableDeleteDialogComponent {
  responsable?: IResponsable;

  constructor(protected responsableService: ResponsableService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.responsableService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
