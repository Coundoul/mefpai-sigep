import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMangasinierFichiste } from '../mangasinier-fichiste.model';
import { MangasinierFichisteService } from '../service/mangasinier-fichiste.service';

@Component({
  templateUrl: './mangasinier-fichiste-delete-dialog.component.html',
})
export class MangasinierFichisteDeleteDialogComponent {
  mangasinierFichiste?: IMangasinierFichiste;

  constructor(protected mangasinierFichisteService: MangasinierFichisteService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mangasinierFichisteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
