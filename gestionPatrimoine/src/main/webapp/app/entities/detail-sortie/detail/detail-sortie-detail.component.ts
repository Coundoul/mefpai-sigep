import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetailSortie } from '../detail-sortie.model';

@Component({
  selector: 'jhi-detail-sortie-detail',
  templateUrl: './detail-sortie-detail.component.html',
})
export class DetailSortieDetailComponent implements OnInit {
  detailSortie: IDetailSortie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailSortie }) => {
      this.detailSortie = detailSortie;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
