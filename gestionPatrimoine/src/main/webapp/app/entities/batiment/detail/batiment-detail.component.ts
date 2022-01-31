import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBatiment } from '../batiment.model';

@Component({
  selector: 'jhi-batiment-detail',
  templateUrl: './batiment-detail.component.html',
})
export class BatimentDetailComponent implements OnInit {
  batiment: IBatiment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ batiment }) => {
      this.batiment = batiment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
