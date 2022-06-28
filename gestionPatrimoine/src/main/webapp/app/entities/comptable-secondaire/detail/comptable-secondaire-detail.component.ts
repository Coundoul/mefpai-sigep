import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IComptableSecondaire } from '../comptable-secondaire.model';

@Component({
  selector: 'jhi-comptable-secondaire-detail',
  templateUrl: './comptable-secondaire-detail.component.html',
})
export class ComptableSecondaireDetailComponent implements OnInit {
  comptableSecondaire: IComptableSecondaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comptableSecondaire }) => {
      this.comptableSecondaire = comptableSecondaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
