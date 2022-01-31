import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeBatiment } from '../type-batiment.model';
import { TypeBatimentService } from '../service/type-batiment.service';

@Component({
  templateUrl: './type-batiment-delete-dialog.component.html',
})
export class TypeBatimentDeleteDialogComponent {
  typeBatiment?: ITypeBatiment;

  constructor(protected typeBatimentService: TypeBatimentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeBatimentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
