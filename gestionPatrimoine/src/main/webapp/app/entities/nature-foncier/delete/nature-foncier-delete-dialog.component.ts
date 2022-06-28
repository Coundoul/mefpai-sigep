import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INatureFoncier } from '../nature-foncier.model';
import { NatureFoncierService } from '../service/nature-foncier.service';

@Component({
  templateUrl: './nature-foncier-delete-dialog.component.html',
})
export class NatureFoncierDeleteDialogComponent {
  natureFoncier?: INatureFoncier;

  constructor(protected natureFoncierService: NatureFoncierService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.natureFoncierService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
