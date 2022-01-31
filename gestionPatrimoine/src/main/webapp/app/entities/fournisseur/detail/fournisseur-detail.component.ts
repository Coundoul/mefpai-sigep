import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFournisseur } from '../fournisseur.model';

@Component({
  selector: 'jhi-fournisseur-detail',
  templateUrl: './fournisseur-detail.component.html',
})
export class FournisseurDetailComponent implements OnInit {
  fournisseur: IFournisseur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fournisseur }) => {
      this.fournisseur = fournisseur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
