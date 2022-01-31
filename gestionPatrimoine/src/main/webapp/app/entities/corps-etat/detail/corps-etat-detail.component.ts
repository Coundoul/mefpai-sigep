import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICorpsEtat } from '../corps-etat.model';

@Component({
  selector: 'jhi-corps-etat-detail',
  templateUrl: './corps-etat-detail.component.html',
})
export class CorpsEtatDetailComponent implements OnInit {
  corpsEtat: ICorpsEtat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ corpsEtat }) => {
      this.corpsEtat = corpsEtat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
