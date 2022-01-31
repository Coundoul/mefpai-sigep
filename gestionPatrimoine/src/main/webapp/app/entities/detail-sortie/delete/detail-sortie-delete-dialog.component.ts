import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetailSortie } from '../detail-sortie.model';
import { DetailSortieService } from '../service/detail-sortie.service';

@Component({
  templateUrl: './detail-sortie-delete-dialog.component.html',
})
export class DetailSortieDeleteDialogComponent {
  detailSortie?: IDetailSortie;

  constructor(protected detailSortieService: DetailSortieService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detailSortieService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
