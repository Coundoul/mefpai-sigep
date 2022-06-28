import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IComptablePrincipale } from '../comptable-principale.model';

@Component({
  selector: 'jhi-comptable-principale-detail',
  templateUrl: './comptable-principale-detail.component.html',
})
export class ComptablePrincipaleDetailComponent implements OnInit {
  comptablePrincipale: IComptablePrincipale | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comptablePrincipale }) => {
      this.comptablePrincipale = comptablePrincipale;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
