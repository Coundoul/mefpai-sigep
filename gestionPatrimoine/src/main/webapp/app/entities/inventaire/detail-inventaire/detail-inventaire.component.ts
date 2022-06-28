import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IDetailInventaire } from '../inventaire.model';
import { InventaireService } from '../inventaire.service';

@Component({
  selector: 'jhi-detail-inventaire',
  templateUrl: './detail-inventaire.component.html',
  styleUrls: ['./detail-inventaire.component.scss'],
})
export class DetailInventaireComponent {
  inventaire: any;
  detailInventaire: any;
  isLoading = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected inventaireService: InventaireService,
    public activeModal: NgbActiveModal
  ) {}
  // ngOnInit(): void {
  //   this.afficher();
  //   alert(this.afficher());
  // }

  getStat(ref: any): void {
    this.inventaireService.detail(ref).subscribe(
      (res: HttpResponse<any>) => {
        this.isLoading = false;
        this.detailInventaire = res.body;
        alert(this.detailInventaire);
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  cancel(): void {
    this.activeModal.dismiss();
  }
}
