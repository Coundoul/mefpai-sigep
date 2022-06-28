import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAtelier } from '../atelier.model';

@Component({
  selector: 'jhi-atelier-detail',
  templateUrl: './atelier-detail.component.html',
})
export class AtelierDetailComponent implements OnInit {
  atelier: IAtelier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ atelier }) => {
      this.atelier = atelier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
