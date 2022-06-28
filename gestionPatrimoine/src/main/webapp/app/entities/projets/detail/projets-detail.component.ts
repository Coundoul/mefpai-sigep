import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjets } from '../projets.model';

@Component({
  selector: 'jhi-projets-detail',
  templateUrl: './projets-detail.component.html',
})
export class ProjetsDetailComponent implements OnInit {
  projets: IProjets | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projets }) => {
      this.projets = projets;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
