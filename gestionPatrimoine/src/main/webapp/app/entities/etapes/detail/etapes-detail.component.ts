import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEtapes } from '../etapes.model';

@Component({
  selector: 'jhi-etapes-detail',
  templateUrl: './etapes-detail.component.html',
})
export class EtapesDetailComponent implements OnInit {
  etapes: IEtapes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etapes }) => {
      this.etapes = etapes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
