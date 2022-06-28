import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalles } from '../salles.model';

@Component({
  selector: 'jhi-salles-detail',
  templateUrl: './salles-detail.component.html',
})
export class SallesDetailComponent implements OnInit {
  salles: ISalles | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salles }) => {
      this.salles = salles;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
